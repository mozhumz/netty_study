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
        //我们的byteBuffer已经存满了，会返回0
        //返回-1是因为客户端主动关闭了channel，注意是主动关闭而不是异常关闭。这时候服务器的与之关联的SelectionKey
        // 会不断的触发SelectionKey.OP_KEY事件，但是当我们去读取数据的时候会一直返回-1(并不会抛出异常)，
        // 所以说一般如果出现返回值为-1的情况下，我们需要在服务器端关闭与客户端相连接的channel，
        // 其会自动的从Selector中取消注册，就不会一直重复的触发该SelectionKey的OP_KEY事件
        //从channel读入buffer
        channel.read(byteBuffer);

        byteBuffer.flip();
        while (byteBuffer.hasRemaining()){
            System.out.println((char)byteBuffer.get());
        }
    }

    @Test
    public void test4(){
        ByteBuffer byteBuffer=ByteBuffer.allocate(10);
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());
    }
}
