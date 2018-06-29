package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.requests.Message;
import javafx.application.Platform;

public class GUIView extends ClientView {
    private final GUIController guiController;
    private final GUIModel guiModel;

    public GUIView(Client client, int playerID) {
        super(client);
        guiModel = new GUIModel(playerID);
        guiController = new GUIController(this,guiModel,playerID);
        register(guiController);
    }

    public GUIController getGuiController() {
        return guiController;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    @Override
    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        if(guiController.isGameStarted()) {
            Platform.runLater(() -> {
                guiController.refreshText(message);
            });
        }
    }

    @Override
    public void endGame() {
        stop();
    }
}
