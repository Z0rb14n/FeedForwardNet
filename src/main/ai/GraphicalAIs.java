package ai;

import processing.core.PApplet;

public class GraphicalAIs<T extends GraphicalAI> extends AIs<T> {
    public void drawAIs(PApplet drawingSurface) {
        for (int i = 0; i < listOfAIs.size(); i++) {
            if (i != previousBest) {
                listOfAIs.get(i).draw(drawingSurface);
            }
        }
        if (previousBest != -1) {
            listOfAIs.get(previousBest).drawBest(drawingSurface);
        }
    }

    public void update() {
        for (GraphicalAI g : listOfAIs) {
            g.update();
        }
    }
}
