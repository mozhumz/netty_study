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


}
