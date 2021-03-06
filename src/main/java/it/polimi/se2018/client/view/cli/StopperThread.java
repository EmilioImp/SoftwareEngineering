package it.polimi.se2018.client.view.cli;


import it.polimi.se2018.client.view.ClientView;

public class StopperThread implements Runnable {
    private final ClientView clientView;
    private final String message;
    private final boolean halt;

    public StopperThread(ClientView clientView, String message, boolean halt) {
        this.clientView = clientView;
        this.message = message;
        this.halt = halt;
    }

    @Override
    public void run() {
        clientView.handleAsyncEvent(halt,message);
    }
}