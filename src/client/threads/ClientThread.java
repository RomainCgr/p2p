/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.threads;

import client.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author romainchigar
 */
public class ClientThread extends Thread {
    
    private ServerSocket socket;
    private Client client;
    
    public ClientThread(Client client) throws IOException {
        this.client = client;
        socket = new ServerSocket(client.getPort());
    }
    
    public void run() {
        while (true) {
            try {
                new SendFileThread(socket.accept()).start();
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
