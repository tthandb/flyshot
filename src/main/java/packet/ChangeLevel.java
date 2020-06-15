package packet;

import java.io.Serializable;

public class ChangeLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    public int level;

    public ChangeLevel() {
    }

    public ChangeLevel(int level) {
        this.level = level;
    }
}
