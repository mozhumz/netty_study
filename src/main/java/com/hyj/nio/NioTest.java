package com.hyj.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class NioTest {
    /**
     * buffer读写
     */
    @Test
    public void test1(){
        IntBuffer intBuffer=IntBuffer.allocate(10);
        for(int i=0;i<10;i++){
            intBuffer.put(new SecureRandom().nextInt(20));
        }
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
        System.out.println(Arrays.toString(intBuffer.array()));
    }

    /**
     * 写文件
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        FileOutputStream fileOutputStream=new FileOutputStream("test2.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(20);
        String data="hello world";
        for(byte b:data.getBytes(StandardCharsets.UTF_8)){
            byteBuffer.put(b);
        }
        //写模式转读模式
        byteBuffer.flip();
        //将buffer数据写入channel
        channel.write(byteBuffer);
        channel.close();
        fileOutputStream.close();
        System.out.println(byteBuffer.hasRemaining());
    }

    /**
     * 读文件
     * @throws Exception
     */
    @Test
    public void test3() throws Exception{
        FileInputStream fileInputStream=new FileInputStream("test2.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(20);
        //从channel读入buffer
        channel.read(byteBuffer);

        byteBuffer.flip();
        while (byteBuffer.hasRemaining()){
            System.out.println((char)byteBuffer.get());
        }
    }
}
