package Project;

import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Threads extends JFrame implements Runnable {
    private final int WIDTH = 17, HEIGHT = 16, TIME = 300;
    private int gameTime;
    private boolean inProgress = false;
    private Map m;
    private Character[] cs;
    private int[] QOL;
    private JLabel[] status; // time, portal, QOL, garbageBag
    private JPanel start = new JPanel();
    private Timer timer;
    private Task tt;

    public Threads() {
        super("潔境");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        start.setBackground(Color.WHITE);
        start.setLayout(null);
        start.setPreferredSize(new Dimension(WIDTH * 50, HEIGHT * 50 + 30));
        JButton[] btn = { new JButton("開始遊戲"), new JButton("規則說明"), new JButton("結束遊戲") };
        for (int i = 0; i < btn.length; i++) {
            btn[i].setBounds(i * 200 + 150, HEIGHT * 50 - 180, 150, 100);
            btn[i].setFont(new Font("Sans", Font.PLAIN, 24));
            start.add(btn[i]);
            btn[i].addActionListener(new Listener());
        }
        add(start);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String c = e.getActionCommand();
            switch (c) {
            case "開始遊戲":
                start();
                break;
            case "規則說明":
                JOptionPane.showMessageDialog(Threads.this, getRule(), "遊戲規則", JOptionPane.PLAIN_MESSAGE);
                break;
            case "結束遊戲":
                System.exit(0);
            }
        }
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            gameTime--;
            if (gameTime != 300 && gameTime % 10 == 0)
                m.setPortal();
        }
    }

    private void start() {
        inProgress = true;
        m = new Map(this, WIDTH, HEIGHT);
        cs = new Character[] { new Player(m), new AI(m, 2), new AI(m, 3), new AI(m, 4) };
        QOL = new int[cs.length];
        status = new JLabel[4];
        timer = new Timer();

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(WIDTH * 50, 30));
        m.setPreferredSize(new Dimension(WIDTH * 50, HEIGHT * 50));
        for (int i = 0; i < status.length; i++) {
            status[i] = new JLabel("");
            p.add(status[i]);
            if (i < status.length - 1)
                p.add(Box.createHorizontalStrut(50));
        }
        add(p, BorderLayout.NORTH);
        add(m);
        pack();
        start.setVisible(false);
        setVisible(true);
        gameTime = TIME;
        m.setCharacter(cs);
        tt = new Task();
        timer.schedule(tt, 1000, 1000);

        new Thread(this).start();
    }

    private String getRule() {
        String s = "遊戲時間：一局 5 分鐘(300 秒)\n";
        s += "操作方式：方向鍵移動，空格放置或撿起垃圾\n";
        s += "每個玩家在同一時間能拿起的垃圾最多為 5 個，若未拿著任何垃圾則無法放下垃圾\n\n";
        s += "每個玩家會各自生成在其地盤上，地盤間以不同底色區隔，藍底為公共區域\n\n";
        s += "地盤上各有一個傳送門，每 10 秒會切換狀態。當傳送門開啟時，可以藉其傳送到另一個同樣顏色的傳送門\n\n";
        s += "每個玩家初始有 10 點生活品質，當生活品質清零時，該玩家即輸，其地盤將會成為公共區域\n";
        s += "地圖上會不定時生成垃圾，若在 5 秒內未將其撿起，則垃圾會汙染其所位於的地盤，導致玩家生活品質減 1\n";
        s += "玩家在地盤上各會有一個家，若是垃圾落於家附近的九宮格而未被清除，則生活品質減 2\n";
        s += "若是垃圾落於公共區域而未被清除，則「所有存活玩家」生活品質減 1\n";
        s += "地盤上各有一個盾牌，拾取者可以加 3 生活品質\n\n";
        s += "當遊戲時間歸零或場上已無任何存活玩家，則遊戲結束並列出排名\n";

        return s;
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
        gameTime = TIME;
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
            case 1: // 傳送門狀態
                s = "傳送門：";
                s += (m.PortalIsOpen()) ? "開啟" : "關閉";
                break;
            case 2: // 生活品質
                s = "生活品質：";
                for (int j = 0; j < cs.length; j++) {
                    s += String.format("玩家%d: %d　", j + 1, cs[j].getQOL());
                }
                break;
            case 3: // 垃圾袋內容
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