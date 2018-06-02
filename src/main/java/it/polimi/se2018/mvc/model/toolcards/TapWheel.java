package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import it.polimi.se2018.utils.exceptions.ToolCardException;


import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

public class TapWheel extends ToolCard {
    private static TapWheel instance = null;

    public static TapWheel instance(){
        if (instance==null) instance = new TapWheel();
        return instance;
    }

    private TapWheel() {
        super("Tap Wheel", "Move up to two dice of the same color that match the color of a die on the Round Track");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolCardNumber) throws HaltException, ChangeActionException {
        return handler.getPlayerRequests(this, toolCardNumber);
    }

    @Override
    public Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player){
        return handler.checkUsability(this, isUsed, player);
    }

    @Override
    public void handleGUI(ToolCardGUIHandler handler, int toolcardnumber) throws ChangeActionException, HaltException {
        handler.getPlayerRequests(this, toolcardnumber);
    }
}

