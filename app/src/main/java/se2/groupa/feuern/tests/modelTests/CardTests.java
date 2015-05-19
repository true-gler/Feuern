package se2.groupa.feuern.tests.modelTests;

import android.test.InstrumentationTestCase;

import se2.groupa.feuern.model.Card;

/**
 * Created by Taurer on 19.05.2015.
 */
public class CardTests extends InstrumentationTestCase{

    public void testCardValue1() throws Exception{
        Card card = new Card(Card.HERZ, Card.ACHT, 0);
        assertEquals(8,card.getValue());
    }

    public void testCardValue2() throws Exception{
        Card card = new Card(Card.PIK, Card.ZEHN, 0);
        assertEquals(10,card.getValue());
    }

    public void testCardValue3() throws Exception{
        Card card = new Card(Card.KARO, Card.UNTER, 0);
        assertEquals(10,card.getValue());
    }

    public void testCardValue4() throws Exception{
        Card card = new Card(Card.KREUZ, Card.ASS, 0);
        assertEquals(11,card.getValue());
    }

    public void testEqualTrue() throws Exception{
        Card card1 = new Card(Card.HERZ, Card.ACHT, 0);
        Card card2 = new Card(Card.HERZ, Card.ACHT, 0);
        assertEquals(true, card1.equals(card2));
    }

    public void testEqualFalse1() throws Exception{
        Card card1 = new Card(Card.HERZ, Card.ACHT, 0);
        Card card2 = new Card(Card.PIK, Card.ACHT, 0);
        assertEquals(false, card1.equals(card2));
    }

    public void testEqualFalse2() throws Exception{
        Card card1 = new Card(Card.HERZ, Card.ACHT, 0);
        Card card2 = new Card(Card.HERZ, Card.ASS, 0);
        assertEquals(false, card1.equals(card2));
    }
}
