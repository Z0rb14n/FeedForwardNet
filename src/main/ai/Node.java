package ai;

import java.util.Random;

// Represents a node
public class Node {
    private static Random RAND = new Random();
    private double[] weights;
    private double bias;

    Node(int numInputs) {
        if (numInputs < 0) throw new IllegalArgumentException();
        weights = new double[numInputs];
        bias = RAND.nextGaussian() / Math.sqrt(2.0 / weights.length);
    }

    // EFFECTS: returns the weights
    public double[] getWeights() {
        return weights;
    }

    // EFFECTS: returns the bias
    public double getBias() {
        return bias;
    }

    // MODIFIES: this
    // EFFECTS: copies the given weights and biases
    public void copyValues(double[] newWeights, double newBias) {
        if (newWeights.length != weights.length) throw new IllegalArgumentException();
        double[] lmao = new double[newWeights.length];
        System.arraycopy(newWeights, 0, lmao, 0, newWeights.length);
        this.bias = newBias;
        this.weights = lmao;
    }

    // MODIFIES: this
    // EFFECTS: randomizes the weights and biases
    void randomize() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = RAND.nextGaussian() / Math.sqrt(weights.length);
        }
        bias = RAND.nextGaussian() / Math.sqrt(2.0 / weights.length);
    }

    // MODIFIES: this
    // EFFECTS: updates the weights and biases
    void mutate(double mutateChance, double mutateLimit) {
        for (int i = 0; i < weights.length; i++) {
            if (mutateChance > RAND.nextDouble()) {
                weights[i] += weights[i] * randBetween(-mutateLimit, mutateLimit);
            }
        }
        if (mutateChance > RAND.nextDouble()) {
            bias += bias * randBetween(-mutateLimit, mutateLimit);
        }
    }

    double fastCalculateOutput(double[] inputs) {
        double result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += inputs[i] * weights[i];
        }
        result += bias;
        return result;
    }

    double calculateOutput(double[] inputs, OutputMode om) {
        if (inputs.length != weights.length) throw new IllegalArgumentException("Invalid number of inputs.");
        double result = fastCalculateOutput(inputs);
        if (om == OutputMode.NONE) return result;
        if (om == OutputMode.SIGMOID) return sigmoid(result);
        else return relu(result);
    }

    // MODIFIES: this
    // EFFECTS: adjusts the seed of the random number generator to seed
    public static void setSeed(long seed) {
        RAND.setSeed(seed);
    }

    // EFFECTS: returns e^val/(e^val + 1)
    public static double sigmoid(double val) {
        return Math.exp(val) / (1 + Math.exp(val));
    }

    // EFFECTS: returns max(0,val);
    public static double relu(double val) {
        return Math.max(0, val);
    }

    // EFFECTS: returns random number between min and max
    public static double randBetween(double min, double max) {
        return RAND.nextDouble() * (max - min) + min;
    }

    // EFFECTS: makes a new node
    Node copy() {
        Node node = new Node(weights.length);
        node.copyValues(weights, bias);
        return node;
    }

    // Represents the output mode
    public enum OutputMode {
        NONE, SIGMOID, RELU
    }
}
