package bg.sofia.uni.fmi.mjt.grep;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scaner = new Scanner(System.in);
		String command = scaner.nextLine();
		GrepController grepController = new GrepController(command);
		grepController.evaluate(command);
	}

}
