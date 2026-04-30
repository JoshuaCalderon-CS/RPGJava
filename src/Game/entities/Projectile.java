/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/
package Game.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Projectile {

    public double x, y;
    public double dx, dy;
    public int size = 6;
    public boolean active = true;

    public ProjectileType type;

    public double speed = 6;

    public Projectile(int x, int y, double dx, double dy, ProjectileType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) length = 1;

        this.dx = (dx / length) * speed;
        this.dy = (dy / length) * speed;
    }

    public void update() {
        x += dx;
        y += dy;

        int width = 400;
        int height = 300;

        if (type == ProjectileType.ENEMY) {

            // 🧱 ENEMY BULLETS BOUNCE
            if (x <= 0 || x >= width - size) {
                dx = -dx;
                x = Math.max(0, Math.min(x, width - size));
            }

            if (y <= 0 || y >= height - size) {
                dy = -dy;
                y = Math.max(0, Math.min(y, height - size));
            }

        } else {

            //PLAYER BULLETS DIE ON EXIT
            if (x < 0 || y < 0 || x > width || y > height) {
                active = false;
            }
        }
    }

    public void draw(Graphics g) {

        if (type == ProjectileType.ENEMY) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }

        g.fillOval((int)x, (int)y, size, size);
    }
}