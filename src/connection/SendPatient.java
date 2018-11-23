/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.Frame;
import Exceptions.Exceptions;

import com.sun.javafx.binding.Logging;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import patient.Patient;
import java.net.SocketException;
import static patient.Patient.RegularExp;

/**
 *
 * @author plaza
 */
public class SendPatient {

    public static void main(String args[]) throws Exceptions, SocketException, Throwable {
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader bufferedreader;
        // FileWriter filewriter ;

        try {

            socket = new Socket("localhost", 9000);
            outputStream = socket.getOutputStream();

        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] symptoms = new String[100];
        String[] signs = new String[100];
        boolean stopfinal = false;

        boolean stop = true;
        boolean correctreggex = true;
        boolean correctreggexSur = true;
        int counter = 0;
        String stopsymptoms;
        String stopsigns;
        String line;
        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Patient patient;
        LocalDateTime date = LocalDateTime.now();

        while ((line = bufferedreader.readLine()) != null) {

            if (line.contains("Connection established")) {
                System.out.println(line + "with the hospital");
                break;
            }

        }
        String name = "";
        String surname = "";

        do {
            try {
                System.out.println("Introduce your name ");
                name = bf.readLine();
                correctreggex = RegularExp(name);
                if (correctreggex == false) {
                    throw new Exceptions(Exceptions.ErrorTypes.WRONG_REGEXPRESSION);

                } else {
                    correctreggex = true;
                    break;

                }
            } catch (Exceptions ex) {

                System.out.println("Error: " + ex);

            }
        } while (correctreggex == false);

        do {
            try {
                System.out.println("Introduce your surname ");
                surname = bf.readLine();
                correctreggexSur = RegularExp(surname);
                if (correctreggexSur == false) {
                    throw new Exceptions(Exceptions.ErrorTypes.WRONG_REGEXPRESSION);

                } else {
                    correctreggexSur = true;
                    break;

                }
            } catch (Exceptions ex) {

                System.out.println("Error: " + ex);

            }
        } while (correctreggexSur == false);

        String current = new java.io.File(".").getCanonicalPath();
        Path path = Paths.get(current, name + "_" + surname);

        if (Files.exists(path)) {
            System.out.println("You are already registered");
            System.out.println("New monitoring...");

            //System.out.println("Introduce your weight (kg)");
            float weight = isFloat("weight");

            System.out.println("Introduce your symptoms... ");
            System.out.println("When you finish introducing your symptoms type 'stop'");

            while (stop) {

                stopsymptoms = bf.readLine();

                if (stopsymptoms.equals("stop")) {
                    stop = false;
                    break;
                } else {
                    symptoms[counter] = stopsymptoms;

                    counter++;

                }

            }
            stop = true;
            counter = 0;
            System.out.println("Introduce your signs... ");
            System.out.println("When you finish introducing your signs type 'stop'");

            while (stop) {

                stopsigns = bf.readLine();
                if (stopsigns.equals("stop")) {
                    stop = false;
                    break;
                } else {
                    signs[counter] = stopsigns;
                    counter++;
                }

            }
            patient = new Patient(name, surname, weight, date, signs, symptoms);

        } else {

            System.out.println("Welcome");
            System.out.println("Start introducing your data...");

            //System.out.println("Introduce your age");
            int age = isInteger("age");

            //System.out.println("Introduce your weight (kg)");
            float weight = isFloat("weight");

            //System.out.println("Introduce your height (m)");
            float height = isFloat("height");

            //date
            System.out.println("Introduce your symptoms... ");
            System.out.println("Suggestions: headache, chest pain, fatigue, vision problems, irregular heartbeat\n"
                    + "difficulty breathing ... ");
            System.out.println("When you finish introducing your symptoms type 'stop'");

            while (stop) {

                stopsymptoms = bf.readLine();

                if (stopsymptoms.equals("stop")) {
                    stop = false;
                    break;
                } else {
                    symptoms[counter] = stopsymptoms;

                    counter++;

                }

            }
            stop = true;
            counter = 0;
            System.out.println("Introduce your signs... ");
            System.out.println("Suggestions: headache, chest pain, fatigue, vision problems, irregular heartbeat\n"
                    + "difficulty breathing ... ");
            System.out.println("when you finish introducing your signs type 'stop'");

            while (stop) {

                stopsigns = bf.readLine();
                if (stopsigns.equals("stop")) {
                    stop = false;
                    break;
                } else {
                    signs[counter] = stopsigns;
                    counter++;
                }

            }
            patient = new Patient(name, surname, age, weight, height, date, signs, symptoms);

        }

        //System.out.println(patient);
        int[][] bitalinoData = Bitalino("20:16:02:14:75:76");

        patient.setDataBitalino(bitalinoData);
        boolean output = true;

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(patient);

            objectOutputStream.flush();

        } catch (SocketException ex) {
            System.out.println("Unable to write the patient data on the server, it is closed.");
            System.out.println("Release just the socket..."
                    + "the server was closed ");

            output = false;
            //System.out.println("");
            releaseResourcesSocket(socket);

        }

        while (output) {
            releaseResources(objectOutputStream, socket);
        }
        System.out.println("\tEND OF THE PROGRAM");
    }

    public static Frame[] frame;

    public static int[][] Bitalino(String mac) throws Throwable {
        BITalino bitalino = null;
        int[][] data = new int[2][10000];
        //mac por consola
        //imorimir fichero al terminar en objeto
        try {
            bitalino = new BITalino();
            //expresion regular para nombre y apellido par no sobreescribir archivos del so
            //excepcion de ints tal y de server y paciente
            //nombre incorrecto excepcion

            String macAddress = mac;
            int SamplingRate = 1000;
            boolean connect = true;
            while (connect) {
                try {
                    System.out.println("Trying to connect to the Bitalino...");

                    bitalino.open(macAddress, SamplingRate);
                    connect = false;

                } catch (Exception ex) {

                    //Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Trying to connect to the Bitalino...");
                }
            }
            connect = true;

            //We want to use A1 and A2
            int[] channelsToAcquire = {0, 1};
            while (connect) {
                try {

                    bitalino.start(channelsToAcquire);
                    connect = false;

                } catch (BITalinoException ex) {

                    //Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Trying to start the Bitalino...");
                }
            }

            //Read 10000 samples 
            frame = bitalino.read(10000);

            //Print the samples
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 10000; j++) {
                    data[i][j] = frame[j].analog[i];

                }

            }
            // System.out.println(data);

            //stop acquisition
            bitalino.stop();

        } catch (BITalinoException ex) {
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private static void releaseResources(ObjectOutputStream objectOutputStream, Socket socket) {
        try {
            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void releaseResourcesSocket(Socket socket) {

        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int isInteger(String name) {

        boolean check = true;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int age = 0;

        while (check) {
            try {
                System.out.println("Introduce your " + name);
                age = Integer.parseInt(bf.readLine());
                check = false;
            } catch (Exception ex) {
                System.out.println("Invalid " + name + ", enter an integer");
            }
        }
        return age;
    }

    private static float isFloat(String name) {

        boolean check = true;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        float data = 0;

        while (check) {
            try {
                System.out.println("Introduce your " + name);
                data = Float.parseFloat(bf.readLine());
                check = false;
            } catch (Exception ex) {
                System.out.println("Invalid " + name + ", enter a float");
            }
        }
        return data;
    }

}
