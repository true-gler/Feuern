package se2.groupa.feuern.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Lukas on 16.03.15.
 */
public class PlayerThread implements Runnable {

    private String servername;
    private Socket clientSocket;

    private BufferedReader input;
    private BufferedWriter output;

    private boolean isRunning;

    public PlayerThread(Socket clientSocket, String servername) {

        this.servername = servername;
        this.clientSocket = clientSocket;

        try {

            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        isRunning = true;

        while (isRunning) {

            try {

                String read = input.readLine();

                if (read.equals("getServername"))
                {
                    output.write("[OK] " + servername);
                    output.flush();
                }
                else if (read.equals("bye"))
                {
                    isRunning = false;

                    output.write("[OK] Bye");
                    output.flush();
                }
                //updateConversationHandler.post(new UpdateUIThread(read));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
