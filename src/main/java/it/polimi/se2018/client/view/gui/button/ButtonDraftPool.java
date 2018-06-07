package it.polimi.se2018.client.view.gui.button;


import it.polimi.se2018.client.view.gui.stategui.State;

public class ButtonDraftPool extends ButtonGame {
    private final int playerID;
    private Boolean usable;

    public ButtonDraftPool(int playerID) {
        this.playerID = playerID;
        usable = false;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler, State currentState){
        usable = handler.checkUsability(this, currentState);
        if(usable) arm();
        else disarm();
    }
}