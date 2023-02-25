package lonli.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class LuaValueRegistrar {
	
	private static final Map<String, Object> values = new HashMap<>();
	private static final List<LuaValue> luaValues = new ArrayList<>();
	
	public static void register(String name, Object obj) {
		if (name == null || name.length() <= 0) throw new IllegalArgumentException("Name must be non-null and not empty.");
		if (obj == null) throw new IllegalArgumentException("Object must be non-null.");
		
		Globals globals = JsePlatform.standardGlobals();
		
		globals.set(name, CoerceJavaToLua.coerce(obj));
		
		for (LuaValue lv : luaValues) {
			lv.set(name, CoerceJavaToLua.coerce(obj));
		}
		
		values.put(name, obj);
	}
	
	public static void registerLuaValue(LuaValue lv) {
		for (String name : values.keySet()) {
			lv.set(name, CoerceJavaToLua.coerce(values.get(name)));
		}
		
		luaValues.add(lv);
	}
	
}