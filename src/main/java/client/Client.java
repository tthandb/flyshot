package client;

import packet.RemoveConnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {
    private final String HOST;
    private final int PORT;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private boolean flag = false;
    private ClientListener clientListener;

    private int id;
    public String playerName;
    private boolean serverAlive = true;

    public Client(String host, int port) {
        HOST = host;
        PORT = port;
        this.id = -1;
    }

    public void connect() {
        try {
            socket = new Socket(HOST, PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            clientListener = new ClientListener();
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.flag = false;
        if (this.socket != null) {
            if (!socket.isClosed() && this.id != -1 && serverAlive) {
                RemoveConnection removeConnection = new RemoveConnection(this.id, this.playerName);
                sendObject(removeConnection);
            }
            if (!socket.isClosed()) {
                try {
                    objectInputStream.close();
                    objectOutputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ConnectionList.connections.remove(this.id);
            }
        }

    }

    @Override
    public void run() {
        try {
            flag = true;

            while (flag) {
                try {
                    Object data = objectInputStream.readObject();
                    clientListener. received(data, this);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                    close();
                } catch (EOFException e) {
                    e.printStackTrace();
                    serverAlive = true;
                    close();
                    System.out.println("Disconnected from server!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            objectOutputStream.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
