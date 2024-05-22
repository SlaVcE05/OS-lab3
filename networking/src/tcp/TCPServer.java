package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer extends Thread{

    private int port;
    private static int messageCounter = 0;
    public TCPServer(int port) {
        this.port = port;
    }

    public synchronized static void incrementMessageCounter() {
        messageCounter++;
    }

    public synchronized static int getMessageCount() {
        return messageCounter;
    }

    @Override
    public void run() {
        System.out.println("SERVER: staring...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("SERVER: started! - " + serverSocket.getInetAddress()+ ":" + serverSocket.getLocalPort() );
        System.out.println("SERVER: waiting for connections...");

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER: new client" + (socket != null ? " - " + socket.getInetAddress()+ ":" + socket.getPort(): ""));
            new Worker(socket).start();
        }

    }



    public static void main(String[] args) {
        TCPServer server = new TCPServer(7000);
        server.start();
    }
}