package com.hyj.zerocopy;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIOServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            while (true){
                try {

                    Socket socket = serverSocket.accept();
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    byte[]buf=new byte[4096];
                    long readCount=0;
                    long totalCount=0;
                    long ms=System.currentTimeMillis();
                    while ((readCount=inputStream.read(buf))!=-1){
                        totalCount+=readCount;
                    }
                    System.out.println("time-ms:"+(System.currentTimeMillis()-ms)+","+totalCount);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
