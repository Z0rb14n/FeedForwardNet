package platformer;

import processing.core.PApplet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

class Platforms {
    private static final int STARTING_X_POS = 0;
    private static final int STARTING_Y_POS = 200;
    private static final int STARTING_WIDTH = 200;
    private final Random RANDOM = new Random();
    private static final long RANDOM_SEED = 69420666;

    private static final int MIN_GAP_SIZE = 10;
    private static final int MAX_GAP_SIZE = 60;
    private static final int MAX_HEIGHT_DIFF = 60;

    private static final Platform STARTING = new Platform(STARTING_WIDTH, STARTING_X_POS, STARTING_Y_POS);
    private int[] xPositions;
    private Platform[] platforms;
    private int[] heightDifferences;
    private int[] gaps;

    Platforms(int number) {
        RANDOM.setSeed(RANDOM_SEED);
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
            int nextGap = RANDOM.nextInt(MAX_GAP_SIZE - MIN_GAP_SIZE) + MIN_GAP_SIZE;
            int width = (int) Math.round(RANDOM.nextGaussian() * 50) + (Platform.MIN_WIDTH) + 50;
            int nextHeightDifference = RANDOM.nextInt(MAX_HEIGHT_DIFF);
            if (RANDOM.nextBoolean()) nextHeightDifference *= -1;
            if (width < Platform.MIN_WIDTH) width = Platform.MIN_WIDTH;
            gaps[i - 1] = nextGap;
            xPositions[i] = previousWidth + xPositions[i - 1] + nextGap;
            heightDifferences[i - 1] = nextHeightDifference;
            platforms[i] = new Platform(width, xPositions[i], previousYPos + nextHeightDifference);
        }
    }

    void adjustPlayerLocation(PlatformerAI pai) {
        for (Platform p : platforms) {
            if (p.adjustPlayerPosition(pai)) {
                return;
            }
        }
    }

    boolean playerCanJump(PlatformerAI pai) {
        for (Platform p : platforms) {
            if (p.playerOnPlatform(pai) || p.isPlayerInside(pai)) return true;
        }
        return false;
    }

    private HashMap<PlatformerAI, Integer> recordedAnswers = new HashMap<>();
    private HashMap<PlatformerAI, Float> recordedPositions = new HashMap<>();

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

    double distanceToEdgeOfPlatform(PlatformerAI pai) {
        int indexAbove = platformAbove(pai);
        if (indexAbove == -1) return -1;
        Platform p = platforms[indexAbove];
        return (pai.getXPosition() - p.getXPosition()) / p.getWidth();
    }

    double gapToNextPlatform(PlatformerAI pai) {
        int index = Arrays.binarySearch(xPositions, Math.round(pai.getXPosition()));
        if (index >= 0) return (double) gaps[index] / MAX_GAP_SIZE;
        return (double) gaps[-(index + 1) - 1] / MAX_GAP_SIZE;
    }

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

    void showPlatforms(PApplet surface) {
        for (Platform p : platforms) {
            p.draw(surface);
        }
    }
}
