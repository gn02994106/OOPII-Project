package Project;

import javax.swing.ImageIcon;

public class Garbage{
    private final ImageIcon icon = new ImageIcon(this.getClass().getResource("Bomb.png"));
    final int locX, locY, endTime;
    public Garbage(int locX, int locY, int startTime){
        this.locX = locX;
        this.locY = locY;
        endTime = startTime - 3;
    }

    public ImageIcon getIcon(){
        return this.icon;
    }
}