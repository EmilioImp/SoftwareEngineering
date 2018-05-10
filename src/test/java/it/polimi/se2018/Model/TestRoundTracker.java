package it.polimi.se2018.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRoundTracker {
    private int roundsNumber;
    private RoundTracker roundTracker;
    private ArrayList<Die> dice1;
    private ArrayList<Die> dice2;

    @Before
    public void init(){
        Random random = new Random();
        roundsNumber = random.nextInt();
        roundTracker = new RoundTracker(roundsNumber);
        Die die1 = new Die(3,Color.GREEN);
        Die die2 = new Die(5,Color.BLUE);
        Die die3 = new Die(1,Color.PURPLE);
        Die die4 = new Die(6,Color.RED);
        dice1 = new ArrayList<>();
        dice1.add(die1);
        dice1.add(die2);
        dice1.add(die3);
        dice1.add(die4);
        roundTracker.updateRoundTracker(dice1);
        Die die5 = new Die(4,Color.RED);
        Die die6 = new Die(3,Color.BLUE);
        Die die7 = new Die(1,Color.YELLOW);
        Die die8 = new Die(5,Color.GREEN);
        dice2 = new ArrayList<>();
        dice2.add(die1);
        dice2.add(die2);
        dice2.add(die3);
        dice2.add(die4);
        roundTracker.updateRoundTracker(dice2);
    }

    @Test
    public void testGetDie(){
        for(int i=0;i<4;i++){
            Assert.assertEquals(dice1.get(i),roundTracker.getDie(0,i));
        }
        for(int i=0;i<4;i++){
            Assert.assertEquals(dice2.get(i),roundTracker.getDie(1,i));
        }
    }

    @Test
    public void testContains() {
        assertTrue(roundTracker.contains(dice1.get(2)));
        assertTrue(!roundTracker.contains(new Die(4, Color.BLUE)));
    }

    @Test
    public void testRemoveFromRoundTracker(){
        Die die = roundTracker.getDie(0,3);
        roundTracker.removeFromRoundTracker(die);
        assertTrue(!roundTracker.contains(die));
    }

    @Test
    public void testAddToRoundTracker(){
        Die die = new Die(3,Color.RED);
        roundTracker.addToRoundTracker(2,die);
        assertEquals(die,roundTracker.getDie(2,0));
    }
}
