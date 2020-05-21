package platformer;

import ai.GraphicalAIs;
import processing.core.PApplet;

class PlatformerAIs extends GraphicalAIs<PlatformerAI> {
    private final static int GLOBAL_HIDDEN_LAYER_NUM = 1;

    PlatformerAIs() {
        super();
    }

    @Override
    public void drawAIs(PApplet drawingSurface) {
        for (int i = 0; i < listOfAIs.size(); i++) {
            if (i != previousBest && listOfAIs.get(i).isAlive()) {
                listOfAIs.get(i).draw(drawingSurface);
            }
        }
        if (previousBest != -1 && listOfAIs.get(previousBest).isAlive()) {
            listOfAIs.get(previousBest).drawBest(drawingSurface);
        }
    }

    void generate(int number) {
        if (number < 0) throw new IllegalArgumentException();
        for (int i = 0; i < number; i++) {
            PlatformerAI pai = new PlatformerAI(GLOBAL_HIDDEN_LAYER_NUM);
            pai.randomize();
            listOfAIs.add(pai);
        }
    }

    boolean isAllDead() {
        for (PlatformerAI pai : listOfAIs) {
            if (pai.isAlive()) return false;
        }
        return true;
    }

    double farthestDistance() {
        double max = -Double.MAX_VALUE;
        for (PlatformerAI pai : listOfAIs) {
            if (pai.getPosition().x > max) {
                max = pai.getPosition().x;
            }
        }
        return max;
    }

    float verticalHeightOfFarthestAI() {
        return listOfAIs.get(farthestDistanceIndex()).getPosition().y;
    }

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
