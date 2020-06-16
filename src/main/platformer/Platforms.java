package platformer;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

// Represents the collection of platforms
class Platforms {
    private static final int STARTING_X_POS = 0;
    private static final int STARTING_Y_POS = 200;
    private static final int STARTING_WIDTH = 200;
    private static final long RANDOM_SEED = 69420666;

    private static final int MIN_GAP_SIZE = 10;
    private static final int MAX_GAP_SIZE = 60;
    private static final int MAX_HEIGHT_DIFF = 60;

    private static final Platform STARTING = new Platform(STARTING_WIDTH, STARTING_X_POS, STARTING_Y_POS);
    private int[] xPositions;
    private Platform[] platforms;
    private int[] heightDifferences;
    private int[] gaps;

    // EFFECTS: initializes the platforms
    Platforms(int number) {
        Random rand = new Random(RANDOM_SEED);
        if (number < 1) throw new IllegalArgumentException();
        platforms = new Platform[number];
        heightDifferences = new int[number - 1];
        gaps = new int[number - 1];
        xPositions = new int[number];
        xPositions[0] = STARTING_X_POS;
        platforms[0] = STARTING.copy();
        int previousWidth = platforms[0].getWidth();
        int previousYPos = platforms[0].getYPosition();
        for (int i = 1; i < number; i++) {
            int nextGap = rand.nextInt(MAX_GAP_SIZE - MIN_GAP_SIZE) + MIN_GAP_SIZE;
            int width = (int) Math.round(rand.nextGaussian() * 50) + (Platform.MIN_WIDTH) + 50;
            int nextHeightDifference = rand.nextInt(MAX_HEIGHT_DIFF);
            if (rand.nextBoolean()) nextHeightDifference *= -1;
            if (width < Platform.MIN_WIDTH) width = Platform.MIN_WIDTH;
            gaps[i - 1] = nextGap;
            xPositions[i] = previousWidth + xPositions[i - 1] + nextGap;
            heightDifferences[i - 1] = nextHeightDifference;
            platforms[i] = new Platform(width, xPositions[i], previousYPos + nextHeightDifference);
        }
    }

    // MODIFIES: pai
    // EFFECTS: adjusts platformerAI location if it is in a platform
    void adjustPlayerLocation(PlatformerAI pai) {
        for (Platform p : platforms) {
            if (p.adjustPlayerPosition(pai)) {
                return;
            }
        }
    }

    // EFFECTS: determines if the given platformerAI can jump
    boolean playerCanJump(PlatformerAI pai) {
        for (Platform p : platforms) {
            if (p.playerOnPlatform(pai) || p.isPlayerInside(pai)) return true;
        }
        return false;
    }

    private HashMap<PlatformerAI, Integer> recordedAnswers = new HashMap<>();
    private HashMap<PlatformerAI, Float> recordedPositions = new HashMap<>();

    // EFFECTS: returns the platform index which the AI is above
    private int platformAbove(PlatformerAI pai) {
        if (recordedPositions.containsKey(pai)) {
            if (recordedPositions.get(pai) == pai.getXPosition()) return recordedAnswers.get(pai);
        }
        int value = -1;
        for (int i = 0; i < platforms.length; i++) {
            if (platforms[i].isPlayerAbove(pai)) {
                value = i;
                break;
            }
        }
        recordedPositions.put(pai, pai.getXPosition());
        recordedAnswers.put(pai, value);
        return value;
    }

    // EFFECTS: returns the distance to the edge of the platform for this platformerAI (scaled to 1)
    double distanceToEdgeOfPlatform(PlatformerAI pai) {
        int indexAbove = platformAbove(pai);
        if (indexAbove == -1) return -1;
        Platform p = platforms[indexAbove];
        return (pai.getXPosition() - p.getXPosition()) / p.getWidth();
    }

    // EFFECTS: returns the gap to the next platform for this platformerAI (scaled to 1)
    double gapToNextPlatform(PlatformerAI pai) {
        int index = Arrays.binarySearch(xPositions, Math.round(pai.getXPosition()));
        if (index >= 0) return (double) gaps[index] / MAX_GAP_SIZE;
        return (double) gaps[-(index + 1) - 1] / MAX_GAP_SIZE;
    }

    // EFFECTS: returns the next height difference for this platformerAI (scaled to 1)
    double nextHeightDiff(PlatformerAI pai) {
        int indexAbove = platformAbove(pai);
        if (indexAbove == -1) {
            int index = Arrays.binarySearch(xPositions, Math.round(pai.getXPosition()));
            if (index >= 0) return (double) heightDifferences[index] / MAX_HEIGHT_DIFF;
            else return (double) heightDifferences[-(index + 1) - 1] / MAX_GAP_SIZE;
        } else {
            return (double) heightDifferences[indexAbove] / MAX_HEIGHT_DIFF;
        }
    }

    // MODIFIES: g
    // EFFECTS: draws all platforms onto given graphics object
    void showPlatforms(Graphics2D g) {
        for (Platform p : platforms) {
            p.draw(g);
        }
    }
}
