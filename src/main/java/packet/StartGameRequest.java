package packet;

import java.io.Serializable;

public class StartGameRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    int level = 1;

    public StartGameRequest(int level) {
        this.level = level;
    }
}
