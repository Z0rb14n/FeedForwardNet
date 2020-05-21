package simple;

import processing.core.PApplet;
import processing.core.PVector;

public class SimpleUI extends PApplet {
    static final int WHITE = 0xFFFFFFFF;
    static final int GREEN = 0xFF00FF00;
    private static final int HUD_TEXT_SIZE = 30;
    private static final int GLOBAL_HIDDEN_LAYER_NUM = 1;
    private static final int GLOBAL_INPUT_NUM = 4;
    private static final int HUD_WIDTH = 80 * (GLOBAL_HIDDEN_LAYER_NUM + 1) + 60;
    private static final int HUD_HEIGHT = GLOBAL_INPUT_NUM * 80 + 60;
    static PVector GOAL = new PVector(800, 500);

    private SimpleConcreteAIs ais = new SimpleConcreteAIs();
    private int gen = 0;

    @Override
    public void settings() {
        size(1366, 750);
    }

    @Override
    public void setup() {
        frameRate(60);
        surface.setTitle("HI");
        ais.generate(500);
    }

    @Override
    public void draw() {
        background(WHITE);
        if (frameCount % 220 == 0) {
            gen++;
            ais.evolve();
            System.gc();
        } else {
            ais.update();
            drawHUD();
            drawGoal();
            ais.drawAIs(this);
        }
    }

    private void drawGoal() {
        strokeWeight(2);
        stroke(0);
        fill(0, 255, 0);
        ellipse(GOAL.x, GOAL.y, 4, 4);
    }

    private void drawHUD() {
        noStroke();
        fill(100, 100, 100, 100);
        rect(0, height - 100, width, 100);
        rect(width - HUD_WIDTH, 0, HUD_WIDTH, HUD_HEIGHT);
        textAlign(RIGHT, CENTER);
        textSize(HUD_TEXT_SIZE);
        fill(0);
        text("Generation: " + gen, width - HUD_TEXT_SIZE, height - 50);
    }
}
