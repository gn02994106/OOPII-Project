package Project;

import javax.swing.ImageIcon;

public class Character{
    Integer time;
    protected int QOL, locX, locY;
    protected Map map;
    public ImageIcon characterImage;
    protected String direction = "";

    public Character(Map map){
        QOL = 5;
        this.map = map;
    }
    void place(){
        if (canPlace())
            time = Threads.getTime();
            map.garbages.add(new Garbage(this, time));
    }

    void getHurt(){
        if (this.getQOL() > 0)
            this.QOL--;
        System.out.println("QOL: " + this.getQOL());
    }
    
    int getQOL(){
        return this.QOL;
    }
    
    private boolean canPlace(){
        for (Garbage garbage : map.garbages){
            if ((garbage.locX == (this.locX)) && (garbage.locY == (this.locY)))
                return false;
        }
        return true;
    }
    
    public void changePos(int xChanged, int yChanged){
        if (((this.locX + xChanged) >= 0) && ((this.locX + xChanged) < map.maxX))
            this.locX += xChanged;
        if (((this.locY + yChanged) >= 0) && ((this.locY + yChanged) < map.maxY))
            this.locY += yChanged;
    }
}
