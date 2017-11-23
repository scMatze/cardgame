package at.htl_klu.cardgame;

/**
 * Created by Matze on 20.06.2017.
 */

public class Player {

    private int health;
    private Weapon damage;

    public Player(int health, Weapon damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Weapon getDamage() {
        return damage;
    }

    public void setDamage(Weapon damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "Player{" +
                "health=" + health +
                ", damage=" + damage +
                '}';
    }
}
