package se2.groupa.feuern.model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Taurer on 30.03.2015.
 */
public class Player implements Observer, Comparable<Player>, Serializable {
    private int id;     //used for lineup
    private String name;
    private Card[] cards;
    private int livePoints;

    public Player(String name) {
        // this.id = id;
        this.name = name;
        this.cards = new Card[3];
        this.livePoints = 4;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public int getLivePoints() {
        return livePoints;
    }

    public void decrementLivePoints() {
        this.livePoints--;
    }

    public boolean isAlive() {
        return (this.livePoints > 0);
    }

    private int getHighestCard() {
        int value = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getValue() > value) {
                value = cards[i].getValue();
            }
        }
        return value;
    }

    public double getCardPoints() {
        int[] sum = new int[4];

        // 3 Cards with same Number (e.g. 3 times SEVEN)
        if (cards[0].getNumber() == cards[1].getNumber() &&
                cards[0].getNumber() == cards[2].getNumber() &&
                cards[1].getNumber() == cards[2].getNumber()) {
            return 31.5;
        }

        //count points from cards with same color
        //init sum[i]=0;
        for (int i = 0; i < sum.length; i++) {
            sum[i] = 0;
        }
        /*
        sum[0] -> points with HERZ cards
        sum[1] -> points with PIK cards
        sum[2] -> points with KARO cards
        sum[3] -> points with KREUZ cards
         */
        //Count points
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == Card.HERZ) {
                sum[0] += cards[i].getValue();
            } else if (cards[i].getColor() == Card.PIK) {
                sum[1] += cards[i].getValue();
            } else if (cards[i].getColor() == Card.KARO) {
                sum[2] += cards[i].getValue();
            } else if (cards[i].getColor() == Card.KREUZ) {
                sum[3] += cards[i].getValue();
            } else {
                return -1;
            }
        }

        //Calc maximum
        int max = 0;
        for (int i = 0; i < sum.length; i++) {
            if (sum[i] > max) {
                max = sum[i];
            }
        }
        return max;
    }

    public boolean hasFire() {
        if (getCardPoints() == 31)
            return true;
        return false;
    }

    @Override
    public void update(Observable observable, Object data) {
        //TODO Notify player over wifi
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Player player) {
        if (this.getCardPoints() < player.getCardPoints())
            return 1;
        if (this.getCardPoints() > player.getCardPoints()) {
            return -1;
        } else {
            return 0;
        }

    }
}
