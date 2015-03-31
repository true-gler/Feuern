package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InputDevice;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import se2.groupa.feuern.network.ServerThread;


/**
 * StartScreen to choose a new game
 */
public class MainActivity extends Activity {

    private EditText textViewServername;
    private Switch switchStartStopServer;

    private Thread serverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        switchStartStopServer = (Switch) findViewById(R.id.switchStartStopServer);
        switchStartStopServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    textViewServername.setEnabled(false);

                    // start server
                    serverThread = new Thread(new ServerThread(textViewServername.getText().toString()));
                    serverThread.start();

                } else {
                    textViewServername.setEnabled(true);

                    if (serverThread != null)
                        serverThread.interrupt();
                }
            }
        });

        textViewServername = (EditText)findViewById(R.id.textServername);
        textViewServername.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0)
                    switchStartStopServer.setEnabled(true);
                else
                    switchStartStopServer.setEnabled(false);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
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

    public void openNewGame(View view){
     //    Intent intent = new Intent(this, nextActivity.class);
     //   startActivity(intent);

        System.out.println("New Game");
        Log.d("MainActivity","debug msg");
        Log.v("MainActivity","View msg");
    }
}
