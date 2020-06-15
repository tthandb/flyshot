package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int PORT;
    private ServerSocket serverSocket;
    private boolean flag = false;
    private int id = 0;

    public Server(int port){
        PORT = port;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        flag = true;
        System.out.println("Server running on port: " + PORT);
        while (flag) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, id);
                ConnectionList.connections.put(id, connection);
                new Thread(connection).start();
                id++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       shutdown();
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void shutdown() {
        flag = false;

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
