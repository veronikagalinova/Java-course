package bg.sofia.uni.fmi.mjt.grep;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GrepController {
	private File pathToDirectoryThree;
	private boolean isCaseSensitivityEnabled = false;
	private boolean isWholeWordsOptionsEnabled = false;
	private String pathToOutputFile;
	private static int NUMBER_OF_PARALLEL_THREADS;

	private List<Future<String>> result = new ArrayList<>();
	private String stringToFind;
	private ExecutorService executorService;

	public GrepController(String command) {
		parseCommand(command);
		executorService = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_THREADS);
	}

	public void evaluate(String command) {
		// TO DO - SEARCH RECURSIVE
		for (File file : pathToDirectoryThree.listFiles()) {
			if (file.isFile()) {
				result.add(executorService
						.submit(new PatternMatcher(file, stringToFind, isCaseSensitivityEnabled,
								isWholeWordsOptionsEnabled)));

			}
		}

		for (Future<String> fs : result) {
			try {
				String line = fs.get();
				System.out.println(line);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseCommand(String command) {
		command = command.replaceAll("\\s{2,}", " ").trim();
		String[] tokens = command.split(" ");

		if (tokens.length < 4) {
			throw new IllegalArgumentException("Usage: "
					+ "grep *[-w|-i] [string_to_find] [path_to_directory_tree] [number_of_parallel_threads] *[path_to_output_file]");
		}

		if (command.contains("-w") && command.contains("-i")) {
			parseCommandWithBothOptions(tokens);
		} else if (command.contains("-w") || command.contains("-i")) {
			parseCommandWithSingleOption(tokens);
		} else {
			parseCommandWithoutOptions(tokens);
		}

		System.out.println("+++string to find " + stringToFind);
		System.out.println("+++path to directory three " + pathToDirectoryThree.getAbsolutePath());
		System.out.println("+++path to output file " + pathToOutputFile);

	}

	private void parseCommandWithBothOptions(String[] tokens) {
		isWholeWordsOptionsEnabled = true;
		isCaseSensitivityEnabled = true;

		final int indexOfSearchedString = 3;
		final int indexOfPath = 4;

		stringToFind = tokens[indexOfSearchedString];
		pathToDirectoryThree = new File(tokens[indexOfPath]);
		final int indexOfThreads = 5;
		NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[indexOfThreads]);
		if (tokens.length == 7) {
			pathToOutputFile = tokens[6];
		}
	}

	private void parseCommandWithSingleOption(String[] tokens) {
		if (tokens[1].equals("-w")) {
			isWholeWordsOptionsEnabled = true;
		} else {
			isCaseSensitivityEnabled = true;
		}
		stringToFind = tokens[2];
		pathToDirectoryThree = new File(tokens[3]);
		NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[4]);
		if (tokens.length == 6) {
			pathToOutputFile = tokens[5];
		}
	}

	private void parseCommandWithoutOptions(String[] tokens) {
		stringToFind = tokens[1];
		pathToDirectoryThree = new File(tokens[2]);
		NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[3]);
		if (tokens.length == 5) {
			pathToOutputFile = tokens[4];
		}
	}

}
