package lonli.lua;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class SoundPool {
	
	private static final Map<String, Sound> registeredSounds = new HashMap<>();
	
	public static final int LOOP_CONTINUOUSLY = Clip.LOOP_CONTINUOUSLY;
	
	public static void load(String tagName, String filePath) {
		File file = new File(filePath);
		
		if (!(file.exists() && !file.isDirectory())) {
			System.err.println("The sound file does not exist: " + file.getAbsolutePath());
			return;
		}
		
		if (has(tagName)) {
			System.err.println("The sound already exists: " + tagName);
			return;
		}
		
		registeredSounds.put(tagName, new Sound(file));
	}
	
	public static void play(String tagName) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).play();
	}
	
	public static void loop(String tagName, int amount) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).loop(amount);
	}
	
	public static void stop(String tagName) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).stop();
	}
	
	public static void setMicrosecondPosition(String tagName, int ms) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).setMicrosecondPosition(ms);
	}
	
	public static void setMillisecondPosition(String tagName, int ms) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).setMillisecondPosition(ms);
	}
	
	public static void dump(String tagName) {
		if (!has(tagName)) {
			System.err.println("The sound is not registered: " + tagName);
			return;
		}
		
		registeredSounds.get(tagName).flush();
		registeredSounds.remove(tagName);
	}
	
	public static boolean has(String tagName) {
		for (String key : registeredSounds.keySet()) {
			if (key.equals(tagName)) return true;
		}
		
		return false;
	}
	
	public static boolean hasPath(String filePath) {
		File file = new File(filePath);
		
		if (!(file.exists() && !file.isDirectory())) return false;
		
		for (Sound sound : registeredSounds.values()) {
			if (sound.getFilePath().equals(file.getAbsolutePath())) return true;
		}
		
		return false;
	}
	
}