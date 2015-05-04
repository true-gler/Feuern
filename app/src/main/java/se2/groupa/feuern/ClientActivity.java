package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import se2.groupa.feuern.adapters.PlayerAdapter;
import se2.groupa.feuern.controller.ApplicationController;
import se2.groupa.feuern.model.GameState;
import se2.groupa.feuern.model.Player;
import se2.groupa.feuern.network.threads.ClientThread;
import se2.groupa.feuern.network.classes.NetworkHelper;
import se2.groupa.feuern.network.classes.Operations;


/** Lobby activity
 * Shows the Server Ip to let others know how to connect (maybe we should do this with a
 * specified port and do auto-discovery)
 *
 * Shows connected Clients
 * and a button start game
 */

public class ClientActivity extends Activity {

    private EditText etServerName;
    private EditText etIPAddress;
    private Button btnConnectDisconnect;
    private ListView lvServerPlayers;
    private PlayerAdapter lvServerPlayersAdapter;
    private String playerName;
    private Handler uiHandler;

    private Thread parentClientThread = null;
    private ClientThread clientThread;

    private ArrayList<Player> currentServerPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                playerName = null;
            } else {
                playerName = extras.getString("PlayerName");
            }
        } else {
            playerName = (String) savedInstanceState.getSerializable("PlayerName");
        }

        initializeUiControls();
        initializeUiHandler();
    }

    public void connectDisconnectServer(View view)
    {
        if (btnConnectDisconnect.getText().toString().toLowerCase().equals("connect")) {
            etServerName.setEnabled(false);
            etIPAddress.setEnabled(false);
            btnConnectDisconnect.setEnabled(false);

            clientThread = new ClientThread(etIPAddress.getText().toString(), NetworkHelper.getPort(), etServerName.getText().toString(), playerName, uiHandler, false);
            parentClientThread = new Thread(clientThread);
            parentClientThread.start();
        }
        else
        {
            if (clientThread != null)
            {
                clientThread.shutdown();
                serverDisconnected();
            }
        }
    }

    public void serverConnected()
    {
        etServerName.setEnabled(false);
        etIPAddress.setEnabled(false);
        btnConnectDisconnect.setText("Disconnect");
        btnConnectDisconnect.setEnabled(true);

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    public void serverDisconnected()
    {
        etServerName.setEnabled(true);
        etIPAddress.setEnabled(true);
        btnConnectDisconnect.setText("Connect");
        btnConnectDisconnect.setEnabled(true);

        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    public void updateServerPlayers(ArrayList<Player> players)
    {
        if (players != null) {
            currentServerPlayers = players;
            lvServerPlayersAdapter.clear();

            for (Player player : currentServerPlayers) {
                lvServerPlayersAdapter.add(player);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
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

    private void initializeUiControls()
    {
        etServerName = (EditText) findViewById(R.id.etServerName);
        etIPAddress = (EditText) findViewById(R.id.etIPAddress);
        btnConnectDisconnect = (Button) findViewById(R.id.btnConnectDisconnect);
        lvServerPlayers = (ListView) findViewById(R.id.lvServerPlayers);

        btnConnectDisconnect.setEnabled(false);
        currentServerPlayers = new ArrayList<Player>();
        lvServerPlayersAdapter = new PlayerAdapter(this, currentServerPlayers);
        lvServerPlayers.setAdapter(lvServerPlayersAdapter);

        etServerName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0 && etIPAddress.getText().toString() != null && etIPAddress.getText().toString().length() > 0)
                    btnConnectDisconnect.setEnabled(true);
                else
                    btnConnectDisconnect.setEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        etIPAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0 && etServerName.getText().toString() != null && etServerName.getText().toString().length() > 0)
                    btnConnectDisconnect.setEnabled(true);
                else
                    btnConnectDisconnect.setEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void initializeUiHandler()
    {
        uiHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == Operations.SetConnected.getValue()) {
                    serverConnected();
                }
                else if (msg.what == Operations.SetDisconnected.getValue()) {
                    serverDisconnected();
                }
                else if (msg.what == Operations.UpdatePlayers.getValue()) {
                    if ((ArrayList<Player>) msg.obj != null) {
                        updateServerPlayers((ArrayList<Player>) msg.obj);
                    }
                }
                else if (msg.what == Operations.MakeToast.getValue()) {
                    if ((String)msg.obj != null)
                        Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == Operations.StartGame.getValue()) {
                    ShowGameActivity((GameState)msg.obj);
                }

                super.handleMessage(msg);
            }
        };
    }

    public void ShowGameActivity(GameState gameState)
    {
        Intent intent = new Intent(this, GameActivity.class);

        ApplicationController.setClientThread(clientThread);
        intent.putExtra("gameState", gameState);
        intent.putExtra("playerName", playerName);

        startActivity(intent);
    }
}
