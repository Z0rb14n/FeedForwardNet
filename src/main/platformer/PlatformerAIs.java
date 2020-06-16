package platformer;

import ai.GraphicalAIs;

import java.awt.*;

// Represents a group of PlatformerAIs
class PlatformerAIs extends GraphicalAIs<PlatformerAI> {
    private final static int GLOBAL_HIDDEN_LAYER_NUM = 1;

    // EFFECTS: initializes the platformerAIs and creates number amount of platformer AIs
    PlatformerAIs(int number) {
        super();
        if (number < 0) throw new IllegalArgumentException();
        for (int i = 0; i < number; i++) {
            PlatformerAI pai = new PlatformerAI(GLOBAL_HIDDEN_LAYER_NUM);
            pai.randomize();
            listOfAIs.add(pai);
        }
    }

    @Override
    // MODIFIES: g
    // EFFECTS: draws all the AIs onto graphics object
    public void drawAIs(Graphics2D g) {
        for (int i = 0; i < listOfAIs.size(); i++) {
            if (i != previousBest && listOfAIs.get(i).isAlive()) {
                listOfAIs.get(i).draw(g);
            }
        }
        if (previousBest != -1 && listOfAIs.get(previousBest).isAlive()) {
            listOfAIs.get(previousBest).drawBest(g);
        }
    }

    // EFFECTS: determines if all the AIs are dead
    boolean isAllDead() {
        for (PlatformerAI pai : listOfAIs) {
            if (pai.isAlive()) return false;
        }
        return true;
    }

    // EFFECTS: returns the AI that has travelled the farthest distance
    double farthestDistance() {
        double max = -Double.MAX_VALUE;
        for (PlatformerAI pai : listOfAIs) {
            if (pai.getPosition().x > max) {
                max = pai.getPosition().x;
            }
        }
        return max;
    }

    // EFFECTS: returns the vertical height of the AI that has travelled the most distance
    float verticalHeightOfFarthestAI() {
        return listOfAIs.get(farthestDistanceIndex()).getPosition().y;
    }

    // EFFECTS: returns the index of the AI that has travelled the farthest distance
    private int farthestDistanceIndex() {
        double max = -Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < listOfAIs.size(); i++) {
            if (listOfAIs.get(i).getPosition().x > max) {
                max = listOfAIs.get(i).getPosition().x;
                index = i;
            }
        }
        return index;
    }
}
