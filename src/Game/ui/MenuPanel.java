/*
Joshua Calderon
Carlos Lopez 
Michael Coker
04/28/2026
Final Project
*/

package Game.ui;

import javax.swing.*;

import Game.GameLauncher;
import Game.PlayerClass;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Rectangle startButton;
    private Rectangle warriorButton;
    private Rectangle archerButton;
    private Rectangle berserkerButton;

    private PlayerClass selectedClass = PlayerClass.WARRIOR;

    public MenuPanel(JFrame frame) {

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                updateButtonBounds(); // ensure correct positions

                if (warriorButton.contains(e.getPoint())) {
                    selectedClass = PlayerClass.WARRIOR;
                    repaint();
                }

                if (archerButton.contains(e.getPoint())) {
                    selectedClass = PlayerClass.ARCHER;
                    repaint();
                }

                if (berserkerButton.contains(e.getPoint())) {
                    selectedClass = PlayerClass.BERSERKER;
                    repaint();
                }

                if (startButton.contains(e.getPoint())) {
                    GameLauncher.start(selectedClass);
                    frame.dispose();
                }
            }
        });
    }

    private void updateButtonBounds() {
        int w = getWidth();
        int h = getHeight();

        int btnW = 120;
        int btnH = 40;

        int btnX = (w - btnW) / 2;
        int btnY = h / 2 + 40;

        startButton = new Rectangle(btnX, btnY, btnW, btnH);

        int centerX = w / 2;
        int classY = btnY + 50;

        warriorButton = new Rectangle(centerX - 180, classY, btnW, btnH);
        archerButton = new Rectangle(centerX - 60, classY, btnW, btnH);
        berserkerButton = new Rectangle(centerX + 60, classY, btnW, btnH);
    }

    private void drawClassButton(Graphics g, Rectangle rect, String text, boolean selected) {
        g.setColor(selected ? Color.GREEN : Color.GRAY);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.setColor(Color.WHITE);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);

        g.setFont(new Font("Arial", Font.PLAIN, 12));

        FontMetrics fm = g.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(text, textX, textY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateButtonBounds(); // keep layout in sync

        int w = getWidth();
        int h = getHeight();

        // background
        g.setColor(Color.RED);
        g.fillRect(0, 0, w, h);

        // title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        String title = "My RPG";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (w - titleWidth) / 2, h / 3);

        // draw class buttons
        drawClassButton(g, warriorButton, "Warrior", selectedClass == PlayerClass.WARRIOR);
        drawClassButton(g, archerButton, "Archer", selectedClass == PlayerClass.ARCHER);
        drawClassButton(g, berserkerButton, "Berserker", selectedClass == PlayerClass.BERSERKER);

        // draw start button
        g.setColor(Color.GRAY);
        g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);

        g.setColor(Color.WHITE);
        g.drawRect(startButton.x, startButton.y, startButton.width, startButton.height);

        g.setFont(new Font("Arial", Font.PLAIN, 14));

        String text = "Start";
        FontMetrics fm = g.getFontMetrics();
        int textX = startButton.x + (startButton.width - fm.stringWidth(text)) / 2;
        int textY = startButton.y + ((startButton.height - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(text, textX, textY);
    }
}
