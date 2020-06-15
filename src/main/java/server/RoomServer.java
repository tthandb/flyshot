package server;

import client.EventBusClass;
import com.google.common.eventbus.Subscribe;
import model.Constants;
import model.InitGameSetupEvent;
import packet.InGameAction;

import java.util.ArrayList;

public class RoomServer extends Server {
    public static ArrayList<WaitingClient> clients;
    public static int level;
    public static GameSetup gameSetup;

    public RoomServer(int port)  {
        super(port);
        clients = new ArrayList<>(Constants.MAX_ROOM_SIZE);
        level = 0;
        EventBusClass.getInstance().register(this);
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        RoomServer.level = level;
    }

    @Subscribe
    public void onGameStartEvent(InitGameSetupEvent initEvent) {
        gameSetup = new GameSetup(clients.size());
        gameSetup.start();
    }

    @Subscribe
    public void onPlayerActionEvent(InGameAction event) {
        gameSetup.controlPlayerAction(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBusClass.getInstance().unregister(this);
    }
}
