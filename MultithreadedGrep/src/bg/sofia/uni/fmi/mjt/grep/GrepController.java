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
	private static int NUMBER_OF_PARALLEL_THREADS;

	private List<Future<String>> result = new ArrayList<>();
	private String stringToFind;
	private ExecutorService executorService;

	public GrepController(String command) {
		parseCommand(command);

	}

	public void evaluate(String command) {
		// parseCommand(command);
		executorService = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_THREADS);
		for (File file : pathToDirectoryThree.listFiles()) {
			if (file.isFile()) {
				result.add(executorService.submit(new PatternMatcher(file, command.split(" ")[1])));
//				result.add(executorService.submit(new PatternMatcher(file, stringToFind)));

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
		String options = null;
		String stringToFind = tokens[1];
		System.out.println("+++string to find " + stringToFind);
		pathToDirectoryThree = new File(tokens[2]);
		System.out.println("+++path to directory three " + pathToDirectoryThree.getAbsolutePath());
		NUMBER_OF_PARALLEL_THREADS = Integer.parseInt(tokens[3]);

		// validate !!!
		// if (tokens.length < 4) {
		// throw new IllegalArgumentException("Usage: "
		// + "grep *[-w|-i] [string_to_find] [path_to_directory_tree]
		// [number_of_parallel_threads] *[path_to_output_file]");
		// }
		//
		// if (!tokens[0].equals("grep")) {
		// throw new IllegalArgumentException("Invalid command " + tokens[0] + "!");
		// }
	}
}
