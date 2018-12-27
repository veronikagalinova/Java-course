package bg.sofia.uni.fmi.mjt.grep;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scaner = new Scanner(System.in);

		// grep [string_to_find] [path_to_directory_tree] [number_of_parallel_threads]

		String command = scaner.nextLine();
		command = command.replaceAll("\\s{2,}", " ").trim();
		System.out.println(command);	
		GrepController demo = new GrepController(command);
		demo.evaluate(command);
	}

}
