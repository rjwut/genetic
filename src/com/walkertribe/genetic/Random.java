package com.walkertribe.genetic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * A demonstration of a non-genetic attempt at the infinite monkey theorem simulation. All monkeys
 * make completely random attempts at writing the desired sequence. As this will take a
 * ridiculously long time for anything but the most trivial sequences, this demo stops after a set
 * number of iterations and prints the best sample found.
 * </p>
 * @author Robert J. Walker
 */
public class Random {
	private static final String POSSIBLE_CHARACTERS = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final long ITERATIONS = 100_000_000;

	/**
	 * Start the simulation.
	 * @param args One argument: the sequence we want a monkey to type
	 */
	public static void main(String[] args) {
		new Random(args[0]);
	}

	/**
	 * Run the simulation, using the given desired sequence. The simulation will produce completely
	 * random samples and compare them against the target sequence to compute their fitness. At the
	 * end of the simulation, it will print the best sample found, along with some statistics.
	 * @param target The sequence we want a monkey to type
	 */
	private Random(String target) {
		// Initialize the fitness distribution Map
		Map<Integer, Integer> distro = new LinkedHashMap<>();
		int len = target.length();

		for (int i = 0; i <= len; i++) {
			distro.put(i, 0);
		}

		int bestFitness = -1;
		String fittest = null;
		long startTime = System.nanoTime();

		// Simulation loop
		for (int i = 0; i < ITERATIONS; i++) {
			String str = generateRandomString(len);
			int fitness = computeFitness(str, target);
			distro.put(fitness, distro.get(fitness) + 1);

			if (fitness > bestFitness) {
				fittest = str;
				bestFitness = fitness;
			}
		}

		// Display statistics
		long stopTime = System.nanoTime();
		long elapsed = stopTime - startTime;
		System.out.println("Elapsed: " + ((Math.round(elapsed / 10_000_000_000L) * 10) + " s (" +
				elapsed + " ns)"));
		double words = ITERATIONS * len / 5.0;
		double minutes = elapsed / 60_000_000_000.0;
		long wpm = (long) Math.round(words / minutes);
		System.out.println("Typing speed: " + wpm + " wpm");
		System.out.println("Fittest: [" + bestFitness + "/" + target.length() + "] " + fittest);
		System.out.println("Distribution:");
		distro.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		});
	}

	/**
	 * Produces a random String.
	 * @param length The desired String length
	 * @return The random String
	 */
	private static String generateRandomString(int length) {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < length; i++) {
			b.append(POSSIBLE_CHARACTERS.charAt((int) (Math.random() * POSSIBLE_CHARACTERS.length())));
		}

		return b.toString();
	}

	/**
	 * Compares the two given Strings and returns the number of matching characters. Assumes that
	 * they are the same length.
	 * @param str The String to evaluate
	 * @param target The target String to compare it against
	 * @return The number of matching characters
	 */
	private static int computeFitness(String str, String target) {
		int len = str.length();
		int fitness = 0;

		for (int i = 0; i < len; i++) {
			fitness += str.charAt(i) == target.charAt(i) ? 1 : 0;
		}

		return fitness;
	}
}
