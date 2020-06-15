package server;

import client.EventBusClass;
import model.Constants;
import model.InitGameSetupEvent;
import model.PlayerInGame;
import packet.*;

import java.util.ArrayList;
import java.util.Map;

public class ServerListener {
    public void received(Object object, Connection connection) {
        if (object instanceof AddConnectionRequest) {
            AddConnectionRequest addConnectionRequest = (AddConnectionRequest) object;
            controlAddConnectionRequest(addConnectionRequest, connection);
        } else if (object instanceof RemoveConnection) {
            RemoveConnection removeConnection = (RemoveConnection) object;
            controlRemoveConnection(removeConnection, connection);
        } else if (object instanceof ReadyRequest) {
            ReadyRequest readyRequest = (ReadyRequest) object;
            controlReadyRequest(readyRequest, connection);
        } else if (object instanceof StartGameRequest) {
            StartGameRequest startGameRequest = (StartGameRequest) object;
            controlStartGameRequest(startGameRequest, connection);
        } else if (object instanceof InGameAction) {
            InGameAction inGameAction = (InGameAction) object;
            inGameAction.playerId = connection.id;
            controlInGameAction(inGameAction);
        } else if (object instanceof ChangeLevel) {
            ChangeLevel changeLevel = (ChangeLevel) object;
            controlChangeLevel(changeLevel, connection);
        }
    }

    private void controlChangeLevel(ChangeLevel packet, Connection connection) {
        Room.setLevel(packet.level);
        RoomUpdate roomUpdate = new RoomUpdate(Room.clients, Room.getLevel());
        for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {

            Connection c = entry.getValue();
            c.sendObject(roomUpdate);
        }
    }

    private void controlReadyRequest(ReadyRequest packet, Connection connection) {
        if (connection.id == packet.id) {
            Room.clients.forEach(waitingClient -> {
                if (waitingClient.id == connection.id)
                    waitingClient.isReady = packet.isReady;
                RoomUpdate roomUpdate = new RoomUpdate(Room.clients, Room.getLevel());
                for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
                    Connection c = entry.getValue();
                    c.sendObject(roomUpdate);
                }
            });
        }
    }

    private void controlAddConnectionRequest(AddConnectionRequest packet, Connection connection) {
        if (ConnectionList.connections.size() <= Constants.MAX_ROOM_SIZE) {
            packet.id = connection.id;
            connection.playerName = packet.playerName;
            boolean isReady = packet.isHost;

            WaitingClient client = new WaitingClient(packet.id, packet.playerName, isReady, packet.isHost);
            Room.clients.add(client);
            RoomUpdate roomUpdate = new RoomUpdate(Room.clients, Room.getLevel());

            for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
                Connection c = entry.getValue();
                if (c != connection) c.sendObject(packet);
                else {
                    AddConnectionResponse addConnectionResponse = new AddConnectionResponse(connection.id, true, packet.playerName, "Client connect successfully!");
                    c.sendObject(addConnectionResponse);
                }
                c.sendObject(roomUpdate);
            }
            System.out.println("Client #" + packet.id + " " + packet.playerName + " is connected!");

        } else {
            AddConnectionResponse addConnectionResponse = new AddConnectionResponse(-1, false, "RoomGUI is full!");
            connection.sendObject(addConnectionResponse);
            connection.close();
        }
    }

    private void controlRemoveConnection(RemoveConnection packet, Connection connection) {
        System.out.println("Client #" + packet.id + ": " + packet.playerName + " has disconnected");
        ConnectionList.connections.get(packet.id).close();
    }

    private void controlStartGameRequest(StartGameRequest packet, Connection connection) {
        if (connection.id == Room.clients.get(0).id) {
            System.out.println("Server Server Listener Start");
            for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
                Connection c = entry.getValue();
                ArrayList<PlayerInGame> playerInGames = new ArrayList<>();
                int numberOfPlayers = Room.clients.size();
                for (int i = 0; i < numberOfPlayers; ++i) {
                    int distance = (Constants.GAME_WIDTH) / numberOfPlayers;
                    PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (i * distance), Constants.GAME_HEIGHT + 20, Room.clients.get(i).id, i);
                    playerInGames.add(playerInGame);
                }
                c.sendObject(new StartGameResponse(playerInGames));
            }
            EventBusClass.getInstance().post(new InitGameSetupEvent());
        }
    }

    private void controlInGameAction(InGameAction packet) {
        EventBusClass.getInstance().post(packet);
    }

}
