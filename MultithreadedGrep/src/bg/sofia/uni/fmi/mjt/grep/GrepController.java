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
	private File pathToOutputFile;
	private static int NUMBER_OF_PARALLEL_THREADS;

	private List<Future<String>> result = new ArrayList<>();
	private String stringToFind;
	private ExecutorService executorService;

	public GrepController(String command) {
		parseCommand(command);
		executorService = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_THREADS);
	}

	public void evaluate(String command) {
		// parseCommand(command);
		for (File file : pathToDirectoryThree.listFiles()) {
			if (file.isFile()) {
				// result.add(executorService.submit(new PatternMatcher(file, command.split("
				// ")[1])));
				result.add(executorService
						.submit(new PatternMatcher(file, stringToFind, isCaseSensitivityEnabled,
								isWholeWordsOptionsEnabled, pathToDirectoryThree)));

			}
		}

		for (Future<String> fs : result) {
			try {
				System.out.println(fs.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
				executorService.shutdown();
			}
		}
	}

	private void parseCommand(String command) {
		String[] tokens = command.split(" ");

		if (tokens.length < 4) {
			throw new IllegalArgumentException("Usage: "
					+ "grep *[-w|-i] [string_to_find] [path_to_directory_tree] [number_of_parallel_threads] *[path_to_output_file]");
		}

		if (command.contains("-w") && command.contains("-i")) {
			isWholeWordsOptionsEnabled = true;
			isCaseSensitivityEnabled = true;
			stringToFind = tokens[3];
			pathToDirectoryThree = new File(tokens[4]);
			NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[5]);
			if (tokens.length == 7) {
				pathToOutputFile = new File(tokens[6]);
			}
		} else if (command.contains("-w")) {
			isWholeWordsOptionsEnabled = true;
			stringToFind = tokens[2];
			pathToDirectoryThree = new File(tokens[3]);
			NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[4]);
			if (tokens.length == 6) {
				pathToOutputFile = new File(tokens[5]);
			}
		} else if (command.contentEquals("-i")) {
			isCaseSensitivityEnabled = true;
			stringToFind = tokens[2];
		} else {
			stringToFind = tokens[1];
			pathToDirectoryThree = new File(tokens[2]);
			NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[3]);
			if (tokens.length == 5) {
				pathToOutputFile = new File(tokens[4]);
			}
		}

		System.out.println("+++string to find " + stringToFind);
		System.out.println("+++path to directory three " + pathToDirectoryThree.getAbsolutePath());
	}

}
