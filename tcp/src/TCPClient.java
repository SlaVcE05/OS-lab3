import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient extends Thread {
    private int serverPort;
    private String serverName;

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            socket = new Socket(InetAddress.getByName(this.serverName), this.serverPort);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//            Scanner scanner = new Scanner(System.in);

            String input = "login";
            writer.write(input + "\n");
            writer.flush();

            String serverResponse = reader.readLine();
            if (!"logged in".equals(serverResponse)) {
                System.out.println("Login failed");
                return;
            }

            System.out.println(serverResponse);

            for (int i = 0; i< 10; i++){
                input = "message " + i;
                writer.write(input + "\n");
                writer.flush();

                serverResponse = reader.readLine();
                System.out.println(serverResponse);
            }

            writer.write("logout\n");
            writer.flush();

/*            while (true) {

                input = scanner.nextLine();
                writer.write(input + "\n");
                writer.flush();


                serverResponse = reader.readLine();
                System.out.println(serverResponse);

                if ("logout".equals(input)) {
                    break;
                }
            }
*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        String serverPort = System.getenv("SERVER_PORT");
        TCPClient client = new TCPClient(serverName, Integer.parseInt(serverPort));
        client.start();
    }
}
