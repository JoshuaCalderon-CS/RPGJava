package Game.ui;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Game.core.Room;
import Game.entities.Enemy;
import Game.entities.EnemyType;
import Game.entities.PlayerState;
import Game.entities.Projectile;
import Game.entities.ProjectileType;
import Game.GameLauncher;
import Game.PlayerClass;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class gamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private PlayerState player;
    private gameGUI parent;
    private Room room;
    private boolean gameOver = false;
    private java.util.ArrayList<Enemy> enemies = new java.util.ArrayList<>();
    private long lastEnemyHit = 0;
	private gameGUI gameGUI;
	private Image swordIcon;
	private Image bowIcon;
	private Image axeIcon;
	private java.util.ArrayList<Projectile> projectiles = new java.util.ArrayList<>();
	private long lastShotTime = 0;
	private int lastDx = 1;
    private int lastDy = 0;
    private java.util.ArrayList<Projectile> enemyProjectiles = new java.util.ArrayList<>();
    private long lastEnemyShot = 0;
    private long enemyShootCooldown = 600; // adjust difficulty
	private long shootCooldown = 400; // ms (change this to balance fire rate)
    private static final int PLAYER_SIZE = 50;
    private static final int W = 400;
    private static final int H = 300;
    private long lastKnockbackTime = 0;
    private double distance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public gamePanel(gameGUI parent, Room room, PlayerState player,
            Image swordIcon, Image bowIcon, Image axeIcon) {
    	this.parent = parent;
    	this.player = player;
    	this.room = room;
    	this.gameGUI = parent;
    	this.swordIcon = swordIcon;
    	this.bowIcon = bowIcon;
    	this.axeIcon = axeIcon;
        setFocusable(true);

        int move = 10;
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "heal");

        getActionMap().put("heal", new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            player.heal(20);
            repaint();
        	}
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "attack");

        getActionMap().put("attack", new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            attack();
        	}
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "shoot");

        getActionMap().put("shoot", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
        // RIGHT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        getActionMap().put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	if (gameOver) return;
                player.x += move;
                lastDx = 1;
                lastDy = 0;
                checkCombat();
                if (player.x > getWidth() - 50) {
                    parent.goToRoom("right", player.x, player.y);
                    return;
                }
                repaint();
            }
        });
        // LEFT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        getActionMap().put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	if (gameOver) return;
                player.x -= move;
                lastDx = -1;
                lastDy = 0;
                checkCombat();
                if (player.x < 0) {
                    parent.goToRoom("left", player.x, player.y);
                    return;
                }
                repaint();
            }
        });

        // UP
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        getActionMap().put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	if (gameOver) return;
                player.y -= move;
                lastDx = 0;
                lastDy = -1;
                checkCombat();
                if (player.y < 0) {
                    parent.goToRoom("up", player.x, player.y);
                    return;
                }
                repaint();
            }
        });

        // DOWN
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        getActionMap().put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	if (gameOver) return;
                player.y += move;
                lastDx = 0;
                lastDy = 1;
                checkCombat();
                if (player.y > getHeight() - 50) {
                    parent.goToRoom("down", player.x, player.y);
                    return;
                }
                repaint();
            }
        });
        javax.swing.SwingUtilities.invokeLater(() -> {
            spawnEnemies();
        });
        new javax.swing.Timer(50, event -> {
        	checkPlayerDeath();
        	if (gameOver) {
                repaint();   
                return;
            }
        	enemyShoot();
        	for (Projectile p : enemyProjectiles) {
        	    p.update();
        	}

        	enemyProjectiles.removeIf(p -> !p.active);
        	for (Projectile p : enemyProjectiles) {

        		Rectangle projBox = new Rectangle((int)p.x, (int)p.y, p.size, p.size);
        	    Rectangle playerBox = new Rectangle(player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);

        	    if (projBox.intersects(playerBox)) {
        	        player.hp -= 5;
        	        p.active = false;
        	    }
        	}
        	for (Projectile p : projectiles) {
        	    p.update();
        	}
        	projectiles.removeIf(p -> !p.active);
        	for (Projectile p : projectiles) {

        		Rectangle projBox = new Rectangle((int)p.x, (int)p.y, p.size, p.size);
        	    for (Enemy e : enemies) {

        	        if (e.dead) continue;

        	        Rectangle enemyBox = new Rectangle((int)e.x, (int)e.y, e.size, e.size);

        	        if (projBox.intersects(enemyBox)) {
        	        	e.takeDamage(player.attack, enemies, player);
        	            p.active = false;

        	            break;
        	        }
        	    }
        	}
        	
            // update enemies OR death animation
            for (Enemy enemy : enemies) {
            	
            	if (enemy.dead) {
            	    enemy.alpha -= 0.05f;

            	    if (enemy.alpha < 0f) {
            	        enemy.alpha = 0f;
            	    }
            	} else {
            		enemy.update(player);
            		clampEnemyToScreen(enemy); 
            	}
            }
            resolveEnemyCollisions();

            enemies.removeIf(enemy -> enemy.dead && enemy.alpha <= 0);
            
            handleEnemyDeaths();
            checkCombat();
            repaint();

        }).start();
    }
    private void resolveEnemyCollisions() {

        for (int i = 0; i < enemies.size(); i++) {

            Enemy a = enemies.get(i);

            for (int j = i + 1; j < enemies.size(); j++) {

                Enemy b = enemies.get(j);

                int ax = a.x;
                int ay = a.y;
                int bx = b.x;
                int by = b.y;

                int dx = ax - bx;
                int dy = ay - by;

                int dist = (int)Math.sqrt(dx * dx + dy * dy);

                int minDist = (a.size + b.size) / 2;

                if (dist == 0) dist = 1;

                if (dist < minDist) {

                    double pushX = (dx / (double)dist);
                    double pushY = (dy / (double)dist);

                    int overlap = minDist - dist;

                    a.x += pushX * (overlap / 2.0);
                    a.y += pushY * (overlap / 2.0);

                    b.x -= pushX * (overlap / 2.0);
                    b.y -= pushY * (overlap / 2.0);
                }
            }
        }
    }
    private void spawnEnemies() {

        enemies.clear();

        int width = getWidth();
        int height = getHeight();

        EnemyType[] pool = {
            EnemyType.MELEE,
            EnemyType.SHOOTER,
            EnemyType.SLIME
        };

        int safeDistance = 80;

        for (int i = 0; i < 3; i++) {

            int x, y;

            do {
                x = (int)(Math.random() * (width - 50));
                y = (int)(Math.random() * (height - 50));
            } while (distance(x, y, player.x, player.y) < safeDistance);

            EnemyType type = pool[(int)(Math.random() * pool.length)];

            if (type == EnemyType.SLIME) {
                enemies.add(new Enemy(x, y, EnemyType.SLIME, 60, 30));
            } else if (type == EnemyType.SHOOTER) {
                enemies.add(new Enemy(x, y, EnemyType.SHOOTER, 30, 20));
            } else {
                enemies.add(new Enemy(x, y, EnemyType.MELEE, 30, 30));
            }
        }
    }
    private void enemyShoot() {

        long now = System.currentTimeMillis();
        if (now - lastEnemyShot < enemyShootCooldown) return;

        lastEnemyShot = now;

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            if (e.type != EnemyType.SHOOTER || e.dead) continue;

            int ex = (int)e.x;
            int ey = (int)e.y;

            int px = player.x;
            int py = player.y;

            double dx = px - ex;
            double dy = py - ey;

            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 0.01) continue;

            enemyProjectiles.add(
                new Projectile(ex, ey, dx / dist, dy / dist, ProjectileType.ENEMY)
            );
        }
    }
    public void centerPlayer() {
        player.x = (getWidth() - PLAYER_SIZE) / 2;
        player.y = (getHeight() - PLAYER_SIZE) / 2;
        repaint();
    }
    private void checkCombat() {

        Rectangle playerBox = new Rectangle(player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            if (e.dead) continue;

            Rectangle enemyBox = new Rectangle((int)e.x, (int)e.y, e.size, e.size);

            long nowe = System.currentTimeMillis();

            if (playerBox.intersects(enemyBox)) {

                if (nowe - lastKnockbackTime > 200) { // 200ms cooldown
                    applyKnockback(e);
                    lastKnockbackTime = nowe;
                }

                player.hp -= 5;

                // ALL enemies can damage on contact
                long now = System.currentTimeMillis();

                if (e.type == EnemyType.SLIME) {
                    if (now - e.lastHitTime > 600) {
                        player.hp -= 3;
                        e.lastHitTime = now;
                    }
                }

                // only melee contact damage applies
                if (player.playerClass != PlayerClass.ARCHER) {
                    e.takeDamage(player.attack, enemies, player);
                }
            }
            }
        }
    private void attack() {

        Rectangle playerBox = new Rectangle(player.x, player.y, 50, 50);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            Rectangle enemyBox = new Rectangle((int)e.x, (int)e.y, e.size, e.size);

            if (playerBox.intersects(enemyBox)) {
            	e.takeDamage(player.attack, enemies, player);
                break; // only hit one enemy per attack
            }
        }
    }
    private void shoot() {

        if (player.playerClass != PlayerClass.ARCHER) return;

        long now = System.currentTimeMillis();
        if (now - lastShotTime < shootCooldown) return;

        lastShotTime = now;

        int px = player.x + PLAYER_SIZE / 2;
        int py = player.y + PLAYER_SIZE / 2;

        double dx = -lastDx;
        double dy = -lastDy;

        // fallback if standing still
        if (dx == 0 && dy == 0) dx = 1;

        projectiles.add(new Projectile(px, py, dx, dy, ProjectileType.PLAYER));
    }
    private void showGameOver(Graphics g) {

        g.setColor(new Color(0, 0, 0, 180)); // dark overlay
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press R to restart", getWidth() / 2 - 90, getHeight() / 2 + 40);
    }
    private void handleEnemyDeaths() {

    	for (int i = 0; i < enemies.size(); i++) {
    	    Enemy e = enemies.get(i);
            if (e.dead && e.hp <= 0) {
                e.kill(player);
            }
        }

        enemies.removeIf(e -> e.dead && e.alpha <= 0);
    }
    private void checkPlayerDeath() {
        if (player.hp <= 0) {
            player.hp = 0;
            gameOver = true;
        }
    }
    private void applyKnockback(Enemy e) {

        double dx = player.x - e.x;
        double dy = player.y - e.y;

        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) dist = 1;

        double force = 10; // tweak this for strength

        double nx = dx / dist;
        double ny = dy / dist;

        // push player away
        player.x += nx * force;
        player.y += ny * force;

        // push enemy opposite direction
        e.x -= nx * force;
        e.y -= ny * force;
    }
    private void clampEnemyToScreen(Enemy e) {
        int padding = 0; // or 5 if you want a small margin

        if (e.x < padding) e.x = padding;
        if (e.y < padding) e.y = padding;

        if (e.x > getWidth() - e.size - padding) {
            e.x = getWidth() - e.size - padding;
        }

        if (e.y > getHeight() - e.size - padding) {
            e.y = getHeight() - e.size - padding;
        }
    }
    @Override
    public void addNotify() {
        super.addNotify();

        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");

        am.put("restart", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                System.out.println("R pressed");

                if (!gameOver) return;

                parent.dispose();
                GameLauncher.showMenu();
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 

        // background
        g.setColor(new java.awt.Color(245, 245, 240));
        g.fillRect(0, 0, getWidth(), getHeight());

        // grid
        g.setColor(new java.awt.Color(210, 210, 210));
        int spacing = 25;

        for (int x = 0; x < getWidth(); x += spacing) {
            g.drawLine(x, 0, x, getHeight());
        }

        for (int y = 0; y < getHeight(); y += spacing) {
            g.drawLine(0, y, getWidth(), y);
        }

        // player
        g.setColor(Color.RED);
        g.fillRect(player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);

        g.setColor(Color.BLACK);
        g.drawRect(player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);

        // icon
        Image icon = null;

        switch (player.playerClass) {
            case WARRIOR:
                icon = swordIcon;
                break;
            case ARCHER:
                icon = bowIcon;
                break;
            case BERSERKER:
                icon = axeIcon;
                break;
        }

        if (icon != null) {
            int iconSize = PLAYER_SIZE / 2;

            g.drawImage(
                icon,
                player.x + (PLAYER_SIZE - iconSize) / 2,
                player.y + (PLAYER_SIZE - iconSize) / 2,
                iconSize,
                iconSize,
                null
            );
        }

        // enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            e.draw(g);

            int ex = (int)e.x;
            int ey = (int)e.y;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(ex, ey - 10, 30, 5);

            g.setColor(Color.RED);
            int barWidth = e.size;
            int hpWidth = (int)(barWidth * ((double)e.hp / e.maxHp));
            g.fillRect(ex, ey - 10, hpWidth, 5);
            g.drawRect(ex, ey - 10, barWidth, 5);
        }

        // projectiles
        for (Projectile p : enemyProjectiles) {
            p.draw(g);
        }
        for (Projectile p : projectiles) {
            p.draw(g);
        }

        // ✅ UI LAST (this fixes your problem)
        g.setColor(Color.BLACK);
        g.drawString("HP: " + player.hp + "/" + player.maxHp, 10, 20);
        g.drawString("Level: " + player.level, 10, 35);
        g.drawString("XP: " + player.xp, 10, 50);
        g.drawString("Press T to shoot (Archer only)", 10, 70);

        if (gameOver) {
            showGameOver(g);
        }
    }
}