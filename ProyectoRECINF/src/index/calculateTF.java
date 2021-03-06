package index;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import system.ContainerDecoder;

public class calculateTF {
	public static HashMap<String, ArrayList<String>> documentTerms = 
			  new HashMap<String, ArrayList<String>>();
	
	public static HashMap<String, HashMap<String, Double>> termTFs =
			new HashMap<String, HashMap<String, Double>>();
	
	public static void main(String[] args) {
		System.out.println("Reading documentAndTerms.txt...");
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("infofiles/documentsAndTerms.txt"));
			
			for (String key : properties.stringPropertyNames()) {
				documentTerms.put(key, ContainerDecoder.decodeList(properties.get(key).toString()));
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println("Calculating frequency for each term...");
		
		for(String entry : documentTerms.keySet()) {
			for(String term : documentTerms.get(entry)) {
				//This word has already appeared before, and in any document.
				if(termTFs.containsKey(term)) {
					//This word has already appeared in this specific document
					if(termTFs.get(term).containsKey(entry)) {
						double increment = termTFs.get(term).get(entry).doubleValue();
						termTFs.get(term).put(entry, increment+1.0);
					}
					//This word has not appeared in this specific document yet.
					else {
						termTFs.get(term).put(entry, 1.0);
					}
				}
				//This word has not appeared before in any document
				else {
					HashMap<String, Double> firstValue = new HashMap<String, Double>();
					firstValue.put(entry, 1.0);
					termTFs.put(term, firstValue);
				}
			}
		}
		
		System.out.println("Calculating TF value for each term...");
		
		for(String term : termTFs.keySet()) {
			for(String document : termTFs.get(term).keySet()) {
				double TF_IDF = 1.0 + (Math.log(termTFs.get(term).get(document).doubleValue()) / Math.log(2));
				termTFs.get(term).put(document, TF_IDF);
			}
		}
		
		System.out.println("Writing TF multimap to file termTF.txt...");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("infofiles/termTF.txt"));

			for(String term : termTFs.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(term).append(":[");
				
				for(String document : termTFs.get(term).keySet()) {
					sb.append("{" + document + ":" + termTFs.get(term).get(document).doubleValue() + "}, ");
				}
				
				sb.append("]");
				writer.write(sb.toString());
				writer.newLine();
			}
			
		    writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
