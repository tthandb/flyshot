package client;

import packet.ReadyRequest;
import packet.StartGameRequest;

public class Player extends Client {
    public boolean isReady;

    public Player(String host, int port) {
        super(host, port);
        isReady = false;
    }

    public void notifyReadyState(boolean isReady) {
        this.isReady = isReady;
        System.out.println(isReady);
        ReadyRequest readyRequest = new ReadyRequest(super.getId(), isReady);
        super.sendObject(readyRequest);
    }

    public void sendStartGameRequest(int level) {
        StartGameRequest startGameRequest = new StartGameRequest(level);
        sendObject(startGameRequest);
    }
}
