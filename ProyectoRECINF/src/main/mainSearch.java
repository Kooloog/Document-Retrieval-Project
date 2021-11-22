package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import index.*;
import system.ContainerDecoder;
import system.Preprocessor;
import system.RankMap;

public class mainSearch {
	
	public static HashMap<String, HashMap<String, Double>> termTFIDFs =
			new HashMap<String, HashMap<String, Double>>();
	
	public static HashMap<String, Double> termIDFs = new HashMap<String, Double>();
	
	public static HashMap<String, Double> documentLengths = new HashMap<String, Double>();
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if(!new File("infofiles/documentsAndTerms.txt").exists()) GetWordsInDocuments.main(args);
		if(!new File("infofiles/termTF.txt").exists()) calculateTF.main(args);
		if(!new File("infofiles/termIDF.txt").exists()) calculateIDF.main(args);
		if(!new File("infofiles/TF-IDF.txt").exists()) getTF_IDFs.main(args);
		if(!new File("infofiles/documentLengths.txt").exists()) getDocumentLengths.main(args);
		
		System.out.println("Preparing search program...");
		String search = null;
		Scanner getSearchInput = new Scanner(System.in);
		Preprocessor.loadFilters();
		
		//Loading TF-IDFs for every term
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("infofiles/TF-IDF.txt"));
			
			for (String key : properties.stringPropertyNames()) {
				termTFIDFs.put(key, ContainerDecoder.decodeMap(properties.get(key).toString()));
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		//Loading IDFs separately for every term
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("infofiles/termIDF.txt"));
			String line;
			
		    while ((line = reader.readLine()) != null) {
		        String[] pair = line.split(":");
		        termIDFs.put(pair[0], Double.parseDouble(pair[1]));
		    }
		} catch (IOException e) { e.printStackTrace(); }
		
		//Loading document scores
		try {
			reader = new BufferedReader(new FileReader("infofiles/documentLengths.txt"));
			String line;
			
		    while ((line = reader.readLine()) != null) {
		        String[] pair = line.split(":");
		        documentLengths.put(pair[0], Double.parseDouble(pair[1]));
		    }
		} catch (IOException e) { e.printStackTrace(); }
		reader.close();
		
		while(true) {	
			//Asking user for search input
			while(search == null) {
				System.out.println("-------------------------------------------------------------");
				System.out.print("Your search (leave empty to exit): ");
				search = getSearchInput.nextLine();
				
				Pattern pat = Pattern.compile("\\s*");
				Matcher mat = pat.matcher(search);
				if(mat.matches()) {
					System.out.println("-------------------------------------------------------------");
					System.out.println("Goodbye.");
					System.exit(0);
				}
			}
			
			ArrayList<String> wordsInSearch = Preprocessor.preprocess(search);
			HashMap<String, Double> documentsAndScore = new HashMap<String, Double>();
			
			for(String term : wordsInSearch) {
				if(termTFIDFs.containsKey(term)) {
					double IDF = termIDFs.get(term).doubleValue();
					
					for(String document : termTFIDFs.get(term).keySet()) {
						double addToScore = IDF * termTFIDFs.get(term).get(document).doubleValue();
								
						if(!documentsAndScore.containsKey(document)) {
							documentsAndScore.put(document, addToScore);
						}
						else {
							double newScore = documentsAndScore.get(document).doubleValue() + addToScore;
							documentsAndScore.put(document, newScore);
						}
					}
				}
			}
			
			if(!documentsAndScore.isEmpty()) {
				for(String document : documentsAndScore.keySet()) {
					double finalScore = documentsAndScore.get(document).doubleValue() / documentLengths.get(document);
					documentsAndScore.put(document, finalScore);
				}
				
				documentsAndScore = RankMap.sort(documentsAndScore);
				
				int cont = 0;
				int breakAt = Math.min(10, documentsAndScore.size());
				
				while(cont < documentsAndScore.size()) {
					for(int i = cont; i<breakAt; i++) {
						String document = documentsAndScore.keySet().toArray()[i].toString();
						String fileFound = "corpus/" + document;
						File file = new File(fileFound);
						String fileContents = new String(Files.readAllBytes(Paths.get(file.toURI())));
						
						System.out.println("-------------------------------------------------------------");
						System.out.println("Document: " + document + 
								"\t(Weight: " + documentsAndScore.get(document) + ")");
						System.out.println("Preview: " + 
								fileContents.substring(0, Math.min(99, fileContents.length())) + "...");
						
						cont++;
						if(cont >= breakAt) break;
					}
					
					if(cont != documentsAndScore.size()) {
						System.out.println("-------------------------------------------------------------");
						System.out.println(cont + " results out of " + documentsAndScore.size() + " shown.");
						System.out.println("-------------------------------------------------------------");
						System.out.print("Enter 1 to show 10 more results, or anything else to do another search: ");
						search = getSearchInput.nextLine();
						System.out.println(search);
						
						if(search.isEmpty()) break;
						else if(search.charAt(0) == '1') {
							breakAt = Math.min(breakAt+10, documentsAndScore.size());
						}
						else break;
					}
					else {
						System.out.println("-------------------------------------------------------------");
						System.out.println("All results shown. Please do another search.");
					}
				}
			}
			else {
				System.out.println("-------------------------------------------------------------");
				System.out.println("No results found.");
			}
			
			search = null;
		}
	}
}
