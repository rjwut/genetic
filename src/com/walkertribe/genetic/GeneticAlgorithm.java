package com.walkertribe.genetic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * A demonstration of a genetic algorithm used for a modified infinite monkey theorem simulation.
 * </p>
 * <p>
 * Suppose that monkeys are genetically predisposed to type a certain sequence of keys on a
 * keyboard, and that we can therefore use selective breeding to eventually produce a monkey that
 * types a desired sequence. In this simulation, the desired sequence itself represents the
 * monkey's genome, and each gene is represented by a letter or space character. For simplicity, we
 * are using all uppercase letters and no punctuation.
 * </p>
 * <p>
 * Starting with a random population of monkeys, we have each type out the desired number of
 * characters, then assign them fitness scores based on how close the sequence was to the target.
 * The fittest monkeys are permitted to breed, and the next population consists of those monkeys
 * and their offspring. Each gene of the offspring is a random choice between the corresponding
 * gene of each parent, with a chance of random mutation. This process continues until a monkey is
 * found which produces the exact desired sequence of characters.
 * </p>
 * @author Robert J. Walker
 */
public class GeneticAlgorithm {
	private static final String POSSIBLE_GENES = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int POPULATION_SIZE = 1000;
	private static final float SURVIVAL_THRESHOLD = 0.2F;
	private static final float MUTATION_RATE = 0.01F;

	/**
	 * Start the simulation.
	 * @param args One argument: the sequence we want a Monkey to type
	 */
	public static void main(String[] args) {
		new GeneticAlgorithm(args[0], POSSIBLE_GENES, POPULATION_SIZE, SURVIVAL_THRESHOLD,
				MUTATION_RATE);
	}

	/**
	 * Run the simulation, using the given desired sequence. For each generation, the simulation
	 * will select the fittest Monkey and print out its fitness score and genome.
	 * @param target The sequence we want a Monkey to type
	 * @param possibleGenes The genes that can be used in the genome
	 * @param populationSize How many Monkeys are in each Population
	 * @param survivalThreshold The percentage of Monkeys that should survive in each generation
	 * @param mutationRate The rate of gene mutation
	 */
	private GeneticAlgorithm(String target, String possibleGenes, int populationSize,
			float survivalThreshold, float mutationRate) {
		Fitness fitness = new Fitness(target);
		int len = target.length();
		Monkey fittest = null;
		int generationCount = 0;
		Map<Integer, Integer> distro = new LinkedHashMap<>();
		long startTime = System.nanoTime();
		Population population = new Population(populationSize, len, possibleGenes, fitness);

		for (int i = 0; i <= len; i++) {
			distro.put(i, 0);
		}
		
		population.storeFitness(distro);

		do {
			population = population.evolve(survivalThreshold, mutationRate, possibleGenes);
			population.storeFitness(distro);
			fittest = population.getFittest();
			int score = fitness.computeFitness(fittest);
			System.out.println(
					"Gen " + pad(++generationCount, 3) + " [" + pad(score, 3) + "/" + len + "] " + fittest
			);
		} while (fitness.computeFitness(fittest) < len);

		long stopTime = System.nanoTime();
		long elapsed = stopTime - startTime;
		System.out.println("Elapsed: " + elapsed + " ns");
		double words = generationCount * len * populationSize / 5.0;
		double minutes = elapsed / 60_000_000_000.0;
		long wpm = (long) Math.round(words / minutes);
		System.out.println("Typing speed: " + wpm + " wpm");
		System.out.println("Distribution:");
		distro.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		});
	}

	/**
	 * Pads the given numeric value with spaces to meet the given String length.
	 * @param value The numeric value to pad
	 * @param length The desired String length
	 * @return The padded value as a String
	 */
	private String pad(int value, int length) {
		String str = Integer.toString(value);

		while (str.length() < length) {
			str = ' ' + str;
		}

		return str;
	}
}
