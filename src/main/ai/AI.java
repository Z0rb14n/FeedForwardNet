package ai;

public abstract class AI {
    protected Node[][] layers; // Node[layer number from left-1][node from top]

    protected int outputNumber;
    protected int inputNumber;
    protected int hiddenLayerNumber;

    // EFFECTS: initializes an AI with given inputNumbers, outputNumbers and hidden layer number
    AI(int inputNumber, int outputNumber, int hiddenLayerNumber) {
        if (hiddenLayerNumber < 0 || outputNumber < 1 || inputNumber < 0) throw new IllegalArgumentException();
        this.hiddenLayerNumber = hiddenLayerNumber;
        layers = new Node[hiddenLayerNumber + 1][];
        for (int i = 0; i < layers.length; i++) {
            if (i == layers.length - 1) {
                layers[i] = new Node[outputNumber];
            } else {
                layers[i] = new Node[inputNumber];
            }
            for (int j = 0; j < layers[i].length; j++) {
                layers[i][j] = new Node(inputNumber);
            }
        }
        this.outputNumber = outputNumber;
        this.inputNumber = inputNumber;
    }

    // MODIFIES: this
    // EFFECTS: randomizes the weights and biases in every node
    public final void randomize() {
        for (Node[] node : layers) {
            for (Node n : node) {
                n.randomize();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: mutates the weights and biases
    final void mutate(double chance, double amt) {
        for (Node[] node : layers) {
            for (Node n : node) {
                n.mutate(chance, amt);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the AI
    public void update() {
        double[] outputs = calcOutputs(getInputs(), Node.OutputMode.SIGMOID);
        doOutputs(outputs);
    }

    // MODIFIES: this
    // EFFECTS: updates the AI
    public void update(Node.OutputMode om) {
        double[] outputs = calcOutputs(getInputs(), om);
        doOutputs(outputs);
    }

    // EFFECTS: returns a fitness value
    public abstract double calculateFitness();

    // MODIFIES: this
    // EFFECTS: performs various outputs with given output array
    protected void doOutputs(double[] outputs) {
        doOutput(determineHighestIndex(outputs));
    }

    // MODIFIES: this
    // EFFECTS: performs the output with given index
    protected abstract void doOutput(int index);

    // MODIFIES: this
    // EFFECTS: resets the AI but DOES NOT reset weights/biases
    public abstract void reset();

    // EFFECTS: calculates the inputs
    protected abstract double[] getInputs();

    // EFFECTS: calculates the output array
    protected final double[] calcOutputs(double[] inputs, Node.OutputMode mode) {
        double[] nextLayerInputs = new double[inputs.length];
        System.arraycopy(inputs, 0, nextLayerInputs, 0, inputs.length);
        double[] output = new double[outputNumber];
        for (int i = 0; i < layers.length; i++) {
            if (i == layers.length - 1) {
                for (int j = 0; j < layers[i].length; j++) {
                    output[j] = layers[i][j].calculateOutput(nextLayerInputs, mode);
                }
            } else {
                double[] tempInputs = new double[nextLayerInputs.length];
                System.arraycopy(nextLayerInputs, 0, tempInputs, 0, nextLayerInputs.length);
                for (int j = 0; j < layers[i].length; j++) {
                    nextLayerInputs[j] = layers[i][j].calculateOutput(tempInputs, mode);
                }
            }
        }
        return output;
    }

    // EFFECTS: calculates the output array quickly (i.e. simple addition of all input weights/biases - no sigmoid)
    protected final double[] fastCalcOutputs(double[] inputs) {
        double[] nextLayerInputs = new double[inputs.length];
        System.arraycopy(inputs, 0, nextLayerInputs, 0, inputs.length);
        double[] output = new double[outputNumber];
        for (int i = 0; i < layers.length; i++) {
            if (i == layers.length - 1) {
                for (int j = 0; j < layers[i].length; j++) {
                    output[j] = layers[i][j].fastCalculateOutput(nextLayerInputs);
                }
            } else {
                double[] tempInputs = new double[nextLayerInputs.length];
                System.arraycopy(nextLayerInputs, 0, tempInputs, 0, nextLayerInputs.length);
                for (int j = 0; j < layers[i].length; j++) {
                    nextLayerInputs[j] = layers[i][j].fastCalculateOutput(tempInputs);
                }
            }
        }
        return output;
    }

    // EFFECTS: determines the highest index in given array
    protected static int determineHighestIndex(double[] arr) {
        int result = -1;
        double max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                result = i;
            }
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: copies the given nodes to AI nodes, assuming it's valid
    public final void copyNodes(Node[][] nodes) {
        if (!verifyNodes(nodes)) throw new IllegalArgumentException();
        Node[][] tempNodes = new Node[nodes.length][];
        for (int i = 0; i < nodes.length; i++) {
            Node[] tempNodeArray = new Node[nodes[i].length];
            for (int j = 0; j < nodes[i].length; j++) {
                tempNodeArray[j] = nodes[i][j].copy();
            }
            tempNodes[i] = tempNodeArray;
        }
        this.layers = tempNodes;
    }

    // EFFECTS: returns true if nodes are of correct size/length
    protected final boolean verifyNodes(Node[][] node) {
        if (node.length != layers.length) return false;
        for (int i = 0; i < layers.length; i++) {
            if (node[i].length != layers[i].length) return false;
        }
        return true;
    }

    // EFFECTS: returns exact copy of this AI
    public abstract AI copy();
}
