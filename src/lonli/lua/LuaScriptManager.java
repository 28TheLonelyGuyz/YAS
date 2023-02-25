package lonli.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;

public class LuaScriptManager {
	
	public static LuaValue getValue(String luaScriptPath, String valueName) {
		LuaValue chunk = getScript(luaScriptPath);
		chunk.call();
		
		return chunk.get(valueName);
	}
	
	public static LuaValue getScript(String luaScriptPath) {
		File script = new File(luaScriptPath);
		
		if (!(script.exists() && !script.isDirectory())) {
			System.err.println("Lua script '" + luaScriptPath + "' does not exist or is invalid.");
			return null;
		}
		
		Globals globals = JsePlatform.standardGlobals();
		
		return globals.loadfile(script.getAbsolutePath());
	}
	
}