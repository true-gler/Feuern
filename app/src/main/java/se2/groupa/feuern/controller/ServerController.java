package se2.groupa.feuern.controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

/**
 * Created by Taurer on 30.03.2015.
 */
public class ServerController extends Observable{
    private  ArrayList<Player> players;
    private CardDeck cardDeck;

    public ServerController() {
        this.players = new ArrayList<Player>();
        cardDeck = new CardDeck();
    }

    public void dealOutCardsToPlayer(){
        for(Player player : players){
            if(player.isAlive()){
                player.setCards(cardDeck.getThreeCardsFromStack());
            }
        }
    }

    public  ArrayList<Player> getPlayers() {
        return players;
    }

    public  void addPlayer(Player player) {
        this.players.add(player);
        addObserver(player);
    }

    public void deletePlayer(Player player) {
        this.players.remove(player);
        deleteObserver(player);
    }

    public int getNumberOfPlayers(){
        return players.size();
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }
}
