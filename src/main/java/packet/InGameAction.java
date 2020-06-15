package packet;

import java.io.Serializable;

public class InGameAction implements Serializable {
    public enum  Action {
        LEFT_PRESSED,
        RIGHT_PRESSED,
        FIRE_PRESSED,
        LEFT_RELEASED,
        RIGHT_RELEASED,
        FIRE_RELEASED,
        UP_PRESSED,
        DOWN_PRESSED,
        UP_RELEASED,
        DOWN_RELEASED
    }

    public Action action;
    public int playerId;

    public InGameAction (Action action) {
        this.action = action;
    }
}
