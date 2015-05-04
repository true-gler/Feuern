package se2.groupa.feuern.network.threads;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.network.classes.CommunicationCommand;
import se2.groupa.feuern.network.classes.NetworkMessage;
import se2.groupa.feuern.network.classes.Operations;

/**
 * Created by Lukas on 16.03.15.
 *
 * An instance of this thread wil run for each connected client
 * --> also one instance for the client located on the server itself
 */
public class ServerThread implements Runnable {

    private String serverName;
    private Socket clientSocket;
    private ServerController serverController;
    private Handler uiHandler;
    private String currentPlayerName;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private boolean isRunning;

    public ServerThread(Socket clientSocket, String serverName, ServerController serverController, Handler uiHandler) {

        this.serverName = serverName;
        this.clientSocket = clientSocket;
        this.serverController = serverController;
        this.uiHandler = uiHandler;
    }

    @Override
    public void run() {


        isRunning = true;

        try {
            output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(this.clientSocket.getInputStream());

            output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, "connection established", null));
            output.flush();

            while (isRunning) {

                try {
                    NetworkMessage read = (NetworkMessage)input.readObject();

                    if (read == null) // client is unavailable
                    {
                        isRunning = false;
                    }
                    else if (read.getMessage().toLowerCase().equals(CommunicationCommand.ReturnGameStateToServer.toString().toLowerCase()))
                    {
                        ListenerThread.broadcastCommand(CommunicationCommand.UpdateGameState, read.getParameter());
                    }
                    else if (read.getMessage().toLowerCase().equals(CommunicationCommand.GetServername.toString().toLowerCase()))
                    {
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, serverName, null));
                    }
                    else if (read.getMessage().toLowerCase().startsWith(CommunicationCommand.Register.toString().toLowerCase()))
                    {
                        if (serverController.getPlayers().size() == 9)
                        {
                            output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "server is already full", null));
                        }
                        else
                        {
                            if (currentPlayerName == null || currentPlayerName.isEmpty()) {
                                String[] str = read.getMessage().split(" "); // command has to look like: "register Lukas"

                                if (str.length > 0) {
                                    String playerName = str[1];

                                    if (serverController.addPlayer(playerName)) {

                                        currentPlayerName = playerName;
                                        output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, "successfully registered", null));

                                        Message msg = uiHandler.obtainMessage();
                                        msg.what = Operations.AddPlayer.getValue();
                                        msg.obj = serverController.getPlayer(currentPlayerName);
                                        uiHandler.sendMessage(msg);
                                    } else {
                                        output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "playername already exists", null));
                                    }
                                } else {
                                    output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "pass playername as parameter", null));
                                }
                            } else {
                                output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "you are already registered", null));
                            }
                        }
                    }
                    else if (read.getMessage().toLowerCase().startsWith(CommunicationCommand.Unregister.toString().toLowerCase()))
                    {
                        if (currentPlayerName == null || currentPlayerName.isEmpty()) {
                            output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "you have not been registered yet", null));
                        }
                        else
                        {
                            unregister(false);
                        }
                    }
                    else if (read.getMessage().toLowerCase().equals(CommunicationCommand.Bye.toString().toLowerCase()) || read.getMessage().toLowerCase().equals(CommunicationCommand.Quit.toString().toLowerCase()))
                    {
                        unregister(true);
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, "Bye", null));
                    }
                    else if (read.getMessage().toLowerCase().equals(CommunicationCommand.Help.toString().toLowerCase()) || read.equals("?"))
                    {
                        String result = "available commands: ";

                        for (CommunicationCommand command : CommunicationCommand.values())
                        {
                            result += command.toString() + ", ";
                        }

                        // remove last ", "
                        result = result.substring(0, result.length() - 2);
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, result, null));
                    }
                    else
                    {
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "invalid command", null));
                    }

                    output.flush();

                } catch (Exception e) {
                    e.printStackTrace();

                    Message msg = uiHandler.obtainMessage();
                    msg.what = Operations.MakeToast.getValue();
                    msg.obj = e.getMessage();
                    uiHandler.sendMessage(msg);
                }
            }

            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();

            Message msg = uiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            uiHandler.sendMessage(msg);
        }
    }

    private void unregister(boolean isSuppressed)
    {
        isRunning = false;

        try {
            if (currentPlayerName != null && !currentPlayerName.isEmpty()) {
                if (serverController.deletePlayer(currentPlayerName)) {
                    if (!isSuppressed) {
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.OK, "successfully unregistered", null));
                        output.flush();
                    }

                    Message msg = uiHandler.obtainMessage();
                    msg.what = Operations.RemovePlayer.getValue();
                    msg.obj = currentPlayerName;
                    uiHandler.sendMessage(msg);

                    currentPlayerName = null;
                } else {
                    if (!isSuppressed) {
                        output.writeObject(new NetworkMessage(NetworkMessage.Status.ERROR, "could not unregister", null));
                        output.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Message msg = uiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            uiHandler.sendMessage(msg);
        }
    }

    public void shutdown() {
        isRunning = false;

        try {
            if (output != null) {
                output.writeObject(new NetworkMessage(NetworkMessage.Status.INFO, CommunicationCommand.ServerShutdown.toString(), null));
                output.flush();
            }

            if (clientSocket != null)
                clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();

            Message msg = uiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            uiHandler.sendMessage(msg);
        }
    }

    public void executeCommand(CommunicationCommand command, Object parameter)
    {
        try {
            if (output != null) {

                /*
                if (command == CommunicationCommand.StartGame) {
                    output.writeObject(new NetworkMessage(NetworkMessage.Status.REQUEST, command.toString(), parameter));
                    output.flush();
                }
                else if (command == CommunicationCommand.StartGame.UpdatePlayers) {
                    output.writeObject(new NetworkMessage(NetworkMessage.Status.REQUEST, command.toString(), parameter));
                    output.flush();
                }
                */

                output.writeObject(new NetworkMessage(NetworkMessage.Status.REQUEST, command.toString(), parameter));
                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();

            Message msg = uiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            uiHandler.sendMessage(msg);
        }
    }
}

