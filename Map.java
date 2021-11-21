package Project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Map extends JPanel implements KeyListener{
    int mapX = 0, mapY = 0;
    Character[] characters = new Character[4];
    Player p;
    boolean[][] canMoveTo;
    int[][] home;
    ArrayList<Garbage> garbages = new ArrayList<>();
    private static Timer timer = new Timer();
    ImageIcon[] icons = new ImageIcon[5];

    private MapType mapType = new Map01();

    public Map(int mapX, int mapY){
        icons[4] = new ImageIcon(getClass().getResource("Background.png"));
        for (int i = 0; i < 4; i++){
            icons[i] = new ImageIcon(getClass().getResource("Home" + (i + 1) +".png"));
        }
        this.mapX = mapX;
        this.mapY = mapY;
        if (mapType instanceof Map01){
            canMoveTo = ((Map01)mapType).getCanMoveTo();
            home = ((Map01)mapType).getHome();
        }
        else{
            canMoveTo = mapType.getCanMoveTo();
            home = mapType.getHome();
        }
        this.setFocusable(true);
        this.addKeyListener(this);
        new Task().run();
        
    }

    class Task extends TimerTask{
        @Override
        public void run(){
            Random rand = new Random();
            int delay = (5000 + (rand.nextInt(2) * 1000)), rX = rand.nextInt(mapX), rY = rand.nextInt(mapY);
            while (canMoveTo[rX][rY] == false){
                rX = rand.nextInt(mapX);
                rY = rand.nextInt(mapY);
            }
            garbages.add(new Garbage(rX, rY, Threads.getTime()));      
            timer.schedule(new Task(), delay);      
        }
    }
    
    void loadCharacter(Character[] characters){
        this.characters = characters;
        p = (Player)this.characters[0];
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        icons[4].paintIcon(this, g, 0, 0);
        for (int i = 0; i < home.length; i++){
            icons[i].paintIcon(this, g, home[i][0] * 50, home[i][1] * 50);
        }
        for (int i = 0; i < characters.length; i++){
            if (characters[i].getQOL() == 0){
                canMoveTo[characters[i].locX][characters[i].locY] = true;
            }
            else{
                canMoveTo[characters[i].locX][characters[i].locY] = false;
                characters[i].getCharacterIcon().paintIcon(this, g, (characters[i].locX * 50), (characters[i].locY * 50));
            }
        }
        for (int i = 0; i < garbages.size(); i++){
            Garbage garbage = garbages.get(i);
            if (Threads.getTime() == (garbage.endTime)){
                int owner = mapType.getOwner(garbage.locX, garbage.locY);
                if ((owner == 0) || (characters[owner - 1].getQOL() == 0)){
                    for (Character c : characters){
                        c.getHurt();
                    }
                }
                else{
                    characters[owner - 1].getHurt();
                    if ((garbage.locX >= (home[owner - 1][0] - 1)) && (garbage.locX <= (home[owner - 1][0] + 1)) && (garbage.locY >= (home[owner - 1][1] - 1)) && (garbage.locY <= (home[owner - 1][1] + 1)))
                        characters[owner - 1].getHurt();
                }
                garbages.remove(i);
                i--;
            }
            garbage.getIcon().paintIcon(this, g, (garbage.locX * 50), (garbage.locY * 50));
        }
        repaint(); 
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (p.getQOL() > 0){
            int kc = e.getKeyCode(), changeX = 0, changeY = 0;
            switch (kc){
                case (KeyEvent.VK_SPACE) :
                    p.place();
                    break;
                case (KeyEvent.VK_LEFT) :
                    if (p.locX == 0)
                        changeX = mapX - 1;
                    else
                        changeX = -1;
                    break;
                case (KeyEvent.VK_RIGHT) :
                    if (p.locX == mapX - 1)
                        changeX = -(mapX - 1);
                    else
                        changeX = 1;
                    break;
                case (KeyEvent.VK_UP) :
                    if (p.locY == 0)
                        changeY = mapY - 1;
                    else
                        changeY = -1;
                    break;
                case (KeyEvent.VK_DOWN) :
                    if (p.locY == mapY - 1)
                        changeY = -(mapY - 1);
                    else
                        changeY = 1;
                    break;
                }
                if ((p.locX + changeX >= 0) && (p.locX + changeX < mapX) && (p.locY + changeY >= 0) && (p.locY + changeY < mapY)){
                    if (canMoveTo[p.locX + changeX][p.locY + changeY] == true){
                        canMoveTo[p.locX][p.locY] = true;
                        canMoveTo[p.locX + changeX][p.locY + changeY] = false;
                        p.changePos(changeX, changeY);
                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}