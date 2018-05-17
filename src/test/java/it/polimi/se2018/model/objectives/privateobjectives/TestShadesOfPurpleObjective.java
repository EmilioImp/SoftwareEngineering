package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.Database;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfPurpleObjective {
    private Square[][] matrix;
    private Database database;
    private ShadesOfPurpleObjective shadesOfPurpleObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        shadesOfPurpleObjective=ShadesOfPurpleObjective.instance("imagePath","title");
        Window window = new Window("BasicMap",0,matrix);
        player = new Player("name",1, window,shadesOfPurpleObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfPurpleObjective.evalPoints(player));
        database.sixSameColoredDice(Color.PURPLE);
        matrix = database.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix);
        player = new Player("name",1, window,shadesOfPurpleObjective);
        assertEquals(21,shadesOfPurpleObjective.evalPoints(player));
    }
}
