package se2.groupa.feuern.tests.modelTests;

import android.test.InstrumentationTestCase;

import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.Player;

/**
 * Created by Taurer on 19.05.2015.
 */
public class PlayerTests extends InstrumentationTestCase{

    public void testGetCardPoints1() throws Exception{
        Card[] cards = new Card[3];
        cards[0] = new Card(Card.HERZ, Card.ACHT, 0);
        cards[1] = new Card(Card.PIK, Card.ACHT, 0);
        cards[2] = new Card(Card.KREUZ, Card.ACHT, 0);
        Player pl = new Player("name");
        pl.setCards(cards);

        assertEquals(30.5, pl.getCardPoints());
    }
    public void testGetCardPoints2() throws Exception{
        Card[] cards = new Card[3];
        cards[0] = new Card(Card.HERZ, Card.ACHT, 0);
        cards[1] = new Card(Card.HERZ, Card.ASS, 0);
        cards[2] = new Card(Card.KREUZ, Card.ACHT, 0);
        Player pl = new Player("name");
        pl.setCards(cards);

        assertEquals(19.0, pl.getCardPoints());
    }

    public void testHasFireTrue() throws Exception{
        Card[] cards = new Card[3];
        cards[0] = new Card(Card.HERZ, Card.ZEHN, 0);
        cards[1] = new Card(Card.HERZ, Card.ASS, 0);
        cards[2] = new Card(Card.HERZ, Card.UNTER, 0);
        Player pl = new Player("name");
        pl.setCards(cards);

        assertEquals(true, pl.hasFire());
    }

    public void testHasFireFalse() throws Exception{
        Card[] cards = new Card[3];
        cards[0] = new Card(Card.HERZ, Card.ZEHN, 0);
        cards[1] = new Card(Card.HERZ, Card.ASS, 0);
        cards[2] = new Card(Card.KREUZ, Card.UNTER, 0);
        Player pl = new Player("name");
        pl.setCards(cards);

        assertEquals(false, pl.hasFire());
    }

    public void testLivePoints() throws Exception{
        Player pl = new Player("name");
        assertEquals(4, pl.getLivePoints());
    }

    public void testDecrementLivePoints() throws Exception{
        Player pl = new Player("name");
        pl.decrementLivePoints();
        assertEquals(3, pl.getLivePoints());
    }
}
