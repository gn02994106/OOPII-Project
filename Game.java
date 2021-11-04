package Project;
public class Game{
    public static void main(String[] args){
        Threads thread = new Threads();
        Thread game = new Thread(thread);
        game.start();
    }
}
