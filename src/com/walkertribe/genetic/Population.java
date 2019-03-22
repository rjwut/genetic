package com.walkertribe.genetic;

import java.util.Arrays;
import java.util.Map;

/**
 * Represents a group of Monkeys.
 * @author Robert J. Walker
 */
public class Population {
	private Monkey[] monkeys;
	private Fitness fitness;

	/**
	 * Creates a new Population of randomly-generated Monkeys.
	 * @param size The number of Monkeys the Population can hold
	 * @param geneCount The number of genes each Monkey should have
	 * @param possibleGenes A String containing all possible gene characters
	 * @param fitness The object which determines a Monkey's fitness
	 */
	public Population(int size, int geneCount, String possibleGenes, Fitness fitness) {
		monkeys = new Monkey[size];
		this.fitness = fitness;

		for (int i = 0; i < monkeys.length; i++) {
			monkeys[i] = new Monkey(geneCount, possibleGenes);
		}

		Arrays.sort(monkeys, fitness);
	}

	/**
	 * Creates a new Population with the given Monkeys in it.
	 * @param monkeys The Monkeys to be in the Population
	 * @param fitness The object which determines a Monkey's fitness
	 */
	private Population(Monkey[] monkeys, Fitness fitness) {
		this.monkeys = monkeys;
		this.fitness = fitness;
		Arrays.sort(monkeys, fitness);
	}

	/**
	 * Returns the size of this population.
	 */
	public int getSize() {
		return monkeys.length;
	}

	/**
	 * Returns the fittest Monkey in this population.
	 * @return The fittest Monkey
	 */
	public Monkey getFittest() {
		return monkeys[0];
	}

	/**
	 * Returns a new Population of evolved Monkeys. Evolution happens by keeping the top X% fittest
	 * Monkeys, culling the rest, then breeding the surviving Monkeys to return the Population to
	 * its original size.
	 * @param survivalThreshold The percentage of Monkeys that should survive the cull
	 * @param mutationRate The rate of gene mutation
	 * @param possibleGenes The genes that can be inserted by mutations
	 * @return A new Population
	 */
	public Population evolve(float survivalThreshold, float mutationRate, String possibleGenes) {
		// Cull population to the fittest Monkeys
		int culledSize = (int) (monkeys.length * survivalThreshold);
		Monkey[] newPopulation = new Monkey[monkeys.length];
		System.arraycopy(monkeys, 0, newPopulation, 0, culledSize);

		// Breed those Monkeys
		int childCount = monkeys.length - culledSize;

		for (int i = 0; i < childCount; i++) {
			Monkey monkey1 = getRandomMonkey(culledSize);
			Monkey monkey2 = getRandomMonkey(culledSize);
			newPopulation[i + culledSize] = monkey1.breed(monkey2, mutationRate, possibleGenes);
		}

		return new Population(newPopulation, fitness);
	}

	/**
	 * Stores the fitness scores for each Monkey in this Population to the given distribution Map.
	 * @param distro The map to store to
	 */
	public void storeFitness(Map<Integer, Integer> distro) {
		for (Monkey monkey : monkeys) {
			int score = monkey.getFitnessScore(fitness);
			distro.put(score, distro.get(score) + 1);
		}
	}

	/**
	 * Returns a random Monkey from this Population whose index is between 0 (inclusive) and
	 * maxIndex (exclusive). 
	 * @param maxIndex The upper bound of the indices from which to select a Monkey
	 * @return The selected Monkey
	 */
	private Monkey getRandomMonkey(int maxIndex) {
		return monkeys[(int) (Math.random() * maxIndex)];
	}
}
