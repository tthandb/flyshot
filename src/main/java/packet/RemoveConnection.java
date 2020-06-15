package packet;

import java.io.Serializable;

public class RemoveConnection implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public String playerName;

    public RemoveConnection() {

    }

    public RemoveConnection(int id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }
}
