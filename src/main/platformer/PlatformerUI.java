package platformer;

import processing.core.PApplet;

import java.util.HashSet;


public class PlatformerUI extends PApplet {
    static final int WHITE = 0xFFFFFFFF;
    static final int BLACK = 0xFF000000;
    static final int PLATFORM_COLOR = 0xFF006400;
    static final int GREEN = 0xFF00FF00;
    static final int BLUE = 0xFF0000FF;

    static final int DEATH_CUTOFF_HEIGHT = 800;

    private static final int PLATFORM_NUM = 200;
    private static final int HUD_TEXT_SIZE = 24;

    private PlatformerAIs ais = new PlatformerAIs();

    private boolean autoPanning = false;
    static Platforms platforms = new Platforms(PLATFORM_NUM);

    private int vertAmountPanned = 0;
    private int horizontalAmountPanned = 0;
    private int gen = 1;

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
        if (ais.isAllDead()) {
            ais.evolve();
            horizontalAmountPanned = 0;
            vertAmountPanned = 0;
            System.out.println("gen " + gen + ", size: " + ais.numberOfAis());
            gen++;
            return;
        }
        pushMatrix();
        translate(horizontalAmountPanned, vertAmountPanned);
        background(WHITE);
        platforms.showPlatforms(this);
        ais.drawAIs(this);
        ais.update();
        popMatrix();
        drawHUD();
        if (autoPanning) {
            autoPan();
        } else {
            manualPan();
        }
    }

    private void drawHUD() {
        noStroke();
        fill(100, 100, 100, 100);
        rect(0, height - 100, width, 100);
        textAlign(RIGHT, CENTER);
        textSize(HUD_TEXT_SIZE);
        fill(0);
        text("Generation: " + gen, width - HUD_TEXT_SIZE, height - 50);
    }

    private HashSet<Character> keysPressed = new HashSet<>();

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

    private void autoPan() {
        boolean isAllDead = ais.isAllDead();
        if (!isAllDead) {
            double maxDistance = ais.farthestDistance();
            float farthestHeight = ais.verticalHeightOfFarthestAI();
            horizontalAmountPanned = (int) Math.round(width / 2.0 - maxDistance);
            vertAmountPanned = Math.round(-farthestHeight + 300);
        }
    }

    private boolean firstPressed = false;

    @Override
    public void keyPressed() {
        keysPressed.add(key);
        if (!firstPressed) {
            firstPressed = true;
            if (key == 'p' || key == 'P') autoPanning = !autoPanning;
        }
    }

    @Override
    public void keyReleased() {
        firstPressed = false;
        keysPressed.remove(key);
    }
}
