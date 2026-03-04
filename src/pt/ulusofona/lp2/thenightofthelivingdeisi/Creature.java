package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.List;

public abstract class Creature {
    private final int id;
    private int team;
    private final String name;
    private int x;
    private int y;
    private Equipment capturedEquipment;
    private int numCapturedOrDestroyedEquipments;
    private boolean wasInfected = false;
    private boolean isInSafeHeaven;

    public Creature(int id, int team, String name, int x, int y) {
        this.id = id;
        this.team = team;
        this.name = name;
        this.x = x;
        this.y = y;
        this.isInSafeHeaven = false;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void captureEquipment(Equipment equipment) {
        this.capturedEquipment = equipment;
        this.numCapturedOrDestroyedEquipments++;
    }

    public Creature getCreatureAtPosition(int x, int y, List<Creature> creatures) {
        for (Creature creature : creatures) {
            if (creature.getX() == x && creature.getY() == y) {
                return creature;
            }
        }
        return null;
    }

    public Equipment getCapturedEquipment() {
        return capturedEquipment;
    }

    public int getNumDestroyedEquipments() {
        return numCapturedOrDestroyedEquipments;
    }

    public void destroyEquipment() {
        numCapturedOrDestroyedEquipments++;
    }

    public void removeEquipment() {
        this.capturedEquipment = null;
    }

    public int getId() {
        return id;
    }

    public int getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCapturedEquipmentSize() {
        return numCapturedOrDestroyedEquipments;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void infectHuman() {
        this.team = 10;
        this.wasInfected = true;

    }

    public boolean isInfected() {
        return wasInfected;
    }

    public String[] getCreatureInfo() {
        String[] info = new String[7];

        info[0] = String.valueOf(getId());
        info[1] = getCreatureName();
        info[2] = getTeamName();
        info[3] = getName();

        if (isInSafeHeaven) {
            info[5] = null;
            info[6] = null;
        } else {
            info[4] = String.valueOf(getX());
            info[5] = String.valueOf(getY());
        }

        info[6] = null;
        return info;
    }

    public boolean hasEquipment(int equipmentTypeId) {
        if (isHuman()) {
            if (capturedEquipment != null) {
                return capturedEquipment.getType() == equipmentTypeId;
            }
        }
        return false;
    }

    public boolean getIsInSafeHeaven() {
        return isInSafeHeaven;
    }

    public void enterSafeHeaven() {
        this.isInSafeHeaven = true;
    }



    public abstract String getSquareInfo();

    public abstract String getCreatureName();

    public abstract String getTeamName();

    public abstract String getCreatureInfoAsString();

    public abstract boolean isHuman();

    public abstract boolean isZombie();

    public abstract boolean isDog();

    public abstract boolean isAdult();

    public abstract boolean move(int xO, int yO, int xD, int yD, Board board, List<Creature> creatures,
                                 List<Equipment> equipmentList, List<SafeHeaven> safeHeavens);

    public abstract boolean isValidEquipment(Equipment equipment);

}

