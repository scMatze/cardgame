package at.htl_klu.cardgame;

/**
 * Created by Matze on 20.12.2017.
 */

public class priceBlank {

    private int positionX;
    private int positionY;
    private int length;
    private int vast;

    public priceBlank(int positionX, int positionY, int length, int vast) {
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
