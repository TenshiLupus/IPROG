
//Second way to implement the thread. Allows extension to other class aside from the thread class, as in the other thread class
public class T2 implements Runnable {
    Thread currentThread = new Thread(this);

    //Global varibles
    String name;
    int sleepTime;
    boolean active = true;

    //Starts the thread on instatiation of the class
    public T2(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
        currentThread.start();
    }

    //Execution code for when the thread is running
    @Override
    public void run() {
        while (currentThread.isAlive()) {
            while (active) {
                try {
                    System.out.println("Tråd 2");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //Switch the active state of the thread
    public void flipActiveMode() {
        this.active = !active;
    }
}
