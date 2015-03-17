package se2.groupa.feuern;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
    }
    //ah ah ah


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
