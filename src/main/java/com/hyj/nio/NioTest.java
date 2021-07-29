package com.hyj.nio;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioTest {
    /**
     * buffer读写
     */
    @Test
    public void test1() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            intBuffer.put(new SecureRandom().nextInt(20));
        }
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
        System.out.println(Arrays.toString(intBuffer.array()));
    }

    /**
     * 写文件
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("test2.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        String data = "hello world";
        for (byte b : data.getBytes(StandardCharsets.UTF_8)) {
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
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("test2.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        //我们的byteBuffer已经存满了，会返回0
        //返回-1是因为客户端主动关闭了channel，注意是主动关闭而不是异常关闭。这时候服务器的与之关联的SelectionKey
        // 会不断的触发SelectionKey.OP_KEY事件，但是当我们去读取数据的时候会一直返回-1(并不会抛出异常)，
        // 所以说一般如果出现返回值为-1的情况下，我们需要在服务器端关闭与客户端相连接的channel，
        // 其会自动的从Selector中取消注册，就不会一直重复的触发该SelectionKey的OP_KEY事件
        //从channel读入buffer
        channel.read(byteBuffer);

        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.println((char) byteBuffer.get());
        }
    }

    @Test
    public void test4() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());
    }

    /**
     * 1 间接缓冲对象HeapByteBuffer，其数组在JVM的堆中，操作系统访问时，
     * 会将该内存先拷贝到操作系统的堆中，然后访问OS堆中的数据
     * 2 直接缓冲对象DirectByteBuffer，持有数组的堆外内存地址Buffer.address，
     * 其数组在JVM堆外，位于操作系统的堆中，操作系统可直接访问
     * （零拷贝，即不需要将JVM的内存拷贝到OS内存）
     */
    @Test
    public void test5() {
        ByteBuffer direct = ByteBuffer.allocateDirect(10);
        System.out.println(direct.getClass());
    }

    /**
     * RandomAccessFile既可以读取文件内容，也可以向文件输出数据。同时，RandomAccessFile支持“随机访问”的方式，
     * 程序快可以直接跳转到文件的任意地方来读写数据。
     * 由于RandomAccessFile可以自由访问文件的任意位置，所以如果需要访问文件的部分内容，而不是把文件从头读到尾，
     * 使用RandomAccessFile将是更好的选择。
     * 与OutputStream、Writer等输出流不同的是，RandomAccessFile允许自由定义文件记录指针，
     * RandomAccessFile可以不从开始的地方开始输出，因此RandomAccessFile可以向已存在的文件后追加内容。
     * 如果程序需要向已存在的文件后追加内容，则应该使用RandomAccessFile。
     * RandomAccessFile的方法虽然多，但它有一个最大的局限，就是只能读写文件，不能读写其他IO节点。
     *
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("test6.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        while (mappedByteBuffer.hasRemaining()) {
            System.out.println((char) mappedByteBuffer.get());
        }
//        mappedByteBuffer.put(0,(byte) 'a');
//        mappedByteBuffer.put(3,(byte) 'b');
        randomAccessFile.close();
    }


    LinkedList<SocketChannel> list = new LinkedList<>();

    /**
     * 传统socket服务
     *
     * @throws Exception
     */
    @Test
    public void test7() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(inetSocketAddress);

        while (true) {
            //阻塞等待新的连接
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (list.isEmpty()) {
                list.add(socketChannel);
            } else {
                //false 每个连接的通道都是新建的
                System.out.println(list.getLast().equals(socketChannel));
            }
            new Thread(() -> {
                try {
                    testReadWriteWithClient(socketChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            System.out.println(serverSocketChannel);

        }
    }

    private void testReadWriteWithClient(SocketChannel socketChannel) throws IOException {
        long msglen = 2 + 3 + 4;
        ByteBuffer bufs[] = new ByteBuffer[3];
        bufs[0] = ByteBuffer.allocate(2);
        bufs[1] = ByteBuffer.allocate(3);
        bufs[2] = ByteBuffer.allocate(4);
        long read = 0;
        while (read < msglen) {
            //阻塞等等读取客户端发送过来的新数据
            long r = socketChannel.read(bufs);
            read += r;
            System.out.println("read:" + read);
            Arrays.asList(bufs).forEach(o -> System.out.println(o.position() + "," + o.limit()));
        }
        for (ByteBuffer buf : bufs) {
            buf.flip();
        }
        long write = 0;
        while (write < msglen) {
            //将bufs的数据发送到客户端
            long w = socketChannel.write(bufs);
            write += w;
            System.out.println("write:" + write);
        }
        for (ByteBuffer buf : bufs) {
            buf.clear();
        }
    }

    /**
     * nio socket服务
     *
     * @throws Exception
     */
    @Test
    public void test8() throws Exception {
        int[] ports = new int[5];
        //创建channel 并注册到选择器上
        Selector selector = Selector.open();
        for (int i = 0; i < 5; i++) {
            ports[i] = 5000 + i;
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //非阻塞
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ports[i]);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(inetSocketAddress);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口:" + ports[i]);
        }

        while (true) {
            // number of keys
            int n = selector.select();
            System.out.println(" number of keys:" + n);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    //获取客户端连接
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    //客户端channel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //在客户端channel注册监听读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //删除该事件
                    iterator.remove();
                    System.out.println("客户端:" + socketChannel);
                } else if (selectionKey.isReadable()) {
                    //和客户端读写数据
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    int read = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        int r = channel.read(byteBuffer);
                        if (r <= 0) {
                            break;
                        }
                        read += r;
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                    }
                    System.out.println("read:" + read + ",channel" + channel);
                    iterator.remove();

                }
            }
        }

    }

    /**
     * nio socket服务-群聊：发送数据到所有客户端
     * @throws Exception
     */
    @Test
    public void test9() throws Exception {
        try {

            testServer();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void testServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocket.bind(address);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        Map<String, SocketChannel> clientMap = new HashMap<>();

        while (true) {
            //监听通道事件
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isAcceptable()) {
                    //建立客户端连接 存储客户端
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    String uuid = "<" + UUID.randomUUID() + ">";
                    clientMap.put(uuid, client);
                    System.out.println("client:" + uuid + "," + client);

                } else if (selectionKey.isReadable()) {
                    try {

                        readMsg(clientMap, selectionKey);
                    }catch (Exception e){
                        selectionKey.cancel();
                        e.printStackTrace();
                    }

                }
            }
            //清除掉已经处理的事件
            selectionKeys.clear();
        }
    }

    private void readMsg(Map<String, SocketChannel> clientMap, SelectionKey selectionKey) throws IOException {
        //读取客户端数据 并发送给所有客户端
        SocketChannel client = (SocketChannel) selectionKey.channel();
        System.out.println("read:" + client);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将数据读入buffer
        int count = client.read(byteBuffer);
        System.out.println("count:"+count);

        //从buffer读出数据
        byteBuffer.flip();
        Charset charset = Charset.forName("utf-8");
        String msg=String.valueOf(charset.decode(byteBuffer).array());
        System.out.println("msg:"+msg);
        String sendKey="";
        for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
            if(client==entry.getValue()){
                sendKey=entry.getKey();
            }
        }

        if(count>0){
            byteBuffer.clear();
            byteBuffer.put((sendKey+":"+msg).getBytes());
            for (SocketChannel socketChannel : clientMap.values()) {
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        testClient();
    }

    /**
     * nio socket客户端
     * @throws Exception
     */
    public static void testClient() throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        //非阻塞
        socketChannel.configureBlocking(false);
        //与服务器建立连接
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));
        //注册连接事件到选择器
        Selector selector = Selector.open();
        socketChannel.register(selector,SelectionKey.OP_CONNECT);

        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if(selectionKey.isConnectable()){
                    SocketChannel client= (SocketChannel) selectionKey.channel();
                    //如果连接挂起
                    if(client.isConnectionPending()){
                        //完成建立连接
                        client.finishConnect();
                        //读取控制台的用户输入数据 发送到服务器
                        ExecutorService pool = Executors.newSingleThreadExecutor();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.put(("连接成功:"+ LocalDateTime.now()).getBytes());
                        byteBuffer.flip();
                        client.write(byteBuffer);
                        pool.submit(()->{
                            while (true){
                                try {

                                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                    String msg=bufferedReader.readLine();
                                    byteBuffer.clear();
                                    byteBuffer.put(msg.getBytes());
                                    byteBuffer.flip();
                                    client.write(byteBuffer);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });


                    }
                    //向服务器注册read事件
                    client.register(selector,SelectionKey.OP_READ);

                }else if(selectionKey.isReadable()){
                    SocketChannel client= (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(byteBuffer);
                    if(count>0){

                        System.out.println("server:"+new String(byteBuffer.array()));
                    }
                }
            }
            selectionKeys.clear();
        }

    }
}
