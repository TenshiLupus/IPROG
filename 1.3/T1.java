import java.util.*;

public class T1 extends Thread {

    String name;
    int sleepTime;
    boolean active = true;

    /**
     * constructor of the thread with desired name and sleeptime as arguments
     *
     * @param name
     * @param sleepTime
     */
    public T1(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    /**
     * Setup the running behavior of the thread
     */
    @Override
    public void run() {

        while (this.isAlive()) {
            while (active) {
                try {
                    System.out.println("tråd 1");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            // try{
            //     System.out.println("im T1, sleeping for: 5 seconds");
            //     Thread.sleep(sleepTime);
            // }catch(InterruptedException ie){
            //     System.out.println(ie.getMessage());
            // }
        }
    }

    /**
     * Switch the active mode of the thread
     */
    public void flipActiveMode(){
        this.active = !active;
    }
}