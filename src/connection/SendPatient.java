/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author plaza
 */
public class SendPatient {

    public static void main(String args[]) {
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;

        //int numberOfPatients;
        //while (){}
        Patient[] patients = new Patient[2];
        patients[0] = new Patient("María Plaza", 20, (float) 40.8);
        patients[1] = new Patient("Claudia Saiz", 21, (float) 32.5);

        try {
            socket = new Socket("localhost", 9000);
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendPatient.class.getName()).log(Level.SEVERE, null, ex);
        }

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
