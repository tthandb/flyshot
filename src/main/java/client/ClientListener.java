package client;

import model.AppPreferences;
import model.EventBusClass;
import model.Level;
import packet.*;

public class ClientListener {
    public void received(Object object, Client client) {
        if (object instanceof AddConnectionRequest) {
            AddConnectionRequest addConnectionRequestPacket = (AddConnectionRequest) object;
            controlAddConnectionRequest(addConnectionRequestPacket, client);
        } else if (object instanceof RemoveConnection) {
            RemoveConnection removeConnectionPacket = (RemoveConnection) object;
            controlRemoveConnection(removeConnectionPacket, client);
        } else if (object instanceof AddConnectionResponse) {
            AddConnectionResponse addConnectionResponsePacket = (AddConnectionResponse) object;
            controlAddConnectionResponse(addConnectionResponsePacket, client);
        } else if (object instanceof RoomUpdate) {
            RoomUpdate roomUpdateInfoPacket = (RoomUpdate) object;
            System.out.println("RoomGUI Size: " + roomUpdateInfoPacket.clients.size());
            controlUpdateRoom(roomUpdateInfoPacket, client);
        } else if (object instanceof ServerClosure) {
            ServerClosure closedServerNotificationPacket = (ServerClosure) object;
            controlClosedServerNotification(closedServerNotificationPacket);
        } else if (object instanceof StartGameResponse) {
            StartGameResponse startGameResponse = (StartGameResponse) object;
            controlStartGameResponse(startGameResponse);
        } else if (object instanceof UpdateInGame) {
            UpdateInGame packet = (UpdateInGame) object;
            controlUpdateInGame(packet);
        }
    }

    private void controlUpdateRoom(RoomUpdate roomUpdate, Client client) {
        System.out.println("----------------------------------------------------");
        System.out.println("ROOM INFO");
        System.out.println("Level: " + roomUpdate.level);
        Level.setSpeed(roomUpdate.level);
        roomUpdate.clients.forEach(e -> {
            System.out.println("\tPosition: " + roomUpdate.clients.indexOf(e) + "\n" +
                    "\tID: " + e.id + "\n" +
                    "\tPlayerInGame name: " + e.playerName + "\n" +
                    "\tReady? " + e.isReady + "\n" +
                    "\tHost? " + e.isHost + "\n"
            );
        });
        EventBusClass.getInstance().post(roomUpdate);
    }

    private void controlAddConnectionRequest(AddConnectionRequest packet, Client client) {
        ConnectionList.connections.put(packet.id, new Connection(packet.id, packet.playerName));
        System.out.println("Player #" + packet.id + " " + packet.playerName + " has connected");
    }

    private void controlAddConnectionResponse(AddConnectionResponse packet, Client client) {
        System.out.println(packet.message);
        client.setId(packet.id);
        AppPreferences.UID = packet.id;

        if (packet.isConnectSuccess)
            ConnectionList.connections.put(packet.id, new Connection(packet.id, packet.playerName));
        EventBusClass.getInstance().post(packet);
    }

    private void controlRemoveConnection(RemoveConnection packet, Client client) {
        System.out.println("Player #" + packet.id + " " + packet.playerName + " has disconnected!");
        ConnectionList.connections.remove(packet.id);
    }

    private void controlClosedServerNotification(ServerClosure packet) {
        ConnectionList.connections.clear();
        EventBusClass.getInstance().post(packet);
    }

    private void controlStartGameResponse(StartGameResponse packet) {
        EventBusClass.getInstance().post(packet);
    }

    private void controlUpdateInGame(UpdateInGame packet) {
        EventBusClass.getInstance().post(packet);
    }
}
