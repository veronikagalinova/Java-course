package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 * Used for static code checks of Java files. Depending on a stream from
 * user-defined configuration or default values, it checks if the following
 * rules are applied:
 * <li>there is only one statement per line;
 * <li>the line lengths do not exceed 100 (or user-defined) characters;
 * <li>the import statements do not use a wildcard;
 * <li>each opening block bracket is on the same line as the declaration.
 */
public class StyleChecker {
	private static final int DEFAULT_LENGHT = 100;
	private int lineLenght;
	private boolean isActiveLineLenght;
	private boolean statementsPerLine;
	private boolean wildcardImport;
	private boolean openingBrackets;

	/**
	 * Creates a StyleChecker with properties having the following default values:
	 * <li>{@code wildcard.import.check.active=true}
	 * <li>{@code statements.per.line.check.active=true}
	 * <li>{@code opening.bracket.check.active=true}
	 * <li>{@code length.of.line.check.active=true}
	 * <li>{@code line.length.limit=100}.
	 **/
	public StyleChecker() {
		this.lineLenght = DEFAULT_LENGHT;
		this.statementsPerLine = true;
		this.wildcardImport = true;
		this.openingBrackets = true;
		this.isActiveLineLenght = true;
	}

	/**
	 * Creates a StyleChecker with custom configuration, based on the content from
	 * the given {@code inputStream}. If the stream does not contain any of the
	 * properties, the missing ones get their default values.
	 **/
	public StyleChecker(InputStream inputStream) {
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			isActiveLineLenght = Boolean.parseBoolean(properties.getProperty("length.of.line.check.active", "true"));
			this.lineLenght = Integer.parseInt(properties.getProperty("line.length.limit", "100"));
			statementsPerLine = Boolean
					.parseBoolean(properties.getProperty("statements.per.line.check.active", "true"));
			wildcardImport = Boolean.parseBoolean(properties.getProperty("wildcard.import.check.active", "true"));
			openingBrackets = Boolean.parseBoolean(properties.getProperty("opening.bracket.check.active", "true"));
		} catch (IOException e) {
			System.err.println("StyleChecker(): error loading properties from input stream");
		}
	}

	/**
	 * For each line from the given {@code source} InputStream writes fix me comment
	 * for the violated rules (if any) with an explanation of the style error
	 * preceded by the line itself in the {@code output}.
	 **/
	public void checkStyle(InputStream source, OutputStream output) {
		if (source == null || output == null) {
			return;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(source));
				OutputStreamWriter osw = new OutputStreamWriter(output);
				BufferedWriter bw = new BufferedWriter(osw)) {

			String line = br.readLine();

			while (line != null) {
				if (statementsPerLine && checkSingleStatementPerLine(line)) {
					bw.write("// FIXME Only one statement per line is allowed\n");
				}

				if (wildcardImport && checkWildcardsInImports(line)) {
					bw.write("// FIXME Wildcards are not allowed in import statements\n");
				}

				if (openingBrackets && isOpeningBracketAtNewLine(line)) {
					bw.write("// FIXME Opening brackets should be placed on the same line as the declaration\n");
				}

				if (isActiveLineLenght && isLineLonger(line)) {
					bw.write(String.format("// FIXME Length of line should not exceed %s characters", lineLenght)
							+ "\n");
				}

				bw.write(line + "\n");

				line = br.readLine();
			}
		} catch (IOException e) {
			System.err.println("Error while reading/writing input: " + e.getMessage());
		}
	}

	private boolean isLineLonger(String line) {
		if (line.startsWith("import")) {
			return false;
		}

		return line.trim().length() >= lineLenght;
	}

	private boolean isOpeningBracketAtNewLine(String line) {
		return line.length() != 0 && line.trim().charAt(0) == '{';
	}

	private boolean checkWildcardsInImports(String line) {
		return line.startsWith("import") && line.indexOf("*") != -1;
	}

	private boolean checkSingleStatementPerLine(String line) {
		String[] statements = line.split(";");
		int count = 0;
		for (String statement : statements) {
			if (!statement.equals("")) {
				count++;
			}
		}
		return count > 1;
	}

}
