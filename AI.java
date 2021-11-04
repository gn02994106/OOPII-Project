package Project;

import javax.swing.ImageIcon;

public class AI extends Character{
    public AI(Map map, int number){
        super(map);
        this.locX = number *50;
        this.locY = number *50;
        characterImage = new ImageIcon("Project/Char3.png");
    }
    public void act(){
    }
}
