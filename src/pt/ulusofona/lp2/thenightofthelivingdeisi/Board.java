package pt.ulusofona.lp2.thenightofthelivingdeisi;

import java.util.ArrayList;

public class Board {
    private final int rows;
    private final int columns;
    private int turn;
    private final int initialTeamId;
    private int currentTeamId;
    private int numTurnsWithoutCaptureorTranformation = 0;
    private int numInvalidPlaysHuman=0;
    private int numInvalidPlaysZombie=0;

    public Board(int rows, int columns, int initialTeamId) {
        this.rows = rows;
        this.columns = columns;
        this.turn = 1;
        this.initialTeamId = initialTeamId;
        this.currentTeamId = initialTeamId;
    }

    public void resetTurnsCaptureOrTransformation() {
        this.numTurnsWithoutCaptureorTranformation = 0;
    }

    public void turnWithoutCaptureOrTransformation() {
        this.numTurnsWithoutCaptureorTranformation++;
    }
    public void numInvalidPlaysHuman(){this.numInvalidPlaysHuman++;}
    public  void numInvalidPlaysZombie(){this.numInvalidPlaysZombie++;}
    public void resetNumInvalidPlaysHuman(){this.numInvalidPlaysHuman=0;}
    public void resetNumInvalidPlaysZombie(){this.numInvalidPlaysZombie=0;}
    public int getNumInvalidPlaysHuman() {
        return numInvalidPlaysHuman;
    }
    public int getNumInvalidPlaysZombie() {
        return numInvalidPlaysZombie;
    }
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getInitialTeamId() {
        return initialTeamId;
    }

    public int getCurrentTeamId() {
        return currentTeamId;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isDay() {
        int turno = getTurn();
        int mod = turno % 4;
        return mod == 1 || mod == 2;
    }

    public int[] getWorldSize(){
        return new int[]{getRows(), getColumns()};
    }

    public boolean gameIsOver(ArrayList<Creature> creatures) {
        boolean hasPassedEnoughTurns = numTurnsWithoutCaptureorTranformation > 7;
        boolean hasPassedInvalidPlaysHuman=numInvalidPlaysHuman >= 6;
        boolean hasPassedInvalidPlaysZombie=numInvalidPlaysZombie >= 6;
        boolean allSameTeam = true;
        Integer firstTeam = null;

        // Itera pelas criaturas para validar as condições
        for (Creature creature : creatures) {
            if (!creature.getSquareInfo().isEmpty()) {
                // Define a equipa de referência (primeira equipa encontrada)
                if (firstTeam == null) {
                    firstTeam = creature.getTeam();
                }
                // Verifica se todas as criaturas válidas pertencem à mesma equipa
                else if (creature.getTeam() != firstTeam) {
                    allSameTeam = false;
                }
            }
        }

        // Verifica se todas as condições foram atendidas antes de retornar
        return hasPassedEnoughTurns || allSameTeam || (hasPassedInvalidPlaysHuman || hasPassedInvalidPlaysZombie);
    }




    public void setTurn(int turn){
        this.turn = turn;
    }

    public void nextTurn() {

        // Altera Equipa que joga a seguir
        if (currentTeamId == 10) {
            currentTeamId = 20;
        } else {
            currentTeamId = 10;
        }

        turn++;
    }

}


