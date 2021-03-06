package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.WindowDatabase;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfYellowObjective {
    private Square[][] matrix;
    private WindowDatabase windowDatabase;
    private ShadesOfYellowObjective shadesOfYellowObjective;
    private Player player;

    @Before
    public void init() {
        windowDatabase = new WindowDatabase();
        windowDatabase.standardWhiteMatrix();
        matrix = windowDatabase.getMatrix();
        shadesOfYellowObjective =ShadesOfYellowObjective.instance();
        shadesOfYellowObjective =ShadesOfYellowObjective.instance();
        Window window = new Window("BasicMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfYellowObjective);
    }

    @Test
    public void testEvalPoints() {
        assertEquals(0, shadesOfYellowObjective.evalPoints(player));
        windowDatabase.sixSameColoredDice(Color.YELLOW);
        matrix = windowDatabase.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfYellowObjective);
        assertEquals(21, shadesOfYellowObjective.evalPoints(player));
    }

    @Test
    public void testToString() {
        String result = "Private Objective:" + "\n" + "Title: \"" + "Shades of Yellow\"      Effect: \"" + "Sum of values on yellow dice" + "\"";
        Assert.assertEquals(result,shadesOfYellowObjective.toString());
    }
}
