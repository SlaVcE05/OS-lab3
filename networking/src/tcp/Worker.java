package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Worker extends Thread {

    private Socket socket;
    public Worker(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            String loginMessage = reader.readLine();
            if (!"login".equals(loginMessage)) {
                writer.write("Unauthorized access. Please login first.\n");
                writer.flush();
                System.out.println("SERVER: connection closed - " + socket.getInetAddress()+ ":" + socket.getPort());
                return;
            }


            writer.write("logged in\n");
            writer.flush();
            System.out.println("SERVER: logged in - " + socket.getInetAddress()+ ":" + socket.getPort());

            String message;
            while ((message = reader.readLine()) != null) {

                if ("logout".equals(message)) {
                    writer.write("logged out\n");
                    writer.flush();
                    System.out.println("SERVER: connection closed - " + socket.getInetAddress()+ ":" + socket.getPort());
                    System.out.println("SERVER: total server messages: " + TCPServer.getMessageCount());
                    break;
                }

                TCPServer.incrementMessageCounter();
                writer.write("echo: " + message + "\n");
                writer.flush();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}