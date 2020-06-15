package client;

public class Connection {
    private int id;
    private String playerName;

    public Connection() {

    }

    public Connection(int id) {
        this.id = id;
    }

    public Connection(int id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
