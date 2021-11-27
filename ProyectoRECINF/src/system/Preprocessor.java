package system;

import java.util.ArrayList;
import java.util.Arrays;

import filters.*;

public class Preprocessor {
	static ArrayList<Filter> filters = new ArrayList<Filter>();
	static ArrayList<String> fileIndexTerms;
	
	public static void loadFilters() {
		filters.add(new ToLowerCaseFilter());
		filters.add(new StopWordRemovalFilter());
		filters.add(new CharacterRemovalFilter("[:,\\.;\\?¿¡!…\"'<>\\(\\)\\[\\]\\{\\}]", " "));
		filters.add(new CharacterRemovalFilter("\\b\\d+\\b", " "));
		filters.add(new StemmerFilter());
		filters.add(new CharacterRemovalFilter("\\b.{1,2}\\b", " "));
		filters.add(new CharacterRemovalFilter("\\s{2,}", " "));
		
		StopWordRemovalFilter.loadStopWords();
	}
	
	public static ArrayList<String> preprocess(String fileContent) {
		
		for(Filter filter : filters) {
			fileContent = filter.execute(fileContent);
		}
		
		fileIndexTerms = new ArrayList<String>(Arrays.asList(fileContent.split("\\s")));
		return fileIndexTerms;
	}
}
