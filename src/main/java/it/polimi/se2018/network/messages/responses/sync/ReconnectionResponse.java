package it.polimi.se2018.network.messages.responses.sync;


public class ReconnectionResponse extends SyncResponse {
   private ModelViewResponse modelViewResponse;

    /**
     * This is the value of players in the current match
     */
    private int playersNumber;

    private final boolean windowSelectionOver;

    public ReconnectionResponse(int playerID, boolean windowSelectionOver) {
        super(playerID);
        this.windowSelectionOver = windowSelectionOver;
    }

    public void setModelViewResponse(ModelViewResponse modelViewResponse) {
        this.modelViewResponse = modelViewResponse;
    }

    public boolean isWindowSelectionOver() {
        return windowSelectionOver;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public ModelViewResponse getModelViewResponse() {
        return modelViewResponse;
    }

    /**
     * Uses the handler to handle this specific disconnection response
     *
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);
    }
}
