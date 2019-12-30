package sentiment_analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class sentiment_analysis {
	public ArrayList<String> records = new ArrayList<>();
	public ArrayList<String> cleaned_records = new ArrayList<>();
	public ArrayList<String> negative_words = new ArrayList<>();
	public ArrayList<String> positive_words = new ArrayList<>();

	public void read_file() throws IOException {

		// https://www.baeldung.com/java-csv-file-array
		try (BufferedReader br = new BufferedReader(new FileReader("twitter.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				records.add(line);

			}
		}
	}

	public void read_positive_negative_words() {
		try (BufferedReader br = new BufferedReader(new FileReader("negative-words.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith(";")) {
					negative_words.add(line);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader("positive-words.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith(";")) {
					positive_words.add(line);
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.print(positive_words);
		// System.out.print(negative_words);
	}

	public void clean_tweets() {
		for (int i = 0; i < records.size(); i++) {
			String cleaned_string = records.get(i).replaceAll("[^a-zA-Z0-9\\s]+", "");
			cleaned_string = cleaned_string.replaceAll("RT", "");
			cleaned_records.add(cleaned_string);
		}

	}

	// https://codingbat.com/doc/java-map-wordcount.html
	public void wordCount() {

		for (int i = 0; i < cleaned_records.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			String[] words = cleaned_records.get(i).split(" ");
			for (int j = 0; j < words.length; j++) {
				if (!words[j].equals(" ")) {
					if (!map.containsKey(words[j])) { // first time we've seen this string
						map.put(words[j], 1);
					} else {
						int count = map.get(words[j]);
						map.put(words[j], count + 1);
					}
				}
			}

			// https://www.geeksforgeeks.org/traverse-through-a-hashmap-in-java/
			Iterator hmIterator = map.entrySet().iterator();
			int neg_count = 0;
			int pos_count = 0;
			String neg_word = "";
			String pos_word = "";
			String Polarity = "";
			while (hmIterator.hasNext()) {

				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				ArrayList<String> positive_wrd = new ArrayList<>();
				ArrayList<Integer> positive_cnt = new ArrayList<>();

				if (negative_words.contains(mapElement.getKey())) {
					neg_word = neg_word + (String) mapElement.getKey() + ",";
					int count = (int) mapElement.getValue();
					neg_count = neg_count + count;
				}

				else if (positive_words.contains(mapElement.getKey())) {
					pos_word = pos_word + (String) mapElement.getKey() + ",";;
					int count = (int) mapElement.getValue();
					pos_count = neg_count + count;
				}
				else
				{
					pos_count = 0;
					neg_count=0;
				}
			}
			String match = "";

			if (neg_count > pos_count) {
				Polarity = "negative";
				match = neg_word;
			} else if (pos_count > neg_count) {
				Polarity = "positive";
				match = pos_word;
			} else if(pos_count == neg_count) {
				Polarity = "neutral";
				match = "no match";
			}
			System.out.println(i + " " + cleaned_records.get(i) + " " + "match: " + match + " Polarity: " + Polarity);
		}
	}

	public static void main(String[] args) throws IOException {

		sentiment_analysis sa = new sentiment_analysis();
		sa.read_file();
		sa.read_positive_negative_words();
		sa.clean_tweets();
		sa.read_positive_negative_words();
		sa.wordCount();

	}

}
