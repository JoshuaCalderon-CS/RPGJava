/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/
package Game.entities;

import java.awt.*;

public class Enemy {

	public int x, y;
	public int hp = 30;

	public double vx = 0;
	public double vy = 0;
	
	public float alpha = 1.0f;
	public boolean dead = false;
	public int deathTimer = 0;
	public EnemyType type;
	
	public int size = 50;
	public int maxHp;
	public long lastHitTime = 0;
	private static final long SLIME_HIT_COOLDOWN = 800; // ms
  
	public Enemy(int x, int y, EnemyType type, int size, int hp) {
	    this.x = x;
	    this.y = y;
	    this.type = type;
	    this.size = size;
	    this.hp = hp;
	    this.maxHp = hp;
	}
	public void takeDamage(int damage, java.util.List<Enemy> enemies, PlayerState player) {

	    hp -= damage;

	    // SLIME SPLIT
	    if (type == EnemyType.SLIME && hp > 0 && size > 15) {

	        int newSize = size / 2;
	        int newHp = Math.max(5, maxHp / 2);

	        Enemy s1 = new Enemy((int)x + 10, (int)y, EnemyType.SLIME, newSize, newHp);
	        Enemy s2 = new Enemy((int)x - 10, (int)y, EnemyType.SLIME, newSize, newHp);

	        enemies.add(s1);
	        enemies.add(s2);

	        // IMPORTANT: mark THIS slime dead, but DO NOT give XP
	        dead = true;
	        hp = 0;
	        return;
	    }

	    if (hp <= 0 && !dead) {
	        kill(player);
	    }
	}
	public void kill(PlayerState player) {
	    if (dead) return;

	    dead = true;

	    player.gainXP(20);
	}
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Composite old = g2.getComposite();
        
        if (type == EnemyType.SLIME) {
        	Graphics2D g3 = (Graphics2D) g;

        	// apply fade
        	g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        	// draw slime
        	g3.setColor(Color.GREEN);
        	g3.fillRect((int)x, (int)y, size, size);

        	// reset alpha so it doesn't affect other drawings
        	g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        	// black outline
        	g.setColor(Color.BLACK);
        	g.drawRect((int)x, (int)y, size, size);
            return;
        }

        g2.setComposite(
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        );

        g.setColor(Color.BLUE);
        g.fillRect((int)x, (int)y, size, size);

        // outline
        g.setColor(Color.BLACK);
        g.drawRect((int)x, (int)y, size, size);

        g2.setComposite(old);
    }
    
    
    public void update(PlayerState player) {

        int px = player.x;
        int py = player.y;

        double dx = px - x;
        double dy = py - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < 0.01) return;

        double speed = 1.5;

        switch (type) {

            case MELEE -> {
                // always chase player
                x += (dx / dist) * speed;
                y += (dy / dist) * speed;
            }

            case SHOOTER -> {

                // keep distance (kite behavior)
                if (dist < 120) {
                    x -= (dx / dist) * speed;
                    y -= (dy / dist) * speed;
                }
                else if (dist > 180) {
                    x += (dx / dist) * speed * 0.5;
                    y += (dy / dist) * speed * 0.5;
                }
            }
            case SLIME -> {

                if (dist > 0.01) {
                    x += (dx / dist) * speed;
                    y += (dy / dist) * speed;
                }

                //slight “wobble” so it feels alive
                x += Math.sin(System.currentTimeMillis() * 0.005 + y) * 0.3;
                y += Math.cos(System.currentTimeMillis() * 0.005 + x) * 0.3;
            }
        }
    }
}