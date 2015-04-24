package se2.groupa.feuern.network.classes;

public enum Operations {
    AddPlayer(1),
    RemovePlayer(2),
    StartGame(3),
    SetConnected(4),
    UpdatePlayers(5),
    MakeToast(6),
    StopServer(7),
    SetDisconnected(8);

    private int value;

    private Operations(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
