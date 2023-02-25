package lonli.lua;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import lonli.Utils;

public class Utilities {
	
	private static final UUID splitterUUID = UUID.randomUUID();
	
	public static boolean randomBool() {
		return randomInt(0,1) > 0;
	}
	
	public static int randomInt(int min, int max) {
		if (min > max) return randomInt(max, min);
		
		return Utils.main.random.nextInt((max - min) + 1) + min;
	}
	
	public static String time() {
		return "[" + new SimpleDateFormat("kk:mm:ss").format(new Date()) + "]";
	}
	
	public static UUID getSplitterUUID() {
		return splitterUUID;
	}
	
	public static String getSplitterUUIDInString() {
		return splitterUUID.toString();
	}
	
	public static String getSplitter() {
		return "splitter-" + getSplitterUUIDInString();
	}
	
	public static String[] split(String str, String splitter) {
		return str.split(splitter);
	}
	
}