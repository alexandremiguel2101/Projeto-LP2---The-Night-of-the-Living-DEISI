package pt.ulusofona.lp2.thenightofthelivingdeisi;

public class Escudo extends Equipment{
    public Escudo(int id, int type, int x, int y) {
        super(id, type, x, y);
    }

    public String getTypeName() {
        return "Escudo de madeira";
    }

    @Override
    public boolean isDefensive(){
        return true;
    }

    @Override
    public String getEquipmentInfoAsString() {
        return getId() + " | " + getTypeName() + " @ " + "(" + getX() + ", " + getY() + ")";
    }
}
