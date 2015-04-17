package se2.groupa.feuern.network;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import se2.groupa.feuern.ServerControllerOperation;
import se2.groupa.feuern.controller.ServerController;

/**
 * Created by Lukas on 16.03.15.
 */
public class ServerThread implements Runnable {

    private String serverName;
    private Socket clientSocket;
    private ServerController serverController;
    private Handler uiHandler;
    private String currentPlayerName;

    private BufferedReader input;
    private BufferedWriter output;

    private boolean isRunning;

    public ServerThread(Socket clientSocket, String serverName, ServerController serverController, Handler uiHandler) {

        this.serverName = serverName;
        this.clientSocket = clientSocket;
        this.serverController = serverController;
        this.uiHandler = uiHandler;

        try {

            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


        try {
            isRunning = true;

            output.write("[OK] connection established\n");
            output.flush();

            while (isRunning) {

                String read = input.readLine();

                if (read.toLowerCase().equals(CommunicationCommand.GetServername.toString().toLowerCase()))
                {
                    output.write("[OK] " + serverName);
                }
                else if (read.toLowerCase().startsWith(CommunicationCommand.Register.toString().toLowerCase()))
                {
                    if (serverController.getPlayers().size() == 9)
                    {
                        output.write("[ER] server is already full");
                    }
                    else
                    {
                        if (currentPlayerName == null || currentPlayerName.isEmpty()) {
                            String[] str = read.split(" ");

                            if (str.length > 0) {
                                String playerName = str[1];

                                if (serverController.addPlayer(playerName)) {
                                    output.write("[OK] successfully registered");

                                    Message msg = uiHandler.obtainMessage();
                                    msg.what = ServerControllerOperation.AddPlayer.getValue();
                                    msg.obj = serverController.getPlayer(playerName);
                                    uiHandler.sendMessage(msg);

                                    currentPlayerName = playerName;
                                } else {
                                    output.write("[ER] playername already exists");
                                }
                            } else {
                                output.write("[ER] pass playername as parameter");
                            }
                        } else {
                            output.write("[ER] you are already registered");
                        }
                    }
                }
                else if (read.toLowerCase().equals(CommunicationCommand.Unregister.toString().toLowerCase()))
                {
                    if (currentPlayerName == null || currentPlayerName.isEmpty()) {
                        output.write("[ER] you have not been registered yet");
                    }
                    else
                    {
                        unregister(false);
                    }
                }
                else if (read.toLowerCase().equals(CommunicationCommand.Bye.toString().toLowerCase()) || read.toLowerCase().equals(CommunicationCommand.Quit.toString().toLowerCase()))
                {
                    unregister(true);
                    isRunning = false;
                    output.write("[OK] Bye");
                }
                else if (read.toLowerCase().equals(CommunicationCommand.Help.toString().toLowerCase()) || read.equals("?"))
                {
                    String result = "[OK] available commands: ";

                    for (CommunicationCommand command : CommunicationCommand.values())
                    {
                        result += command.toString() + ", ";
                    }

                    // remove last ", "
                    result = result.substring(0, result.length() - 2);
                    output.write(result);
                }
                else
                {
                    output.write("[ER] invalid command");
                }

                output.newLine();
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // close connections
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void unregister(boolean isSuppressed)
    {
        try {
            if (currentPlayerName != null && !currentPlayerName.isEmpty()) {
                if (serverController.deletePlayer(currentPlayerName)) {
                    if (!isSuppressed)
                        output.write("[OK] successfully unregistered");

                    Message msg = uiHandler.obtainMessage();
                    msg.what = ServerControllerOperation.RemovePlayer.getValue();
                    msg.obj = currentPlayerName;
                    uiHandler.sendMessage(msg);

                    currentPlayerName = null;
                } else {
                    if (!isSuppressed)
                        output.write("[ER] could not unregister");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;

        try {
            if (output != null) {
                output.write("[IF] Server has been closed\n");
                output.flush();
            }

            if (clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

