package at.htl_klu.cardgame;

/**
 * Created by Matze on 14.01.2018.
 */

public class HandCard {
    private String name;
    private boolean filledwithcard;

    public HandCard(String name, boolean filled) {
        this.name = name;
        this.filledwithcard = filled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFilled() {
        return filledwithcard;
    }

    public void setFilled(boolean filled) {
        this.filledwithcard = filled;
    }
}
