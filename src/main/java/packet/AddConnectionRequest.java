package packet;

import java.io.Serializable;

public class AddConnectionRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public String playerName;
    public boolean isHost;

       public AddConnectionRequest(String playerName, boolean isHost) {
        this.playerName = playerName;
        this.isHost = isHost;
    }
}
