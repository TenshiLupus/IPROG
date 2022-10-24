import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Main {


    public static void main(String[] args) throws Exception{
        InputStream is = new URL("https://rainymood.com/").openStream();
        String text = new Scanner(is).useDelimiter("\\A").next();
        System.out.println(text);
    }
}