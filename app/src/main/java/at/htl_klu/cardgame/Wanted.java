package at.htl_klu.cardgame;

/**
 * Created by Glups on 21/12/2017.
 */

public class Wanted {

    private int positionX;
    private int positionY;
    private int length;
    private int vast;

    public Wanted(int positionX, int positionY, int length, int vast) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.length = length;
        this.vast = vast;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getVast() {
        return vast;
    }

    public void setVast(int vast) {
        this.vast = vast;
    }
}
