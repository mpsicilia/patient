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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
                Patient[] patient = new Patient[2];
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                int i;
                for (i = 0; i < 2; i++) {
                    objectRead = objectInputStream.readObject();
                    patient[i] = (Patient) objectRead;
                    System.out.println(patient[i].toString());
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
}
