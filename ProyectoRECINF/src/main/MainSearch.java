package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

import system.Preprocessor;

public class MainSearch {
	public static HashMap<String, HashSet<String>> indexTerms = new HashMap<String, HashSet<String>>();
	
	public static void main(String[] args) {
		File corpusFolder = new File("corpus");
		String fileContents = "";
		int current = 1;
		
		Preprocessor.loadFilters();
		
		for (File file : corpusFolder.listFiles()) {
			try {
				fileContents = new String(Files.readAllBytes(Paths.get(file.toURI())));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Reading file #" + current++);
			indexTerms.put(file.getName(), Preprocessor.preprocess(fileContents));
			
			if(current >= 10) break;
		}
		
		for(String s : indexTerms.keySet()) {
			System.out.println(s + ": " + indexTerms.get(s));
		}
	}
}
