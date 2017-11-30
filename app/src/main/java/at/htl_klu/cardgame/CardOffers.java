package at.htl_klu.cardgame;

/**
 * Created by Glups on 30/11/2017.
 */

public class CardOffers {

    String name;
    int price;
    int Anzahl;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAnzahl() {
        return Anzahl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAnzahl(int anzahl) {
        Anzahl = anzahl;
    }

    public CardOffers(String name, int price, int anzahl) {
        this.name = name;
        this.price = price;
        Anzahl = anzahl;
    }
}

