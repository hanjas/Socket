/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Server;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    
    static Server s;
    static ServerSocket ss;
    static Socket clientSocket;
    
    public static void main(String[] args) throws IOException {
        s = new Server();
        ss = new ServerSocket(1122);
        while(true) {
            System.out.println("waiting for a connection....");
            clientSocket = ss.accept();
            Thread thread = new Thread(s);
            thread.start();
        }
    }
    public void run() {
        try {
            server();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void server() throws IOException {
            System.out.println("accepted connection :"+clientSocket);
            
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