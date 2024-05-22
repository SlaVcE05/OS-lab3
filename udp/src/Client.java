import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class Client extends Thread{
    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            if (!scanner.hasNext())
                continue;

            String message = scanner.nextLine();

            buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

            try {
                socket.send(packet);
                buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length, address, serverPort);
                socket.receive(packet);
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 4445;
        TCPClient client = new TCPClient(serverName, port);
        client.start();
    }
}
