package ai;

import java.awt.*;

// Represents a group of graphical AIs
public class GraphicalAIs<T extends GraphicalAI> extends AIs<T> {
    // MODIFIES: g
    // EFFECTS: draws all the AIs onto graphics object
    public void drawAIs(Graphics2D g) {
        for (int i = 0; i < listOfAIs.size(); i++) {
            if (i != previousBest) {
                listOfAIs.get(i).draw(g);
            }
        }
        if (previousBest != -1) {
            listOfAIs.get(previousBest).drawBest(g);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates all the AIs
    public void update() {
        for (GraphicalAI g : listOfAIs) {
            g.update();
        }
    }
}
