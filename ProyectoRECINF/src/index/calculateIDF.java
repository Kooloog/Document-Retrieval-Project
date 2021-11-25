package index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import system.ContainerDecoder;

public class calculateIDF {
	public static HashMap<String, HashMap<String, Double>> termTFs =
			new HashMap<String, HashMap<String, Double>>();
	
	public static HashMap<String, Double> termIDFs = new HashMap<String, Double>();
	
	public static void main(String[] args) {
		System.out.println("Reading termTF.txt...");
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("infofiles/termTF.txt"));
			
			for (String key : properties.stringPropertyNames()) {
				termTFs.put(key, ContainerDecoder.decodeMap(properties.get(key).toString()));
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println("Calculating IDF for each term...");
		
		double numberOfDocuments = new File("corpus").list().length;
		for(String document : termTFs.keySet()) {
			double IDF = (Math.log(numberOfDocuments / termTFs.get(document).size()) / Math.log(2));
			termIDFs.put(document, IDF);
		}
		
		System.out.println("Writing TF multimap to file termIDF.txt...");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("infofiles/termIDF.txt"));
			
			for(String term : termIDFs.keySet()) {
				writer.write(term + ":" + termIDFs.get(term));
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
