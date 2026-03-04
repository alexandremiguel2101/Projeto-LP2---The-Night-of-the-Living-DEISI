package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.ArrayList;
import java.util.List;

public class SafeHeaven {
    private final int x;
    private final int y;
    private List<Integer> list;

    public SafeHeaven(int x, int y) {
        this.x = x;
        this.y = y;
        this.list = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSquareInfo(){
        return "SH";
    }

    public void addIdInSafeHeaven(int id) {
        list.add(id);
    }

    public List<Integer> getIdsInSafeHeaven() {
        return list;
    }
}
