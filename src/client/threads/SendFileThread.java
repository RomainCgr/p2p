/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ByteArrayWrapper;

/**
 *
 * @author romainchigar
 */
public class SendFileThread extends Thread {
    
    private Socket socket;
    
    public SendFileThread(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        byte[] buf = new byte[1024];
        try {
            socket.getInputStream().read(buf);
            String path = new String(ByteArrayWrapper.wrap(buf));
            File f = new File("./upload/" + path);
            socket.getOutputStream().write((int)f.length());
            FileInputStream fis = new FileInputStream(f);
            byte[] fileBuf = new byte[(int)f.length()];
            fis.read(fileBuf);
            socket.getOutputStream().write(fileBuf);
            fis.close();
            socket.close();
            finalize();
        } catch (IOException ex) {
            Logger.getLogger(SendFileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(SendFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
