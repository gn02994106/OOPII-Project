
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Threads extends JFrame implements Runnable {
    private final int WIDTH = 17, HEIGHT = 16;
    private int gameTime = 5;
    private boolean inProgress = true;
    private Map m = new Map(this, WIDTH, HEIGHT);
    private Character[] cs = { new Player(m), new AI(m, 2), new AI(m, 3), new AI(m, 4) };
    private int[] QOL = new int[cs.length];
    private JLabel[] status = new JLabel[3]; // time, QOL, garbageBag
    private Timer timer = new Timer();
    private Task tt;

    public Threads() {
        super("潔境");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(WIDTH * 50, 20));
        m.setPreferredSize(new Dimension(WIDTH * 50, HEIGHT * 50));
        for (int i = 0; i < status.length; i++) {
            status[i] = new JLabel("");
            p.add(status[i]);
            if (i < status.length - 1)
                p.add(Box.createHorizontalStrut(80));
        }
        add(p, BorderLayout.NORTH);
        add(m);
        pack();

        m.setCharacter(cs);
        tt = new Task();
        timer.schedule(tt, 1000, 1000);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            gameTime--;
        }
    }

    @Override
    public void run() {
        while (gameTime != 0) {
            printLabel();
            for (int i = 0; i < cs.length; i++) {
                QOL[i] = cs[i].getQOL();
            }
            Arrays.sort(QOL); // 由小到大排列
            if (QOL[QOL.length - 2] == 0 || gameTime == 0) {
                inProgress = false;
                tt.cancel();
                printLabel();

                int count = 0;
                for (int i = QOL.length - 1; i >= 0; i -= count) {
                    count = 0;
                    for (Character c : cs) {
                        if (c.getRank() == 0 && c.getQOL() == QOL[i]) {
                            c.setRank(QOL.length - i);
                            count++;
                        }
                    }
                    if (count == 0)
                        break;
                }
                String s = "遊戲結束，排名為：\n";
                count = 0;
                for (int i = 1; i <= cs.length; i += count) {
                    count = 0;
                    s += String.format("第%d名：　", i);
                    for (int j = 0; j < cs.length; j++) {
                        if (cs[j].getRank() == i) {
                            s += String.format("玩家%d　", (j + 1));
                            count++;
                        }
                    }
                    s += "\n";
                    if (count == 0) {
                        count++;
                        continue;
                    }
                }
                s += "再來一局？";
                if (JOptionPane.showConfirmDialog(this, s, "Game over", JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION)
                    replay();
                else
                    System.exit(0);
            }
        }
    }

    private void replay() {
        inProgress = true;
        gameTime = 5;
        m.initialize();
        for (int i = 0; i < cs.length; i++) {
            int x = (i % 2 == 0) ? 0 : m.getMapWidth() - 1;
            int y = (i < 2) ? 0 : m.getMapHeight() - 1;
            cs[i].changePos(x, y);
            cs[i].setQOL(5);
            cs[i].setRank(0);
            cs[i].setGarbageBag();
        }
        tt = new Task();
        timer.schedule(tt, 1000, 1000);

    }

    public int getTime() {
        return gameTime;
    }

    private void printLabel() {
        String s = "";
        for (int i = 0; i < status.length; i++) {
            switch (i) {
            case 0: // 遊戲倒計時
                s = "剩餘時間：" + gameTime;
                break;
            case 1: // 生活品質
                s = ("生活品質：");
                for (int j = 0; j < cs.length; j++) {
                    s += String.format("玩家%d: %d　", j + 1, cs[j].getQOL());
                }
                break;
            case 2: // 垃圾袋內容
                s = "手持：";
                for (boolean boo : cs[0].getGarbageBag()) {
                    s += (boo) ? " 垃圾 " : "  空  ";
                }
            }
            status[i].setText(s);
        }
    }

    public boolean getGameStatus() {
        return inProgress;
    }
}