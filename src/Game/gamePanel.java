package Game;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Graphics;

public class gamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

    private int playerX = 50;
    private int playerY = 50;

    public gamePanel() {
        setFocusable(true);

        int move = 10;

        // RIGHT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        getActionMap().put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                playerX += move;
                repaint();
            }
        });

        // LEFT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        getActionMap().put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                playerX -= move;
                repaint();
            }
        });

        // UP
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        getActionMap().put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                playerY -= move;
                repaint();
            }
        });

        // DOWN
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        getActionMap().put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                playerY += move;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // keep square inside window
        playerX = Math.max(0, Math.min(playerX, getWidth() - 50));
        playerY = Math.max(0, Math.min(playerY, getHeight() - 50));

        g.fillRect(playerX, playerY, 50, 50);
    }
}