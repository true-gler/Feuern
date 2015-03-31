package se2.groupa.feuern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import se2.groupa.feuern.others.Card;
import se2.groupa.feuern.others.CardDeck;

/**
 * Displays your own hand when its your turn and let you choose to change one card or to
 * take the whole three cards of the mid
 * (Evtl könnten wir einbauen ob die App im Background ist -> dann notification)
 *
 * reacts   on callbacks for "handy hinunterlegen" -> obegehn
 *          on callbacks for "handy schütteln" -> weiter
 *
 * Also provides to look in the cards of another user :)
 *
 */

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //Hier würden jetzt zu Testzwecken für einen Spieler drei Karten ausgeteilt
        //und drei weitere Karten offen hingelegt:
        CardDeck carddeck = new CardDeck();

        Card [] cards_own = carddeck.getThreeCardsFromStack();

        Card ownCardsRight = cards_own[0];
        Card ownCardsMiddle = cards_own[1];
        Card ownCardsLeft = cards_own[2];

        Log.i("Karte Rechts: ", ownCardsRight.getNumber() + " " + ownCardsRight.getColor());

        ImageButton btn_ownCardsRight  = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle  = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft  = (ImageButton) findViewById(R.id.ownCardsLeft);

        btn_ownCardsRight.setImageResource(ownCardsRight.getDrawable());
        btn_ownCardsMiddle.setImageResource(ownCardsMiddle.getDrawable());
        btn_ownCardsLeft.setImageResource(ownCardsLeft.getDrawable());

        Card [] cards_public = carddeck.getThreeCardsFromStack();

        Card publicCardsRight = cards_public[0];
        Card publicCardsMiddle = cards_public[1];
        Card publicCardsLeft = cards_public[2];

        ImageButton btn_publicCardsRight  = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle  = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft  = (ImageButton) findViewById(R.id.publicCardsLeft);

        btn_publicCardsRight.setImageResource(publicCardsRight.getDrawable());
        btn_publicCardsMiddle.setImageResource(publicCardsMiddle.getDrawable());
        btn_publicCardsLeft.setImageResource(publicCardsLeft.getDrawable());


    }
    //ah ah ah 13zoller 15zoller is the best
    //das ist ein test


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
