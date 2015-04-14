package se2.groupa.feuern.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import se2.groupa.feuern.controller.ServerController;

/**
 * Created by Lukas on 16.03.15.
 */
public class ListenerThread implements Runnable {


    private String servername;
    private ServerSocket serverSocket;
    private static final int SERVERPORT = 8888;
    private ArrayList<ServerThread> serverThreads;
    private ServerController serverController;

    public ListenerThread(String servername, ServerController serverController) {

        this.servername = servername;
        this.serverThreads = new ArrayList<ServerThread>();
        this.serverController = serverController;
    }

    public void run() {

        Socket socket = null;

        try {
            serverSocket = new ServerSocket(SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

            try {

                /*
                To use telnet from localhost:

                configuration (tcp redirect to emulator):
                    telnet localhost 5554
                    redir add tcp:8888:8888
                    quit

                connect to server:
                    telnet localhost 8888
                 */

                socket = serverSocket.accept();

                ServerThread threadForClient = new ServerThread(socket, servername, serverController);
                serverThreads.add(threadForClient);
                new Thread(threadForClient).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
