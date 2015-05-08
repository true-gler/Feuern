package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se2.groupa.feuern.controller.ApplicationController;


/**
 * StartScreen to choose a new game
 */
public class MainActivity extends Activity {

    private EditText etNickname;
    private Button btnServer;
    private Button btnClient;
    public String playerName;

    public void testGameActivity (View view){
        Intent intent = new Intent(this, GameActivity.class );
        startActivity(intent);
    }

    public void startClientActivity(View view){

        Intent client= new Intent(this, ClientActivity.class);
        client.putExtra("PlayerName", etNickname.getText().toString());
        startActivity(client);
    }

    public void startServerActivity(View view){

        Intent server= new Intent(this, ServerActivity.class);
        server.putExtra("PlayerName", etNickname.getText().toString());
        startActivity(server);
    }

    public void exitApplication(View view)
    {
        this.finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        btnServer = (Button) findViewById(R.id.btnServer);
        btnClient = (Button) findViewById(R.id.btnClient);
        btnServer.setEnabled(false);
        btnClient.setEnabled(false);

        etNickname = (EditText) findViewById(R.id.etNickname);
        etNickname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0) {
                    btnServer.setEnabled(true);
                    btnClient.setEnabled(true);
                } else {
                    btnServer.setEnabled(false);
                    btnClient.setEnabled(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        etNickname.setText(ApplicationController.getPlayerName());

    }

    @Override
    protected void onPause() {
        super.onPause();

        ApplicationController.setPlayerName(etNickname.getText().toString());
    }
}
