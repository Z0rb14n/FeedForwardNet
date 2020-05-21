package ai;

import processing.core.PApplet;

public abstract class GraphicalAI extends AI {
    protected GraphicalAI(int inputNum, int outputNum, int hiddenLayerNum) {
        super(inputNum, outputNum, hiddenLayerNum);
    }

    // MODIFIES: drawingSurface
    // EFFECTS: draws this AI onto the drawing surface
    public abstract void draw(PApplet drawingSurface);

    // MODIFIES: drawingSurface
    // EFFECTS: draws this AI onto the drawing surface
    public abstract void drawBest(PApplet drawingSurface);
}
