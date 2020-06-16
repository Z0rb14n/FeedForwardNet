package simple;

import ai.GraphicalAIs;

// Represents a collection of SimpleConcreteAIs
class SimpleConcreteAIs extends GraphicalAIs<SimpleConcreteAI> {
    private static final int GLOBAL_HIDDEN_LAYER_NUM = 1;

    // EFFECTS: initializes SimpleConcreteAI collection with ais
    SimpleConcreteAIs(int number) {
        super();
        if (number < 0) throw new IllegalArgumentException();
        for (int i = 0; i < number; i++) {
            SimpleConcreteAI cai = new SimpleConcreteAI(GLOBAL_HIDDEN_LAYER_NUM);
            cai.randomize();
            listOfAIs.add(cai);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates all the ais
    public void update() {
        for (SimpleConcreteAI scai : listOfAIs) {
            scai.update();
        }
    }
}
