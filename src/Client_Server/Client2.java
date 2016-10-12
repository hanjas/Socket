/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Twins
 */
public class Client2 {
    public static void main(String[] args) throws IOException {
        int fileSize =  6022386;
        long start = System.currentTimeMillis();
        int bytesread;
        int current = 0;
        Socket sock = new Socket("localhost", 1122);
        System.out.println("connecting.....");
        String fileName = "som.jpg";
        OutputStream os = sock.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(fileName);
//        pw.flush();
        File myFile = new File(fileName);
//        byte[] byteArray = new byte[(int)myFile.length()];
        byte[] byteArray = new byte[fileSize];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("copy2-"+fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesread = is.read(byteArray, 0 , byteArray.length);
        current = bytesread;
        
        do{
            bytesread = is.read(byteArray, current, (byteArray.length - current));
            System.out.println("BytesRead = "+bytesread+" current "+current+" remaining "+(byteArray.length-current)+" byteArray "+byteArray);
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
