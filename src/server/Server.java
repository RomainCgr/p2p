/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author romainchigar
 */
public class Server {
    
    public static int UDP_PORT = 1963;
    
    private DatagramSocket dgSocket;
    private DatagramPacket dgPacket;
    
    public Server() throws UnknownHostException, SocketException, IOException {
        dgSocket = new DatagramSocket(UDP_PORT);
        dgSocket.setSoTimeout(0);
        go();
    }
    
    private void go() throws IOException {
        if (receive().equals("CONNECTION"))
            System.out.println("Connexion acceptée.");
        else
            System.out.println("Connexion refusée");
    }
    
    private String receive() throws IOException {
        dgPacket = new DatagramPacket(new byte[10], 10);
        dgSocket.receive(dgPacket);
        return new String(dgPacket.getData());
    }
    
    public static void main(String[] args) throws SocketException, IOException {
        Server server = new Server();
    }
}
