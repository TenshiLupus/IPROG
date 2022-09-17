import java.util.Random;

public class T2 implements Runnable{

    String name;
    int time;
    Random r = new Random();

    public T2(String name){
        this.name = name;
        time = r.nextInt(999);
    } 

    @Override
    public void run() {
         System.out.printf("im T2 sleeping for: %s seconds", this.name);
    }

}