package Project;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Threads implements Runnable{
    private static Integer time = 300;
    private static boolean gameOver = false;
    private static Map map = new Map(16,16);
    private static Player c1 = new Player(map);
    private static AI c2 = new AI(map,1);
    private static AI c3 = new AI(map,2);
    private static AI c4 = new AI(map,3);
    private static JLabel timer, QOL;

    public Threads(){
        super();
        JFrame fra = new JFrame("Game");
        timer = new JLabel("Time:" + time);
        QOL = new JLabel(String.format("QOL: c1:%s, c2:%s, c3:%s, c4:%s",c1.getQOL(), c2.getQOL(), c3.getQOL(), c4.getQOL()));
        fra.setSize(820,800);
        fra.setLocationRelativeTo(null);
        fra.setResizable(false);
        fra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fra.add(map);
        map.add(timer);
        map.add(QOL);
        fra.setVisible(true);
    }
    public void run(){
        map.loadCharacter(c1, c2, c3, c4);
        while ((!(gameOver)) && (time > 0)){
            if (c2.getQOL() > 0){
                c2.act();
            }
            if (c3.getQOL() > 0){
                c3.act();
            }
            if (c4.getQOL() > 0){
                c4.act();
            }
            countdown();
        }
        System.out.println("End.");
    }
    private static void countdown(){
        try{
            Thread.sleep(1000);
            timer.setText("Time: " + --time);
            QOL.setText(String.format("QOL: c1:%s, c2:%s, c3:%s, c4:%s",c1.getQOL(), c2.getQOL(), c3.getQOL(), c4.getQOL()));
            map.repaint();
        }
        catch (InterruptedException e){
            System.exit(0);
        }
    }

    public static Integer getTime(){
        return time;
    }
}
