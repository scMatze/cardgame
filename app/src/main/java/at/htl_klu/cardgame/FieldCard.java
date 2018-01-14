package at.htl_klu.cardgame;

/**
 * Created by Matze on 14.01.2018.
 */

public class FieldCard {

    private String name;
    private int number;

    public FieldCard(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
