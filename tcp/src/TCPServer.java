import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TCPServer extends Thread{

    private int port;
    private static int messageCounter = 0;
    private static Lock counterLock = new ReentrantLock();

    public TCPServer(int port) {
        this.port = port;
    }

    public static void incrementMessageCounter() {
        counterLock.lock();
        try {
            messageCounter++;
        } finally {
            counterLock.unlock();
        }
    }

    public static int getMessageCount() {
        counterLock.lock();
        try {
            return messageCounter;
        } finally {
            counterLock.unlock();
        }
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
                //accept metodot e blokiracki

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
