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

    private PlayerState player;
    private gameGUI parent;
    private Room room;
    private static final int PLAYER_SIZE = 50;
    private static final int W = 400;
    private static final int H = 300;

    public gamePanel(gameGUI parent, Room room, PlayerState player) {
    	this.parent = parent;
    	this.player = player;
    	this.room = room;
        setFocusable(true);

        int move = 10;

        // RIGHT
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        getActionMap().put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                player.x += move;
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
                player.x -= move;
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
                player.y -= move;
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
                player.y += move;
                if (player.y > getHeight() - 50) {
                    parent.goToRoom("down", player.x, player.y);
                    return;
                }

                repaint();
            }
        });
    }
    
    public void centerPlayer() {
        player.x = (getWidth() - PLAYER_SIZE) / 2;
        player.y = (getHeight() - PLAYER_SIZE) / 2;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // light paper background
        g.setColor(new java.awt.Color(245, 245, 240));
        g.fillRect(0, 0, getWidth(), getHeight());

        // notebook grid lines
        g.setColor(new java.awt.Color(210, 210, 210));

        int spacing = 25; // grid size

        // vertical lines
        for (int x = 0; x < getWidth(); x += spacing) {
            g.drawLine(x, 0, x, getHeight());
        }

        // horizontal lines
        for (int y = 0; y < getHeight(); y += spacing) {
            g.drawLine(0, y, getWidth(), y);
        }

        // player
        g.setColor(java.awt.Color.RED);
        g.fillRect(player.x, player.y, 50, 50);
    }
    
}