package Project;

public abstract class MapType {
    protected boolean[][] canMoveTo;
    protected Obj[] obj;
    boolean portalOpen, shieldUsed;

    public MapType() {
        canMoveTo = new boolean[17][16];
        // blank map
        for (int i = 0; i < canMoveTo.length; i++) {
            for (int j = 0; j < canMoveTo[i].length; j++) {
                canMoveTo[i][j] = true;
            }
        }
        setObstacle();
    }

    public boolean[][] getCanMoveTo() {
        return canMoveTo;
    }

    public Obj[] getObject() {
        return obj;
    }

    abstract public int getOwner(int x, int y);

    abstract protected void setObstacle();
}
