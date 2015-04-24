package se2.groupa.feuern.network.threads;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.network.classes.CommunicationCommand;
import se2.groupa.feuern.network.classes.NetworkHelper;

/**
 * Created by Lukas on 16.03.15.
 *
 * 2 main functions:
 *  - Listen on port and assign incoming connections a separate thread
 *  - Holds a list of all connected clients
 *      --> used for broadcasting commands
*/
public class ListenerThread implements Runnable {


    private String servername;
    private ServerSocket serverSocket;
    private static final int SERVERPORT = NetworkHelper.getPort();
    private ArrayList<ServerThread> serverThreads;
    private ServerController serverController;
    private Handler uiHandler;
    private boolean isListening;

    public ListenerThread(String serverName, ServerController serverController, Handler uiHandler) {

        this.servername = serverName;
        this.serverThreads = new ArrayList<ServerThread>();
        this.serverController = serverController;
        this.uiHandler = uiHandler;
        this.serverSocket = serverSocket;
    }

    public void run() {

        Socket socket = null;
        isListening = true;

        try {
            serverSocket = new ServerSocket(SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isListening) {

            try {

                /*
                To use telnet from localhost to emulator, following configuration needs to be made:

                configuration (tcp redirect to emulator):
                    telnet localhost 5554
                    redir add tcp:8888:8888
                    quit

                connect to server:t
                    telnet localhost 8888
                */

                socket = serverSocket.accept();

                ServerThread threadForClient = new ServerThread(socket, servername, serverController, uiHandler);
                serverThreads.add(threadForClient);
                new Thread(threadForClient).start();

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                isListening = false;
            }
        }
    }

    public void shutdown() {

        // close all open connections from other clients
        for (ServerThread st : serverThreads)
        {
            st.shutdown();
        }

        isListening = false;

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastCommand(CommunicationCommand command, Object parameter)
    {
        for (ServerThread st : serverThreads)
        {
            st.executeCommand(command, parameter);
        }
    }
}
