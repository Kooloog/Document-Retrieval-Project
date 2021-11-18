package index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import system.Preprocessor;

public class GetWordsInDocuments {
	public static HashMap<String, ArrayList<String>> indexTerms = 
				  new HashMap<String, ArrayList<String>>();
	
	public static void main(String[] args) {
		File corpusFolder = new File("corpus");
		String fileContents = "";
		
		Preprocessor.loadFilters();
		
		System.out.println("Reading files from corpus folder and applying filters...");
		
		for (File file : corpusFolder.listFiles()) {
			try {
				fileContents = new String(Files.readAllBytes(Paths.get(file.toURI())));
			} catch (IOException e) { e.printStackTrace(); }
			
			indexTerms.put(file.getName(), Preprocessor.preprocess(fileContents));
		}
		
		System.out.println("Saving preprocessed terms to file documentsAndTerms.txt");
		
	    BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("infofiles/documentsAndTerms.txt"));

			for(String entry : indexTerms.keySet()) {
				writer.write(entry + ":" + indexTerms.get(entry));
				writer.newLine();
			}
			
		    writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
