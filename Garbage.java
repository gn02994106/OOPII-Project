package Project;

public class Garbage extends Obj {
    private final int endTime;

    public Garbage(int x, int y, int startTime) {
        super(x, y, "Garbage.png");
        endTime = startTime - 5;
    }

    public int getEndTime() {
        return endTime;
    }
}