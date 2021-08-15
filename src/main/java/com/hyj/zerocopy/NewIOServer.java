package com.hyj.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8899);
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(inetSocketAddress);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            while (true){
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(true);
                    int readcount=0;
                    int total=0;
                    while ((readcount)!=-1){
                        readcount=socketChannel.read(byteBuffer);
                        total+=readcount;
                        byteBuffer.rewind();
                    }
                    System.out.println("server-read:"+total);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
