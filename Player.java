package Project;

import javax.swing.ImageIcon;

public class Player extends Character{
    public Player(Map map){
        super(map);
        this.locX = 400;
        this.locY = 400;
        characterImage = new ImageIcon("Project/Char2.png");
    }
}
