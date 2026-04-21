package Game;

import javax.swing.JFrame;

public class gameGUI extends JFrame {
	private static final long serialVersionUID = 1L;

    public gameGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setContentPane(new gamePanel()); // use your panel

        setVisible(true);
    }

    public static void main(String[] args) {
        new gameGUI();
    }
}