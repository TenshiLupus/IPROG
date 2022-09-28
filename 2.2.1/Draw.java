import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Draw extends JFrame{

  private Paper p = new Paper();

  public static void main(String[] args) {
    new Draw();
  }

  public Draw() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    getContentPane().add(p, BorderLayout.CENTER);

    setSize(640, 480);
    setVisible(true);
  }

  
}


class Paper extends JPanel implements Runnable{

  private HashSet hs = new HashSet();
  public final static int SERVICE_PORT = 2000;
  private final static int BUFFER_SIZE = 1024;
  
  
  public Paper() {
    setBackground(Color.white);
    addMouseListener(new L1());
    addMouseMotionListener(new L2());
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.black);
    Iterator i = hs.iterator();
    while(i.hasNext()) {
      Point p = (Point)i.next();
      g.fillOval(p.x, p.y, 2, 2);  
    }
  }

  private void addPoint(Point p) {
    hs.add(p);
    repaint();
  }
 
  class L1 extends MouseAdapter {
    public void mousePressed(MouseEvent me) {
      addPoint(me.getPoint());
    }
  }

  class L2 extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent me) {
      addPoint(me.getPoint());
    }
  }

  class InputReceiver implements Runnable{
    DatagramSocket socket;
    byte[] buffer;

    InputReceiver(DatagramSocket s){
      socket = s;
      buffer = new byte[BUFFER_SIZE];
    }

    @Override 
    public void run() {
      // TODO Auto-generated method stub
      while(true){
        try{
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String data = new String(packet.getData());
          System.out.println("received " + data);
          
        }catch(Exception e){
          System.out.println(e);
        }
      }
    }

    class OutputSender implements Runnable{
      DatagramSocket socket;
      private String hostName;

      OutputSender(DatagramSocket s, String h){
        socket = s;
        hostName = h;
      }

      public void outputPoint(String point) throws Exception {
        byte[] buffer = point.getBytes();
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SERVICE_PORT);
        socket.send(packet);
      }

      @Override
      public void run() {
        // TODO Auto-generated method stub
        boolean connected = false;
        do{
          try{
            outputPoint("40 50");
            connected = true;
          }catch(Exception e){

          }
        }while(!connected);
        while(true){
          try{
            
          }
        }
      }
      
    }
}

  @Override
  public void run() {
    
  }
}

