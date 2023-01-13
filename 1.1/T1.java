import java.util.*;

//Decllare the class as its own thread
public class T1 extends Thread {

    //Goblal variables 
    String name;
    int sleepTime;
    boolean active = true;


    //Global variables 
    public T1(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {

        //Execute while the class thread is active
        while (this.isAlive()) {
            while (active) {
                try {
                    //pause the thread for one second
                    System.out.println("tråd 1");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        
        }
    }

    //helper method to switch the running state of the thread
    public void flipActiveMode(){
        this.active = !active;
    }
}