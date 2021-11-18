package system;

import java.util.ArrayList;
import java.util.Arrays;

import filters.CharacterRemovalFilter;

public class DecodeList {
	public static ArrayList<String> decode(String listOfTerms) {
		CharacterRemovalFilter removeBrackets = new CharacterRemovalFilter("[\\[\\]]", "");
		listOfTerms = removeBrackets.execute(listOfTerms);
		
		ArrayList<String> decodedTermList;
		decodedTermList = new ArrayList<String>(Arrays.asList(listOfTerms.split("\\W+")));
		return decodedTermList;
	}
}
