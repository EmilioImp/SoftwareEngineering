package it.polimi.se2018.controller;

import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Timing;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.controller.placementlogic.DiePlacer;
import it.polimi.se2018.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.utils.Observer;

import java.time.Duration;

public class Controller extends Observable<Message> implements Observer<Message>, MessageHandler, Timing {
    private Board model;
    private ToolCardController toolCardController;
    private Duration timeout;
    private WaitingThread alarm;

    public Controller() {
        super();
        timeout = Duration.ofSeconds(60);
        alarm = new WaitingThread(timeout, this);
    }

    public void setModel(Board model) {
        this.model = model;
    }

    //reads player, checks if it's his turn, call performMove
    private void checkInput(Message message){
        Player player = model.getPlayerByID(message.getPlayerID());
        if (!model.getRound().isYourTurn(player)) model.notify(new TextResponse(message.getPlayerID(),"It's not your turn"));
        else message.handle(this);
    }

    @Override
    public void performMove(SetupMessage setupMessage){
        notify(setupMessage);
    }

    //use a toolcards
    @Override
    public void performMove(ToolCardMessage toolCardMessage) {
        Player player = model.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            ToolCard toolCard = model.getToolCards()[player.getCardInUse()];
            Response response = toolCard.handle(toolCardController,toolCardMessage);
            model.notify(response);
        }
        catch (ToolCardException e) {model.notify(new TextResponse(toolCardMessage.getPlayerID(),e.getMessage()));}
    }

    @Override
    public void performMove(InputMessage inputMessage) {
        Player player = model.getPlayerByID(inputMessage.getPlayerID());
        try {
            player.getDieInHand().setValue(inputMessage.getDieValue());
            model.notify(new ModelViewResponse(model.modelViewCopy()));
        } catch (DieException e) {
            model.notify(new TextResponse(inputMessage.getPlayerID(), e.getMessage()));
        }
    }

    @Override
    public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
        Player player = model.getPlayerByID(toolCardRequestMessage.getPlayerID());
        if(player.hasUsedCard()) model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"You have already used a Tool Card"));
        else {
            ToolCard toolCard = model.getToolCards()[toolCardRequestMessage.getToolCardNumber()];
            if (toolCard.handleCheck(new ToolCardChecker(model), model.getToolCardsUsage()[toolCardRequestMessage.getToolCardNumber()], player)) {
                player.setCardInUse(toolCardRequestMessage.getToolCardNumber());
                model.notify(new ToolCardResponse(toolCardRequestMessage.getPlayerID(), toolCardRequestMessage.getToolCardNumber()));
            }
            else model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"You can't use that Tool Card!"));;
        }
    }

    //place a die
    @Override
    public void performMove(PlaceMessage placeMessage) {
        Player player = model.getPlayerByID(placeMessage.getPlayerID());
        if(!player.hasDieInHand()) model.notify(new TextResponse(placeMessage.getPlayerID(),"You haven't selected a die"));
        else {
            try {
                Die die = player.getDieInHand();
                if(player.isFirstMove()) {
                    placeDie(new DiePlacerFirst(die,placeMessage.getFinalPosition(),player.getWindow()));
                    player.setFirstMove(false);
                }
                else {
                    placeDie(new DiePlacerNormal(die,placeMessage.getFinalPosition(),player.getWindow()));
                }
                player.dropDieInHand();
                model.notify(new ModelViewResponse(model.modelViewCopy()));
            }
            catch(InvalidPlacementException e) {model.notify(new TextResponse(placeMessage.getPlayerID(),"You can't place the die there"));}
        }
    }

    //draft a die
    @Override
    public void performMove(DraftMessage draftMessage) {
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        if (player.hasDraftedDie()) model.notify(new TextResponse(draftMessage.getPlayerID(),"You have already drafted"));
        else {
            try {
                draft(draftMessage);
                model.notify(new ModelViewResponse(model.modelViewCopy()));
            } catch (NoDieException e) {
                model.notify(new TextResponse(draftMessage.getPlayerID(),"The die you want to draft does not exit"));
            }
        }
    }

    //pass
    @Override
    public void performMove(PassMessage passMessage) {
        Player player = model.getPlayerByID(passMessage.getPlayerID());
        if (model.getRound().isLastTurn()) {
            this.endRound();
        }
        else {
            pass(player);
        }
    }

    private void endRound() {
        if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            startRound();
            model.notify(new ModelViewResponse(model.modelViewCopy()));
        }
    }

    //evaluates point for all players
    private void evaluatePoints() {
        for(Player player: model.getPlayers()) {
            int score = player.getPrivateObjective().evalPoints(player);
            for(PublicObjective pub: model.getPublicObjectives()) {
                score+=pub.evalPoints(player);
            }
            score+=player.getFavorPoints();
            score-=player.getWindow().countEmptySlots();
            player.setScore(score);
        }
    }

    private void startRound() {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    private void endMatch() {
        model.notify(new ModelViewResponse(model.modelViewCopy()));
        evaluatePoints();
    }

    private void draft(DraftMessage draftMessage) throws NoDieException {
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        player.setDieInHand(die);
        player.setHasDraftedDie(true);
    }

    private void pass(Player player) {
        model.getRound().changeTurn();
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.dropDieInHand();
            model.getDraftPool().addToDraftPool(die);
        }
        model.notify(new ModelViewResponse(model.modelViewCopy()));
        timeout = Duration.ofSeconds(60);
        alarm = new WaitingThread(timeout, this);
        alarm.start();
    }

    private void placeDie(DiePlacer diePlacer) throws InvalidPlacementException {
        diePlacer.placeDie();
    }

    public void startMatch() {
        toolCardController = new ToolCardController(model);
        model.notify(new ModelViewResponse(model.modelViewCopy()));
        alarm.start();
    }

    @Override
    public void update(Message input) {
        if (input instanceof SetupMessage)
            input.handle(this);
        else
            checkInput(input);
    }

    @Override
    public void onTimesUp() {
        pass(model.getPlayerByID(model.getRound().getCurrentPlayerIndex()));
    }
}
