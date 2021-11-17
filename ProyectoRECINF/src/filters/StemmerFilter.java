package filters;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.porterStemmer;

public class StemmerFilter implements Filter {
	
	public String execute(String content) {
		SnowballStemmer stemmer = new porterStemmer();
		
		String[] wordlist = content.split("\\W+");
		StringBuilder sb = new StringBuilder();
		
		for(String s : wordlist) {
		    stemmer.setCurrent(s);
		    stemmer.stem();
		    sb.append(stemmer.getCurrent()).append(" ");
		}
		
		return sb.toString();
	}
}