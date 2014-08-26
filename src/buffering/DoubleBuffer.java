package buffering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class DoubleBuffer extends JFrame implements MouseMotionListener,ActionListener {
  private int mX,mY;
  private Image fadeInImage;
  private Timer t = new Timer(10,this);
  private BufferStrategy myStrategy;
  private native int GetCursorPosX();
  private native int GetCursorPosY();
  private Render r;
  private boolean started = false;
  private MouseMover m;
  private GraphicsDevice screen;

  private class MoveBuff{
    public Point pt;
    public double alpha;

    public MoveBuff(){
      pt = new Point();
    }
  }
  public DoubleBuffer(){
    super("Double Buffer v1.0",GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    this.addKeyListener(new KeyListener(){
      public void keyTyped(KeyEvent e){
        if(e.getKeyChar() == (char)27){
          if(r!=null){
            r.setRun(false);
          }
          if(m!=null)
            m.setDone(true);
          screen.setFullScreenWindow(null);
          setVisible(false);
          dispose();
          System.exit(0);
        }
      }
      public void keyPressed(KeyEvent e){}
      public void keyReleased(KeyEvent e){}
    });
    loadImage();
    addMouseMotionListener(this);
    screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    this.setResizable(false);
    this.setIgnoreRepaint(true);
    this.setDefaultLookAndFeelDecorated(false);
    this.setUndecorated(true);
    setVisible(true);

    //FULLSCREEN EXCLUSIVE MODE
    screen.setFullScreenWindow(this);
    DisplayMode dm = new DisplayMode(1280,1024,DisplayMode.BIT_DEPTH_MULTI,60);
    screen.setDisplayMode(dm);
    try{
      Thread.sleep(2000);
    }catch(InterruptedException e){System.out.println("Your shit got fucked up");}
    try{
      this.createBufferStrategy(2);
    }
    catch(IllegalArgumentException e){ System.out.println("IllegalArgumentException: " + e);}
    catch(IllegalStateException e){ System.out.println("IllegalStateException: " + e);}
    myStrategy = this.getBufferStrategy();
    t.start();
    r = new Render(myStrategy,this,fadeInImage,screen);
    //mouse mover//
    m = new MouseMover(screen,fadeInImage.getWidth(null),fadeInImage.getHeight(null));
    m.start();
  }
  private void loadImage(){
    ImageIcon icon = new ImageIcon("JavaManBox.gif");
    fadeInImage = icon.getImage();
  }
  public void mouseMoved(MouseEvent me){
    mX = (int)me.getPoint().getX()+15;
    mY = (int)me.getPoint().getY()+15;
    if(r!=null){
      if(!started){
        r.start();
        started = true;
      }
      r.setPoints(mX,mY);
      r.moveBuff();
    }
  }
  public void mouseDragged(MouseEvent me){
    mouseMoved(me);
  }
  public void actionPerformed(ActionEvent ae){
    if(r!=null){
      if(!started){
        r.start();
        started = true;
      }
      r.actionTrail();
    }
  }
  public static void main(String[] args){
    new DoubleBuffer();
  }
  public int getCursorPosx(){
    return GetCursorPosX();
  }
  public int getCursorPosy(){
    return GetCursorPosY();
  }
  static{
    System.loadLibrary("CursorPos");
  }
}