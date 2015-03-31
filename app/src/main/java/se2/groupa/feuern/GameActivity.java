package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.others.Card;
import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

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

        //Hier werden jetzt zu Testzwecken einfach vier Spieler-Objekte initialisiert
        int number = 4;

        ArrayList<Player> players = new ArrayList<Player>(number);
        for (int i =1; i<=number; i++){
            players.add(new Player(i, "player"+i));
        }

        final GameController gameController = new GameController(players);
        gameController.dealingOutCards();

        Player player1_real= players.get(1);
        final Card [] cards_player1 = player1_real.getCards();

        final ImageButton btn_ownCardsRight  = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle  = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft  = (ImageButton) findViewById(R.id.ownCardsLeft);

        btn_ownCardsRight.setImageResource(cards_player1[0].getDrawable());
        btn_ownCardsMiddle.setImageResource(cards_player1[1].getDrawable());
        btn_ownCardsLeft.setImageResource(cards_player1[2].getDrawable());

        ImageButton btn_publicCardsRight  = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle  = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft  = (ImageButton) findViewById(R.id.publicCardsLeft);

        btn_publicCardsRight.setImageResource(gameController.getGameState().getPublicCards()[0].getDrawable());
        btn_publicCardsMiddle.setImageResource(gameController.getGameState().getPublicCards()[1].getDrawable());
        btn_publicCardsLeft.setImageResource(gameController.getGameState().getPublicCards()[2].getDrawable());


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
