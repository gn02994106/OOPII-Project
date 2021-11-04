package Project;

import javax.swing.ImageIcon;

public class Garbage{
    public ImageIcon image  = new ImageIcon("Project/Bomb.png");
    int locX, locY;
    Integer endTime;
    public Garbage(Character character, Integer startTime){
        this.locX = character.locX;
        this.locY = character.locY;
        endTime = startTime - 3;
    }
}
