package at.htl_klu.cardgame;

/**
 * Created by Matze on 20.06.2017.
 */

public class Enemy {

    private int life;
    private int damage;
    private int heal;

    public Enemy(int life, int damage, int heal) {
        this.life = life;
        this.damage = damage;
        this.heal = heal;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "life=" + life +
                ", damage=" + damage +
                ", heal=" + heal +
                '}';
    }
}
