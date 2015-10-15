/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;
import util.ByteArrayWrapper;

/**
 *
 * @author romainchigar
 */
public class TcpServerListenThread extends Thread {

    private Server server;
    private Socket socket;

    public TcpServerListenThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void run() {
        try {
            while (socket.isConnected()) {
                String msg[] = receive().split(":");
                if (msg[0].equals("REFRESH")) {
                   socket.getOutputStream().write(server.getFilesList().getBytes());
                } else if (msg[0].equals("GET")) {
                    socket.getOutputStream().write(server.getFile(msg[1]));
                } else if (msg[0].equals("ADD")) {
                    server.put(Integer.parseInt(msg[2]), msg[1]);
                } else if (msg[0].equals("QUIT")) {
                    break;
                }
            }
            socket.close();
            finalize();
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable ex) {
            Logger.getLogger(TcpServerListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String receive() throws SocketException, IOException {
        byte[] buf = new byte[socket.getReceiveBufferSize()];
        socket.getInputStream().read(buf);
        return new String(ByteArrayWrapper.wrap(buf));
    }
}
