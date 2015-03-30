package se2.groupa.feuern.others;

import java.util.ArrayList;

/**
 * Created by Taurer on 30.03.2015.
 */
public class CardDeck {
    private  ArrayList<Card> cards;
    private  ArrayList<Card> stackCards;

    public CardDeck() {
        cards = new ArrayList<Card>();

        cards.add(new Card(Card.HERZ, Card.SIEBEN, 7));
        cards.add(new Card(Card.HERZ, Card.ACHT, 8));
        cards.add(new Card(Card.HERZ, Card.NEUN, 9));
        cards.add(new Card(Card.HERZ, Card.ZEHN, 10));
        cards.add(new Card(Card.HERZ, Card.UNTER, 10));
        cards.add(new Card(Card.HERZ, Card.OBER, 10));
        cards.add(new Card(Card.HERZ, Card.KOENIG, 10));
        cards.add(new Card(Card.HERZ, Card.ASS, 11));

        cards.add(new Card(Card.PIK, Card.SIEBEN, 7));
        cards.add(new Card(Card.PIK, Card.ACHT, 8));
        cards.add(new Card(Card.PIK, Card.NEUN, 9));
        cards.add(new Card(Card.PIK, Card.ZEHN, 10));
        cards.add(new Card(Card.PIK, Card.UNTER, 10));
        cards.add(new Card(Card.PIK, Card.OBER, 10));
        cards.add(new Card(Card.PIK, Card.KOENIG, 10));
        cards.add(new Card(Card.PIK, Card.ASS, 11));

        cards.add(new Card(Card.KARO, Card.SIEBEN, 7));
        cards.add(new Card(Card.KARO, Card.ACHT, 8));
        cards.add(new Card(Card.KARO, Card.NEUN, 9));
        cards.add(new Card(Card.KARO, Card.ZEHN, 10));
        cards.add(new Card(Card.KARO, Card.UNTER, 10));
        cards.add(new Card(Card.KARO, Card.OBER, 10));
        cards.add(new Card(Card.KARO, Card.KOENIG, 10));
        cards.add(new Card(Card.KARO, Card.ASS, 11));

        cards.add(new Card(Card.KREUZ, Card.SIEBEN, 7));
        cards.add(new Card(Card.KREUZ, Card.ACHT, 8));
        cards.add(new Card(Card.KREUZ, Card.NEUN, 9));
        cards.add(new Card(Card.KREUZ, Card.ZEHN, 10));
        cards.add(new Card(Card.KREUZ, Card.UNTER, 10));
        cards.add(new Card(Card.KREUZ, Card.OBER, 10));
        cards.add(new Card(Card.KREUZ, Card.KOENIG, 10));
        cards.add(new Card(Card.KREUZ, Card.ASS, 11));

        stackCards = (ArrayList<Card>) cards.clone();
    }

    public Card[] getThreeCardsFromStack(){
        Card[] cards = new Card[3];
        int randNumb;
        for (int i = 0; i < cards.length; i++) {
            randNumb = (int) Math.round(Math.random()*(stackCards.size()+1));
            if(stackCards.isEmpty()){return null;}
            cards[i] = stackCards.get(randNumb);
            stackCards.remove(cards[i]);
        }
        return cards;
    }

    public void resetCardDeck(){
        stackCards = (ArrayList<Card>) cards.clone();
    }

}
