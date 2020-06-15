package packet;

import model.PlayerInGame;

import java.io.Serializable;
import java.util.ArrayList;

public class StartGameResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<PlayerInGame> playerInGames;

    public StartGameResponse(ArrayList<PlayerInGame> playerInGames) {
        this.playerInGames = new ArrayList<>();
        this.playerInGames.addAll(playerInGames);

    }
}
