package se2.groupa.feuern.model;

import java.io.Serializable;
import java.util.ArrayList;

import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.CardDeck;
import se2.groupa.feuern.model.Player;

/**
 * Created by Markus on 31.03.2015.
 */

public class GameState implements Serializable {

    private CardDeck deck;
    private ArrayList<Player> players;
    private Card[] publicCards;
    private int counter;
    private Player nowTurnPlayer;
    private Player nextTurnPlayer;

    public GameState(ArrayList<Player> players){

        this.players = players;
        this.deck = new CardDeck();
        this.counter = 0;
        this.nowTurnPlayer = players.get(0);
        this.nextTurnPlayer = players.get(1);

        //Dies stellt den Anfangszustand des Spiels dar:
        //Die Spieler sind alle Spieler die am Anfang beigetreten sind, der counter ist auf 0
        //und ein CardDeck.Objekt wird initialisiert.

    }

    public Player getNowTurnPlayer(){
        return this.nowTurnPlayer;
    }
    public void setNowTurnPlayer(Player player){
        this.nowTurnPlayer = player;
    }

    public void setNextTurnPlayer(Player player){
        this.nextTurnPlayer = player;
    }
    public Player getNextTurnPlayer(){
        return this.nextTurnPlayer;
    }

    public void setPublicCards(Card [] cards){
        this.publicCards = cards;
    }

    public CardDeck getCardDeck (){
        return this.deck;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public void incCounter(){
        this.counter++;
    }

    public int getCounter(){
        return this.counter;
    }

    public Card[] getPublicCards(){
        return this.publicCards;
    }




}
