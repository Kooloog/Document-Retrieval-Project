package index;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import system.ContainerDecoder;

public class getDocumentLengths {
	public static HashMap<String, HashMap<String, Double>> termTFIDFs =
			new HashMap<String, HashMap<String, Double>>();
	
	public static HashMap<String, Double> documentLengths = new HashMap<String, Double>();
	
	public static Double getLengthOfDocument(String documentToFind) {
		double length = 0;
		
		for(String term : termTFIDFs.keySet()) {
			if(termTFIDFs.get(term).containsKey(documentToFind)) {
				length += Math.pow(termTFIDFs.get(term).get(documentToFind), 2);
			}
		}
		
		return Math.sqrt(length);
	}
	
	public static void main(String[] args) {
		System.out.println("Reading TF-IDF.txt to get document lengths...");
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("infofiles/TF-IDF.txt"));
			
			for (String key : properties.stringPropertyNames()) {
				termTFIDFs.put(key, ContainerDecoder.decodeMap(properties.get(key).toString()));
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println("Calculating length for each document (this may take a while)...");
		
		for(String term : termTFIDFs.keySet()) {
			for(String document : termTFIDFs.get(term).keySet()) {
				if(!documentLengths.containsKey(document)) {
					documentLengths.put(document, getLengthOfDocument(document));
				}
			}
		}
		
		System.out.println("Saving lengths to documentLengths.txt...");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("infofiles/documentLengths.txt"));
			
			for(String document : documentLengths.keySet()) {
				writer.write(document + ":" + documentLengths.get(document));
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
