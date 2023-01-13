import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Draw extends JFrame {

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

//JavaFX UI application that is utilized to draw on a paper
class Paper extends JPanel implements Runnable {

  //Global variables
  private HashSet hs = new HashSet();
  private DatagramSocket socket;
  private InputReceiver ir;
  private OutputSender os;

  public final static int SERVICE_PORT = 2000;
  private final static int BUFFER_SIZE = 1024;

  //
  public Paper() {
    setBackground(Color.white);
    addMouseListener(new L1());
    addMouseMotionListener(new L2());
    try{
      socket = new DatagramSocket();
      ir = new InputReceiver(socket);
      os = new OutputSender(socket, "localhost");
      
  }catch(SocketException se){
    
  }
  }

  @Override
  public void run(){
    while(true){}
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.black);
    Iterator i = hs.iterator();
    while (i.hasNext()) {
      Point p = (Point) i.next();
      g.fillOval(p.x, p.y, 2, 2);
    }
  }

  private void addPoint(Point p) {
    hs.add(p);
    repaint();
  }


  //Helper classes to obtain and output the location of the mouse at all times
  class L1 extends MouseAdapter {
    public void mousePressed(MouseEvent me) {
      addPoint(me.getPoint());
      os.outputPoint(me.getPoint());
    }
  }

  class L2 extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent me) {
      addPoint(me.getPoint());
      os.outputPoint(me.getPoint());
    }
  }

  //Listens for all incoming datagrams sent in trought the UDP socket
  class InputReceiver implements Runnable {
    DatagramSocket socket;
    byte[] buffer;
    private Thread currentThread = new Thread(this);

    InputReceiver(DatagramSocket s) {
      socket = s;
      buffer = new byte[BUFFER_SIZE];
      currentThread.start();
    }

    @Override
    public void run() {
      // TODO Auto-generated method stub
      while (true) {
        try {
          //Stores the incoming data in batches and hadnel its convertion to a point in the viewport 
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String data = new String(packet.getData());
          System.out.println("this is the received data" + "#"+data+"#");
        
          //Converts the data to a point object 
          String[] pointPosition = data.split(" ");
          System.out.println(pointPosition);
          String pos1 = pointPosition[0].trim();
          String pos2 = pointPosition[1].trim();
          int p1 = Integer.parseInt(pos1);
          int p2 = Integer.parseInt(pos2);
          Point p = new Point(p1, p2);
          
          addPoint(p);
          System.out.println("RECEIVED PACKET");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  //Helper class that handles the data ouptut to the other end of the datagram socket
  class OutputSender implements Runnable {
    private DatagramSocket socket;
    private String hostName;
    private Thread currentThread = new Thread(this);

    OutputSender(DatagramSocket s, String h) {
      socket = s;
      hostName = h;
      currentThread.start();
    }

    //Converts the point into a format that'll be able to be read by the receiver
    public void outputPoint(Point point) {   
      String pointString = Integer.toString(point.x) + " " + Integer.toString(point.y);
      byte[] buffer = pointString.getBytes();
      try{
      InetAddress address = InetAddress.getByName(hostName);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SERVICE_PORT);
      socket.send(packet);
      System.out.println("OUTPUTTING DATA");
      }catch(IOException se){
        System.out.println(se.getMessage());
      }
    }

    //Run indefinitely
    @Override
    public void run() {
      while (true) {
      }
    }
  }
}
