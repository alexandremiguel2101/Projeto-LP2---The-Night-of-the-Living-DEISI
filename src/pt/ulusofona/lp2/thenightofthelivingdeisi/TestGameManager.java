package pt.ulusofona.lp2.thenightofthelivingdeisi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestGameManager {

    GameManager gameManager = new GameManager();
    List<Equipment> ListEquipment = new ArrayList<>();
    List<SafeHeaven> ListSafeHeaven = new ArrayList<>();
    Board b = new Board(7, 7, 10);

    @Test
    public void TestAdultoMovePegaPistolaEAtaca() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 1);
        Creature z = new Adulto(3, 20, "Alexandre-Zombie", 1, 4);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        creatures.add(z);
        Equipment e = new Pistola(-2, 1, 1, 2);
        ListEquipment.add(e);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do Adulto");
        assertTrue(c.move(1, 2, 1, 3, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para pistola");
        assertTrue(c.move(1, 3, 1, 4, b, creatures, ListEquipment, ListSafeHeaven), "Ataque a zombie mal(Adulto)");

    }

    @Test
    public void TestAdultoMovePegaEscudoEDefende() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 2);
        Creature z = new Adulto(3, 20, "Alexandre-Zombie", 1, 4);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        creatures.add(z);
        Equipment e = new Escudo(0, 0, 1, 3);
        ListEquipment.add(e);
        assertTrue(c.move(1, 2, 1, 3, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para pistola");
        if (z.isZombie()) {
            assertTrue(z.move(1, 4, 1, 3, b, creatures, ListEquipment, ListSafeHeaven), "Ataque de zombie mal");
        }


    }

    @Test
    public void TestVampiroMoveParaCimaDeZombie() {

        Creature c = new Vampiro(1, 20, "Duarte", 1, 1);
        Creature z = new Adulto(2, 20, "Pedro", 1, 2);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (!b.isDay()) {
            assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do Vampiro para cima de zombie");
        }

    }
    @Test
    public void TestCrincaMoveParaCimaDeVampiro() {

        Creature c = new Crianca(1, 10, "Diogo", 1, 1);
        Creature v = new Vampiro(1, 20, "Duarte", 1, 2);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal da Crianca (foi para cima de zombie)");
    }


    @Test
    public void TestIdosoMovePegaPistolaDepoisMove() {

        Creature c = new Idoso(1, 10, "Pedro", 1, 1);

        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        Equipment e = new Pistola(-2, 1, 1, 2);
        ListEquipment.add(e);
        if (b.isDay()) {

            assertTrue(c.move(1, 1, 2, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do Idoso");
            assertTrue(c.move(2, 2, 3, 3, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover idoso para pistola");
            assertTrue(c.move(3, 3, 4, 4, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover idoso para outra casa depois de pegar arma na anterior");
        }

    }

    @Test
    public void TestZombieAdultoMoveAtacaCrianca() {

        Creature z = new Adulto(1, 20, "Rafael", 1, 1);
        Creature c = new Crianca(2, 10, "Andre", 1, 3);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (z.isZombie()) {
            assertTrue(z.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do zombie Adulto");
            assertTrue(z.move(1, 2, 1, 3, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do zombie Adulto a mover para cima de crianca");
        }

    }

    @Test
    public void TestZombieCrincaMove() {

        Creature c = new Crianca(1, 10, "Andre", 1, 1);

        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (c.isZombie()) {
            assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do zombie Crinca");
        }

    }

    @Test
    public void TestZombieIdosoMove() {

        Creature c = new Idoso(1, 10, "Carlos", 1, 1);

        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (c.isZombie()) {
            assertTrue(c.move(1, 1, 2, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do zombie Idoso");
        }

    }

    @Test
    public void TestCaoMove() {

        Creature c = new Cao(1, 10, "Bobby", 1, 1);

        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal do Cao");
    }

    @Test
    public void TestAdultoMoveParaEspada() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 1);
        Equipment e = new Espada(-1, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para espada");
    }

    @Test
    public void TestAdultoMoveParaPistola() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 1);
        Equipment e = new Pistola(-2, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para pistola");
    }

    @Test
    public void TestAdultoMoveParaEscudo() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 1);
        Equipment e = new Escudo(-3, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para escudo");
    }

    @Test
    public void TestAdultoMoveParaLixiviaEDefende() {

        Creature c = new Adulto(1, 10, "Alexandre", 1, 1);
        Creature v = new Vampiro(1, 20, "Ze", 1, 3);
        Equipment e = new Lixivia(-4, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover adulto para lixivia");
        if(!b.isDay())
        {
            assertTrue(v.move(1, 3, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover vampiro para adulto com lixivia");
        }
    }

    @Test
    public void TestVampiroDestroiEspada() {

        Creature c = new Vampiro(1, 20, "Ze", 1, 1);
        Equipment e = new Espada(-4, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (!b.isDay()) {
            assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento mal ao mover Vampiro para destruir espada");
        }

    }

    @Test
    public void TestZombieAtacaCao() {

        Creature c = new Cao(1, 10, "Bobby", 1, 1);
        Creature z = new Adulto(1, 20, "Ze Antonio", 1, 2);
        Equipment e = new Espada(-4, 1, 1, 2);
        ListEquipment.add(e);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        if (z.isZombie()) {
            assertTrue(z.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento errado do zombie para cima de cao");
        }

    }

    @Test
    public void TestIdosoMoveDeDiaEPegaArma() {

        Creature c = new Idoso(1, 10, "Antonio", 1, 1);
        List<Creature> creatures = new ArrayList<>();
        creatures.add(c);
        Equipment e = new Espada(-4, 1, 2, 2);
        ListEquipment.add(e);
        if (!b.isDay()) {
            assertTrue(c.move(1, 1, 1, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento errado do idoso de noite");
        }

        assertTrue(c.move(1, 1, 2, 2, b, creatures, ListEquipment, ListSafeHeaven), "Movimento errado do idoso ao mover para arma");
    }




}

