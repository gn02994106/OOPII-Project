package Project;

import javax.swing.ImageIcon;

public abstract class Character {
    protected int QOL = 5, rank = 0, x, y;
    private Map m;
    private final ImageIcon icon;
    private boolean[] garbageBag = new boolean[5]; // true:有

    public Character(Map m, int number) {
        this.m = m;
        this.icon = new ImageIcon(this.getClass().getResource("C" + number + ".png"));
    }

    public void place() {
        if (canPlace()) {
            for (int i = 0; i < garbageBag.length; i++) {
                if (garbageBag[i] == true) {
                    garbageBag[i] = false;
                    m.addGarbage(x, y);
                    break;
                }
            }
        } else {
            for (int i = 0; i < garbageBag.length; i++) {
                if (garbageBag[i] == false) {
                    for (int j = 0; j < (m.getNumberOfGarbages()); j++) {
                        if ((m.getGarbage(j).getX() == x) && (m.getGarbage(j).getY() == y)) {
                            garbageBag[i] = true;
                            m.removeGarbage(j);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setQOL(int n) {
        if (n > 0)
            QOL = n;
        else if (n < 0 && QOL > 0)
            QOL--;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQOL() {
        return QOL;
    }

    public int getRank() {
        return rank;
    }

    public void setGarbageBag() {
        garbageBag = new boolean[5];
    }

    public boolean[] getGarbageBag() {
        return garbageBag;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private boolean canPlace() {
        for (int i = 0; i < m.getNumberOfGarbages(); i++) {
            if (m.getGarbage(i).getX() == x && m.getGarbage(i).getY() == y)
                return false;
        }
        return true;
    }

    public void changePos(int x, int y) {
        Obj[] o = m.getObjs();
        m.setCanMoveTo(this.x, this.y, true);
        if (m.PortalIsOpen()) {
            for (int i = 4; i < 8; i++) {
                if (x == o[i].getX() && y == o[i].getY()) {
                    x = (i % 2 == 0) ? o[i + 1].getX() : o[i - 1].getX();
                    y = (i % 2 == 0) ? o[i + 1].getY() : o[i - 1].getY();
                    break;
                }
            }
        }

        for (int i = 8; i < 12; i++) {
            if (!((Shield) o[i]).isUsed() && x == o[i].getX() && y == o[i].getY()) {
                ((Shield) o[i]).use();
                setQOL(getQOL() + 3);
                break;
            }
        }

        this.x = x;
        this.y = y;
        m.setCanMoveTo(x, y, false);
    }
}