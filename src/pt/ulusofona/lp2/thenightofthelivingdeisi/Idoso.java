package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.List;

public class Idoso extends Creature {

    public Idoso(int id, int team, String name, int x, int y) {
        super(id, team, name, x, y);
    }

    public String getCreatureName() {
        return "Idoso";
    }

    private boolean isValidMove(int xO, int yO, int xD, int yD, Board board) {
        int dx = Math.abs(xD - xO);
        int dy = Math.abs(yD - yO);

        // Humanos só podem se mover durante o dia
        if (!board.isDay() && isHuman()) {
            return false;
        }

        // O movimento deve ser para uma posição adjacente diagonal
        return dx == 1 && dy == 1;
    }

    private void handleEquipment(int xO, int yO, int xD, int yD, List<Equipment> equipmentList) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == xD && equipment.getY() == yD) {

                // Caso já possua equipamento, deixa-o na casa de partida
                if (getCapturedEquipment() != null) {
                    Equipment currentEquipment = getCapturedEquipment(); // Obtém o equipamento capturado atualmente
                    removeEquipment(); // Remove o equipamento capturado do personagem
                    currentEquipment.setPosition(xO, yO); // Posiciona o equipamento na casa de partida
                    equipmentList.add(currentEquipment); // Adiciona o equipamento de volta à lista
                }

                // Captura ou destrói o equipamento na casa de chegada
                if (isHuman()) {
                    captureEquipment(equipment); // Captura o novo equipamento
                    equipmentList.remove(equipment); // Remove o equipamento capturado da lista
                } else {

                    destroyEquipment(); // Destrói o equipamento
                    equipmentList.remove(equipment); // Remove o equipamento destruído da lista
                }
                break;
            } else {
                if (getCapturedEquipment() != null){
                    Equipment currentEquipment = getCapturedEquipment(); // Obtém o equipamento capturado atualmente
                    removeEquipment(); // Remove o equipamento capturado do personagem
                    currentEquipment.setPosition(xO, yO); // Posiciona o equipamento na casa de partida
                    equipmentList.add(currentEquipment);
                    break;
                }

            }
        }
    }

    @Override
    public boolean isHuman() {
        if (isInfected()) {
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

        if (!isValidMove(xO, yO, xD, yD, board)){
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

        //Movimentos idoso humano para cima de outro humano
        if (isHuman() && creaturePosDestino != null && creaturePosDestino.isHuman()) {
            board.numInvalidPlaysHuman();
            return false;
        }

        //Moviemnto idoso humano para cima de zombie
        if (isHuman() && creaturePosDestino != null && creaturePosDestino.isZombie()) {
            board.numInvalidPlaysHuman();
            return false;
        }

        if (isZombie()) {
            // Mover para cima de zombie
            if (creaturePosDestino != null && creaturePosDestino.isZombie()) {
                board.numInvalidPlaysZombie();
                return false;
            }
            // Mover para cima de humano
            if (creaturePosDestino != null && creaturePosDestino.isHuman()) {

                // Humano sem equipamento
                if (creaturePosDestino.getCapturedEquipment() == null) {
                    creaturePosDestino.infectHuman();
                    board.nextTurn();
                    board.resetTurnsCaptureOrTransformation();
                    return true;
                }

                // Se possuir escudo ou espada passa o turno
                if (creaturePosDestino.getCapturedEquipment().isEscudo() || creaturePosDestino.getCapturedEquipment().isEspada()) {
                    board.nextTurn();
                    board.turnWithoutCaptureOrTransformation();
                    return true;
                }

                // Se possuir lixivia
                if (creaturePosDestino.getCapturedEquipment().isLixivia()) {
                    // Baixar quantidade de lixivia
                    creaturePosDestino.getCapturedEquipment().spendBleach();
                    if (creaturePosDestino.getCapturedEquipment().getAmountBleach() < 0.0) {
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
                if (creaturePosDestino.getCapturedEquipment().isPistola()) {
                    // Baixar as balas
                    creaturePosDestino.getCapturedEquipment().spendBullet();
                    if (creaturePosDestino.getCapturedEquipment().getAmountBullets() < 0) {
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
        }

        handleEquipment(xO, yO, xD, yD,equipmentList);

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
            if (getTeam() == 20) {
                return "H:" + getId();
            }
            if (getTeam() == 10) {
                return "Z:" + getId();
            }
        }
        return "";
    }

    @Override
    public String getTeamName() {
        if (getTeam() == 10) {
            if (isInfected()) {
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
        if (isHuman()) {
            if (getCapturedEquipment() == null) {
                if (getIsInSafeHeaven()) {
                    return getId() + " | " + getCreatureName() + " | " + getTeamName() + " | "
                            + getName() + " | +" + getCapturedEquipmentSize() + " @ Safe Haven";
                }
                return getId() + " | " + getCreatureName() + " | " + getTeamName() + " | "
                        + getName() + " | +" + getCapturedEquipmentSize() + " @ (" + getX() + ", " + getY() + ")";
            } else {
                if (getIsInSafeHeaven()) {
                    return getId() + " | " + getCreatureName() + " | " + getTeamName() + " | "
                            + getName() + " | +" + getCapturedEquipmentSize() + " @ Safe Haven"
                            + " | " + getCapturedEquipment().getEquipmentInfoAsString();
                }
                return getId() + " | " + getCreatureName() + " | " + getTeamName() + " | "
                        + getName() + " | +" + getCapturedEquipmentSize() + " @ (" + getX() + ", " + getY() + ")"
                        + " | " + getCapturedEquipment().getEquipmentInfoAsString();
            }
        } else {
            return getId() + " | " + getCreatureName() + " | " + getTeamName() + " | "
                    + getName() + " | -" + getNumDestroyedEquipments() + " @ (" + getX() + ", " + getY() + ")";
        }

    }

}
