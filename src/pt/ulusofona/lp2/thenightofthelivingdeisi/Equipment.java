package pt.ulusofona.lp2.thenightofthelivingdeisi;

public abstract class Equipment {
    private final int id;
    private final int type;
    private int x;
    private int y;

    public Equipment(int id, int type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void spendBleach() {
    }

    public double getAmountBleach(){
        return 0;
    }

    public void spendBullet() {

    }

    public int getAmountBullets() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSquareInfo(){
        return "E:" + getId();
    }

    public String[] getEquipmentInfo() {
        String[] info = new String[5];
        info[0] = String.valueOf(getId());
        info[1] = String.valueOf(getType());
        info[2] = String.valueOf(getX());
        info[3] = String.valueOf(getY());
        info[4] = null;
        return info;
    }

    public boolean isEscudo() {
        return type == 0;
    }

    public boolean isEspada() {
        return type == 1;
    }

    public boolean isLixivia() {
        return type == 3;
    }

    public boolean isPistola() {
        return type == 2;
    }

    public abstract String getEquipmentInfoAsString();

    public abstract boolean isDefensive();

}

