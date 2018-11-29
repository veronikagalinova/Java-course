package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

public class StyleCheckerTest {
	private static StyleChecker checker;

	@Before
	public void setup() {
		checker = new StyleChecker();
	}

	@Test
	public void testImports() {
		ByteArrayInputStream input = new ByteArrayInputStream("import java.util.*;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Wildcards are not allowed in import statements\nimport java.util.*;", actual.trim());
	}

	@Test
	public void testConstrucorWithArguments() {
		ByteArrayInputStream style = new ByteArrayInputStream(
				"line.length.limit=50\nwildcard.import.check.active=true".getBytes());
		checker = new StyleChecker(style);

		String line = getLongLine();
		ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());
		assertEquals("// FIXME Length of line should not exceed 50 characters\n" + line, actual.trim());
	}

	@Test
	public void testBrackets() {
		ByteArrayInputStream input = new ByteArrayInputStream("public static void main(String[] args)\n{".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals(
				"public static void main(String[] args)\n"
						+ "// FIXME Opening brackets should be placed on the same line as the declaration\n{",
				actual.trim());
	}

	@Test
	public void testLenght() {
		String line = getLongLine();
		ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Length of line should not exceed 100 characters\n" + line.toString(), actual.trim());
	}

	@Test
	public void testStatements() {
		ByteArrayInputStream input = new ByteArrayInputStream("foo;foo;foo;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Only one statement per line is allowed\nfoo;foo;foo;", actual.trim());
	}

	@Test
	public void testEmptyInput() {
		ByteArrayInputStream input = new ByteArrayInputStream("".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("", actual.trim());
	}

	private static String getLongLine() {
		StringBuilder line = new StringBuilder();
		final int end = 100;
		for (int i = 0; i < end; i++) {
			line.append(i);
		}
		return line.toString();
	}
}
