import java.util.*;

public class T1 extends Thread{

    String name;
    int sleepTime;

    public T1(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
        start();
    }

    @Override
    public void run(){
        System.out.printf("im T1 sleeping for: %s seconds\n", this.sleepTime);
    }
}