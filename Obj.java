package Project;

import javax.swing.ImageIcon;

public class Obj {
    protected final ImageIcon icon;
    protected final int x, y;

    public Obj(int x, int y, String s) {
        this.x = x;
        this.y = y;
        icon = new ImageIcon(getClass().getResource(s));
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
