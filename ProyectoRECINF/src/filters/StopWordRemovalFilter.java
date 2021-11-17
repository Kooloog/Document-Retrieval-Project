package filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class StopWordRemovalFilter implements Filter {

	@Override
	public String execute(String content) {
		File stopWordsFile = new File("stopwords.txt");
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		
		try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFile))) {
		    while ((line = br.readLine()) != null) {
		       lines.add(line.toLowerCase());
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] wordlist = content.split("\\W+");
		StringBuilder sb = new StringBuilder();
		
		for(String s : wordlist) {
		    if (!lines.contains(s)) sb.append(s).append(" ");
		}
		
		return sb.toString();
	}
	
}
