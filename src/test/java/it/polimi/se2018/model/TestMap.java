package it.polimi.se2018.model;

import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

public class TestMap {
    private String title;
    private int level;
    private String imagePath;
    private Square[][] matrix = new Square[4][5];
    private Map map;

    @Before
    public void init() {
        title = "Title";
        imagePath = "Image path";
        Random random = new Random();
        level = random.nextInt(7);
        for (int i = 0; i<4; i++) {
            for (int j = 0; j<5; j++) {
                Color color = Color.values()[random.nextInt(6)];
                int value = random.nextInt(6)+1;
                Coordinate coordinate = new Coordinate(i, j);
                Square square = new Square(color, value, coordinate);
                matrix[i][j] = square;
            }
        }
        map = new Map(title, level, imagePath, matrix);
    }

    @Test
    public void testGetTitle() {
        Assert.assertEquals(title, map.getTitle());
        Assert.assertNotEquals("Not title", map.getTitle());
    }

    @Test
    public void testGetLevel() {
        Assert.assertEquals(level, map.getLevel());
        Assert.assertNotEquals(level + 1, map.getLevel());
    }

    @Test
    public void testGetImagePath() {
        Assert.assertEquals(imagePath, map.getImagePath());
        Assert.assertNotEquals("Not image path", map.getImagePath());
    }

    @Test
    public void testGetSquare() {
        Random random = new Random();
        Coordinate coordinate = new Coordinate(random.nextInt(4), random.nextInt(5));
        Assert.assertEquals(matrix[coordinate.getRow()][coordinate.getCol()], map.getSquare(coordinate));
        if (coordinate.getCol() < 3){
            Assert.assertNotEquals(matrix[coordinate.getRow()][coordinate.getCol() + 1], map.getSquare(coordinate));
        }
        else {
            Assert.assertNotEquals(matrix[coordinate.getRow()][coordinate.getCol() - 1], map.getSquare(coordinate));
        }
    }

    @Test
    public void testGetRows() {
        Assert.assertEquals(matrix.length, map.getRows());
    }

    @Test
    public void testGetCols() {
        Assert.assertEquals(matrix[0].length, map.getCols());
    }

    @Test
    public void testGetDie() {
        Random random = new Random();
        Color colorOne = Color.values()[random.nextInt(6)];
        int valueOne = random.nextInt(6)+1;
        Die dieOne = new Die(valueOne,colorOne);
        Color colorTwo = Color.values()[random.nextInt(6)];
        int valueTwo = random.nextInt(6)+1;
        Die dieTwo = new Die(valueTwo,colorTwo);
        Coordinate coordinate = new Coordinate(random.nextInt(4), random.nextInt(5));
        Assert.assertNotEquals(dieOne, map.getDie(coordinate));
        matrix[coordinate.getRow()][coordinate.getCol()].setDie(dieOne);
        Assert.assertEquals(dieOne, map.getDie(coordinate));
        Assert.assertNotEquals(dieTwo, map.getDie(coordinate));
    }

}