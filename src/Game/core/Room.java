/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/
package Game.core;

import java.awt.Color;
import java.util.ArrayList;

public class Room {

    public int id;
    public boolean visited;

    // visuals
    public Color backgroundColor = Color.WHITE;

    // gameplay elements (expand later)
    public ArrayList<String> enemies = new ArrayList<>();
    public ArrayList<String> items = new ArrayList<>();

    // optional flags
    public boolean hasWater = false;
    public boolean isSafeZone = false;

    // connections
    public Room left, right, up, down;

    public Room(int id) {
        this.id = id;
    }
    public void reset() {
        enemies.clear();   // if room owns enemies
        visited = false;   // example flag
    }
}