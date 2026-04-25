package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
	private Rectangle startButton;
    
    public MenuPanel(JFrame frame) {

        // Button position and size
        startButton = new Rectangle(300, 250, 200, 50);
        PlayerState player = new PlayerState();
        Room startRoom = new Room(0);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (startButton.contains(e.getPoint())) {

            	    GameLauncher.start(); // start the actual game

            	    frame.dispose(); // close menu window
            	}
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, w, h);

        // title (centered)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        String title = "My RPG";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (w - titleWidth) / 2, h / 3);

        // button size based on window
        int btnW = 140;
        int btnH = 40;
        int btnX = (w - btnW) / 2;
        int btnY = h / 2;

        startButton = new Rectangle(btnX, btnY, btnW, btnH);

        g.setColor(Color.GRAY);
        g.fillRect(btnX, btnY, btnW, btnH);

        g.setColor(Color.WHITE);
        g.drawRect(btnX, btnY, btnW, btnH);

        String text = "Start";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, btnX + (btnW - textWidth) / 2, btnY + 25);
    }
}