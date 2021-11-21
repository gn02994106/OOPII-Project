package Project;

import javax.swing.ImageIcon;

public abstract class Character{
    protected int QOL = 5, totalPlace = 0, locX, locY;
    protected Map map;
    private final ImageIcon characterIcon;
    protected String direction = "";
    protected boolean[] garbageBag = new boolean[5];    //true:有

    public Character(Map map, int number){
        this.map = map;
        this.characterIcon = new ImageIcon(this.getClass().getResource("Char" + number + ".png"));
    }
    
    public void place(){
        if (canPlace()){
            for (int i = 0; i < garbageBag.length; i++){
                if (garbageBag[i] == true){
                    garbageBag[i] = false;
                    map.garbages.add(new Garbage(this.locX, this.locY, Threads.getTime()));
                    totalPlace++;
                    break;
                }
            }
        }
        else{
            for (int i = 0; i < garbageBag.length; i++){
                if (garbageBag[i] == false){
                    for(int j = 0; j < (map.garbages.size()); j++){
                        if ((map.garbages.get(j).locX == this.locX) && (map.garbages.get(j).locY == this.locY)){
                            garbageBag[i] = true;
                            map.garbages.remove(j);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public ImageIcon getCharacterIcon(){
        return this.characterIcon;
    }

    public void getHurt(){
        if (this.getQOL() > 0)
            this.QOL--;
    }
    
    public int getQOL(){
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
        this.locX += xChanged;
        this.locY += yChanged;
    }
}