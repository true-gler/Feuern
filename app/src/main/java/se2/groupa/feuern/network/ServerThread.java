package se2.groupa.feuern.network;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import se2.groupa.feuern.ServerActivity;
import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.others.Player;

/**
 * Created by Lukas on 16.03.15.
 */
public class ServerThread implements Runnable {

    private String servername;
    private Socket clientSocket;
    private ServerController serverController;

    private BufferedReader input;
    private BufferedWriter output;

    private boolean isRunning;

    public ServerThread(Socket clientSocket, String servername, ServerController serverController) {

        this.servername = servername;
        this.clientSocket = clientSocket;
        this.serverController = serverController;

        try {

            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {


        try {
            isRunning = true;
            //output.write("--------------------------------------\n");
            //output.write("Welcome to FEUERN\n");
            //output.write("Server: " + servername + "\n");
            //output.write("IP-Address: " + clientSocket.getInetAddress().getHostAddress() + "\n");
            //output.write("--------------------------------------\n");
            output.write("[OK] connection established");
            output.flush();

            while (isRunning) {

                String read = input.readLine();

                if (read.equals("server"))
                {
                    output.write("[OK] " + servername);
                }
                else if (read.startsWith("registerAsPlayer"))
                {
                    String[] str = read.split(" ");
                    String playerName = str[1];

                    if (serverController.addPlayer(playerName)) {
                        output.write("[OK] player successfully registered");
                    }
                    else
                    {
                        output.write("[ERR] playername already exists");
                    }
                }
                else if (read.startsWith("unregister"))
                {
                    String[] str = read.split(" ");
                    String playerName = str[1];

                    if (serverController.deletePlayer(playerName)) {
                        output.write("[OK] successfully unregistered");
                    }
                    else
                    {
                        output.write("[ERR] could not unregister");
                    }
                }
                else if (read.equals("bye") || read.equals("quit"))
                {
                    isRunning = false;
                    output.write("[OK] Bye");
                }
                else if (read.equals("help") || read.equals("?"))
                {
                    output.write("[OK] available commands: server, bye, help");
                }
                else
                {
                    output.write("[ERR] invalid command");
                }

                output.newLine();
                output.flush();
            }

            // close connections
            input.close();
            output.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            input = null;
            output = null;
            clientSocket = null;
        }
    }
}
