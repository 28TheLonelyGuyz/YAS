package lonli.modsupport;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import lonli.lua.LuaValueRegistrar;

public class ModEvent {
	
	private final List<LuaValue> scripts = new ArrayList<>();
	
	private Mod mod;
	private File directory;
	private String eventName;
	
	public ModEvent(Mod mod, String eventName, File directory) {
		this.mod = mod;
		this.eventName = eventName;
		this.directory = directory;
		
		read(this.directory);
	}
	
	private void read(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			for (File luaScript : dir.listFiles()) {
				if (luaScript.isDirectory()) read(luaScript);
				if (!luaScript.isDirectory() && luaScript.getName().endsWith(".lua")) {
					LuaValue g = JsePlatform.standardGlobals();
					g.get("dofile").call( LuaValue.valueOf( luaScript.getAbsolutePath() ) );
					
					LuaValueRegistrar.registerLuaValue(g);
					
					ModScriptProperties properties = new ModScriptProperties(luaScript, mod);
					
					g.set("info", CoerceJavaToLua.coerce(properties));
					g.set("properties", CoerceJavaToLua.coerce(properties));
					
					g.set("gv", CoerceJavaToLua.coerce(this.mod.getGlobalVariables()));
					g.set("globalVariables", CoerceJavaToLua.coerce(this.mod.getGlobalVariables()));
					
					scripts.add(g);
				}
			}
		}
	}
	
	public void trigger() {
		for (LuaValue script : scripts) {
			LuaValue onTrigger = script.get("onTrigger");
			
			onTrigger.call();
		}
	}
	
}