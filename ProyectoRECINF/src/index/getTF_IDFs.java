package index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import system.ContainerDecoder;

public class getTF_IDFs {
	public static HashMap<String, HashMap<String, Double>> termTFs =
			new HashMap<String, HashMap<String, Double>>();
	
	public static ArrayList<Double> IDFList = new ArrayList<Double>();
	
	public static void main(String[] args) {
		System.out.println("Reading termTF.txt to multiply IDF values...");
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("infofiles/termTF.txt"));
			
			for (String key : properties.stringPropertyNames()) {
				termTFs.put(key, ContainerDecoder.decodeMap(properties.get(key).toString()));
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println("Getting array of IDF values...");
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("infofiles/termIDF.txt"));
			String line;
			
		    while ((line = reader.readLine()) != null) {
		        String[] pair = line.split(":");
		        IDFList.add(Double.parseDouble(pair[1]));
		    }
		    
		    reader.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println("Multiplying TFs and IDFs of each term...");
		
		int count = 0;
		for(String term : termTFs.keySet()) {
			for(String document : termTFs.get(term).keySet()) {
				double TF_IDF = termTFs.get(term).get(document) * IDFList.get(count);
				termTFs.get(term).put(document, TF_IDF);
			}
			count++;
		}
		
		System.out.println("Writing final TF-IDF values to TF-IDF.txt...");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("infofiles/TF-IDF.txt"));

			count = 0;
			for(String term : termTFs.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(term + ":[");
				
				for(String document : termTFs.get(term).keySet()) {
					sb.append("{" + document + ":" + termTFs.get(term).get(document).doubleValue() + "}, ");
				}
				
				sb.append("]");
				writer.write(sb.toString());
				writer.newLine();
				count++;
			}
			
		    writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
