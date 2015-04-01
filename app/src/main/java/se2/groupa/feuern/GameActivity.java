package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.controller.GameState;
import se2.groupa.feuern.others.Card;
import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

/**
 * Displays your own hand when its your turn and let you choose to change one card or to
 * take the whole three cards of the mid
 * (Evtl könnten wir einbauen ob die App im Background ist -> dann notification)
 * <p/>
 * reacts   on callbacks for "handy hinunterlegen" -> obegehn
 * on callbacks for "handy schütteln" -> weiter
 * <p/>
 * Also provides to look in the cards of another user :)
 */

public class GameActivity extends Activity {

    final TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
    final TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
    final ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
    final ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
    final ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
    final ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
    final ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
    final ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
    protected GameController gameController;

    protected void initiate(){

        //Erschafft einen neuen GamenController und teilt Karten an die Spieler aus.

        gameController.dealingOutCards();
        img_nowTurnPlayer.setText(gameController.getGameState().getNowTurnPlayer().getName());
        img_nextTurnPlayer.setText(gameController.getGameState().getNextTurnPlayer().getName());

        btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
        btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
        btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());

        btn_publicCardsRight.setImageResource(gameController.getGameState().getPublicCards()[0].getDrawable());
        btn_publicCardsMiddle.setImageResource(gameController.getGameState().getPublicCards()[1].getDrawable());
        btn_publicCardsLeft.setImageResource(gameController.getGameState().getPublicCards()[2].getDrawable());
    }


    //Erstmal zu Versuchszwecken:
    public GameState ownCardsRightClick(){



        btn_publicCardsRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameController.AustauchVonKarten(gameController.getGameState().getNowTurnPlayer().getCards()[0],
                        gameController.getGameState().getPublicCards()[0]);

                btn_ownCardsRight.setImageResource(gameController.getGameState().
                        getNowTurnPlayer().getCards()[0].getDrawable());

                btn_publicCardsRight.setImageResource(gameController.getGameState()
                        .getPublicCards()[0].getDrawable());

            }
        });


        btn_publicCardsMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameController.AustauchVonKarten(gameController.getGameState().getNowTurnPlayer().getCards()[0],
                        gameController.getGameState().getPublicCards()[1]);

                btn_ownCardsRight.setImageResource(gameController.getGameState().
                        getNowTurnPlayer().getCards()[0].getDrawable());

                btn_publicCardsRight.setImageResource(gameController.getGameState()
                        .getPublicCards()[1].getDrawable());

            }

        });

        btn_publicCardsLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameController.AustauchVonKarten(gameController.getGameState().getNowTurnPlayer().getCards()[0],
                        gameController.getGameState().getPublicCards()[2]);

                btn_ownCardsRight.setImageResource(gameController.getGameState().
                        getNowTurnPlayer().getCards()[0].getDrawable());

                btn_publicCardsRight.setImageResource(gameController.getGameState()
                        .getPublicCards()[2].getDrawable());

            }


        }

        );
        return gameController.getGameState();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Hier werden jetzt zu Testzwecken einfach vier Spieler-Objekte initialisiert
        int number = 4;

        ArrayList<Player> players = new ArrayList<Player>(number);
        for (int i = 1; i <= number; i++) {
            players.add(new Player(i, "player" + i));
        }
        this.gameController = new GameController(players);
        initiate();

        Button btn_next = (Button) findViewById(R.id.buttonNext);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameController.doStep();
                img_nowTurnPlayer.setText(gameController.getGameState().getNowTurnPlayer().getName());
                img_nextTurnPlayer.setText(gameController.getGameState().getNextTurnPlayer().getName());

            }
        });

        btn_ownCardsRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ownCardsRightClick();
            }
        });

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
