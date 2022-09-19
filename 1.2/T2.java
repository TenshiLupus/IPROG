
public class T2 implements Runnable {
    Thread currentThread = new Thread(this);

    String name;
    int sleepTime;
    boolean active = true;

    public T2(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
        currentThread.start();
    }

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
            // try{
            //     System.out.println("im T2, sleeping for: 5 seconds");
            //     Thread.sleep(sleepTime);
            // }catch(InterruptedException ie){
            //     System.out.println(ie.getMessage());
            // }
        }
    }

    public void flipActiveMode() {
        this.active = !active;
    }
}
