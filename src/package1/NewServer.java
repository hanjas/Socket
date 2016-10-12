/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package package1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Twins
 */
public class NewServer extends Thread{
    
    public static void server() throws IOException {
        ServerSocket ss = new ServerSocket(1122);
        while(true) {
            System.out.println("waiting for a connection....");
            Socket clientSocket = ss.accept();
            System.out.println("accepted connection :"+clientSocket);
            
            InputStream is = clientSocket.getInputStream();
//            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String fileName = bfr.readLine();
            
            System.out.println("server side got the file name : "+fileName);
            File myFile = new File(fileName);
            byte[] byteArray = new byte[(int)myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(byteArray, 0 , byteArray.length);
            OutputStream os = clientSocket.getOutputStream();
            System.out.println("sending .....");
            os.write(byteArray, 0 , byteArray.length);
            os.flush();
            clientSocket.close();
        }
    }
    
    public static void client(int index) throws IOException {
        int fileSize =  6022386;
        long start = System.currentTimeMillis();
        int bytesread;
        int current = 0;
        Socket sock = new Socket("localhost", 1122);
        System.out.println("connecting.....");
        String fileName = "video.avi";
        OutputStream os = sock.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(fileName);
//        pw.flush();
        
        byte[] byteArray = new byte[fileSize];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("video-copy"+ index+".avi");
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
    
    public static void main(String args[]) throws IOException {
        NewServer ns = new NewServer();
        ns.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i=0; i<1; i++) {
            System.out.println("calling "+i);
            client(i);
        }
    }
    
    public void run() {
        try {
            server();
        } catch (IOException ex) {
            Logger.getLogger(NewServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
