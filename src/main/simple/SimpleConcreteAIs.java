package simple;

import ai.GraphicalAIs;

class SimpleConcreteAIs extends GraphicalAIs<SimpleConcreteAI> {
    private static final int GLOBAL_HIDDEN_LAYER_NUM = 1;

    SimpleConcreteAIs() {
        super();
    }

    public void update() {
        for (SimpleConcreteAI scai : listOfAIs) {
            scai.update();
        }
    }

    void generate(int number) {
        if (number < 0) throw new IllegalArgumentException();
        for (int i = 0; i < number; i++) {
            SimpleConcreteAI cai = new SimpleConcreteAI(GLOBAL_HIDDEN_LAYER_NUM);
            cai.randomize();
            listOfAIs.add(cai);
        }
    }
}
