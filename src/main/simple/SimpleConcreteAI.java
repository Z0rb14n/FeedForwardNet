package simple;

import ai.AI;
import ai.GraphicalAI;
import processing.core.PVector;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static simple.SimplePanel.GOAL;

// Represents the SimpleAI
public class SimpleConcreteAI extends GraphicalAI {
    private final static int INPUT_NUMBER = 4;
    private static final int GLOBAL_OUTPUT_NUM = 4;
    private PVector pos;
    private PVector vel;
    private PVector acc;

    // EFFECTS: initializes the SimpleConcreteAi with given number of layers
    SimpleConcreteAI(int hiddenLayerNum) {
        super(INPUT_NUMBER, GLOBAL_OUTPUT_NUM, hiddenLayerNum);
        pos = new PVector(200, 200);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
    }

    @Override
    // MODIFIES: g
    // EFFECTS: draws this AI onto the given graphics object
    public void draw(Graphics2D g) {
        Shape shape = new Rectangle2D.Float(pos.x, pos.y, 20, 40);
        g.setColor(Color.WHITE);
        g.fill(shape);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(shape);
    }

    @Override
    // MODIFIES: g
    // EFFECTS: draws this AI (as the best AI) onto the given graphics object
    public void drawBest(Graphics2D g) {
        Shape shape = new Rectangle2D.Float(pos.x, pos.y, 20, 40);
        g.setColor(Color.GREEN);
        g.fill(shape);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(shape);
    }

    @Override
    // EFFECTS: returns the fitness of this AI
    public double calculateFitness() {
        return -1 * PVector.dist(pos, GOAL);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: performs the given output given the index (of the output)
    protected void doOutput(int index) {
        switch (index) {
            case 0: {
                pos.add(0, 4);
                break;
            }
            case 1: {
                pos.add(4, 0);
                break;
            }
            case 2: {
                pos.sub(0, 4);
                break;
            }
            case 3: {
                pos.sub(4, 0);
                break;
            }
            case 4: {
                break;
            }
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: resets the AI to it's initial position
    public void reset() {
        pos.set(200, 200);
        vel.set(0, 0);
        acc.set(0, 0);
    }

    @Override
    // EFFECTS: gets the various inputs of this AI
    protected double[] getInputs() {
        double[] inputs = new double[INPUT_NUMBER];
        inputs[0] = (GOAL.x - pos.x) / 400;
        inputs[1] = (GOAL.y - pos.y) / 400;
        inputs[2] = vel.x;
        inputs[3] = vel.y;
        return inputs;
    }

    @Override
    // EFFECTS: returns a deep copy of this AI
    public AI copy() {
        SimpleConcreteAI simpleConcreteAI = new SimpleConcreteAI(layers.length - 1);
        simpleConcreteAI.copyNodes(layers);
        return simpleConcreteAI;
    }
}
