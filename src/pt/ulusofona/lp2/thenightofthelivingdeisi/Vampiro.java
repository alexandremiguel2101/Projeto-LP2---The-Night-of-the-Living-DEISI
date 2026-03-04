package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.List;

public class Vampiro extends Creature{

    public Vampiro(int id, int team, String name, int x, int y) {
        super(id, team, name, x, y);
    }

    public String getCreatureName() {
        return "Vampiro";
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
        return false;
    }

    @Override
    public boolean isAdult() {
        return false;
    }

    @Override
    public boolean move(int xO, int yO, int xD, int yD, Board board, List<Creature> creatures,
                        List<Equipment> equipmentList,List<SafeHeaven> safeHeaven) {
        // Verifica se está no turno certo
        if (getTeam() != board.getCurrentTeamId()) {
            board.numInvalidPlaysZombie();
            return false;
        }


        if(board.isDay())
        {
            board.numInvalidPlaysZombie();
            return false;
        }

        // Verifica se a movimentação é para uma posição adjacente até 2 casas
        if (!((xD == xO && Math.abs(yD - yO) <= 1) ||
                (yD == yO && Math.abs(xD - xO) <= 1) ||
                (Math.abs(xD - xO) == Math.abs(yD - yO) && Math.abs(xD - xO) <= 1))) {
            board.numInvalidPlaysZombie();
            return false;
        }

        Creature creaturePosDestino = null;
        for (Creature creature : creatures) {
            if (creature.getX() == xD && creature.getY() == yD) {
                creaturePosDestino = creature;
                break;
            }
        }

        // Mover para cima de zombie
        if (creaturePosDestino != null && creaturePosDestino.isZombie()) {
            board.numInvalidPlaysZombie();
            return false;
        }

        if (creaturePosDestino != null && creaturePosDestino.isHuman()) {

            // Humano sem equipamento
            if (creaturePosDestino.getCapturedEquipment() == null){
                creaturePosDestino.infectHuman();
                board.nextTurn();
                board.resetTurnsCaptureOrTransformation();
                return true;
            }

            // Se possuir escudo ou espada passa o turno
            if (creaturePosDestino.getCapturedEquipment().isEscudo() || creaturePosDestino.getCapturedEquipment().isEspada()){
                board.nextTurn();
                board.turnWithoutCaptureOrTransformation();
                return true;
            }

            // Se possuir lixivia
            if (creaturePosDestino.getCapturedEquipment().isLixivia()){
                // Baixar quantidade de lixivia
                creaturePosDestino.getCapturedEquipment().spendBleach();
                if (creaturePosDestino.getCapturedEquipment().getAmountBleach() < 0.0){
                    creaturePosDestino.removeEquipment();
                    creaturePosDestino.infectHuman();
                    board.resetTurnsCaptureOrTransformation();
                } else {
                    board.turnWithoutCaptureOrTransformation();
                }
                board.nextTurn();
                return true;
            }

            // Se possuir pistola
            if (creaturePosDestino.getCapturedEquipment().isPistola()){
                // Baixar as balas
                creaturePosDestino.getCapturedEquipment().spendBullet();
                if (creaturePosDestino.getCapturedEquipment().getAmountBullets() < 0){
                    creaturePosDestino.removeEquipment();
                    creaturePosDestino.infectHuman();
                    board.resetTurnsCaptureOrTransformation();
                } else {
                    board.turnWithoutCaptureOrTransformation();
                }
                board.nextTurn();
                return true;
            }

        }

        // Verifica se exite equipamento na posição de destino
        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == xD && equipment.getY() == yD) {
                destroyEquipment();
                equipmentList.remove(equipment);
                break;
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
        return "Z:" + getId();
    }

    @Override
    public String getTeamName() {
        return "Zombie";
    }

    @Override
    public String getCreatureInfoAsString() {
        return getId() + " | " + getCreatureName() + " | " + getName() + " | -" + getNumDestroyedEquipments() + " @ (" + getX() + ", " + getY() + ")";

    }

}
