package Project;

public class Map01 extends MapType {
    public Map01() {
        super();
        obj = new Obj[12];
        int[][] home = { { 4, 3 }, { 12, 3 }, { 4, 12 }, { 12, 12 } },
                portal = { { 3, 0 }, { 13, 15 }, { 3, 15 }, { 13, 0 } },
                shield = { { 0, 4 }, { 16, 4 }, { 0, 11 }, { 16, 11 } };
        for (int i = 0; i < obj.length; i++) {
            int x = -1, y = -1;
            String s = "";
            if (i >= 0 && i < 4) {
                x = home[i][0];
                y = home[i][1];
                s = String.format("Home%d.png", i + 1);
            } else if (i >= 4 && i < 8) {
                x = portal[i - 4][0];
                y = portal[i - 4][1];
                s = (i < 6) ? "Portal1.png" : "Portal2.png";
            } else {
                x = shield[i - 8][0];
                y = shield[i - 8][1];
            }
            obj[i] = (i < 8) ? new Obj(x, y, s) : new Shield(x, y);
        }
    }

    @Override
    public int getOwner(int x, int y) {
        if (x >= 0 && x <= 7) {
            if (y >= 0 && y <= 6)
                return 1;
            else if (y >= 9 && y <= 15)
                return 3;
        } else if (x >= 9 && x <= 16) {
            if (y >= 0 && y <= 6)
                return 2;
            else if (y >= 9 && y <= 15)
                return 4;
        }
        return 0;
    }

    @Override
    protected void setObstacle() {
        int[][] obstacle = new int[][] { { 1, 0 }, { 4, 0 }, { 7, 0 }, { 12, 0 }, { 15, 0 }, { 1, 1 }, { 4, 1 },
                { 8, 1 }, { 9, 1 }, { 12, 1 }, { 15, 1 }, { 3, 2 }, { 13, 2 }, { 6, 3 }, { 8, 3 }, { 10, 3 }, { 2, 4 },
                { 6, 4 }, { 10, 4 }, { 14, 4 }, { 2, 5 }, { 3, 5 }, { 4, 5 }, { 7, 5 }, { 8, 5 }, { 12, 5 }, { 13, 5 },
                { 14, 5 }, { 9, 6 }, { 1, 7 }, { 4, 7 }, { 12, 7 }, { 15, 7 }, { 2, 8 }, { 5, 8 }, { 7, 8 }, { 9, 8 },
                { 11, 8 }, { 14, 8 }, { 7, 9 }, { 2, 10 }, { 3, 10 }, { 4, 10 }, { 8, 10 }, { 9, 10 }, { 12, 10 },
                { 13, 10 }, { 14, 10 }, { 2, 11 }, { 14, 11 }, { 6, 12 }, { 8, 12 }, { 10, 12 }, { 3, 13 }, { 6, 13 },
                { 10, 13 }, { 13, 13 }, { 1, 14 }, { 4, 14 }, { 7, 14 }, { 8, 14 }, { 12, 14 }, { 15, 14 }, { 1, 15 },
                { 4, 15 }, { 9, 15 }, { 12, 15 }, { 15, 15 }, };
        for (int[] o : obstacle) {
            canMoveTo[o[0]][o[1]] = false;
        }
    }
}
