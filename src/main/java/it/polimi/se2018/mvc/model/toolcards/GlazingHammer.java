package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

public class GlazingHammer extends ToolCard {
    private static GlazingHammer instance = null;

    public static GlazingHammer instance(){
        if (instance==null) instance = new GlazingHammer();
        return instance;
    }

    private
    GlazingHammer() {
        super("Glazing Hammer",
                "Re-roll all dice in the Draft Pool, (you can only in second turn of the round and before drafting)");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) {
        handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber) {
        return handler.getPlayerRequests(this, toolcardnumber);
    }

    @Override
    public Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player){
        return handler.checkUsability(this, isUsed, player);
    }

    @Override
    public void handleGUI(ToolCardGUIHandler handler, int toolcardnumber) {
        handler.getPlayerRequests(this, toolcardnumber);
    }
}
