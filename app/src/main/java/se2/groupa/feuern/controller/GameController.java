package se2.groupa.feuern.controller;

import java.util.ArrayList;

import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.GameState;
import se2.groupa.feuern.model.Player;

/**
 * Created by Markus on 27.03.2015.
 */

public class GameController {

    private GameState gamestate;


    public GameController(ArrayList<Player> players) {

        this.gamestate = new GameState(players);

    }

    public GameState getGameState() {
        return this.gamestate;
    }

    public GameState dealingOutCards() {

        for (Player i : this.gamestate.getPlayers()) {
            i.setCards(this.gamestate.getCardDeck().getThreeCardsFromStack());

        }

        //this.gamestate.setPublicCards(this.gamestate.getCardDeck().getThreeCardsFromStack());

        return this.gamestate;
    }

    public GameState KeepCardsFinishDealing(){
        this.gamestate.setPublicCards(this.gamestate.getCardDeck().getThreeCardsFromStack());
        return this.gamestate;
    }

    public GameState DontKeepCardsFinishDealing(){
        this.gamestate.setPublicCards(this.gamestate.getNowTurnPlayer().getCards());
        this.gamestate.getNowTurnPlayer().setCards(this.gamestate.getCardDeck().getThreeCardsFromStack());
        return this.gamestate;
    }

    public GameState doStep() {

        //Diese Methode weist nur dem Spieler der jetzt an der Reihe ist, den nächsten Spieler zu
        //Und sucht für den nächsten Spieler, den spieler mit dem nächst größeren Index
        //Diese Methode sollte nach jedem Zug getätigt werden!

        //Erst den jetzigen Spieler
        this.gamestate.setNowTurnPlayer(this.gamestate.getNextTurnPlayer());

        //Ist der nächste Spieler der letzte Spieler, so weise dem NextTurnPlayer wieder den ersten Spieler zu
        if (this.gamestate.getPlayers().indexOf(this.gamestate.getNextTurnPlayer()) == this.gamestate.getPlayers().size()-1) {
            this.gamestate.setNextTurnPlayer(this.gamestate.getPlayers().get(0));
        } else {
            //Ist diese nicht der Fall, dann setze einfach den NextTurnPlayer auf den Player, mit dem
            //nächste größeren Index
            this.gamestate.setNextTurnPlayer(
                    this.gamestate.getPlayers().get(((this.gamestate.getPlayers().
                            indexOf(this.gamestate.getNextTurnPlayer())) + 1)));

        }

        return this.gamestate;
    }

    public GameState swapAllCards(){
        Card[]cards = new Card[3];
        cards = gamestate.getNowTurnPlayer().getCards();
        gamestate.getNowTurnPlayer().setCards(gamestate.getPublicCards());
        gamestate.setPublicCards(cards);
        return this.gamestate;
    }


    public GameState swapCards(Card ownCard, Card publicCard) { //Kommentar

        int indexOwnCard = 0;
        for (int i = 0; i < this.gamestate.getNowTurnPlayer().getCards().length; i++) {
            if (this.gamestate.getNowTurnPlayer().getCards()[i].equals(ownCard)) {
                //Merke dir hier den Index in den Karten des Spielers, die getauscht werden sollen
                indexOwnCard = i;
            }
        }
        int indexPublicCard = 0;
        for (int i = 0; i < this.gamestate.getPublicCards().length; i++) {
            if (this.gamestate.getPublicCards()[i].equals(publicCard)) {
                //Merke dir hier den Index in den Öffentlichen Karten, die getauscht werden sollen
                indexPublicCard = i;
            }
        }
        Card switchCard = this.gamestate.getNowTurnPlayer().getCards()[indexOwnCard];
        this.gamestate.getNowTurnPlayer().getCards()[indexOwnCard] = this.gamestate.getPublicCards()[indexPublicCard];
        this.gamestate.getPublicCards()[indexPublicCard] = switchCard;

        return this.gamestate;
    }

    public int stop(){

        int stop = gamestate.getPlayers().indexOf(gamestate.getNowTurnPlayer());

        return stop;
    }



}
