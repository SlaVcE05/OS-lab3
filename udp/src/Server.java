import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
public class Server extends Thread {

    private DatagramSocket socket;
    private byte[] buffer;

    List<InetAddress> logged = new ArrayList<>();

    public Server(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.buffer = new byte[256];
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("RECEIVED: " + received);

                String newMessage = processMessage(received,packet.getAddress());

                buffer = newMessage.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    private String processMessage(String message, InetAddress ip){

        if (message.equals("login")) {
            logged.add(ip);
            return "logged in";
        }

        if (message.equals("logout") && logged.contains(ip)) {
            logged.remove(ip);
            return "logged out ";
        }

        if (logged.contains(ip))
            return "echo- " + message;

        return "not logged in";
    }

    public static void main(String[] args) {
        System.out.println("Server is on...");
        TCPServer server = new TCPServer(4445);
        server.start();
    }
}
