package se2.groupa.feuern.others;

import java.util.ArrayList;

import se2.groupa.feuern.R;

/**
 * Created by Taurer on 30.03.2015.
 */
public class CardDeck {
    private  ArrayList<Card> cards;
    private  ArrayList<Card> stackCards;

    public CardDeck() {
        cards = new ArrayList<Card>();

        cards.add(new Card(Card.HERZ, Card.SIEBEN, 7, R.drawable.herz_sieben));
        cards.add(new Card(Card.HERZ, Card.ACHT, 8, R.drawable.herz_acht));
        cards.add(new Card(Card.HERZ, Card.NEUN, 9, R.drawable.herz_neun));
        cards.add(new Card(Card.HERZ, Card.ZEHN, 10, R.drawable.herz_zehn));
        cards.add(new Card(Card.HERZ, Card.UNTER, 10, R.drawable.herz_unter));
        cards.add(new Card(Card.HERZ, Card.OBER, 10, R.drawable.herz_ober));
        cards.add(new Card(Card.HERZ, Card.KOENIG, 10, R.drawable.herz_koenig));
        cards.add(new Card(Card.HERZ, Card.ASS, 11, R.drawable.herz_ass));

        cards.add(new Card(Card.PIK, Card.SIEBEN, 7, R.drawable.pik_sieben));
        cards.add(new Card(Card.PIK, Card.ACHT, 8, R.drawable.pik_acht));
        cards.add(new Card(Card.PIK, Card.NEUN, 9, R.drawable.pik_neun));
        cards.add(new Card(Card.PIK, Card.ZEHN, 10, R.drawable.pik_zehn));
        cards.add(new Card(Card.PIK, Card.UNTER, 10, R.drawable.pik_unter));
        cards.add(new Card(Card.PIK, Card.OBER, 10, R.drawable.pik_ober));
        cards.add(new Card(Card.PIK, Card.KOENIG, 10, R.drawable.pik_koenig));
        cards.add(new Card(Card.PIK, Card.ASS, 11, R.drawable.pik_ass));

        cards.add(new Card(Card.KARO, Card.SIEBEN, 7, R.drawable.karo_sieben));
        cards.add(new Card(Card.KARO, Card.ACHT, 8, R.drawable.karo_acht));
        cards.add(new Card(Card.KARO, Card.NEUN, 9, R.drawable.karo_neun));
        cards.add(new Card(Card.KARO, Card.ZEHN, 10, R.drawable.karo_zehn));
        cards.add(new Card(Card.KARO, Card.UNTER, 10, R.drawable.karo_unter));
        cards.add(new Card(Card.KARO, Card.OBER, 10, R.drawable.karo_ober ));
        cards.add(new Card(Card.KARO, Card.KOENIG, 10, R.drawable.karo_koenig));
        cards.add(new Card(Card.KARO, Card.ASS, 11, R.drawable.karo_ass));

        cards.add(new Card(Card.KREUZ, Card.SIEBEN, 7, R.drawable.kreuz_sieben));
        cards.add(new Card(Card.KREUZ, Card.ACHT, 8, R.drawable.kreuz_acht));
        cards.add(new Card(Card.KREUZ, Card.NEUN, 9, R.drawable.kreuz_neun));
        cards.add(new Card(Card.KREUZ, Card.ZEHN, 10, R.drawable.kreuz_zehn));
        cards.add(new Card(Card.KREUZ, Card.UNTER, 10, R.drawable.kreuz_unter));
        cards.add(new Card(Card.KREUZ, Card.OBER, 10, R.drawable.kreuz_ober));
        cards.add(new Card(Card.KREUZ, Card.KOENIG, 10, R.drawable.kreuz_koenig));
        cards.add(new Card(Card.KREUZ, Card.ASS, 11, R.drawable.kreuz_ass));

        stackCards = (ArrayList<Card>) cards.clone();
    }

    public Card[] getThreeCardsFromStack(){
        Card[] cards = new Card[3];
        int randNumb;
        for (int i = 0; i < cards.length; i++) {
            randNumb = (int) Math.round(Math.random()*(stackCards.size()));
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
