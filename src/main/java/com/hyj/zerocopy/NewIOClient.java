package com.hyj.zerocopy;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost",8899));
            socketChannel.configureBlocking(true);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            String fileName="E:\\迅雷下载/ss.zip";
            FileInputStream inputStream=(new FileInputStream(fileName));
            FileChannel inputStreamChannel = inputStream.getChannel();

            long readcount=0;
            long total=0;
            long ms=System.currentTimeMillis();
            long pos=0;
            long size = inputStreamChannel.size();
            while (pos<size)
            //大文件传输的时候，windows对一次传输的大小是有限制的
            {
                long trans=inputStreamChannel.transferTo(pos, size,socketChannel);
                pos+=trans;
                total+=trans;
            }
//            while ((readcount=inputStreamChannel.read(byteBuffer))!=-1){
//                total+=readcount;
//                byteBuffer.flip();
//                socketChannel.write(byteBuffer);
//                byteBuffer.clear();
//            }
            System.out.println("client-total:"+(System.currentTimeMillis()-ms)+","+total);
            socketChannel.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
