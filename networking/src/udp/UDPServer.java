package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
public class UDPServer extends Thread {

    private DatagramSocket socket;
    private byte[] buffer;

    List<String> logged = new ArrayList<>();

    public UDPServer(int port) {
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
                String newMessage = processMessage(received,(packet.getAddress() + ":" + packet.getPort()));

                buffer = newMessage.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    private String processMessage(String message, String ipAndPort){

        if (message.equals("login")) {
            logged.add(ipAndPort);
            System.out.println("SERVER: logged in - " + ipAndPort);
            return "logged in";
        }

        if (message.equals("logout") && logged.contains(ipAndPort)) {
            logged.remove(ipAndPort);
            System.out.println("SERVER: logged out - " + ipAndPort);
            return "logged out ";
        }

        if (logged.contains(ipAndPort))
            return "echo- " + message;

        return "not logged in";
    }

    public static void main(String[] args) {
        System.out.println("udp.Server is on...");
        UDPServer server = new UDPServer(6000);
        server.start();
    }
}
