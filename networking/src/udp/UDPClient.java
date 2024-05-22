package udp;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Scanner;


public class UDPClient extends Thread{
    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort) {
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
        buffer = "login".getBytes();
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

        for (int i = 0; i < 10; i++){
            try {
                Thread.sleep(new Random().nextInt(4500)+500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            buffer = ("message " + i).getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

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

        buffer = "logout".getBytes();
        packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try {
            socket.send(packet);
            buffer = new byte[256];
            packet = new DatagramPacket(buffer, buffer.length, address, serverPort);
            socket.receive(packet);
            System.out.println(new String(packet.getData(), 0, packet.getLength()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        /*
        Scanner scanner = new Scanner(System.in);

        while (true) {


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

        */
    }



    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));
        UDPClient client = new UDPClient(serverName, port);
        client.start();
    }
}
