/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import patient.Patient;

/**
 *
 * @author plaza
 */
public class SendPatient {

    public static void main(String args[]) throws IOException {
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        //int numberOfPatients;
        //while (){}
        try {
            socket = new Socket("localhost", 9000);
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] symptoms = null;
        String[] signs = null;
        boolean stop = true;
        int counter = 0;
        String stopsymptoms = null;
        String stopsigns = null;

        System.out.println("connection stablished");
        System.out.println("introduce your name ");
        String name = bf.readLine();
        System.out.println("introduce your age");
        int age = bf.read();
        System.out.println("introduce your weight (kg)");
        float weight = bf.read();
        System.out.println("introduce your height (m)");
        float height = bf.read();
        //date
        System.out.println("introduce your symptoms... ");
        System.out.println("when you finish introducing your symptoms type 'x'");

        while (stop) {

            stopsymptoms = bf.readLine();
            if (stopsymptoms.equals('x')) {
                stop = false;
                break;
            } else {
                symptoms[counter] = stopsymptoms;
                counter++;
            }

        }
        stop=true;
        counter=0;
        System.out.println("introduce your signs... ");
        System.out.println("when you finish introducing your signs type 'x'");

        while (stop) {

            stopsigns = bf.readLine();
            if (stopsigns.equals('x')) {
                stop = false;
                break;
            } else {
                signs[counter] = stopsigns;
                counter++;
            }

        }
        LocalDateTime date = LocalDateTime.now();
        Patient patient=new Patient(name, age, weight, height, date, signs, symptoms);
        

        /*Patient[] patients = new Patient[2];
        patients[0] = new Patient("María Plaza", 20, (float) 40.8);
        patients[1] = new Patient("Claudia Saiz", 21, (float) 32.5);*/

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(patients[0]);
            objectOutputStream.writeObject(patients[1]);
            objectOutputStream.flush();
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResources(objectOutputStream, socket);

        }
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

}
