package platformer;

import ai.AI;
import ai.GraphicalAI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class PlatformerAI extends GraphicalAI {
    private static final int GLOBAL_INPUT_NUM = 3;
    private static final int GLOBAL_OUTPUT_NUM = 3;

    private final static float HORIZONTAL_VELOCITY = 5.0f;
    private final static float GRAVITY = 2.5f;
    public final static float STARTING_VERT_POS = 200;
    private final static PVector STARTING_POS = new PVector(20, STARTING_VERT_POS);
    private final static PVector STARTING_VEL = new PVector(HORIZONTAL_VELOCITY, 0);
    private final static PVector STARTING_ACC = new PVector(0, GRAVITY);
    private final static int BLACK = PlatformerUI.BLACK;
    private final static int GREEN = PlatformerUI.GREEN;
    private final static int WHITE = PlatformerUI.WHITE;
    private final static int BLUE = PlatformerUI.BLUE;

    final static int PLAYER_WIDTH = 20;
    private final static int PLAYER_HEIGHT = 50;
    private PVector pos = STARTING_POS.copy();
    private PVector vel = STARTING_VEL.copy();
    private PVector acc = STARTING_ACC.copy();
    private boolean isDead = false;
    private boolean canJump = true;

    PlatformerAI(int hiddenLayerNum) {
        super(GLOBAL_INPUT_NUM, GLOBAL_OUTPUT_NUM, hiddenLayerNum);
    }

    // MODIFIES: this
    // EFFECTS: jumps very high lmao
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

    float getXPosition() {
        return pos.x;
    }

    PVector getPosition() {
        return pos;
    }

    float getPlayerBottomVerticalPos() {
        return pos.y + PLAYER_HEIGHT;
    }

    boolean isAlive() {
        return !isDead;
    }

    Rectangle getBoundingBox() {
        return new Rectangle(Math.round(pos.x), Math.round(pos.y), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    @Override
    public void draw(PApplet surface) {
        surface.pushStyle();
        surface.strokeWeight(1);
        surface.stroke(BLACK);
        surface.fill(WHITE);
        surface.rect(pos.x, pos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        surface.fill(BLUE);
        surface.ellipse(pos.x, pos.y, 5, 5);
        surface.popStyle();
    }

    public void drawBest(PApplet surface) {
        surface.pushStyle();
        surface.strokeWeight(1);
        surface.stroke(BLACK);
        surface.fill(GREEN);
        surface.rect(pos.x, pos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        surface.fill(BLUE);
        surface.ellipse(pos.x, pos.y, 5, 5);
        surface.popStyle();
    }

    @Override
    public double calculateFitness() {
        return pos.x;
    }

    @Override
    public void update() {
        Platforms p = PlatformerUI.platforms;
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

    void forceCanJump(float groundPos) {
        pos.set(pos.x, groundPos - PLAYER_HEIGHT);
        canJump = true;
        vel.set(vel.x, 0);
    }

    private void adjustVelocityIfOnPlatform() {
        if (canJump && vel.y != 0) {
            vel.set(vel.x, 0);
        }
    }

    private void testIfDead() {
        isDead = pos.y > PlatformerUI.DEATH_CUTOFF_HEIGHT;
    }

    @Override
    protected void doOutput(int index) {
        if (index == 0) {
            bigJump();
        } else if (index == 1) {
            smallJump();
        }
    }

    @Override
    public void reset() {
        pos = STARTING_POS.copy();
        vel = STARTING_VEL.copy();
        acc = STARTING_ACC.copy();
        isDead = false;
        canJump = true;
    }

    @Override
    protected double[] getInputs() {
        double[] inputs = new double[GLOBAL_INPUT_NUM];
        inputs[0] = PlatformerUI.platforms.distanceToEdgeOfPlatform(this);
        inputs[1] = PlatformerUI.platforms.gapToNextPlatform(this);
        inputs[2] = PlatformerUI.platforms.nextHeightDiff(this);
        return inputs;
    }

    @Override
    public AI copy() {
        PlatformerAI pai = new PlatformerAI(hiddenLayerNumber);
        pai.copyNodes(layers);
        return pai;
    }
}
