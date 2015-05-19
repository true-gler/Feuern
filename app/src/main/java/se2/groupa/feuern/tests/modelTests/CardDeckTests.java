package se2.groupa.feuern.tests.modelTests;

import android.test.InstrumentationTestCase;

import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.CardDeck;

/**
 * Created by Taurer on 19.05.2015.
 */
public class CardDeckTests extends InstrumentationTestCase {

    public void testGetThreeCardsFromStack() throws Exception{
        CardDeck deck = new CardDeck();
        Card[] threeCards;

        threeCards = deck.getThreeCardsFromStack();
        assertEquals(3, threeCards.length);
    }

    public void testStackEmpty() throws Exception{
        CardDeck deck = new CardDeck();
        Card[] threeCards;

        for (int i = 0; i < 30; i++) {
            deck.getThreeCardsFromStack();
        }

        threeCards = deck.getThreeCardsFromStack();
        assertEquals(null, threeCards);
    }

    public void testStackReset() throws Exception{
        CardDeck deck = new CardDeck();
        Card[] threeCards;

        do{
            threeCards = deck.getThreeCardsFromStack();
        }while(threeCards != null);

        deck.resetCardDeck();

        threeCards = deck.getThreeCardsFromStack();

        assertEquals(3, threeCards.length);
    }


}
