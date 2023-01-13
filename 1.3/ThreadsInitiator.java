import java.util.*;

public class ThreadsInitiator {

    /**
     * Main function that initiates all subsequent threads
     * @param args console arguments passed into the program
     */
    // Main will act as the main thread of execution
    public static void main(String[] args) {
        // create first thread
        T1 tone = new T1("tone", 5000);
        tone.start();

        // sleep main thread for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }

        // create second thread
        T2 ttwo = new T2("ttwo", 5000);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }

        //switch active mode in thread one
        tone.flipActiveMode();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }

        //switch active mode in thread two
        ttwo.flipActiveMode();

        //notify end of program
        System.out.println("program ending");
    }
}
