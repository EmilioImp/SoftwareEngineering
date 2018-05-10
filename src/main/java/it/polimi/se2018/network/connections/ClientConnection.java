package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.RemoteException;

public interface ClientConnection {

    void sendMessage(Message message) throws RemoteException;
    void receiveResponse(Response response);
}
