package simple;

import ai.AI;
import ai.GraphicalAI;
import processing.core.PApplet;
import processing.core.PVector;

import static simple.SimpleUI.GOAL;

public class SimpleConcreteAI extends GraphicalAI {
    private final static int INPUT_NUMBER = 4;
    private static final int GLOBAL_OUTPUT_NUM = 4;
    private PVector pos;
    private PVector vel;
    private PVector acc;

    SimpleConcreteAI(int hiddenLayerNum) {
        super(INPUT_NUMBER, GLOBAL_OUTPUT_NUM, hiddenLayerNum);
        pos = new PVector(200, 200);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
    }

    @Override
    public void draw(PApplet drawingSurface) {
        drawingSurface.pushStyle();
        drawingSurface.stroke(0);
        drawingSurface.strokeWeight(2);
        drawingSurface.fill(SimpleUI.WHITE);
        drawingSurface.rect(pos.x, pos.y, 20, 40);
        drawingSurface.popStyle();
    }

    @Override
    public void drawBest(PApplet drawingSurface) {
        drawingSurface.pushStyle();
        drawingSurface.stroke(0);
        drawingSurface.strokeWeight(2);
        drawingSurface.fill(SimpleUI.GREEN);
        drawingSurface.rect(pos.x, pos.y, 20, 40);
        drawingSurface.popStyle();
    }

    @Override
    public double calculateFitness() {
        return -1 * PVector.dist(pos, GOAL);
    }

    @Override
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
    public void reset() {
        pos.set(200, 200);
        vel.set(0, 0);
        acc.set(0, 0);
    }

    @Override
    protected double[] getInputs() {
        double[] inputs = new double[INPUT_NUMBER];
        inputs[0] = (GOAL.x - pos.x) / 400;
        inputs[1] = (GOAL.y - pos.y) / 400;
        inputs[2] = vel.x;
        inputs[3] = vel.y;
        return inputs;
    }

    @Override
    public AI copy() {
        SimpleConcreteAI simpleConcreteAI = new SimpleConcreteAI(layers.length - 1);
        simpleConcreteAI.copyNodes(layers);
        return simpleConcreteAI;
    }
}
