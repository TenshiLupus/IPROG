
public class T2 implements Runnable {
    Thread currentThread = new Thread(this);

    String name;
    int sleepTime;
    boolean active = true;

    /**
     * constructor of the thread with desired name and sleeptime as arguments
     *
     * @param name
     * @param sleepTime
     */
    public T2(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
        currentThread.start();
    }

    /**
     * Setup the running behavior of the thread
     */
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

    /**
     * Switch the active mode of the thread
     */
    public void flipActiveMode() {
        this.active = !active;
    }
}
