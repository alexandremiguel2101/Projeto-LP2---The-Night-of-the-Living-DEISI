package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.List;

public class Cao extends Creature{

    public Cao(int id, int team, String name, int x, int y) {
        super(id, team, name, x, y);
    }

    public String getCreatureName() {
        return "Cão";
    }

    @Override
    public boolean isHuman() {
        return getTeam() != 10;
    }

    @Override
    public boolean isZombie() {
        return !isHuman();
    }

    @Override
    public boolean isDog() {
        return true;
    }

    @Override
    public boolean isAdult() {
        return false;
    }

    @Override
    public boolean move(int xO, int yO, int xD, int yD, Board board, List<Creature> creatures,
                        List<Equipment> equipmentList, List<SafeHeaven> safeHeavens) {
        // Verifica se está no turno certo
        if (getTeam() != board.getCurrentTeamId()) {
            board.numInvalidPlaysHuman();
            return false;
        }

        // Verifica se a movimentação é para uma posição adjacente até 2 casas
        if (!((xD == xO && (Math.abs(yD - yO) <= 2)) || (yD == yO && (Math.abs(xD - xO) <= 2)))) {
            board.numInvalidPlaysHuman();
            return false;
        }

        for (Creature creature : creatures) {
            if (creature.getX() == xD && creature.getY() == yD) {
                board.numInvalidPlaysHuman();
                return false;
            }
        }

        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == xD && equipment.getY() == yD) {
                board.numInvalidPlaysHuman();
                return false;
            }
        }

        for (SafeHeaven safeHeaven : safeHeavens) {
            if (safeHeaven.getX() == xD && safeHeaven.getY() == yD) {
                safeHeaven.addIdInSafeHeaven(getId());
                enterSafeHeaven();
                board.nextTurn();
                board.turnWithoutCaptureOrTransformation();
                return true;
            }
        }

        setPosition(xD, yD);
        board.nextTurn();
        board.turnWithoutCaptureOrTransformation();
        return true;

    }

    @Override
    public boolean isValidEquipment(Equipment equipment) {
        return false;
    }

    @Override
    public String getSquareInfo() {
        if (!getIsInSafeHeaven()) {
            return  "H:" + getId();
        }
        return "";
    }

    @Override
    public String getTeamName() {
        return "Humano";
    }

    @Override
    public String getCreatureInfoAsString() {
        if (getIsInSafeHeaven()) {
            return getId() + " | " + getCreatureName() + " | " + getName() + " @ Safe Haven";
        }
        return getId() + " | " + getCreatureName() + " | " + getName() + " @ (" + getX() + ", " + getY() + ")";
    }

}
