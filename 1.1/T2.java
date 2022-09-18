

public class T2 implements Runnable{
    Thread currentThread = new Thread(this);

    String name;
    int sleepTime;

    public T2(String name, int sleepTime){
        this.name = name;
        this.sleepTime = sleepTime;
        currentThread.start();
    } 

    @Override
    public void run() {
        try{
        System.out.printf("im T2 sleeping for: %s seconds\n", this.sleepTime);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}