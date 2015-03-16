package se2.groupa.feuern.network;

/**
 * Created by Lukas on 16.03.15.
 */
public class UpdateUIThread implements Runnable {
    private String msg;

    public UpdateUIThread(String str) {
        this.msg = str;
    }

    @Override
    public void run() {
        //text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
    }
}
