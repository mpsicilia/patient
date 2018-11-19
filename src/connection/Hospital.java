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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
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
        try {
            while (true) {
                //Thie executes when we have a client
                Socket socket = serverSocket.accept();
                new Thread(new ServerClientThread(socket)).start();
            }
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }

    private static void releaseResourcesClient(InputStream inputStream, Socket socket) {

        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class ServerClientThread implements Runnable {

        Object objectRead;
        Socket socket;

        private ServerClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            try {
                //Read from the client

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
                String emg = objectInputStream.readUTF();
                String ecg = objectInputStream.readUTF();

                FileOutputStream fileOut = new FileOutputStream(path + "/ss_" + patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                System.out.println(path + "/ss_" +patient.getMonitoring().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(patient);
                out.close();
                fileOut.close();            
                //file bitalino
                /*FileOutputStream fileOutBit = new FileOutputStream(path + "/bitalino_" + patient.getMonitoring().format(DateTimeFormatter.ISO_DATE));
                ObjectOutputStream outBit = new ObjectOutputStream(fileOutBit);
                outBit.writeUTF("\nEMG: " + emg);
                outBit.writeUTF("\nECG: " + ecg);
                
                outBit.close();
                fileOutBit.close(); */
                

            } catch (IOException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                releaseResourcesClient(inputStream, socket);
            }
        }
    }
}
