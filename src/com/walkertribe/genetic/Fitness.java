package com.walkertribe.genetic;

import java.util.Comparator;

/**
 * An object which can evaluate the fitness of individual Monkeys.
 * @author Robert J. Walker
 */
public class Fitness implements Comparator<Monkey> {
	private String optimal;
	private int length;

	/**
	 * Creates a Fitness object which optimizes for the given genome. Note that in most cases the
	 * genome would dictate behavior, and the fitness function would test the desirability of the
	 * outcome of that behavior. However, for simplicity in this example, we're testing the genome
	 * directly.
	 * @param optimal The genome to optimize for
	 */
	public Fitness(String optimal) {
		this.optimal = optimal;
		length = optimal.length();
	}

	/**
	 * Computes the fitness of a particular Monkey.
	 * @param monkey The Monkey to evaluate
	 * @return The fitness score for that Monkey, between 0 and the length of the genome
	 */
	public int computeFitness(Monkey monkey) {
		int correct = 0;

		for (int i = 0; i < length; i++) {
			if (monkey.getGene(i) == optimal.charAt(i)) {
				correct++;
			}
		}

		return correct;
	}

	/**
	 * Sorts fitter Monkeys earlier.
	 * @param monkey1 The first Monkey to compare
	 * @param monkey2 The second Monkey to compare
	 */
	@Override
	public int compare(Monkey monkey1, Monkey monkey2) {
		return monkey2.getFitnessScore(this) - monkey1.getFitnessScore(this);
	}
}