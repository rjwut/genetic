package com.walkertribe.genetic;

/**
 * Represents a single individual in the population.
 * @author Robert J. Walker
 */
public class Monkey {
	private char[] genome;
	private int score = -1;

	/**
	 * Creates a new Monkey with randomized genes.
	 * @param geneCount The number of genes the Monkey should have
	 * @param possibleGenes A String containing all possible gene characters
	 */
	public Monkey(int geneCount, String possibleGenes) {
		genome = new char[geneCount];
		int possibleLength = possibleGenes.length();

		for (int i = 0; i < geneCount; i++) {
			genome[i] = possibleGenes.charAt((int) (Math.random() * possibleLength));
		}
	}

	/**
	 * Creates a new Monkey with the specified genome.
	 * @param genome The genome for the new Monkey
	 */
	private Monkey(char[] genome) {
		this.genome = genome;
	}

	/**
	 * Breeds this Monkey with the given Monkey to produce one offspring.
	 * @param otherMonkey The Monkey to breed with this Monkey
	 * @return An offspring Monkey
	 */
	public Monkey breed(Monkey otherMonkey, float mutationRate, String possibleGenes) {
		if (genome.length != otherMonkey.genome.length) {
			throw new IllegalArgumentException(
					"A monkey with " + genome.length + " genes cannot breed with a monkey with " +
					otherMonkey.genome.length + " genes"
			);
		}

		char[] childGenome = new char[genome.length];

		for (int i = 0; i < genome.length; i++) {
			if (Math.random() < mutationRate) { // Mutation!
				childGenome[i] = possibleGenes.charAt((int) (Math.random() * possibleGenes.length()));
			} else { // Take gene from one of the parents
				childGenome[i] = (Math.random() < 0.5 ? genome : otherMonkey.genome)[i];
			}
		}

		return new Monkey(childGenome);
	}

	/**
	 * Returns a specific gene.
	 * @param index The index of the gene to return
	 * @return The gene at that index
	 */
	char getGene(int index) {
		return genome[index];
	}

	/**
	 * Returns the fitness score for this Monkey, using the given Fitness object. Note that the
	 * score is cached for performance, so the Fitness object is ignored after the first
	 * invocation.
	 * @param fitness The Fitness object to use to compute the score
	 * @return The fitness score, from 0 to the length of the genome
	 */
	int getFitnessScore(Fitness fitness) {
		if (score == -1) {
			score = fitness.computeFitness(this);
		}

		return score;
	}

	/**
	 * Prints out the Monkey's genome (the String it types).
	 */
	@Override
	public String toString() {
		return String.valueOf(genome);
	}
}
