package se2.groupa.feuern.model;

import java.io.Serializable;

/**
 * Created by Taurer on 30.03.2015.
 */
public class Card implements Serializable {

    private int color;
    private int number;
    private int value;
    //Hier wird noch ein zusätzliches Attribut eingeführt, um auf die .png-Dateien im drawable Ordner zu zeigen
    private int drawable;


    //TODO private Image image;

    public static final int HERZ = 1;
    public static final int PIK = 2;
    public static final int KARO = 3;
    public static final int KREUZ = 4;

    public static final int SIEBEN = 5;
    public static final int ACHT = 6;
    public static final int NEUN = 7;
    public static final int ZEHN = 8;
    public static final int UNTER = 9;
    public static final int OBER = 10;
    public static final int KOENIG = 11;
    public static final int ASS = 12;

    public Card(int color, int number, int drawable) {
        this.color = color;
        this.number = number;
        switch(number){
            case SIEBEN:
                this.value = 7;
                break;
            case ACHT:
                this.value = 8;
                break;
            case NEUN:
                this.value = 9;
                break;
            case ASS:
                this.value = 11;
                break;
            default:
                this.value = 10;
        }
        this.drawable = drawable;
        //TODO this.image = new Image(<path>);
    }

    public int getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public int getValue() {
        return value;
    }

    public int getDrawable() { return this.drawable; }

    //TODO getter Image

    @Override
    public boolean equals(Object o) {
        Card card = (Card) o;
        if(this.getColor() == card.getColor() && this.getNumber() == card.getNumber()){
            return true;
        }
        return false;
    }
}
