package it.polimi.se2018.controller.placementlogic;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.network.messages.Coordinate;

public class DiePlacerAlone extends DiePlacer {

    public DiePlacerAlone(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die);
    }
}
