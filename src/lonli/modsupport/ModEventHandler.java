package lonli.modsupport;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class ModEventHandler {
	
	private final Map<String, ModEvent> events = new HashMap<>();
	
	private Mod mod;
	
	public ModEventHandler(Mod mod) {
		this.mod = mod;
	}
	
	public void enable(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			if (existsAndDir(dir, "onClose")) events.put("onClose", new ModEvent(this.mod, "onClose", new File(dir, "onClose")));
			if (existsAndDir(dir, "onFocus")) events.put("onFocus", new ModEvent(this.mod, "onFocus", new File(dir, "onFocus")));
			if (existsAndDir(dir, "onInput")) events.put("onInput", new ModEvent(this.mod, "onInput", new File(dir, "onInput")));
			if (existsAndDir(dir, "onLoad")) events.put("onLoad",  new ModEvent(this.mod, "onLoad", new File(dir, "onLoad")));
			if (existsAndDir(dir, "onLostFocus")) events.put("onLostFocus", new ModEvent(this.mod, "onLostFocus", new File(dir, "onLostFocus")));
			if (existsAndDir(dir, "onMinimizeWindow")) events.put("onMinimizeWindow", new ModEvent(this.mod, "onMinimizeWindow", new File(dir, "onMinimizeWindow")));
			if (existsAndDir(dir, "onUnminimizeWindow")) events.put("onUnminimizeWindow", new ModEvent(this.mod, "onUnminimizeWindow", new File(dir, "onUnminimizeWindow")));
			if (existsAndDir(dir, "onUpdate")) events.put("onUpdate", new ModEvent(this.mod, "onUpdate", new File(dir, "onUpdate")));
		}
	}
	
	private boolean existsAndDir(File dir, String filePath) {
		File file = new File(dir, filePath);
		
		return (file.exists() && file.isDirectory());
	}
	
	public void trigger(String eventName) {
		if (!has(eventName)) {
			System.err.println("[" + mod.getName() + "]: The event does not exist: " + eventName);
			return;
		}
		
		events.get(eventName).trigger();
	}
	
	public boolean has(String eventName) {
		for (String key : events.keySet()) {
			if (key.equals(eventName)) return true;
		}
		
		return false;
	}
	
}