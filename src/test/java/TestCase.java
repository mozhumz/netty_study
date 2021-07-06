import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCase {
    public static void main(String[] args) throws IOException {
        new FileInputStream(new File("")).read();
    }
}
