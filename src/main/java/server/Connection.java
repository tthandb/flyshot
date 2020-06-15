package server;

import packet.RemoveConnection;
import packet.RoomUpdate;
import packet.ServerClosure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Connection implements Runnable {
    private final Socket socket;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    ServerListener serverListener;
    private boolean flag = false;

    public String playerName;
    public int id;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            serverListener = new ServerListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        flag = true;
        while (flag) {
            try {
                Object data = objectInputStream.readObject();
                serverListener.received(data, this);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (playerName != null) {
            if (id == 0) {
                ServerClosure serverClosure = new ServerClosure("Host was out!");

                for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
                    Connection connection = entry.getValue();
                    if (connection.id != this.id) {
                        connection.sendObject("RoomGUI host has quit");
                    }
                }
            } else {

                RoomServer.clients.removeIf(client -> client.id == this.id);
                RoomUpdate roomUpdate = new RoomUpdate(RoomServer.clients, RoomServer.getLevel());
                for (Map.Entry<Integer, Connection> entry : ConnectionList.connections.entrySet()) {
                    Connection connection = entry.getValue();
                    if (connection.id != this.id) {
                        RemoveConnection removeConnection = new RemoveConnection(this.id, this.playerName);
                        connection.sendObject(removeConnection);
                        connection.sendObject(roomUpdate);
                    }
                }
            }
        }
        flag = false;
        try {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConnectionList.connections.remove(this.id);

    }

    public void sendObject(Object packet) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
