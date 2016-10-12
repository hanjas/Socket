/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }
    public void run() {
        try {
            client();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void client() throws IOException {
        int fileSize =  6022386;
        long start = System.currentTimeMillis();
        int bytesread;
        int current = 0;
        Socket sock = new Socket("localhost", 1122);
        System.out.println("connecting.....");
        String fileName = "cat.jpg";
        OutputStream os = sock.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(fileName);
//        pw.flush();
        
        byte[] byteArray = new byte[fileSize];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("video-copy1.avi");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesread = is.read(byteArray, 0 , byteArray.length);
        current = bytesread;
        
        do{
            bytesread = is.read(byteArray, current, (byteArray.length - current));
            if(bytesread >= 0)
                current += bytesread;
        } while(bytesread > -1 );
        
        bos.write(byteArray, 0, current);
        bos.flush();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        os.flush();
        
        bos.close();
        sock.close();
    }
}