package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardHandler {

    void useCard(CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(EglomiseBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(FluxBrush toolCard, ToolCardMessage toolCardMessage);
    void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage);
    void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage);
    void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage);
    void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage);
    void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
}
