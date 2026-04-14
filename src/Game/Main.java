package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JPanel implements ActionListener {

    // Constants for window settings
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int DELAY = 16; // ~60 Frames Per Second

    public Main() {
        // Set the background color to blue
        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Initialize the timer to fire every 16ms
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // This is where you would draw your objects
        // For now, it just clears the screen with the background color
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // This method is called every 16ms by the timer
        repaint(); 
    }

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("60Hz Blue Window");
            Main panel = new Main();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); // Adjusts window to the preferred size of the panel
            frame.setLocationRelativeTo(null); // Centers the window
            frame.setVisible(true);
        });
    }
}