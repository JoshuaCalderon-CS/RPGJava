package Game.entities;

import Game.PlayerClass;

public class PlayerState {
	public PlayerClass playerClass;
	public int hp;
	public int maxHp;
	public int attack;
	public int gridX = 1;
	public int gridY = 1;

	public int x;
	public int y;

	public int xp = 0;
	public int level = 1;
	
	public void checkLevelUp() {

	    while (xp >= 100) {
	        xp -= 100;
	        level++;
	        maxHp += 10;
	        hp = maxHp;
	        attack += 2;
	    }
	}
	public void heal(int amount) {
	    hp = Math.min(maxHp, hp + amount);
	}
	public void applyClassStats() {

	    maxHp = playerClass.maxHp;
	    attack = playerClass.attack;
	    hp = maxHp;
	}
	public void gainXP(int amount) {
	    xp += amount;

	    while (xp >= 100) {
	        xp -= 100;
	        level++;
	        maxHp += 10;
	        attack += 2;
	        hp = maxHp;
	    }
	}
}
