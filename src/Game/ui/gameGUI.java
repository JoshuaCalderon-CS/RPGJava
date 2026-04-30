/***************************************************
Carlos Lopez
Michael Coker
Joshua Calderon
4/20/26
Final Project RPGJava
 ***************************************************/
package Game.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Game.core.Room;
import Game.entities.PlayerState;
import Game.GameLauncher;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

public class gameGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private Room currentRoom;
    private PlayerState player;
    private Image swordIcon;
    private Image bowIcon;
    private Image axeIcon;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    

    public gameGUI(Room room, PlayerState player, int x, int y, String direction) {
        this.currentRoom = room;
        this.player = player;
        
        swordIcon = new ImageIcon(getClass().getResource("/assets/tool_sword_b.png")).getImage();
        bowIcon = new ImageIcon(getClass().getResource("/assets/tool_bow.png")).getImage();
        axeIcon = new ImageIcon(getClass().getResource("/assets/tool_axe.png")).getImage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        setTitle("Room " + room.id);

        gamePanel panel = new gamePanel(this, room, player,
        	    swordIcon, bowIcon, axeIcon);

        setContentPane(panel);

        // SCREEN CENTERING LOGIC
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        int gridWidth = 3 * WIDTH;
        int gridHeight = 3 * HEIGHT;

        int offsetX = (screen.width - gridWidth) / 2;
        int offsetY = (screen.height - gridHeight) / 2;

        // GRID POSITIONING (centered world)
        setLocation(
            offsetX + player.gridX * WIDTH,
            offsetY + player.gridY * HEIGHT
        );

        setVisible(true);

        panel.centerPlayer();
    }
    public void goToRoom(String direction, int x, int y) {

        // 1. update grid position
        if (direction.equals("right")) player.gridX++;
        if (direction.equals("left")) player.gridX--;
        if (direction.equals("up")) player.gridY--;
        if (direction.equals("down")) player.gridY++;

        // 2. wrap around (if using looping world)
        player.gridX = (player.gridX + 3) % 3;
        player.gridY = (player.gridY + 3) % 3;

        // 3. GET NEXT ROOM (THIS IS WHERE YOUR LINE GOES)
        Room next = GameLauncher.map[player.gridY][player.gridX];

        // 4. create new state for spawn position
        PlayerState state = player;
        state.gridX = player.gridX;
        state.gridY = player.gridY;

        // 5. spawn position inside new room
        if (direction.equals("right")) {
            state.x = 10;
            state.y = y;
        }

        if (direction.equals("left")) {
            state.x = 340;
            state.y = y;
        }

        if (direction.equals("up")) {
            state.x = x;
            state.y = 240;
        }

        if (direction.equals("down")) {
            state.x = x;
            state.y = 10;
        }

        // 6. open new room
        new gameGUI(next, state, this.getX(), this.getY(), direction);
        this.dispose();
    }
    
}