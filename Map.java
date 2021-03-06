package Project;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Map extends JPanel implements KeyListener {
    private Threads t;
    private int width = 0, height = 0, rankCount = 4;
    private Character[] cs;
    private Player p;
    private boolean portalOpened = true;
    private boolean[][] canMoveTo;
    private Obj[] obj;
    private ArrayList<Garbage> gs = new ArrayList<>();
    private Timer timer;
    private final ImageIcon bg = new ImageIcon(getClass().getResource("Background.png"));
    private final Random r = new Random();

    private MapType mapType = new Map01();

    public Map(Threads t, int width, int height) {
        this.t = t;
        this.width = width;
        this.height = height;
        this.setFocusable(true);
        this.addKeyListener(this);
        obj = mapType.getObject();
        initialize();
    }

    public void initialize() {
        portalOpened = true;
        canMoveTo = mapType.getCanMoveTo();
        gs = new ArrayList<>();
        timer = new Timer();
        new Task().run();
        repaint();
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            int delay = (5000 + r.nextInt(3) * 1000), rX = r.nextInt(width), rY = r.nextInt(height);
            while (canMoveTo[rX][rY] == false) {
                rX = r.nextInt(width);
                rY = r.nextInt(height);
            }
            gs.add(new Garbage(rX, rY, t.getTime()));
            timer.schedule(new Task(), delay);
        }
    }

    protected void paintComponent(Graphics g) {
        if (t.getGameStatus()) {
            super.paintComponent(g);
            bg.paintIcon(this, g, 0, 0);
            for (Obj o : obj) {
                if (o instanceof Shield)
                    if (((Shield) o).isUsed())
                        continue;
                o.getIcon().paintIcon(this, g, o.getX() * 50, o.getY() * 50);
            }
            for (Character c : cs) {
                if (c.getQOL() == 0)
                    canMoveTo[c.getX()][c.getY()] = true;
                else {
                    canMoveTo[c.getX()][c.getY()] = false;
                    c.getIcon().paintIcon(this, g, c.getX() * 50, c.getY() * 50);
                }
            }
            for (int i = 0; i < gs.size(); i++) {
                if (checkGarbage(i))
                    i--;
                if (i >= 0) {
                    Garbage garbage = gs.get(i);
                    garbage.getIcon().paintIcon(this, g, garbage.getX() * 50, garbage.getY() * 50);
                }
            }
            repaint();
        } else
            timer.cancel();
    }

    public void setCharacter(Character[] cs) {
        this.cs = cs;
        p = (Player) this.cs[0];
    }

    public void setCanMoveTo(int x, int y, boolean b) {
        canMoveTo[x][y] = b;
    }

    public Obj[] getObjs() {
        return obj;
    }

    public boolean PortalIsOpen() {
        return portalOpened;
    }

    public void setPortal() {
        portalOpened = !portalOpened;
    }

    public int getMapWidth() {
        return this.width;
    }

    public int getMapHeight() {
        return this.height;
    }

    public int getNumberOfGarbages() {
        return gs.size();
    }

    public Garbage getGarbage(int index) {
        return gs.get(index);
    }

    public void addGarbage(int x, int y) {
        gs.add(new Garbage(x, y, t.getTime()));
    }

    public void removeGarbage(int index) {
        gs.remove(index);
    }

    private boolean checkGarbage(int index) {
        if (index >= 0) {
            Garbage g = gs.get(index);
            if (t.getTime() == g.getEndTime()) {
                int owner = mapType.getOwner(g.getX(), g.getY());
                if (owner == 0 || cs[owner - 1].getQOL() == 0) {
                    boolean check = false;
                    int count = 0;
                    for (Character c : cs) {
                        c.setQOL(-1);
                        if ((c.getRank() == 0) && (c.getQOL() == 0)) {
                            count++;
                            check = true;
                        }
                    }
                    if (count >= 1)
                        rankCount -= (count - 1);
                    for (Character c : cs) {
                        if (c.getRank() == 0 && c.getQOL() == 0)
                            c.setRank(rankCount);
                    }
                    if (check)
                        rankCount--;
                } else {
                    cs[owner - 1].setQOL(-1);
                    if (g.getX() >= obj[owner - 1].getX() - 1 && g.getX() <= obj[owner - 1].getX() + 1
                            && g.getY() >= obj[owner - 1].getY() - 1 && g.getY() <= obj[owner - 1].getY() + 1)
                        cs[owner - 1].setQOL(-1);
                    if (cs[owner - 1].getQOL() == 0 && cs[owner - 1].getRank() == 0)
                        cs[owner - 1].setRank(rankCount--);
                }
                gs.remove(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (p.getQOL() > 0) {
            int kc = e.getKeyCode(), changedX = p.getX(), changedY = p.getY();
            switch (kc) {
            case (KeyEvent.VK_SPACE):
                p.place();
                break;
            case (KeyEvent.VK_LEFT):
                changedX = (p.getX() == 0) ? (width - 1) : (p.getX() - 1);
                break;
            case (KeyEvent.VK_RIGHT):
                changedX = (p.getX() == width - 1) ? 0 : (p.getX() + 1);
                break;
            case (KeyEvent.VK_UP):
                changedY = (p.getY() == 0) ? (height - 1) : (p.getY() - 1);
                break;
            case (KeyEvent.VK_DOWN):
                changedY = (p.getY() == height - 1) ? 0 : (p.getY() + 1);
                break;
            }
            if (canMoveTo[changedX][changedY]) {
                p.changePos(changedX, changedY);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}