/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import util.ByteArrayWrapper;

/**
 *
 * @author romainchigar
 */
public class Client {

    //Connexion UDP
    private DatagramSocket dgSocket;
    private DatagramPacket dgPacket;
    private boolean isConnected;
    //Connexion TCP
    private Socket socket;
    private int port;
    //Serial 
    private static int SERVER_TCP_PORT_SERIAL = 51000;

    public Client(int port) throws UnknownHostException, SocketException, IOException, ClassNotFoundException {
        dgSocket = new DatagramSocket(port);
        dgSocket.setSoTimeout(0);
        port = SERVER_TCP_PORT_SERIAL++;
        isConnected = false;
        go();
    }

    private void go() throws UnknownHostException, IOException, ClassNotFoundException {
        System.out.print("> ");
        Scanner sc = new Scanner(System.in);
        String cmd[];
        while (!(cmd = sc.nextLine().split(" "))[0].equals("quit")) {
            if (cmd[0].equals("connect")) {
                if (!isConnected) {
                    System.out.println("En attente de connexion...");
                    send("CONNECTION");
                    dgPacket = new DatagramPacket(new byte[8], 8);
                    dgSocket.receive(dgPacket);
                    if ((new String(dgPacket.getData()).equals("ACCEPTED"))) {
                        System.out.println("Connexion établie.");
                        isConnected = true;
                        socket = new Socket(InetAddress.getByName("localhost"), server.Server.TCP_PORT);
                        socket.setSoTimeout(0);
                        System.out.println(socket.isConnected() ? "OK" : "NON");
                    } else {
                        System.out.println("La connexion a été refusée.");
                    }
                } else {
                    System.out.println("Serveur déjà connecté.");
                }
            } else if (!isConnected) {
                System.out.println("Le client n'est pas connecté au serveur.");
            } else if (cmd[0].equals("add")) {
                if ((new File("./upload/" + cmd[1]).exists())) {
                    socket.getOutputStream().write(("ADD:" + cmd[1] + ":" + port).getBytes());
                } else {
                    System.out.println("Le fichier n'existe pas ou n'est pas dans le bon dossier.");
                }
            } else if (cmd[0].equals("list")) {
                socket.getOutputStream().write(("REFRESH").getBytes());
                displayFiles(read().split("&"));
            } else if (cmd[0].equals("get")) {
                socket.getOutputStream().write(("GET:" + cmd[1]).getBytes());
                int clientPort = Integer.parseInt(read());
                reclaimFile(clientPort, cmd[1]);
            }
            System.out.print("> ");
        }
        System.exit(0);
    }

    private void send(String msg) throws UnknownHostException, IOException {
        dgPacket = new DatagramPacket(new byte[1024], 1024, InetAddress.getByName("localhost"),
                server.Server.UDP_PORT);
        dgPacket.setData(msg.getBytes());
        dgSocket.send(dgPacket);
    }

    private void displayFiles(String[] msg) {
        for (String s : msg) {
            System.out.println(s);
        }
    }

    private String read() throws SocketException, IOException {
        byte[] buf = new byte[socket.getReceiveBufferSize()];
        socket.getInputStream().read(buf);
        return new String(ByteArrayWrapper.wrap(buf));
    }

    private void reclaimFile(int port, String path) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(InetAddress.getByName("localhost"), port);
        clientSocket.getOutputStream().write(path.getBytes());
        byte[] buf = new byte[4];
        socket.getInputStream().read(buf);
        int fileSize = Integer.parseInt(new String(ByteArrayWrapper.wrap(buf)));
        File f = new File("./download/" + path);
        byte[] fileBuf = new byte[fileSize];
        socket.getInputStream().read(fileBuf);
        new FileOutputStream(f).write(fileBuf);
    }

    public int getPort() {
        return this.port;
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException, ClassNotFoundException {
        Client client = new Client(1961);
    }
}
