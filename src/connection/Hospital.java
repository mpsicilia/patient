/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

/**
 *
 * @author claudiasaiz
 */
//import static connection.SendPatient.frame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import patient.Patient;

public class Hospital {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        //Create a service that is waiting in port 9000
        ServerSocket serverSocket = new ServerSocket(9000);

        new Thread(new StopThread(serverSocket)).start();
        try {

            while (true) {

                //Thie executes when we have a client
                System.out.println("Trying to accept the connection of the patient...");
                Socket socket = serverSocket.accept();

                new Thread(new ServerClientThread(socket, serverSocket)).start();
            }
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }

    public static class StopThread implements Runnable {

        ServerSocket serverSocket = null;
        String password = "111tele";

        private StopThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;

        }

        @Override
        public void run() {
            //Its running until the doctor stop the program
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            boolean run = true;
            while (run) {
                System.out.println("Do you want to close the server? [yes/no]");
                try {
                    if (bufferedReader.readLine().contains("yes")) {
                        System.out.println("Introduce the password");
                        if (bufferedReader.readLine().contains(password)) {

                            run = false;
                            System.out.println("run:" + run);
                            break;
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("Closing the program");
            releaseResourcesServer(serverSocket);
            System.exit(0);

        }
    }

    private static class ServerClientThread implements Runnable {

        Object objectRead;
        Socket socket;
        ServerSocket serversocket;

        private ServerClientThread(Socket socket, ServerSocket serversocket) {
            this.socket = socket;
            this.serversocket = serversocket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;
            try {
                //Read from the client
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                String connection = "Connection established";
                printWriter.println(connection);

                inputStream = socket.getInputStream();

                // Patient patient = new Patient();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                objectRead = objectInputStream.readObject();
                Patient patient = (Patient) objectRead;
                System.out.println("patient received:" + patient.toString());
                String current = new java.io.File(".").getCanonicalPath();
                Path path = Paths.get(current, patient.getName() + "_" + patient.getSurname());
                if (!Files.exists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                FileOutputStream fileOut = new FileOutputStream(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                System.out.println(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(patient);
                out.close();
                fileOut.close();
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));

                //Patient patient2 = (Patient) input.readObject();
                //System.out.println(patient2);
            } catch (IOException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                releaseResourcesClient(inputStream, socket);
            }
        }
    }

    private static void releaseResourcesClient(InputStream inputStream, Socket socket) {

        try {
            inputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(Hospital.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Hospital.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(Hospital.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
