package pt.ulusofona.lp2.thenightofthelivingdeisi;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameManager {

    private Board board;
    private ArrayList<Creature> creatures;
    private ArrayList<Equipment> equipmentList;
    private ArrayList<SafeHeaven> safeHeavens;
    private final Map<Creature, Integer> creatureTypes = new HashMap<>();


    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {

        board = null;creatures = new ArrayList<>();equipmentList = new ArrayList<>();safeHeavens = new ArrayList<>();

        int lineWithError = 1;

        try (Scanner scanner = new Scanner(file)) {
            // Valida se o arquivo está vazio
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException(lineWithError);
            }

            // Ler dimensões do tabuleiro
            String[] boardDimensions = scanner.nextLine().split(" ");
            if (boardDimensions.length != 2) {
                throw new InvalidFileException(lineWithError);
            }
            int rows = Integer.parseInt(boardDimensions[0]);int columns = Integer.parseInt(boardDimensions[1]);
            lineWithError++;
            // Ler a equipe que joga primeiro
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException(lineWithError);
            }
            int initialTeamId = Integer.parseInt(scanner.nextLine());
            if (initialTeamId != 10 && initialTeamId != 20) {
                throw new InvalidFileException(lineWithError);
            }
            lineWithError++;
            // Inicializa o tabuleiro
            board = new Board(rows, columns, initialTeamId);
            // Ler quantidade de criaturas
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException(lineWithError);
            }
            int numCreatures = Integer.parseInt(scanner.nextLine());
            if (numCreatures < 0) {
                throw new InvalidFileException(lineWithError);
            }
            lineWithError++;
            // Ler criaturas
            for (int i = 0; i < numCreatures; i++) {

                String[] creatureData = scanner.nextLine().split(" : ");
                if (creatureData.length != 6 || !scanner.hasNextLine()) {
                    throw new InvalidFileException(lineWithError);
                }

                int id = Integer.parseInt(creatureData[0]);int team = Integer.parseInt(creatureData[1]);
                int type = Integer.parseInt(creatureData[2]);String name = creatureData[3];
                int row = Integer.parseInt(creatureData[4]);int col = Integer.parseInt(creatureData[5]);

                // Validações
                if (team != 10 && team != 20) {
                    throw new InvalidFileException(lineWithError);
                }
                if ((type == 3 && team == 10) || (type == 4 && team == 20)) {
                    throw new InvalidFileException(lineWithError);
                }

                // Criar a criatura
                Creature creature = switch (type) {
                    case 0 -> new Crianca(id, team, name, row, col);case 1 -> new Adulto(id, team, name, row, col);
                    case 2 -> new Idoso(id, team, name, row, col);case 3 -> new Cao(id, team, name, row, col);
                    case 4 -> new Vampiro(id, team, name, row, col);default -> throw new InvalidFileException(lineWithError);
                };
                creatureTypes.put(creature, Integer.valueOf(type));
                creatures.add(creature);
                lineWithError++;
            }

            // Ler quantidade de equipamentos
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException(lineWithError);
            }

            int numEquipment = Integer.parseInt(scanner.nextLine());
            lineWithError++;

            // Ler equipamentos
            for (int i = 0; i < numEquipment; i++) {
                if (!scanner.hasNextLine()) {
                    throw new InvalidFileException(lineWithError);
                }

                String[] equipmentData = scanner.nextLine().split(" : ");
                if (equipmentData.length != 4) {
                    throw new InvalidFileException(lineWithError);
                }

                int id = Integer.parseInt(equipmentData[0]);int type = Integer.parseInt(equipmentData[1]);
                int x = Integer.parseInt(equipmentData[2]);int y = Integer.parseInt(equipmentData[3]);

                // Criar o equipamento
                Equipment equipment = switch (type) {
                    case 0 -> new Escudo(id, type, x, y);case 1 -> new Espada(id, type, x, y);
                    case 2 -> new Pistola(id, type, x, y);case 3 -> new Lixivia(id, type, x, y);
                    default -> throw new InvalidFileException(lineWithError);
                };
                equipmentList.add(equipment);
                lineWithError++;
            }

            // Ler quantidade de SafeHeavens
            if (scanner.hasNextLine()) {

            int numSafeHeavens = Integer.parseInt(scanner.nextLine());
            lineWithError++;

            // Ler SafeHeavens
                for (int i = 0; i < numSafeHeavens; i++) {
                    if (!scanner.hasNextLine()) {
                        throw new InvalidFileException(lineWithError);
                    }

                    String[] safeHeavenData = scanner.nextLine().split(" : ");
                    if (safeHeavenData.length != 2) {
                        throw new InvalidFileException(lineWithError);
                    }
                    int x = Integer.parseInt(safeHeavenData[0]);int y = Integer.parseInt(safeHeavenData[1]);
                    // Validações de coordenadas
                     if (x < 0 || x >= rows || y < 0 || y >= columns) {
                     throw new InvalidFileException(lineWithError);
                     }

                safeHeavens.add(new SafeHeaven(x, y));
                lineWithError++;
                }
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidFileException(lineWithError);
        }
    }

    public int[] getWorldSize() {
        if (board != null) {
            return board.getWorldSize();
        }
        return new int[]{0, 0};
    }

    public int getInitialTeamId() {
        return board.getInitialTeamId();
    }

    public int getCurrentTeamId() {
        return board.getCurrentTeamId();
    }

    public boolean isDay() {
        if (board != null){
            return board.isDay();
        }
        return true;
    }

    public String getSquareInfo(int x, int y) {

        if (x < 0 || x > board.getColumns() || y < 0 || y > board.getRows()){
            return null;
        }

        if (board == null) {
            return "";
        }

        for (Creature creature : creatures) {
            if (creature.getX() == x && creature.getY() == y) {
               return creature.getSquareInfo();
            }
        }

        for (Equipment equipment : equipmentList) {
            if (equipment.getX() == x && equipment.getY() == y) {
                return equipment.getSquareInfo();
            }
        }

        for (SafeHeaven safeHeaven : safeHeavens) {
            if (safeHeaven.getX() == x && safeHeaven.getY() == y) {
                return safeHeaven.getSquareInfo();
            }
        }

        return "";
    }

    public String[] getCreatureInfo(int id) {

        for (Creature creature : creatures) {
            if (creature.getId() == id) {
                return creature.getCreatureInfo();
            }
        }
        return null;
    }

    public String getCreatureInfoAsString(int id) {

        for (Creature creature : creatures) {
            if (creature.getId() == id) {
                return creature.getCreatureInfoAsString();
            }
        }
        return null;
    }

    public String[] getEquipmentInfo(int id) {

        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                return equipment.getEquipmentInfo();
            }
        }
        return null;
    }

    public String getEquipmentInfoAsString(int id) {

        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                return equipment.getEquipmentInfoAsString();
            }
        }
        return null;
    }

    public boolean hasEquipment(int creatureId, int equipmentTypeId) {

        for (Creature creature : creatures) {
            if (creature.getId() == creatureId) {
                return creature.hasEquipment(equipmentTypeId);
            }
        }
        return false;
    }

    public boolean move(int xO, int yO, int xD, int yD) {
        if (xD < 0 || yD < 0 || xD >= board.getColumns() || yD >= board.getRows()) {
            return false;
        }

        for (Creature creature : creatures) {
            if (creature.getX() == xO && creature.getY() == yO) {

                // Verifica se está no turno certo
                if (creature.getTeam() != board.getCurrentTeamId()) {
                    if(creature.isHuman())
                    {
                        board.numInvalidPlaysHuman();
                    }
                    else {
                        board.numInvalidPlaysZombie();
                    }
                    return false;
                }

                return creature.move(xO, yO, xD, yD, board, creatures, equipmentList, safeHeavens); // Implementar movimentos das criaturas nas respectivas classes
            }
        }

        return false;
    }

    public boolean gameIsOver() {

        if (board != null){
            return board.gameIsOver(creatures);
        }

        return true;
    }

    public ArrayList<String> getSurvivors() {
        ArrayList<String> resultado = new ArrayList<>();

        resultado.add("Nr. de turnos terminados:");

        resultado.add(String.valueOf(board.getTurn()-1));
        resultado.add("Nr. de jogadas invalidas:");
        resultado.add("humanos:"+ String.valueOf(board.getNumInvalidPlaysHuman())+" zombies:"+String.valueOf(board.getNumInvalidPlaysZombie()));

        resultado.add("");

        resultado.add("OS VIVOS");
        for (Creature creature : creatures) {
            if (creature.isHuman()) {
                resultado.add(creature.getId() + " " + creature.getName());
            }
        }

        resultado.add("");

        resultado.add("OS OUTROS");
        for (Creature creature : creatures) {
            if (!creature.isHuman()) {
                resultado.add(creature.getId() + " (antigamente conhecido como " + creature.getName() + ")");
            }
        }
        resultado.add("-----");

        return resultado;
    }

    public JPanel getCreditsPanel() {

        JPanel creditsPanel = new JPanel();

        creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));
        creditsPanel.setBackground(Color.BLACK); // Define a cor de fundo para dar contraste

        JLabel title = new JLabel("╔═══╗  The Night of the Living DEISI  ╔═══╗");
        title.setFont(new Font("Monospaced", Font.BOLD, 26));
        title.setForeground(Color.CYAN); // Texto em ciano
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel("║ Desenvolvido por: Alexandre Oliveira e Rafael Martins ║");
        name.setFont(new Font("Monospaced", Font.PLAIN, 22));
        name.setForeground(Color.LIGHT_GRAY);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel date = new JLabel("║ Ano: 2024 ║");
        date.setFont(new Font("Monospaced", Font.PLAIN, 22));
        date.setForeground(Color.LIGHT_GRAY);
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel course = new JLabel("║ Projeto de LP2 - Universidade Lusófona ║");
        course.setFont(new Font("Monospaced", Font.PLAIN, 22));
        course.setForeground(Color.LIGHT_GRAY);
        course.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel symbolLineTop = new JLabel("╚════════════════════════════════════════════════╝");
        symbolLineTop.setFont(new Font("Monospaced", Font.PLAIN, 20));
        symbolLineTop.setForeground(Color.CYAN);
        symbolLineTop.setAlignmentX(Component.CENTER_ALIGNMENT);

        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        creditsPanel.add(title);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creditsPanel.add(symbolLineTop);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        creditsPanel.add(name);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creditsPanel.add(date);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creditsPanel.add(course);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return creditsPanel;
    }

    public HashMap<String,String> customizeBoard() {
        return null;
    }

    public void saveGame(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Escrever dimensões do tabuleiro
            writer.write(board.getRows() + " " + board.getColumns());
            writer.newLine();

            // Escrever o ID da equipe que joga primeiro
            writer.write(String.valueOf(board.getInitialTeamId()));
            writer.newLine();

            // Escrever a quantidade de criaturas
            writer.write(String.valueOf(creatures.size()));
            writer.newLine();

            // Escrever os dados das criaturas
            for (Creature creature : creatures) {
                int type = creatureTypes.get(creature); // Recuperar o tipo do mapa

                writer.write(creature.getId() + " : " +
                        creature.getTeam() + " : " +
                        type + " : " +
                        creature.getName() + " : " +
                        creature.getX() + " : " +
                        creature.getY());
                writer.newLine();
            }

            // Escrever a quantidade de equipamentos
            writer.write(String.valueOf(equipmentList.size()));
            writer.newLine();

            // Escrever os dados dos equipamentos
            for (Equipment equipment : equipmentList) {
                writer.write(equipment.getId() + " : " +
                        equipment.getType() + " : " +
                        equipment.getX() + " : " +
                        equipment.getY());
                writer.newLine();
            }

            // Escrever a quantidade de SafeHeavens
            writer.write(String.valueOf(safeHeavens.size()));
            writer.newLine();

            // Escrever as coordenadas dos SafeHeavens
            for (SafeHeaven safeHeaven : safeHeavens) {
                writer.write(safeHeaven.getX() + " : " + safeHeaven.getY());
                writer.newLine();
            }
        }
    }



    public List<Integer> getIdsInSafeHaven() {

        List<Integer> list = new ArrayList<>();

        if (safeHeavens == null) {
            return list;
        }

        for (SafeHeaven safeHeaven : safeHeavens){
            list.addAll(safeHeaven.getIdsInSafeHeaven());
        }

        return list;
    }

}
