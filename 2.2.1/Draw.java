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
    synchronizeclientInput(); 
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

  public void synchronizeclientInput(){
    try{

    byte[] sendingDataBuffer = new byte[BUFFER_SIZE];
    byte[] receivingDataBuffer = new byte[BUFFER_SIZE];
    DatagramSocket clientSocket = new DatagramSocket();
    InetAddress IPAddress = InetAddress.getByName("localhost");

    String data = "position";
    sendingDataBuffer = data.getBytes();
 
    DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,IPAddress, SERVICE_PORT);
    clientSocket.send(sendingPacket);

    DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
    clientSocket.receive(receivingPacket);
        
    String receivedData = new String(receivingPacket.getData());
    System.out.println("Sent from the server: " + receivedData);

    clientSocket.close();

    }catch(SocketException se){

    }catch(IOException ie){

    }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    
  }
}