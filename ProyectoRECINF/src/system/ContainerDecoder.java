package system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import filters.CharacterRemovalFilter;

public class ContainerDecoder {
	public static ArrayList<String> decodeList(String listOfTerms) {
		CharacterRemovalFilter removeBrackets = new CharacterRemovalFilter("[\\[\\]]", "");
		listOfTerms = removeBrackets.execute(listOfTerms);
		
		ArrayList<String> decodedTermList;
		decodedTermList = new ArrayList<String>(Arrays.asList(listOfTerms.split("\\W+")));
		return decodedTermList;
	}
	
	public static HashMap<String, Double> decodeMap(String listOfDocuments) {
		CharacterRemovalFilter removeBrackets = new CharacterRemovalFilter("[\\[\\]\\{\\}\\s]", "");
		listOfDocuments = removeBrackets.execute(listOfDocuments);
		
		ArrayList<String> decodedDocumentList;
		decodedDocumentList = new ArrayList<String>(Arrays.asList(listOfDocuments.split("\\,")));
		
		HashMap<String, Double> documentTFs = new HashMap<String, Double>();
		for(String document : decodedDocumentList) {
			String[] pair = document.split(":");
			documentTFs.put(pair[0], Double.parseDouble(pair[1]));
		}
		
		return documentTFs;
	}
}
