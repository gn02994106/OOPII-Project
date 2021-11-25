
import javax.swing.ImageIcon;

public class Garbage {
    private final ImageIcon icon = new ImageIcon(this.getClass().getResource("Bomb.png"));
    private final int x, y, endTime;

    public Garbage(int x, int y, int startTime) {
        this.x = x;
        this.y = y;
        endTime = startTime - 3;
    }

    public ImageIcon getIcon() {
        return this.icon;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEndTime() {
        return endTime;
    }
}