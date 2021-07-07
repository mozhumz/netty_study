import com.hyj.mode.decorator.ConcreteComponent;
import com.hyj.mode.decorator.ConcreteDecorator1;
import com.hyj.mode.decorator.ConcreteDecorator2;
import org.junit.Test;

import java.io.*;

public class TestCase {
    public static void main(String[] args) throws IOException {
        new FileInputStream(new File("")).read();
        new DataInputStream(new BufferedInputStream(new FileInputStream("")));
    }

    @Test
    public void test1(){
        ConcreteComponent component=new ConcreteComponent();
//        component.doSth();
//        System.out.println("----------------");
//        new ConcreteDecorator1(component).doSth();
        System.out.println("----------------");
        new ConcreteDecorator1(new ConcreteDecorator2(component)).doSth();
    }
}
