package pt.ulusofona.lp2.thenightofthelivingdeisi;

public class Espada extends Equipment{

    public Espada(int id, int type, int x, int y) {
        super(id, type, x, y);
    }

    public String getTypeName() {
        return "Espada samurai";
    }

    @Override
    public boolean isDefensive(){
        return false;
    }

    @Override
    public String getEquipmentInfoAsString() {
        return getId() + " | " + getTypeName() + " @ " + "(" + getX() + ", " + getY() + ")";
    }
}
