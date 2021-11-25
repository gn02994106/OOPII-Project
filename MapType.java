
public abstract class MapType {
    protected boolean[][] canMoveTo;
    protected int[][] home;

    public MapType() {
        canMoveTo = new boolean[17][16];
        // blank map
        for (int i = 0; i < canMoveTo.length; i++) {
            for (int j = 0; j < canMoveTo[i].length; j++) {
                canMoveTo[i][j] = true;
            }
        }
    }

    public boolean[][] getCanMoveTo() {
        return canMoveTo;
    }

    public int[][] getHome() {
        return home;
    }

    abstract public int getOwner(int x, int y);

    abstract protected void setObstacle();
}
