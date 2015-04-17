package se2.groupa.feuern;

public enum ServerControllerOperation {
    AddPlayer(1), RemovePlayer(2);
    private int value;

    private ServerControllerOperation(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
