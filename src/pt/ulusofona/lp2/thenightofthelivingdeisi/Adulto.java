package pt.ulusofona.lp2.thenightofthelivingdeisi;

import javax.swing.text.DefaultStyledDocument;
import java.util.List;

public class Adulto extends Creature {

    public Adulto(int id, int team, String name, int x, int y) {
        super(id, team, name, x, y);
    }

    public String getCreatureName() {
        return "Adulto";
    }

    private boolean isValidMove(int xO, int yO, int xD, int yD) {

        return (xD == xO && Math.abs(yD - yO) <= 2) ||
                (yD == yO && Math.abs(xD - xO) <= 2) ||
                (Math.abs(xD - xO) == Math.abs(yD - yO) && Math.abs(xD - xO) <= 2);
    }

    private void handleEquipment(int xO, int yO, int xD, int yD, List<Equipment> equipmentList) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == xD && equipment.getY() == yD) {
                if (getCapturedEquipment() != null) {
                    Equipment currentEquipment = getCapturedEquipment();
                    removeEquipment();
                    currentEquipment.setPosition(xO, yO);
                    equipmentList.add(currentEquipment);
                }

                if (isHuman()) {
                    captureEquipment(equipment);
                } else {
                    destroyEquipment();
                }
                equipmentList.remove(equipment);
                break;
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
        return true;
    }

    private boolean isPositionFree(int x, int y, List<Creature> creatures, List<SafeHeaven> safeHeavens, List<Equipment> equipmentList, Board board) {

        // Verifica se já existe uma criatura na posição (x, y)
        for (Creature e : creatures) {
            if (e.getX() == x && e.getY() == y) {
                return false; // A posição está ocupada
            }
        }
        // Verifica se já existe um safeHeaven na posição (x, y)
        for (SafeHeaven sh : safeHeavens) {
            if (sh.getX() == x && sh.getY() == y) {
                return false; // A posição está ocupada
            }
        }

        // Verifica se já existe um equipamento na posição (x, y)
        for (Equipment eq : equipmentList) {
            if (eq.getX() == x && eq.getY() == y) {
                return false; // A posição está ocupada
            }
        }

        return true; // A posição está livre
    }

    @Override
    public boolean move(int xO, int yO, int xD, int yD, Board board, List<Creature> creatures,
                        List<Equipment> equipmentList, List<SafeHeaven> safeHeavens) {


        if (!isValidMove(xO, yO, xD, yD)) {
            board.numInvalidPlaysHuman();
            return false;
        }

        if (xO == xD && yO == yD) {
            if (isZombie()) {
                board.numInvalidPlaysZombie();
            } else {
                board.numInvalidPlaysHuman();
            }
            return false;
        }

        Creature creaturePosDestino = getCreatureAtPosition(xD, yD, creatures);

        // Movimentos de um humano para cima de outro humano
        if (isHuman() && creaturePosDestino != null && creaturePosDestino.isHuman()) {
            if (getCapturedEquipment() != null || creaturePosDestino.getCapturedEquipment() != null) {
                board.numInvalidPlaysHuman();
                return false;
            }

            if (!creaturePosDestino.isAdult()) {
                board.numInvalidPlaysHuman();
                return false;
            }
            Creature c = null;
            String id = Integer.toString(getCreatureAtPosition(xO, yO, creatures).getId()) + Integer.toString(creaturePosDestino.getId());
            int idInt = Integer.parseInt(id);

            // Verifica se já existe uma criatura com id
            for (Creature e : creatures) {
                if (e.getId() == idInt) {
                    board.numInvalidPlaysHuman();
                    return false;
                }
            }

            // Verificar se as posições ao redor estão livres (esquerda, cima, direita e baixo)
            if (isPositionFree(xO - 1, yO, creatures, safeHeavens, equipmentList, board) && (xO - 1 < board.getRows() && yO < board.getColumns())) { // Esquerda
                c = new Crianca(idInt, 20, getCreatureAtPosition(xO, yO, creatures).getName() + " & " + creaturePosDestino.getName(), xO - 1, yO);
            } else if (isPositionFree(xO, yO - 1, creatures, safeHeavens, equipmentList, board) && (xO < board.getRows() && yO - 1 < board.getColumns())) { // Cima
                c = new Crianca(idInt, 20, getCreatureAtPosition(xO, yO, creatures).getName() + " & " + creaturePosDestino.getName(), xO, yO - 1);
            } else if (isPositionFree(xO + 1, yO, creatures, safeHeavens, equipmentList, board) && (xO + 1 < board.getRows() && yO < board.getColumns())) { // Direita
                c = new Crianca(idInt, 20, getCreatureAtPosition(xO, yO, creatures).getName() + " & " + creaturePosDestino.getName(), xO + 1, yO);
            } else if (isPositionFree(xO, yO + 1, creatures, safeHeavens, equipmentList, board) && (xO < board.getRows() && yO + 1 < board.getColumns())) { // Baixo
                c = new Crianca(idInt, 20, getCreatureAtPosition(xO, yO, creatures).getName() + " & " + creaturePosDestino.getName(), xO, yO + 1);
            }

            // Se uma nova criatura foi criada, adiciona ela à lista de criaturas e avança o turno
            if (c != null) {
                creatures.add(c);
                board.nextTurn();
                return true;
            }
            board.numInvalidPlaysHuman();
            return false;
        }


        //Moviemnto adulto humano para cima de zombie
        if (isHuman() && creaturePosDestino != null && creaturePosDestino.isZombie()) {
            if (getCapturedEquipment() != null && (getCapturedEquipment().isPistola() || getCapturedEquipment().isEspada())) {
                if (getCapturedEquipment().isPistola() && getCapturedEquipment().getAmountBullets() != 0) {
                    getCapturedEquipment().spendBullet();
                    if (getCapturedEquipment().getAmountBullets() < 0) {
                        removeEquipment();
                    }
                }
                creatures.remove(creaturePosDestino);
                setPosition(xD, yD);
                getCapturedEquipment().setPosition(xD, yD);
                board.resetTurnsCaptureOrTransformation();
                board.nextTurn();
                return true;
            } else {
                board.numInvalidPlaysHuman();
                return false;
            }
        }

        if (isZombie()) {

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

        handleEquipment(xO, yO, xD, yD, equipmentList);

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

        if (getCapturedEquipment() != null) {
            getCapturedEquipment().setPosition(xD, yD);
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
