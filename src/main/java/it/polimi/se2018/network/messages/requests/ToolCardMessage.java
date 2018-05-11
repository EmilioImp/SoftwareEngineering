package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.model.Player;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message from a player wishing to specify the inputs needed to use the Tool Card in his hand
 */
public class ToolCardMessage extends Message {

    /**
     * This is the value a player wants to give to a Die drafted from the Dice Bag
     * Used by {@link it.polimi.se2018.model.toolcards.FluxRemover}
     */
    private int value;

    /**
     * This is the number of the Tool Card as displayed on the array in {@link it.polimi.se2018.model.Board}
     */
    private final int toolCardNumber;

    /**
     * This contains all the positions you want to move a die from, each value in the Array List is associated to a die to move
     */
    private ArrayList<Coordinate> startingPosition;

    /**
     * This contains all the positions you want to move a die to, each value in the Array List is associated to a die to move
     */
    private ArrayList<Coordinate> finalPosition;

    /**
     * This contains all the positions on the Draft Pool of a Die you want to select, each value is associated to a die to select
     */
    private ArrayList draftPoolPosition;

    /**
     * This contains all the positions on the Round Tracker of a Die you want to select, each value is associated to a die to select
     */
    private ArrayList roundTrackerPosition; //x is the Turn, y is the position

    public ToolCardMessage(Player player, int value, int toolCardNumber) {
        super(player);
        this.value = value;
        startingPosition = new ArrayList<>();
        this.toolCardNumber=toolCardNumber;
        finalPosition = new ArrayList<>();
        draftPoolPosition = new ArrayList<>();
        roundTrackerPosition = new ArrayList<>();
    }

    /**
     * @return the starting positions of dice you want to move
     */
    public List<Coordinate> getStartingPosition() {
        return startingPosition;
    }

    /**
     * @return the final positions of dice you want to move
     */
    public List<Coordinate> getFinalPosition() {
        return finalPosition;
    }

    /**
     * @return the position on the Draft Pool of the dice you want to select
     */
    public List<Integer> getDraftPoolPosition() {
        return draftPoolPosition;
    }

    /**
     * @return the position on the Round Trucker of the dice you want to select
     */
    public List<Coordinate> getRoundTrackerPosition() {
        return roundTrackerPosition;
    }

    /**
     * @param coordinate is the coordinate of the starting position you want to add to the list
     */
    public void addStartingPosition(Coordinate coordinate) {
        startingPosition.add(coordinate);
    }

    /**
     * @param coordinate is the coordinate of the final position you want to add to the list
     */
    public void addFinalPosition(Coordinate coordinate) {
        finalPosition.add(coordinate);
    }

    /**
     * @param coordinate is the coordinate of the die on the Draft Pool you want to add to the list
     */
    public void addDraftPoolPosition(int coordinate) {
        draftPoolPosition.add(coordinate);
    }

    /**
     * @param coordinate is the coordinate of the die on the Round Tracker you want to add to the list
     */
    public void addRoundTrackerPosition(Coordinate coordinate) {
        roundTrackerPosition.add(coordinate);
    }

    /**
     * @return the value used by {@link it.polimi.se2018.model.toolcards.FluxRemover}
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value is the value you want to set for {@link it.polimi.se2018.model.toolcards.FluxRemover}
     */
    public void addValue(int value) {
        this.value = value;
    }

    /**
     * @return the number of the Tool Card
     */
    public int getToolCardNumber() {
        return toolCardNumber;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }
}