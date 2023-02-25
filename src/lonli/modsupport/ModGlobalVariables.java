package lonli.modsupport;

import java.util.Map;
import java.util.HashMap;

public class ModGlobalVariables {
	
	private final Map<String, Object> variables = new HashMap<>();
	private Mod mod;
	
	public ModGlobalVariables(Mod mod) {
		this.mod = mod;
	}
	
	public void set(String name, Object obj) {
		if (!has(name)) variables.put(name, obj);
		else variables.replace(name, obj);
	}
	
	public Object get(String name) {
		if (!has(name)) {
			System.err.println("[" + mod.getName() + "]: The variable does not exist: " + name);
			return null;
		}
		
		return variables.get(name);
	}
	
	public void remove(String name) {
		if (has(name)) variables.remove(name);
	}
	
	public boolean has(String name) {
		return variables.containsKey(name);
	}
	
	public boolean hasValue(Object value) {
		return variables.containsValue(value);
	}
	
}