package platformer;

import javax.swing.*;
import java.awt.*;

// Represents the main frame for the PlatformerAI application
class PlatformerFrame extends JFrame {
    static final Dimension SIZE = new Dimension(1366, 750);

    // EFFECTS: initializes the PlatformerFrame with given title and update timer
    private PlatformerFrame() {
        super("Platformer-AI Attempt");
        setPreferredSize(SIZE);
        setMinimumSize(SIZE);
        setSize(SIZE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PlatformerPanel pp = new PlatformerPanel();
        add(pp);
        addKeyListener(pp);
        Timer timer = new Timer(20, e -> repaint());
        setVisible(true);
        timer.start();
    }

    // Main method to run PlatformerFrame
    public static void main(String[] args) {
        new PlatformerFrame();
    }
}
