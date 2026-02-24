package pt.ulusofona.lp2.thenightofthelivingdeisi;

public class Lixivia extends Equipment{
    private double amountBleach;

    public Lixivia(int id, int type, int x, int y) {
        super(id, type, x, y);
        this.amountBleach = 1.0;
    }

    public String getTypeName() {
        return "Lixívia";
    }

    @Override
    public double getAmountBleach() {
        return Math.round(amountBleach * 10.0) / 10.0; //Arredonda para 1 casa decimal
    }

    @Override
    public void spendBleach() {
        if (Math.round(amountBleach * 10.0) / 10.0 == 0.1) {
            amountBleach = 0.0; // Define como 0 e impede decremento adicional
        } else if (Math.round(amountBleach * 10.0) / 10.0 == 0.4 ||
                Math.round(amountBleach * 10.0) / 10.0 == 0.7 ||
                Math.round(amountBleach * 10.0) / 10.0 == 1.0 ||
                Math.round(amountBleach * 10.0) / 10.0 == 0.0) {
            amountBleach -= 0.3; // Reduz 0.3 apenas se não for 0.1
        }
    }


    @Override
    public boolean isDefensive(){
        return true;
    }

    @Override
    public String getEquipmentInfoAsString() {
        return getId() + " | " + getTypeName() + " @ " + "(" + getX() + ", " + getY() + ")" + " | " + getAmountBleach() + " litros";
    }
}
