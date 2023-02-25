package lonli;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

public class SpecificResponses {
	
	private static boolean cached = false;
	private static List<String[]> q = new ArrayList<>();
	private static List<String[]> a = new ArrayList<>();
	private static Random rand = new Random();
	
	public static void cache() {
		if (!cached) {
			reload();
			
			Utils.println("Specific responses: " + q.size());
			cached = true;
		}
	}
	
	public static void reload() {
		Utils.println("Loading specific responses...");
		
		clear();
		
		File countFile;
		
		try {
			countFile = new File(Utils.TEMP, "assets/specific_responses_count.dat");
			if (countFile.exists() && !countFile.isDirectory()) countFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Utils.deleteDir(new File(Utils.TEMP, "assets/specific_responses"));
		
		countFile = Utils.loadFileFromAssets("assets/specific_responses_count.dat", Utils.TEMP);
		String line = Utils.getFileLines(countFile)[0];
		
		if (Utils.isInt(line)) {
			int count = Integer.parseInt(line);
			
			if (count <= 0) {
				Utils.println("No specific responses found.");
				return;
			}
			
			for (int i = 0; i < count; i++) {
				File qFile = Utils.loadFileFromAssets("assets/specific_responses/" + i + "_q.txt", Utils.TEMP);
				File aFile = Utils.loadFileFromAssets("assets/specific_responses/" + i + "_a.txt", Utils.TEMP);
				
				String[] questions = Utils.getFileLines(qFile);
				String[] answers = Utils.getFileLines(aFile);
				
				q.add(questions);
				a.add(answers);
				
				int percent = (int) Math.round((i * 100) / (count - 1));
				
				Utils.println("Loading specific responses... (" + percent + "%)");
			}
			
			try {
				countFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Utils.deleteDir(new File(Utils.TEMP, "assets/specific_responses"));
			
			Utils.println("Loaded specific responses...");
			return;
		}
		
		Utils.println("Failed to load any specific responses.");
	}
	
	public static void clear() {
		q.clear();
		a.clear();
	}
	
	public static boolean containsQuestion(String question) {
		for (String[] questions : q) {
			for (String qu : questions) {
				if (qu.equalsIgnoreCase(question)) return true;
			}
		}
		
		return false;
	}
	
	public static boolean containsAnswer(String answer) {
		for (String[] answers : a) {
			for (String an : answers) {
				if (an.equalsIgnoreCase(answer)) return true;
			}
		}
		
		return false;
	}
	
	public static String[] getQuestions(String answer) {
		if (!containsAnswer(answer)) return null;
		
		int i = 0;
		for (String[] answers : a) {
			for (String an : answers) {
				if (an.equalsIgnoreCase(answer)) return q.get(i);
			}
			
			i++;
		}
		
		return null;
	}
	
	public static String[] getAnswers(String question) {
		if (!containsQuestion(question)) return null;
		
		int i = 0;
		for (String[] questions : q) {
			for (String qu : questions) {
				if (qu.equalsIgnoreCase(question)) return a.get(i);
			}
			
			i++;
		}
		
		return null;
	}
	
	public static String getQuestion(String answer) {
		if (!containsAnswer(answer)) return null;
		
		String[] questions = getQuestions(answer);
		
		return questions[rand.nextInt(questions.length)];
	}
	
	public static String getAnswer(String question) {
		if (!containsQuestion(question)) return null;
		
		String[] answers = getAnswers(question);
		
		return answers[rand.nextInt(answers.length)];
	}
	
	public static boolean isCached() {
		return cached;
	}
	
}