package simple;

import processing.core.PVector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

// Represents the MainPanel for the SimpleConcreteAI Application (drawing surface and updating)
public class SimplePanel extends JPanel {
    private static final int WIDTH = (int) Math.round(SimpleFrame.SIZE.getWidth());
    private static final int HEIGHT = (int) Math.round(SimpleFrame.SIZE.getHeight());
    private static final int HUD_TEXT_SIZE = 30;
    private static final int GLOBAL_HIDDEN_LAYER_NUM = 1;
    private static final int GLOBAL_INPUT_NUM = 4;
    private static final int HUD_WIDTH = 80 * (GLOBAL_HIDDEN_LAYER_NUM + 1) + 60;
    private static final int HUD_HEIGHT = GLOBAL_INPUT_NUM * 80 + 60;
    private static final Color HUD_BACKGROUND_COLOR = new Color(100, 100, 100, 100);
    private static final int AI_NUMBER = 500;
    private static final Font HUD_FONT = new Font("Arial", Font.PLAIN, HUD_TEXT_SIZE);
    static PVector GOAL = new PVector(800, 500);

    private SimpleConcreteAIs ais = new SimpleConcreteAIs(AI_NUMBER);
    private int gen = 0;
    private long frameCount = 1;

    // EFFECTS: initializes simple panel with a given background color
    SimplePanel() {
        super();
        setBackground(Color.WHITE);
    }

    @Override
    // MODIFIES: g
    // EFFECTS: paints this simple panel onto the given graphics object
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        update();
        drawHud(g2d);
        drawGoal(g2d);
        ais.drawAIs(g2d);
        frameCount++;
    }

    // MODIFIES: g
    // EFFECTS: draws the end goal for the AIs onto the given graphics object
    private void drawGoal(Graphics2D g) {
        Shape shape = new Ellipse2D.Float(GOAL.x - 2, GOAL.y - 2, 4, 4);
        g.setColor(Color.GREEN);
        g.fill(shape);
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.draw(shape);
    }

    // MODIFIES: g
    // EFFECTS: draws the HUD onto the given graphics object
    private void drawHud(Graphics2D g) {
        g.setColor(HUD_BACKGROUND_COLOR);
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);
        g.fillRect(WIDTH - HUD_WIDTH, 0, HUD_WIDTH, HUD_HEIGHT);
        g.setFont(HUD_FONT);
        FontMetrics fm = g.getFontMetrics();
        String str = "Generation: " + gen;
        int textWidth = fm.stringWidth(str);
        g.setColor(Color.BLACK);
        g.drawString(str, WIDTH - HUD_TEXT_SIZE - textWidth, HEIGHT - 50 - Math.floorDiv(HUD_TEXT_SIZE, 2));
    }

    // MODIFIES: this
    // EFFECTS: updates all the AIs and/or evolves them
    private void update() {
        if (frameCount % 220 == 0) {
            gen++;
            ais.evolve();
            System.gc();
        } else {
            ais.update();
        }
    }
}
