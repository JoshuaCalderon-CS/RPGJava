
/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/package Game;

public enum PlayerClass {

    WARRIOR(120, 6),
    ARCHER(80, 10),
    BERSERKER(100, 14);

    public final int maxHp;
    public final int attack;

    PlayerClass(int maxHp, int attack) {
        this.maxHp = maxHp;
        this.attack = attack;
    }
}