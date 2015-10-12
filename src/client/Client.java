/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author romainchigar
 */
public class Client {
    
    //Connexion UDP
    private DatagramSocket dgSocket;
    private DatagramPacket dgPacket;
    //Connexion TCP
    private ServerSocket socket;
    //Serial 
    public static int TCP_PORT_SERIAL = 51000;
    
    public Client(int port) throws UnknownHostException, SocketException, IOException {
        dgSocket = new DatagramSocket(port);
        go();
    }
    
    private void go() throws UnknownHostException, IOException {
        System.out.print("> ");
        Scanner sc = new Scanner(System.in);
        String cmd;
        while (!(cmd = sc.nextLine()).equals("quit")) {
            if (cmd.equals("connect"))
                send("CONNECTION");
            System.out.print("> ");
        }
    }
    
    private void send(String msg) throws UnknownHostException, IOException {
        dgPacket = new DatagramPacket(new byte[1024], 1024, InetAddress.getByName("localhost"),
                server.Server.UDP_PORT);
        dgPacket.setData(msg.getBytes());
        dgSocket.send(dgPacket);
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        Client client = new Client(1961);
    }
}
