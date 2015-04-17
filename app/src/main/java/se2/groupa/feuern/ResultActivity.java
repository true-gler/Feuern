package se2.groupa.feuern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import se2.groupa.feuern.adapters.PlayerAdapter;
import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.model.Player;


public class ResultActivity extends Activity {

    private GameController gameController = FeuernHelper.gameController;
    private ArrayList<Player> playerList;
    private ListView listView;
    private PlayerAdapter playerAdapter;

    public void fillListView(){
        listView = (ListView) findViewById(R.id.listView);
        playerAdapter = new PlayerAdapter(this, playerList);
        listView.setAdapter(playerAdapter);
        playerAdapter.add(new Player("Michael"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = (ListView) findViewById(R.id.listView);
        playerList = new ArrayList<Player>();
        playerAdapter = new PlayerAdapter(this, playerList);
        listView.setAdapter(playerAdapter);
        //playerAdapter.add(new Player("Michael"));
        //playerAdapter.addAll(gameController.getGameState().getPlayers());
        playerList.addAll(gameController.getGameState().getPlayers());
        Collections.sort(playerList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
