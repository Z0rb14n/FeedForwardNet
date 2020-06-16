package ai;

import java.awt.*;

// Represents a graphical AI that can be drawn
public abstract class GraphicalAI extends AI {
    // EFFECTS: initializes the graphical AI with given input number, output number and hidden layer number
    protected GraphicalAI(int inputNum, int outputNum, int hiddenLayerNum) {
        super(inputNum, outputNum, hiddenLayerNum);
    }

    // MODIFIES: g
    // EFFECTS: draws this AI onto the drawing surface
    public abstract void draw(Graphics2D g);

    // MODIFIES: g
    // EFFECTS: draws this AI onto the drawing surface
    public abstract void drawBest(Graphics2D g);
}
