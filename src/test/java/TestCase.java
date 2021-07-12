import com.hyj.mode.decorator.ConcreteComponent;
import com.hyj.mode.decorator.ConcreteDecorator1;
import com.hyj.mode.decorator.ConcreteDecorator2;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestCase {
    public static void main(String[] args) throws IOException {
        new FileInputStream(new File("")).read();
        new DataInputStream(new BufferedInputStream(new FileInputStream("")));
    }

    @Test
    public void test1() {
        ConcreteComponent component = new ConcreteComponent();
//        component.doSth();
//        System.out.println("----------------");
//        new ConcreteDecorator1(component).doSth();
        System.out.println("----------------");
        new ConcreteDecorator1(new ConcreteDecorator2(component)).doSth();
    }

    @Test
    public void test2() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            iterator.remove();
        }
        System.out.println(set);
    }

    @Test
    public void test3() throws Exception{
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
            System.out.println("监听端口:"+ports[i]);
        }

        while (true){
            // number of keys
            int n = selector.select();
            System.out.println(" number of keys:"+n);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
                    //获取客户端连接
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //注册监听读事件
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    //删除该事件
                    iterator.remove();
                    System.out.println("客户端:"+socketChannel);
                }else if(selectionKey.isReadable()){
                    //和客户端读写数据
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    int read=0;
                    while (true){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        int r=channel.read(byteBuffer);
                        if(r<=0){
                            break;
                        }
                        read+=r;
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                    }
                    System.out.println("read:"+read+",channel"+channel);
                    iterator.remove();

                }
            }
        }

    }
}
