package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.List;

public class Crianca extends Creature{

    public Crianca(int id, int team, String name, int x, int y) {
        super(id, team, name, x, y);
    }

    public String getCreatureName() {
        return "Criança";
    }

    private boolean isValidMove(int xO, int yO, int xD, int yD, Board board) {
        // Verifica se a movimentação é para uma posição adjacente
        return (xD == xO && (yD == yO + 1 || yD == yO - 1)) ||
                (yD == yO && (xD == xO + 1 || xD == xO - 1));
    }

    private void handleEquipment(int xO, int yO, int xD, int yD, List<Equipment> equipmentList) {

    }

    @Override
    public boolean isHuman() {
        if (isInfected()){
            return false;
        }
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
                        List<Equipment> equipmentList, List<SafeHeaven> safeHeavens) {

        // Verifica se a movimentação é para uma posição adjacente
        if (!isValidMove(xO, yO, xD, yD, board)) {
            if(isZombie())
            {
                board.numInvalidPlaysZombie();
            }
            else {
                board.numInvalidPlaysHuman();
            }
            return false;
        }

        Creature creaturePosDestino = getCreatureAtPosition(xD, yD, creatures);

        //Movimentos criança humana para cima de outra criatura
        if (isHuman() && creaturePosDestino != null){
            board.numInvalidPlaysHuman();
            return false;
        }

        // movimento criança zombie
        // Verifica se existe criatura na posição de destino, se for zombie -> false,
        // se for humano -> true mas nao movimenta (transforma pos destino) - se nao tiver equipamento
        // se tiver escudo ou espada -> passa o turno e nao movimenta,
        // se tiver pistola ou lixivia -> -1 bala ou -0.3 lixivia
        //Movimentos criança zombie para cima de outra criatura
        if (isZombie()){
            // Mover para cima de zombie
            if (creaturePosDestino != null && creaturePosDestino.isZombie()) {
                board.numInvalidPlaysZombie();
                return false;
            }

            // Mover para cima de cao
            if (creaturePosDestino != null && creaturePosDestino.isDog()) {
                board.numInvalidPlaysZombie();
                return false;
            }

            // Mover para cima de humano
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
                    if (creaturePosDestino.getCapturedEquipment().getAmountBleach() < 0){
                        creaturePosDestino.removeEquipment();
                        creaturePosDestino.infectHuman();
                        board.nextTurn();
                        board.resetTurnsCaptureOrTransformation();
                    } else {
                        board.nextTurn();
                        board.turnWithoutCaptureOrTransformation();
                    }
                    return true;
                }

                // Se possuir pistola
                if (creaturePosDestino.getCapturedEquipment().isPistola()){
                    // Baixar as balas
                    creaturePosDestino.getCapturedEquipment().spendBullet();
                    if (creaturePosDestino.getCapturedEquipment().getAmountBullets() < 0){
                        creaturePosDestino.removeEquipment();
                        creaturePosDestino.infectHuman();
                        board.nextTurn();
                        board.resetTurnsCaptureOrTransformation();
                    } else {
                        board.nextTurn();
                        board.turnWithoutCaptureOrTransformation();
                    }
                    return true;
                }

            }
        }
        // Verifica se exite equipamento na posição de destino
        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == xD && equipment.getY() == yD) {
                if (isHuman()){
                    if (equipment.isDefensive()){ // Crianças apenas podem capturar equipamentos defensivos
                        captureEquipment(equipment);
                        equipmentList.remove(equipment);
                    } else {
                        board.numInvalidPlaysHuman();
                        return false; // Não pode capturar equipamento ofensivo
                    }
                } else {
                    destroyEquipment();
                    equipmentList.remove(equipment);
                }
                break;
            }
        }
        for (SafeHeaven safeHeaven : safeHeavens) {
            if (safeHeaven.getX() == xD && safeHeaven.getY() == yD) {
                if (isHuman()) {
                    safeHeaven.addIdInSafeHeaven(getId());
                    enterSafeHeaven();
                    board.nextTurn();
                    board.turnWithoutCaptureOrTransformation();
                    return true;
                } else {
                    board.numInvalidPlaysZombie();
                    return false;
                }
            }
        }
        if (getCapturedEquipment() != null){
            getCapturedEquipment().setPosition(xD, yD);
        }
        setPosition(xD, yD);
        board.nextTurn();
        board.turnWithoutCaptureOrTransformation();
        return true;
    }

    @Override
    public boolean isValidEquipment(Equipment equipment) {
        return equipment.isDefensive();
    }

    @Override
    public String getSquareInfo() {
        if (!getIsInSafeHeaven()){
            if (getTeam() == 20){
                return  "H:" + getId();
            }
            if (getTeam() == 10) {
                return "Z:" + getId();
            }
        }
        return "";
    }

    @Override
    public String getTeamName() {
        if (getTeam() == 10){
            if (isInfected()){
                return "Zombie (Transformado)";
            } else {
                return "Zombie";
            }
        } else {
            return "Humano";
        }
    }

    @Override
    public String getCreatureInfoAsString() {
        if (isHuman()){
            if (getCapturedEquipment() == null){
                if (getIsInSafeHeaven()) {
                    return getId() + " | " +  getCreatureName() + " | " + getTeamName() + " | "
                            + getName() + " | +" + getCapturedEquipmentSize() + " @ Safe Haven";
                }
                return getId() + " | " +  getCreatureName() + " | " + getTeamName() + " | "
                        + getName() + " | +" + getCapturedEquipmentSize() + " @ (" + getX() + ", " + getY() + ")";
            } else {
                if (getIsInSafeHeaven()) {
                    return getId() + " | " +  getCreatureName() + " | " + getTeamName() + " | "
                            + getName() + " | +" + getCapturedEquipmentSize() + " @ Safe Haven"
                            + " | " + getCapturedEquipment().getEquipmentInfoAsString();
                }
                return getId() + " | " +  getCreatureName() + " | " + getTeamName() + " | "
                        + getName() + " | +" + getCapturedEquipmentSize() + " @ (" + getX() + ", " + getY() + ")"
                        + " | " + getCapturedEquipment().getEquipmentInfoAsString();
            }
        } else {
            return getId() + " | " +  getCreatureName() + " | " + getTeamName() + " | "
                    + getName() + " | -" + getNumDestroyedEquipments() + " @ (" + getX() + ", " + getY() + ")";
        }

    }

}
