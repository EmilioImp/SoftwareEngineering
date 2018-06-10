package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityWindow implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityWindow(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public boolean checkUsability(ButtonSquare buttonSquare) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return false;
    }

    @Override
    public boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonToolCard buttonToolCard) {
        // adesso funziona così: se sto usando una toolcarda posso schiacciare su tutte le toolcard
        //se premo su un'altra toolcard non succede niente, se premom su quella scelta precedentemente
        //butto tutte le selezioni effettuate e faccio ripartire le scelte iniziali, come nella CLi
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return false;
    }
}
