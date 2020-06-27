package packet;

import java.io.Serializable;

public class LoseGame implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public boolean isReady;

    public LoseGame(int id, boolean isReady) {
        this.id = id;
        this.isReady = isReady;
    }
}
