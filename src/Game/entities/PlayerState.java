/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/
package Game.entities;

import Game.PlayerClass;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<String> abilities = new ArrayList<>();

	public String[] pendingChoices = null;
	public boolean choosingAbility = false;
	public String[] getAbilityChoicesForLevel() {

	    if (playerClass == PlayerClass.WARRIOR) {
	        if (level == 3) {
	            return new String[]{"IMMUNITY", "DASH"};
	        }
	        /*if (level == 5) {
	            return new String[]{"BIG_SLASH", "EARTHSHATTER"};
	        }*/
	    }

	    return null;
	}
	
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

	        String[] choices = getAbilityChoicesForLevel();
	        if (choices != null) {
	            pendingChoices = choices;
	            choosingAbility = true;
	            break; // STOP leveling until player chooses
	        }
	    }
	}
	public void chooseAbility(String ability) {
	    if (pendingChoices == null) return;

	    for (String a : pendingChoices) {
	        if (a.equals(ability)) {
	            abilities.add(a);
	            break;
	        }
	    }

	    pendingChoices = null;
	    choosingAbility = false;
	}
	public boolean isImmune = false;
	public long immunityEndTime = 0;

	public void takeDamage(int amount) {

	    // passive immunity check
	    if (isImmune) return;

	    hp -= amount;

	    if (hp < 0) hp = 0;
	}
	public void activateImmunity(long durationMs) {
	    isImmune = true;
	    immunityEndTime = System.currentTimeMillis() + durationMs;
	}
}
