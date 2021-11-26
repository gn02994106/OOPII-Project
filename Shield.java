package Project;

public class Shield extends Obj {
    private boolean used = false;

    public Shield(int x, int y) {
        super(x, y, "Shield.png");
    }

    public boolean isUsed() {
        return used;
    }

    public void use() {
        used = true;
    }
}