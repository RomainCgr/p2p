/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.threads.TcpServerThread;
import util.ByteArrayWrapper;

/**
 *
 * @author romainchigar
 */
public class Server {

    //Ports de communication
    public static int UDP_PORT = 1963;
    public static int TCP_PORT = 2001;

    //Connexion UDP
    private DatagramSocket dgSocket;
    private DatagramPacket dgPacket;
    //Gestion des fichiers
    private Map<String, Integer> files;

    public Server() throws UnknownHostException, SocketException, IOException {
        dgSocket = new DatagramSocket(UDP_PORT);
        dgSocket.setSoTimeout(0);
        files = new HashMap<String, Integer>();
        (new TcpServerThread(this)).start();
        go();
    }

    private void go() throws IOException {
        while (true) {
            if (receive().contains("CONNECTION")) {
                System.out.println("Connexion acceptée de " + dgPacket.getAddress()
                        + ":" + dgPacket.getPort());
                dgPacket.setData("ACCEPTED".getBytes());
                dgSocket.send(dgPacket);
            } else {
                System.out.println("Connexion refusée");
            }
        }
    }

    private String receive() throws IOException {
        dgPacket = new DatagramPacket(new byte[1024], 1024);
        dgSocket.receive(dgPacket);
        System.out.println(new String(ByteArrayWrapper.wrap(dgPacket.getData())));
        return new String(ByteArrayWrapper.wrap(dgPacket.getData()));
    }

    public void put(int port, String path){
        files.put(path, port);
    }

    public String getFilesList() {
        String msg = "";
        for (String s : files.keySet())
            msg += s + "&";
        return msg;
    }

    public int getFile(String path) {
        return files.get(path);
    }

    public static void main(String[] args) throws SocketException, IOException {
        Server server = new Server();
    }
}
