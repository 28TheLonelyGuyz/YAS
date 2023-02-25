package lonli.modsupport;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class ModReader {
	
	private static List<Mod> mods = new ArrayList<>();
	
	public static void readDir(String dirPath) {
		File dir = new File(dirPath);
		
		if (!(dir.exists() && dir.isDirectory())) return;
		
		for (File modf : dir.listFiles()) {
			if (modf.isDirectory()) {
				File mod = new File(modf, "mod.json");
				
				if (mod.exists() && !mod.isDirectory()) mods.add(new Mod(modf));
			}
		}
	}
	
	public static void textEntered(String text) {
		for (Mod mod : mods) {
			mod.textEntered(text);
		}
	}
	
	public static void triggerModEvent(String eventName) {
		for (Mod mod : mods) {
			mod.getEventHandler().trigger(eventName);
		}
	}
	
	protected static void removeMod(Mod mod) {
		mods.remove(mod);
	}
	
	public static Mod[] getMods() {
		return mods.toArray(new Mod[0]);
	}
	
	public static int getAmount() {
		return mods.size();
	}
	
}