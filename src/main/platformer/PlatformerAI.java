package platformer;

import ai.AI;
import ai.GraphicalAI;
import processing.core.PVector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

// Represents the PlatformerAI
public class PlatformerAI extends GraphicalAI {
    private static final int GLOBAL_INPUT_NUM = 3;
    private static final int GLOBAL_OUTPUT_NUM = 3;

    private final static float HORIZONTAL_VELOCITY = 5.0f;
    private final static float GRAVITY = 2.5f;
    private final static float STARTING_VERT_POS = 200;
    private final static PVector STARTING_POS = new PVector(20, STARTING_VERT_POS);
    private final static PVector STARTING_VEL = new PVector(HORIZONTAL_VELOCITY, 0);
    private final static PVector STARTING_ACC = new PVector(0, GRAVITY);

    final static int PLAYER_WIDTH = 20;
    private final static int PLAYER_HEIGHT = 50;
    private PVector pos = STARTING_POS.copy();
    private PVector vel = STARTING_VEL.copy();
    private PVector acc = STARTING_ACC.copy();
    private boolean isDead = false;
    private boolean canJump = true;

    // EFFECTS: initializes the PlatformerAI with inputs, outputs and hidden layers
    PlatformerAI(int hiddenLayerNum) {
        super(GLOBAL_INPUT_NUM, GLOBAL_OUTPUT_NUM, hiddenLayerNum);
    }

    // MODIFIES: this
    // EFFECTS: jumps very high
    private void bigJump() {
        vel.set(HORIZONTAL_VELOCITY, -30);
        canJump = false;
    }

    // MODIFIES: this
    // EFFECTS: jumps moderately high
    private void smallJump() {
        vel.set(HORIZONTAL_VELOCITY, -15);
        canJump = false;
    }

    // EFFECTS: returns the x position of the platformerAI
    float getXPosition() {
        return pos.x;
    }

    // EFFECTS: returns the position of this platformerAI
    PVector getPosition() {
        return pos;
    }

    // EFFECTS: gets the lowest vertical position of this platformerAI
    float getPlayerBottomVerticalPos() {
        return pos.y + PLAYER_HEIGHT;
    }

    // EFFECTS: determines if this AI is alive
    boolean isAlive() {
        return !isDead;
    }

    // EFFECTS: gets the bounding box of the player
    Rectangle getBoundingBox() {
        return new Rectangle(Math.round(pos.x), Math.round(pos.y), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    @Override
    // MODIFIES: g
    // EFFECTS: draws the ai onto given graphics object
    public void draw(Graphics2D g) {
        Shape shape = new Rectangle2D.Float(pos.x, pos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        Shape ellipse = new Ellipse2D.Float(pos.x - 2.5f, pos.y - 2.5f, 5, 5);
        g.setColor(Color.GREEN);
        g.fill(shape);
        g.setColor(Color.WHITE);
        g.fill(ellipse);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.BLACK);
        g.draw(shape);
        g.draw(ellipse);
    }

    @Override
    // MODIFIES: g
    // EFFECTS: draws the ai (as the best AI) onto given graphics object
    public void drawBest(Graphics2D g) {
        Shape shape = new Rectangle2D.Float(pos.x, pos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        Shape ellipse = new Ellipse2D.Float(pos.x - 2.5f, pos.y - 2.5f, 5, 5);
        g.setColor(Color.GREEN);
        g.fill(shape);
        g.setColor(Color.BLUE);
        g.fill(ellipse);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.BLACK);
        g.draw(shape);
        g.draw(ellipse);
    }

    @Override
    // EFFECTS: returns the fitness of the AI
    public double calculateFitness() {
        return pos.x;
    }

    @Override
    // MODIFIES: this
    // EFFECTS: updates the AI and its position
    public void update() {
        Platforms p = PlatformerPanel.getPlatforms();
        testIfDead();
        if (isDead) return;
        p.adjustPlayerLocation(this);
        adjustVelocityIfOnPlatform();
        if (canJump) {
            super.update();
        }
        canJump = p.playerCanJump(this);
        if (!isDead) {
            if (!canJump) {
                acc.set(0, GRAVITY);
                pos.add(vel);
                vel.add(acc);
            } else {
                pos.add(vel);
                vel.add(acc.x, 0);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the ai to specific position and velocity and marks that it can jump
    void forceCanJump(float groundPos) {
        pos.y = groundPos - PLAYER_HEIGHT;
        vel.y = 0;
        canJump = true;
    }

    // MODIFIES: this
    // EFFECTS: sets its vertical velocity accordingly if it is on a platform
    private void adjustVelocityIfOnPlatform() {
        if (canJump && vel.y != 0) vel.y = 0;
    }

    // MODIFIES: this
    // EFFECTS: modifies the isDead variable if it is below the death cutoff height
    private void testIfDead() {
        isDead = pos.y > PlatformerPanel.DEATH_CUTOFF_HEIGHT;
    }

    @Override
    // MODIFIES: this
    // EFFECTS: performs the given output of the given index
    protected void doOutput(int index) {
        if (index == 0) {
            bigJump();
        } else if (index == 1) {
            smallJump();
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: resets the AI
    public void reset() {
        pos = STARTING_POS.copy();
        vel = STARTING_VEL.copy();
        acc = STARTING_ACC.copy();
        isDead = false;
        canJump = true;
    }

    @Override
    // EFFECTS: gets the inputs for the AI
    protected double[] getInputs() {
        double[] inputs = new double[GLOBAL_INPUT_NUM];
        inputs[0] = PlatformerPanel.getPlatforms().distanceToEdgeOfPlatform(this);
        inputs[1] = PlatformerPanel.getPlatforms().gapToNextPlatform(this);
        inputs[2] = PlatformerPanel.getPlatforms().nextHeightDiff(this);
        return inputs;
    }

    @Override
    // EFFECTS: returns a deep copy of this AI
    public AI copy() {
        PlatformerAI pai = new PlatformerAI(hiddenLayerNumber);
        pai.copyNodes(layers);
        return pai;
    }
}
