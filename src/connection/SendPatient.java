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

    public static void main(String args[]) throws IOException, SocketException, Throwable {
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
        String name;
        String surname;

        do {
            System.out.println("introduce your name ");
            name = bf.readLine();
            correctreggex = RegularExp(name);
            if (correctreggex == false) {
                System.out.println("Introduce a valid name");
                //mandar excepcion
            } 
          
        } while (correctreggex == false);
        
        boolean correctreggexSur=true;
       
        
        do {
            System.out.println("introduce your surname ");
            surname = bf.readLine();
            correctreggexSur = RegularExp(surname);
            if (correctreggexSur == false) {
                System.out.println("Introduce a valid surname");

            } 
          
        } while (correctreggexSur == false);
      
     

        String current = new java.io.File(".").getCanonicalPath();
        Path path = Paths.get(current, name + "_" + surname);

        if (Files.exists(path)) {
            System.out.println("You are already registered");
            System.out.println("New monitoring...");

            System.out.println("introduce your weight (kg)");
            float weight = Float.parseFloat(bf.readLine());

            System.out.println("introduce your symptoms... ");
            System.out.println("when you finish introducing your symptoms type 'stop'");

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
            System.out.println("introduce your signs... ");
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
            patient = new Patient(name, surname, weight, date, signs, symptoms);

        } else {
            System.out.println("Welcome");
            System.out.println("Start introducing your data...");

            System.out.println("introduce your age");
            int age = Integer.parseInt(bf.readLine());
            System.out.println("introduce your weight (kg)");
            float weight = Float.parseFloat(bf.readLine());
            System.out.println("introduce your height (m)");
            float height = Float.parseFloat(bf.readLine());
            //date
            System.out.println("introduce your symptoms... ");
            System.out.println("Suggestions: headache, chest pain, fatigue, vision problems, irregular heartbeat\n"
                    + "difficulty breathing ... ");
            System.out.println("when you finish introducing your symptoms type 'stop'");

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
            System.out.println("introduce your signs... ");
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
        //CAMBIAR MAC AL FINAL PROYECTO

        patient.setDataBitalino(bitalinoData);

        //try {
        System.out.println("iwdks");
        objectOutputStream = new ObjectOutputStream(outputStream);
        if (objectOutputStream == null) {
            System.out.println("Throw");
            //throw new Exceptions(Exceptions.ERRORS.NO_SERVER_AVAILABLE);
        }
        objectOutputStream.writeObject(patient);
        objectOutputStream.flush();
        //throw new Exceptions(Exceptions.ERRORS.NO_SERVER_AVAILABLE);
        /*} catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            System.out.println(ex);
            //System.out.println("");
            //releaseResources(objectOutputStream, socket);
            
          
            
            //Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);

        } *//*finally {
            //releaseResources(objectOutputStream, socket);

        }*/
        releaseResources(objectOutputStream, socket);

    }

//public static Frame[] frame;
    public static int[][] Bitalino(String mac) throws Throwable {
        int[][] data = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                data[i][j] = 0;
            }
        }
        return data;
    }


    /*public static int[][] Bitalino(String mac) throws Throwable {
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
            try {
                bitalino.open(macAddress, SamplingRate);

            

} catch (BITalinoException ex) {

                Logger.getLogger(SendPatient.class
.getName()).log(Level.SEVERE, null, ex);
            }

            //We want to use A1 and A2
            int[] channelsToAcquire = {0, 1};
            bitalino.start(channelsToAcquire);

            //Read 10000 samples 
            frame = bitalino.read(10000);
            //int[] dataEMG = new int[10000];
            //int[] dataECG = new int[10000];

            //Print the samples
            /*for (int i = 0; i < frame.length; i++) {
                dataEMG[i] = frame[i].analog[0];
                dataECG[i] = frame[i].analog[1];

            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 10000; j++) {
                    data[i][j] = frame[j].analog[i];

                }

            }
            // System.out.println(data);

            //stop acquisition
            bitalino.stop();

        

} catch (BITalinoException ex) {
            Logger.getLogger(SendPatient.class
.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }*/
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

}
