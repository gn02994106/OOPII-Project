package Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Map extends JPanel implements KeyListener{
    int column = 0, row = 0, test = 0, mapX = 0, mapY = 0, maxX = 0, maxY = 0;
    Integer time;
    Character[] characters = new Character[4];
    ArrayList<Garbage> garbages = new ArrayList<>();

    public Map(int column, int row){
        this.column = column;
        this.row = row;
        maxX = column * 50;
        maxY = row * 50;
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    void loadCharacter(Character c1, Character c2, Character c3, Character c4){
        characters[0] = c1;
        characters[1] = c2;
        characters[2] = c3;
        characters[3] = c4;
    }

    protected void paintComponent(Graphics g){
        time = Threads.getTime();
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        for (Character c : characters){
            c.characterImage.paintIcon(this, g, c.locX, c.locY);
        }
        for (int i = 0; i < garbages.size(); i++){
            Garbage garbage = garbages.get(i);
            if (time.equals(garbage.endTime)){
                for (Character c : characters){
                    if (c.locX == garbage.locX)
                        if ((c.locY > (garbage.locY - 100)) || (c.locY < (garbage.locY + 100)))
                            c.getHurt();
                    else if (c.locY == garbage.locY)
                        if ((c.locX > (garbage.locX - 100)) || (c.locX < (garbage.locX + 100)))
                            c.getHurt();
                }
                garbages.remove(i);
                i--;
            }
            garbage.image.paintIcon(this, g, garbage.locX, garbage.locY);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (characters[0].getQOL() > 0){
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_SPACE)
                characters[0].place();
            if (keyCode == KeyEvent.VK_LEFT)
                characters[0].changePos(-50, 0);
            else if (keyCode == KeyEvent.VK_RIGHT)
                characters[0].changePos(50, 0);
            else if (keyCode == KeyEvent.VK_UP)
                characters[0].changePos(0, -50);
            else if (keyCode == KeyEvent.VK_DOWN)
                characters[0].changePos(0, 50);
        }
        repaint(); 
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
