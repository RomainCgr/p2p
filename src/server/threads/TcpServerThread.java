/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author romainchigar
 */
public class TcpServerThread extends Thread {
    
    private Server server;
    private ServerSocket socket;
    
    public TcpServerThread(Server server) throws IOException {
        this.server = server;
        socket = new ServerSocket(Server.TCP_PORT);
    }
    
    public void run() {
        while (true) {
            try {
                new TcpServerListenThread(server, socket.accept()).start();
            } catch (IOException ex) {
                System.err.println("Une erreur s'est produit.");
            }
        }
    }
}
