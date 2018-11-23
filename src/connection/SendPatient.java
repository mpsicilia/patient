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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import patient.Patient;
import java.net.SocketException;
import static patient.Patient.RegularExp;

/**
 *
 * @author plaza
 */
public class SendPatient {
   
    public static void main(String args[]) throws Exceptions, SocketException, Throwable {
        //Initialization of the resources
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;
       BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader bufferedreader;

        try {

            socket = new Socket("localhost", 9000);
            outputStream = socket.getOutputStream();

        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }

  
        String line;
        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while ((line = bufferedreader.readLine()) != null) {

            if (line.contains("Connection established")) {
                System.out.println(line + " with the hospital");
                break;
            }
        }
        
                
        //Acquistion of patient's data
        Patient patient;
        //Time of the current monitoring
        LocalDateTime date = LocalDateTime.now();
        //Name and surname of the patient
        String name = isRegularExpression("name");
        String surname = isRegularExpression("surname");

        //Cheking if the patient already exists
        String current = new java.io.File(".").getCanonicalPath();
        Path path = Paths.get(current, name + "_" + surname);


        boolean stop = true;
        String stopsymptoms;
        String stopsigns;
        String[] symptoms = new String[100];
        String[] signs = new String[100];
        int counter = 0;
        
        //If the patient exists, we ask for his weight, signs and symptoms  
        if (Files.exists(path)) {
            System.out.println("You are already registered");
            System.out.println("New monitoring...");

            float weight = isFloat("weight");

            System.out.println("Introduce your symptoms... ");
            System.out.println("Suggestions: headache, chest pain, fatigue, vision problems, irregular heartbeat\n"
                    + "difficulty breathing ... ");
            symptoms = getSignsOrSymptoms("symptoms");

            
            System.out.println("Introduce your signs... ");
            signs = getSignsOrSymptoms("signs");

            //Store the data in the patient object
            patient = new Patient(name, surname, weight, date, signs, symptoms);

        } else {
            //The patient is new, so we ask for all the data
            System.out.println("Welcome");
            System.out.println("Start introducing your data...");

            //Age of the patient
            int age = isInteger("age");

            //Weight of the patient
            float weight = isFloat("weight");

            //Height of the patient
            float height = isFloat("height");

            //Symptoms of hypertension
            System.out.println("Introduce your symptoms... ");
            System.out.println("Suggestions: headache, chest pain, fatigue, vision problems, irregular heartbeat\n"
                    + "difficulty breathing ... ");
            symptoms = getSignsOrSymptoms("symptoms");

            //Signs of hypertension
            System.out.println("Introduce your signs... ");
            signs = getSignsOrSymptoms("signs");
            
            //Store the data in the `patient object
            patient = new Patient(name, surname, age, weight, height, date, signs, symptoms);

        }
        
        //Getting the ECG and EMG from the bitalino    
        /* System.out.println("It's time to record your EMG and ECG...");
        System.out.println("Introduce the MAC of your Bitalino");
        String BitalinoMAC = bf.readLine();
        int[][] bitalinoData = Bitalino(BitalinoMAC);*/
        int[][] bitalinoData = Bitalino("20:16:02:14:75:76");
        //Store the EMG and ECG in the Patient
        patient.setDataBitalino(bitalinoData);
        
        //This boolean is used to release all the used resources if no error occurs
        boolean output = true;

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            //Send the object Patient to the server
            objectOutputStream.writeObject(patient);
            objectOutputStream.flush();

        } catch (SocketException ex) {
            System.out.println("Unable to write the patient data on the server, it is closed.");
            System.out.println("Release just the socket..."
                    + "the server was closed ");
            //If an error occurs, we don't want the program to release inexistent resources
            //so, we put the boolean to false
            output = false;
            //Release the open resources
            releaseResourcesSocket(socket);

        }

        if (output) {
            //Release the open resources
            releaseResources(objectOutputStream, socket);
        }
        System.out.println("\tEND OF THE PROGRAM");
    }

    //Function in which we get the data from the bitalino
    public static Frame[] frame;

    public static int[][] Bitalino(String mac) throws Throwable {
        BITalino bitalino = null;
        int[][] data = new int[2][10000];
        
        try {
            bitalino = new BITalino();

            int SamplingRate = 1000;
            boolean connect = true;
            while (connect) {
                try {
                    System.out.println("Trying to connect to the Bitalino...");

                    bitalino.open(mac, SamplingRate);
                    connect = false;

                } catch (Throwable ex) {
                    System.out.println("BITalino unconnected.");
                }
            }
            connect = true;

            //We want to use A1(EMG) and A2(ECG)
            int[] channelsToAcquire = {0, 1};
            bitalino.start(channelsToAcquire);


            //Read 10000 samples 
            frame = bitalino.read(10000);

            //Store the samples
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 10000; j++) {
                    data[i][j] = frame[j].analog[i];
                }
            }
            
            bitalino.stop();

        } catch (BITalinoException ex) {
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Return the obtained bitalino data
        return data;
    }

    private static void releaseResources(ObjectOutputStream objectOutputStream, Socket socket) {
        try {
            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void releaseResourcesSocket(Socket socket) {

        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Function to check that the AGE introduced by the patient is an integer
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
    
    //Function to check that the WEIGHT and HEIGHT introduced by the patient are floats
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

    //Function to check that the NAME and SURNAME introduced by the patient are valid regular expressions
    private static String isRegularExpression(String name) throws IOException {
        boolean correctreggex = true;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String string = "";
        do {
            try {
                System.out.println("Introduce your " + name);
                string = bf.readLine();
                correctreggex = RegularExp(string);
                
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
        
        return string;
    }
    
    private static String [] getSignsOrSymptoms(String s) throws IOException{
        String [] strings = new String[100];
        boolean stop = true;
        String nomore = "";
        int counter = 0;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("When you finish introducing your " + s + " type 'stop'");

            while (stop) {
                nomore = bf.readLine();

                if (nomore.equals("stop")) {
                    stop = false;
                    break;
                } else {
                    strings[counter] = nomore;
                    counter++;
                }
            }
        return strings;
    }
 }
