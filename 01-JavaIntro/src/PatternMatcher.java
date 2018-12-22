import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
	public static boolean match(String s, String p) {
		String regex = p.replaceAll("\\*", ".*").replaceAll("\\?", ".?");
//		return s.matches(".*" + regex + ".*");
		Pattern a = Pattern.compile(".*" + regex + ".*");
		Matcher m = a.matcher(s);
		return m.matches();
 	}
}
