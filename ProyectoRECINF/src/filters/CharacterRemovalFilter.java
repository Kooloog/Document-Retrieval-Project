package filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterRemovalFilter implements Filter {
	String regex, replace;
	
	public CharacterRemovalFilter(String sPattern, String sReplace) {
		regex = sPattern;
		replace = sReplace;
	}

	@Override
	public String execute(String content) {
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(content);
		return mat.replaceAll(replace);
	}
}
