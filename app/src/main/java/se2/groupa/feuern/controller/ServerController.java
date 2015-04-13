package se2.groupa.feuern.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import se2.groupa.feuern.network.ListenerThread;
import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

/**
 * Created by Taurer on 30.03.2015.
 */
public class ServerController {
    private static HashMap<String, Player> players;
    private static CardDeck cardDeck;
    private static Thread listenerThread = null;
    private String servername;

    public ServerController(String servername) {
        players = new HashMap<String, Player>();
        cardDeck = new CardDeck();
        this.servername = servername;
    }

    public static void dealOutCardsToPlayer(){
        for(Player player : players.values()){
            if(player.isAlive()){
                player.setCards(cardDeck.getThreeCardsFromStack());
            }
        }
    }

    public static HashMap<String, Player> getPlayers() {
        return players;
    }

    public static boolean addPlayer(String playerName) {
        if (!players.containsKey(playerName)) {
            players.put(playerName, new Player(playerName));
            return true;
        }

        return false;
    }

    public static boolean deletePlayer(String playerName) {
        if (players.containsKey(playerName)) {
            players.remove(playerName);
            return true;
        }

        return false;
    }

    public static int getNumberOfPlayers(){
        return players.size();
    }
}
