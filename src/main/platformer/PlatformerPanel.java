package platformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.HashSet;

// Represents the Main Panel for PlatformerAI (i.e. handles updating and drawing)
class PlatformerPanel extends JPanel implements KeyListener {
    static final Color PLATFORM_COLOR = new Color(0, 100, 0);
    static final int DEATH_CUTOFF_HEIGHT = 800;

    private static final int PLATFORM_NUM = 200;
    private static final int AI_NUM = 500;
    private static final int HUD_TEXT_SIZE = 24;
    private static final int WIDTH = (int) Math.round(PlatformerFrame.SIZE.getWidth());
    private static final int HEIGHT = (int) Math.round(PlatformerFrame.SIZE.getHeight());
    private static final Color HUD_BACKGROUND = new Color(100, 100, 100, 100);
    private static final Font HUD_TEXT = new Font("Arial", Font.PLAIN, HUD_TEXT_SIZE);

    private PlatformerAIs ais = new PlatformerAIs(AI_NUM);

    private boolean autoPanning = false;
    private static Platforms platforms = new Platforms(PLATFORM_NUM);

    private int vertAmountPanned = 0;
    private int horizontalAmountPanned = 0;
    private int gen = 1;

    // EFFECTS: initializes PlatformerPanel with given size and background color
    PlatformerPanel() {
        super();
        setPreferredSize(PlatformerFrame.SIZE);
        setBackground(Color.WHITE);
    }

    // EFFECTS: returns the platforms instance
    static Platforms getPlatforms() {
        return platforms;
    }

    @Override
    // MODIFIES: this, g
    // EFFECTS; updates the AIs and renders the ais onto the graphics object
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        update();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform af = g2d.getTransform();
        g2d.translate(horizontalAmountPanned, vertAmountPanned);
        platforms.showPlatforms(g2d);
        ais.drawAIs(g2d);
        ais.update();
        g2d.setTransform(af);
        drawHud(g2d);
    }

    // MODIFIES: this
    // EFFECTS: updates all the AIs
    private void update() {
        if (ais.isAllDead()) {
            ais.evolve();
            horizontalAmountPanned = 0;
            vertAmountPanned = 0;
            System.out.println("gen " + gen + ", size: " + ais.numberOfAis());
            gen++;
            return;
        }
        if (autoPanning) {
            autoPan();
        } else {
            manualPan();
        }
    }

    // MODIFIES: g
    // EFFECTS: draws the hud onto the graphics object
    private void drawHud(Graphics2D g) {
        g.setColor(HUD_BACKGROUND);
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);
        g.setFont(HUD_TEXT);
        FontMetrics fm = g.getFontMetrics();
        String str = "Generation: " + gen;
        int textWidth = fm.stringWidth(str);
        g.setColor(Color.BLACK);
        g.drawString(str, WIDTH - HUD_TEXT_SIZE - textWidth, HEIGHT - 50 - Math.floorDiv(HUD_TEXT_SIZE, 2));
    }

    // MODIFIES: this
    // EFFECTS: sets the pan amount by using the distance the AIs travelled
    private void autoPan() {
        boolean isAllDead = ais.isAllDead();
        if (!isAllDead) {
            double maxDistance = ais.farthestDistance();
            float farthestHeight = ais.verticalHeightOfFarthestAI();
            horizontalAmountPanned = (int) Math.round(PlatformerFrame.SIZE.getWidth() / 2.0 - maxDistance);
            vertAmountPanned = Math.round(-farthestHeight + 300);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the pan amount given the keys pressed
    private void manualPan() {
        if (!autoPanning) {
            if (keysPressed.contains('d')) {
                horizontalAmountPanned -= 20;
            }
            if (keysPressed.contains('a')) {
                horizontalAmountPanned += 20;
            }
            if (keysPressed.contains('w')) {
                vertAmountPanned += 20;
            }
            if (keysPressed.contains('s')) {
                vertAmountPanned -= 20;
            }
        }
    }

    private HashSet<Character> keysPressed = new HashSet<>(26);
    private HashMap<Character, Boolean> firstPressed = new HashMap<>(26);

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    // MODIFIES: this
    // EFFECTS: signals that specific keys are pressed and/or toggles autopanning
    public void keyPressed(KeyEvent e) {
        char result = e.getKeyChar();
        keysPressed.add(result);
        if (firstPressed.get(result) == null || !firstPressed.get(result)) {
            firstPressed.put(result, true);
            if (result == 'p' || result == 'P') autoPanning = !autoPanning;
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: signals that specific keys are released
    public void keyReleased(KeyEvent e) {
        char result = e.getKeyChar();
        firstPressed.put(result, false);
        keysPressed.remove(result);
    }
}
