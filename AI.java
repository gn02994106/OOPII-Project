
public class AI extends Character {
    public AI(Map m, int number) {
        super(m, number);
        switch (number) {
        case 2:
            x = m.getMapWidth() - 1;
            y = 0;
            break;
        case 3:
            x = 0;
            y = m.getMapHeight() - 1;
            break;
        case 4:
            x = m.getMapWidth() - 1;
            y = m.getMapHeight() - 1;
        }
    }

    void act() {
    }
}