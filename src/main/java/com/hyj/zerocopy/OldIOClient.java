package com.hyj.zerocopy;

import java.io.*;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args) {
        try {
            Socket socket=new Socket("localhost",8899);
            String fileName="E:\\迅雷下载/ss.zip";
            InputStream fileInputStream=(new FileInputStream(fileName));
            OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            byte[]buf=new byte[4096];
            long readCount=0;
            long total=0;
            long ms=System.currentTimeMillis();
            while ((readCount=fileInputStream.read(buf))!=-1){
                total+=readCount;
                outputStream.write(buf);
            }
            outputStream.close();
            socket.close();
            System.out.println("time-ms:"+(System.currentTimeMillis()-ms)+","+total);


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
