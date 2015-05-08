package se2.groupa.feuern.controller;

import java.util.ArrayList;

import se2.groupa.feuern.model.GameState;
import se2.groupa.feuern.network.threads.ClientThread;
import se2.groupa.feuern.network.threads.ListenerThread;
import se2.groupa.feuern.network.threads.ServerThread;

/**
 * Created by Lukas on 04.05.15.
 */
public class ApplicationController {

    private static ListenerThread listenerThread;
    private static ClientThread clientThread;
    private static ArrayList<ServerThread> serverThreads;

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        ApplicationController.playerName = playerName;
    }

    private static String playerName;

    public static ListenerThread getListenerThread() {

        return listenerThread;
    }

    public static void setListenerThread(ListenerThread listenerThread) {
        ApplicationController.listenerThread = listenerThread;
    }

    public static ClientThread getClientThread() {
        return clientThread;
    }

    public static void setClientThread(ClientThread clientThread) {
        ApplicationController.clientThread = clientThread;
    }

    public static ArrayList<ServerThread> getServerThreads() {
        return serverThreads;
    }

    public static void setServerThreads(ArrayList<ServerThread> serverThreads) {
        ApplicationController.serverThreads = serverThreads;
    }

    public static void addServerThread(ServerThread serverThread)
    {
        ApplicationController.serverThreads.add(serverThread);
    }
}
