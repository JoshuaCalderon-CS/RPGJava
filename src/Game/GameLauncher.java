package Game;

import javax.swing.JFrame;

import Game.core.Room;
import Game.entities.PlayerState;
import Game.ui.MenuPanel;
import Game.ui.gameGUI;

public class GameLauncher {

    public static Room[][] map;

    public static void main(String[] args) {
    	showMenu();
    }

    	public static void showMenu() {

    	    JFrame frame = new JFrame("Menu");
    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	    int width = 400;
    	    int height = 300;

    	    frame.setSize(width, height);
    	    frame.setLocationRelativeTo(null);

    	    frame.setContentPane(new MenuPanel(frame));
    	    frame.setVisible(true);
    	}

    public static void start(PlayerClass chosenClass) {

    	PlayerState player = new PlayerState();

    	player.playerClass = chosenClass; 
    	player.applyClassStats();

        Room r0 = new Room(0);
        Room r1 = new Room(1);
        Room r2 = new Room(2);
        Room r3 = new Room(3);
        Room r4 = new Room(4);
        Room r5 = new Room(5);
        Room r6 = new Room(6);
        Room r7 = new Room(7);
        Room r8 = new Room(8);

        map = new Room[][] {
            {r0, r1, r2},
            {r3, r4, r5},
            {r6, r7, r8}
        };

        player.gridX = 1;
        player.gridY = 1;
        
        

        new gameGUI(r4, player, 0, 0, "start");
    }
}