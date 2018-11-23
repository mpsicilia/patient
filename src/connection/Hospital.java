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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import patient.Patient;

public class Hospital {

    public static void main(String args[]) throws IOException {
        //Create a server that is waiting in port 9000
        ServerSocket serverSocket = new ServerSocket(9000);

        //Thread that is always there waiting if the server wants to finish
        new Thread(new StopThread(serverSocket)).start();

        try {
            while (true) {
                //Thie executes when we have a client
                System.out.println("\nTrying to accept the connection of the patient...\n");
                Socket socket = serverSocket.accept();
                //One thread to serve each patient(client)
                new Thread(new ServerClientThread(socket, serverSocket)).start();
            }
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }

    public static class StopThread implements Runnable {

        ServerSocket serverSocket = null;
        String password = "111tele";
        //The server will stop if the password is correct

        private StopThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;

        }

        @Override
        public void run() {
            //It's running until the doctor stop the program
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            boolean run = true;
            
            while (run) {
                System.out.println("Write the password if you want to stop the server");
                try {
                    if (bufferedReader.readLine().contains(password)) {
                        run = false;
                        break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("Closing the server...");
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
            //Boolean used when the patient disconnects
            //When the patient disconnects, the catch is going to get the exception and release the resources of the patient
            //Also, the boolean catched will be true so the program doesn't continue
            boolean catched = false;
            try {
                //Read from the client
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                //Sending this string to the patient(client) so it can start 
                String connection = "Connection established";
                printWriter.println(connection);

                //Catching the exception if the patient disconnects before finishing
                try {
                    inputStream = socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    //Getting the objects the clients send through the socket
                    objectRead = objectInputStream.readObject();

                } catch (SocketException ex) {
                    System.out.println("Patient disconnected. \n"
                            + "It was not possible to get the data.");
                    releaseResourcesClient(inputStream, socket);
                    catched = true;
                }
                //This if avoids the continuation of the program when the patient has disconnected
                if (!catched) {
                    Patient patient = (Patient) objectRead;
                    //Inform the hospital when patients introduce new data
                    System.out.println("Patient " + patient.getName() + " "
                            + patient.getSurname() + " has introduced a new monitoring.");

                    //Cheking if the folder of the patient already exists, if not we create it
                    //Each patient has a folder
                    String current = new java.io.File(".").getCanonicalPath();
                    Path path = Paths.get(current, patient.getName() + "_" + patient.getSurname());
                    if (!Files.exists(path)) {
                        try {
                            Files.createDirectories(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //Introducing the obtained data in a file
                    //Each monitoring is introduced in a new file inside the patient's folder
                    FileOutputStream fileOut = new FileOutputStream(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                    // System.out.println(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(patient);
                    out.close();
                    fileOut.close();

                    //Following lines to see if the object has been properly writen in the file
                    //ObjectInputStream input = new ObjectInputStream(new FileInputStream(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
                    //Patient patient2 = (Patient) input.readObject();
                    //System.out.println(patient2);
                }
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
