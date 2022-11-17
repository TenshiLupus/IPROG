import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client {



    public static void main(String[] args) {
        try {

            Scanner inputScanner = new Scanner(System.in);
            File file = new File("x.wav");


            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("Not Supported");
            }
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();

                    Boolean active = true;

                    while (active) {
                        Boolean record = true;
                        System.out.println("R: record, S: Stop, Q: stop");

                        while(record) {
                            String query = inputScanner.next();
                            switch (query) {
                                case "q" -> {

                                    record = false;
                                    active = false;
                                    targetLine.stop();
                                    targetLine.close();

                                }
                                case "r" -> {
                                    Thread audioRecorderThread = new Thread() {
                                        @Override
                                        public void run() {

                                            targetLine.start();
                                            AudioInputStream recordingStream = new AudioInputStream(targetLine);
                                            File outputFile = new File("recording.wav");

                                            try {
                                                AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    audioRecorderThread.start();
                                }
                                case "s" -> {
                                    record = false;
                                    targetLine.stop();
                                    targetLine.close();
                                }
                            }
                        }
                    }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}