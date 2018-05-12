package it.polimi.se2018;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.MapBuilder;
import javafx.util.Pair;

import java.util.List;

public class Database {
    private Square[][] matrix;
    private List<Pair<Map,Map>> defaultMaps;

    public Database() {
        defaultMaps = MapBuilder.create();
    }

    public List<Pair<Map,Map>> getDefaultMaps() {return defaultMaps;}

    public Square[][] getMatrix() {
        return matrix;
    }

    public void standardWhiteMatrix(){
        matrix = new Square[4][5];
        for(int row=0;row<4;row++){
            for(int col=0;col<5;col++){
                matrix[row][col] = new Square(Color.WHITE,0,new Coordinate(row,col));
            }
        }
    }

    public void sixSameColoredDice(Color color){
        standardWhiteMatrix();
        matrix[1][2].setDie(new Die(2, color));
        matrix[0][3].setDie(new Die(4, color));
        matrix[2][1].setDie(new Die(5, color));
        matrix[3][4].setDie(new Die(3, color));
        matrix[0][1].setDie(new Die(5, color));
        matrix[1][4].setDie(new Die(2, color));

    }

    public void initMatrixFullOfDice(){
        standardWhiteMatrix();
        matrix[0][0].setDie(new Die(1, Color.YELLOW));
        matrix[0][1].setDie(new Die(2, Color.RED));
        matrix[0][2].setDie(new Die(3, Color.PURPLE));
        matrix[0][3].setDie(new Die(4, Color.GREEN));
        matrix[0][4].setDie(new Die(5, Color.BLUE));
        matrix[1][0].setDie(new Die(6, Color.BLUE));
        matrix[1][1].setDie(new Die(1, Color.PURPLE));
        matrix[1][2].setDie(new Die(2, Color.GREEN));
        matrix[1][3].setDie(new Die(3, Color.BLUE));
        matrix[1][4].setDie(new Die(4, Color.YELLOW));
        matrix[2][0].setDie(new Die(5, Color.RED));
        matrix[2][1].setDie(new Die(6, Color.YELLOW));
        matrix[2][2].setDie(new Die(1, Color.RED));
        matrix[2][3].setDie(new Die(2, Color.YELLOW));
        matrix[2][4].setDie(new Die(3, Color.BLUE));
        matrix[3][0].setDie(new Die(4, Color.GREEN));
        matrix[3][1].setDie(new Die(5, Color.RED));
        matrix[3][2].setDie(new Die(6, Color.PURPLE));
        matrix[3][3].setDie(new Die(1, Color.BLUE));
        matrix[3][4].setDie(new Die(2, Color.GREEN));


    }
}
