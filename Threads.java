package Project;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;

public class Threads implements Runnable{
    private static final int MAP_X = 17, MAP_Y =16;
    private static Integer gameTime = 300;
    private static boolean gameOver = false;
    private static Map map = new Map(MAP_X,MAP_Y);
    private static Character[] characters = {new Player(map), new AI(map,2), new AI(map,3), new AI(map,4)};
    private static JLabel time, QOL, garbageBag;
    private static Timer timer = new Timer();

    public Threads(){
        super();
        JFrame fra = new JFrame("Game");
        time = new JLabel("");
        QOL = new JLabel("");
        garbageBag = new JLabel("");

        fra.setSize((MAP_X * 50) + 15, (MAP_Y * 50) + 80);
        fra.setLocationRelativeTo(null);
        fra.setResizable(false);
        fra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        map.setBounds(0, 40, (MAP_X * 50), (MAP_Y * 50));
        time.setBounds(275, 0, 100, 20);
        QOL.setBounds(400, 0, 200, 20);
        garbageBag.setBounds(275, 20, (MAP_X * 50) + 15, 20);
        fra.setLayout(null);
        fra.add(map);
        fra.add(time);
        fra.add(QOL);
        fra.add(garbageBag);

        fra.setVisible(true);
        
        map.loadCharacter(characters);
        new Task().run();
    }
    private class Task extends TimerTask{
        @Override
            public void run(){
                timer.schedule(new Task(), 1000);
                gameTime--;
            }
    }
    @Override
    public void run(){
        while ((!(gameOver)) && (gameTime > 0)){
            printLabel();
             for (int i = 1; i <= 3; i++){
                if (characters[i].getQOL() > 0)
                    ((AI)characters[i]).act();
            }
        }
        System.out.println("End.");
    }

    public static Integer getTime(){
        return gameTime;
    }

    private void printLabel(){
        time.setText("Time: " + gameTime);

        QOL.setText(String.format("QOL: c1:%s, c2:%s, c3:%s, c4:%s",characters[0].getQOL(), characters[1].getQOL(), characters[2].getQOL(), characters[3].getQOL()));
        
        String s = "";
        for (boolean boo : characters[0].garbageBag){
            if (boo == true)
                s = s + "garbage  ";
            else
                s = s + "empty    ";
        }
        garbageBag.setText(String.format("GarbageBag: %s",s));
    }
}