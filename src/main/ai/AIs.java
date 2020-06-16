package ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

// Represents a collection of AIs
public class AIs<T extends AI> implements Iterable<T> {
    private final double KILL_RATE = 0.500000000000000000;
    private final double MUTATION_CHANCE = 0.2;
    private final double MUTATION_AMOUNT = 0.1;
    protected ArrayList<T> listOfAIs = new ArrayList<>();
    protected int previousBest = -1;

    // EFFECTS: initializes the AIs
    AIs() {
    }

    // EFFECTS: gets index of best AI
    public int getPreviousBest() {
        return previousBest;
    }

    // EFFECTS: gets the number of AIs
    public int numberOfAis() {
        return listOfAIs.size();
    }

    // MODIFIES: this
    // EFFECTS: kills a desired amount of AIs and repopulates it with new AIs
    public void evolve() {
        final int desiredSize = listOfAIs.size();
        listOfAIs.sort(new AIComparator());
        final int amountKilled = (int) Math.round(KILL_RATE * listOfAIs.size());
        listOfAIs.subList(0, amountKilled - 1).clear(); // remove all from 0 - amountKilled -1
        final int currentSize = listOfAIs.size();
        int currentIndex = 0;
        previousBest = listOfAIs.size() - 1;
        while (listOfAIs.size() < desiredSize) {
            T ai = (T) listOfAIs.get(currentIndex).copy();
            ai.mutate(MUTATION_CHANCE, MUTATION_AMOUNT);
            listOfAIs.add(ai);
            currentIndex++;
            if (currentIndex == currentSize) {
                currentIndex = 0;
            }
        }

        for (T ai : listOfAIs) {
            ai.reset();
        }
    }

    @Override
    // EFFECTS: returns an iterator over the AIs
    public Iterator<T> iterator() {
        return listOfAIs.iterator();
    }

    // Represents a fitness value comparator
    public class AIComparator implements Comparator<AI> {
        @Override
        // EFFECTS: compares the two AIs and returns a value if they are greater, equal or less than
        public int compare(AI o1, AI o2) {
            if (o1.calculateFitness() > o2.calculateFitness()) return 1;
            if (o1.calculateFitness() == o2.calculateFitness()) return 0;
            if (o1.calculateFitness() < o2.calculateFitness()) return -1;
            else throw new IllegalArgumentException();
        }
    }
}
