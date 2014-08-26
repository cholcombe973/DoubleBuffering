package buffering;

import java.awt.*;
import java.util.Random;
import javax.swing.JOptionPane;

public class MouseMover extends Thread{
  private Robot bot;
  private Random r;
  private boolean left=false,up=false,done=false;
  private GraphicsDevice screen;
  private int screenW,screenH,picWidth,picHeight;
  private XPoint currentPos;

  public MouseMover(GraphicsDevice screen,int picWidth,int picHeight){
    this.screen = screen;
    Random r = new Random(System.currentTimeMillis());
    screenW = screen.getDisplayMode().getWidth();
    screenH = screen.getDisplayMode().getHeight();
    this.picHeight = picHeight;
    this.picWidth = picWidth;
    try{
      bot = new Robot(screen);
      }catch(AWTException e){System.out.println("AWT Exception: " + e);}
      currentPos = new XPoint(r.nextInt(screenH),r.nextInt(screenW));
      bot.mouseMove(currentPos.x,currentPos.y);
  }
  public void run(){
    while(!done){
      genPoint();
      bot.mouseMove(currentPos.getX(),currentPos.getY());
    }
  }
  private void genPoint(){
    if(left){
      if((currentPos.x - 10)<=(picWidth/2)){
        left = false;
      }
      else
        currentPos.x -= 10;
    }
    else{
      if((currentPos.x +10)>=(screenW-(picWidth/2))){
        left = true;
      }
      else
        currentPos.x += 10;
    }
    if(up){
      if((currentPos.y - 10)<=(picHeight/2)){
        up = false;
      }
      else
        currentPos.y -= 10;
    }
    else{
      if((currentPos.y + 10)>=(screenH-(picHeight/2))){
        up = true;
      }
      else
        currentPos.y += 10;
    }
  }
  public void setDone(boolean done){
    this.done = done;
  }
}