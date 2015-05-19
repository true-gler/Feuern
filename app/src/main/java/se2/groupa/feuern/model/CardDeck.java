package se2.groupa.feuern.model;

import java.io.Serializable;
import java.util.ArrayList;

import se2.groupa.feuern.R;

/**
 * Created by Taurer on 30.03.2015.
 */
public class CardDeck implements Serializable {
    private  ArrayList<Card> cards;
    private  ArrayList<Card> stackCards;

    public CardDeck() {
        cards = new ArrayList<Card>();

        cards.add(new Card(Card.HERZ, Card.SIEBEN, R.drawable.herz_sieben));
        cards.add(new Card(Card.HERZ, Card.ACHT, R.drawable.herz_acht));
        cards.add(new Card(Card.HERZ, Card.NEUN, R.drawable.herz_neun));
        cards.add(new Card(Card.HERZ, Card.ZEHN, R.drawable.herz_zehn));
        cards.add(new Card(Card.HERZ, Card.UNTER, R.drawable.herz_unter));
        cards.add(new Card(Card.HERZ, Card.OBER, R.drawable.herz_ober));
        cards.add(new Card(Card.HERZ, Card.KOENIG, R.drawable.herz_koenig));
        cards.add(new Card(Card.HERZ, Card.ASS, R.drawable.herz_ass));

        cards.add(new Card(Card.PIK, Card.SIEBEN, R.drawable.pik_sieben));
        cards.add(new Card(Card.PIK, Card.ACHT, R.drawable.pik_acht));
        cards.add(new Card(Card.PIK, Card.NEUN, R.drawable.pik_neun));
        cards.add(new Card(Card.PIK, Card.ZEHN, R.drawable.pik_zehn));
        cards.add(new Card(Card.PIK, Card.UNTER, R.drawable.pik_unter));
        cards.add(new Card(Card.PIK, Card.OBER, R.drawable.pik_ober));
        cards.add(new Card(Card.PIK, Card.KOENIG, R.drawable.pik_koenig));
        cards.add(new Card(Card.PIK, Card.ASS, R.drawable.pik_ass));

        cards.add(new Card(Card.KARO, Card.SIEBEN, R.drawable.karo_sieben));
        cards.add(new Card(Card.KARO, Card.ACHT, R.drawable.karo_acht));
        cards.add(new Card(Card.KARO, Card.NEUN, R.drawable.karo_neun));
        cards.add(new Card(Card.KARO, Card.ZEHN, R.drawable.karo_zehn));
        cards.add(new Card(Card.KARO, Card.UNTER, R.drawable.karo_unter));
        cards.add(new Card(Card.KARO, Card.OBER, R.drawable.karo_ober ));
        cards.add(new Card(Card.KARO, Card.KOENIG, R.drawable.karo_koenig));
        cards.add(new Card(Card.KARO, Card.ASS, R.drawable.karo_ass));

        cards.add(new Card(Card.KREUZ, Card.SIEBEN, R.drawable.kreuz_sieben));
        cards.add(new Card(Card.KREUZ, Card.ACHT, R.drawable.kreuz_acht));
        cards.add(new Card(Card.KREUZ, Card.NEUN, R.drawable.kreuz_neun));
        cards.add(new Card(Card.KREUZ, Card.ZEHN, R.drawable.kreuz_zehn));
        cards.add(new Card(Card.KREUZ, Card.UNTER, R.drawable.kreuz_unter));
        cards.add(new Card(Card.KREUZ, Card.OBER, R.drawable.kreuz_ober));
        cards.add(new Card(Card.KREUZ, Card.KOENIG, R.drawable.kreuz_koenig));
        cards.add(new Card(Card.KREUZ, Card.ASS, R.drawable.kreuz_ass));

        stackCards = (ArrayList<Card>) cards.clone();
    }

    public Card[] getThreeCardsFromStack(){
        Card[] cards = new Card[3];
        int randNumb;
        for (int i = 0; i < cards.length; i++) {
            randNumb = (int) Math.round(Math.random()*(stackCards.size()-1));
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
