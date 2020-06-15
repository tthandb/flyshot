package server;

import java.io.Serializable;

public class WaitingClient implements Serializable {
    private static final long serialVersionUID =1L;

    public int id;
    public String playerName;
    public boolean isReady;
    public boolean isHost;
    public WaitingClient(int id, String playerName, boolean isReady, boolean isHost){
        this.id = id;
        this.playerName = playerName;
        this.isReady = isReady;
        this.isHost = isHost;
    }
}
