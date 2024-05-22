package tcp;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread {

    private Socket socket;
    private static File messageCounterFile;

    public Worker(Socket socket) {
        this.socket = socket;
        messageCounterFile = new File("../../serverData/counter.txt");
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

//                incrementCounter();
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

    private synchronized static void incrementCounter() throws IOException {
        RandomAccessFile messageCounterRaf = new RandomAccessFile(messageCounterFile, "rw");
        Integer currentClientsCounter = null;
        try {
            currentClientsCounter = messageCounterRaf.readInt();
            System.out.printf("Total Number of Messages: %d\n",currentClientsCounter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentClientsCounter == null) {
            currentClientsCounter = 0;
        }
        currentClientsCounter++;
        messageCounterRaf.seek(0);
        messageCounterRaf.writeInt(currentClientsCounter);
        messageCounterRaf.close();
    }

}
