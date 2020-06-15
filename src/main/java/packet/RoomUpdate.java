package packet;

import server.WaitingClient;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomUpdate implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<WaitingClient> clients;
    public int level;

    public RoomUpdate(ArrayList<WaitingClient> clients, int level) {
        this.clients = new ArrayList<>();
        this.clients.addAll(clients);
        this.level = level;
    }

}
