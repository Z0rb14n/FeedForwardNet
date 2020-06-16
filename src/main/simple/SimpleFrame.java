package simple;

import javax.swing.*;
import java.awt.*;

// Represents the Frame for the SimpleConcreteAI application
public class SimpleFrame extends JFrame {
    static final Dimension SIZE = new Dimension(1366, 750);

    // EFFECTS: initializes SimpleFrame with given title, size, main panel and update timer
    private SimpleFrame() {
        super("Simple AI Attempt");
        setPreferredSize(SIZE);
        setMinimumSize(SIZE);
        setSize(SIZE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SimplePanel sp = new SimplePanel();
        add(sp);
        Timer timer = new Timer(20, e -> repaint());
        setVisible(true);
        timer.start();
    }

    // Main function to run the SimpleConcreteAI Application
    public static void main(String[] args) {
        new SimpleFrame();
    }
}
