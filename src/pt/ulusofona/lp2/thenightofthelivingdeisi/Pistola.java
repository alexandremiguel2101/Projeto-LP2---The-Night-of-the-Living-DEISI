package pt.ulusofona.lp2.thenightofthelivingdeisi;

public class Pistola extends Equipment{
    private int numBullets;

    public Pistola(int id, int type, int x, int y) {
        super(id, type, x, y);
        this.numBullets = 3;
    }

    public String getTypeName() {
        return "Pistola Walther PPK";
    }

    public int getNumBullets() {
        return numBullets;
    }

    @Override
    public void spendBullet() {
        this.numBullets--;
    }

    @Override
    public int getAmountBullets() {
        return this.numBullets;
    }

    @Override
    public boolean isDefensive(){
        return false;
    }

    @Override
    public String getEquipmentInfoAsString() {
        return getId() + " | " + getTypeName() + " @ " + "(" + getX() + ", " + getY() + ")" + " | " + getNumBullets() + " balas";
    }
}
