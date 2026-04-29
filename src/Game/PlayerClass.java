package Game;

public enum PlayerClass {
    // information on the player classes with there health and damage
    WARRIOR(120, 6),
    ARCHER(80, 10),
    BERSERKER(100, 14);
    // holds data on the player classes max health and attack 
    public final int maxHp;
    public final int attack;
   // constructor that stores information on player class type
    PlayerClass(int maxHp, int attack) {
        this.maxHp = maxHp;
        this.attack = attack;
    }
}