package Project;

public class AI extends Character{
    public AI(Map map, int number){
        super(map, number);
        switch(number){
            case 2:
                locX = map.mapX - 1;
                locY = 0;
                break;
            case 3:
                locX = 0;
                locY = map.mapY - 1;
                break;
            case 4:
                locX = map.mapX - 1;
                locY = map.mapY - 1;
        }
    }
    public void act(){
    }
}
