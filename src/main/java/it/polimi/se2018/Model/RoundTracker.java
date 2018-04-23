package it.polimi.se2018.Model;

import java.util.ArrayList;

public class RoundTracker {
    private ArrayList<Die>[] dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    public RoundTracker(int roundsNumber) {
        this.dice = new ArrayList[roundsNumber];
        turn=0; //this variable matches the actual turns -1
    }

    public ArrayList<Die>[] getRoundTracker() {
        return dice;
    }

    //!ADD EXCEPTIONS
    public void removeFromRoundTracker(int index, Die die) { //used by ToolCards
        dice[index].remove(die);
    }

    //!ADD EXCEPTIONS
    public void addToRoundTracker(int index, Die die) { //used by ToolCards
        dice[index].add(die);
    }

    //!ADD EXCEPTIONS
    void updateRoundTracker(ArrayList<Die> remainingDice) { //increments current turn, fills roundTracker with remaining dice from draftPool
        dice[turn]=(ArrayList<Die>)remainingDice.clone();
        turn ++;
    }

}
