package lonli.modsupport;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import lonli.lua.LuaValueRegistrar;
import lonli.lua.Utilities;
import lonli.Utils;

public class Mod {
	
	private JSONParser parser = new JSONParser();
	private List<ModCommand> commands = new ArrayList<>();
	
	private String name;
	private String author;
	private File directory;
	private ModGlobalVariables globalVariables;
	private ModEventHandler events;
	
	public Mod(File directory) {
		this.directory = directory;
		
		File infoFile = new File(this.directory, "mod.json");
		
		if (infoFile.exists() && !infoFile.isDirectory()) {
			try {
				FileReader reader = new FileReader(infoFile);
				JSONObject info = (JSONObject) parser.parse(reader);
				
				this.name = (String) info.get("name");
				this.author = (String) info.get("author");
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Mod directory '" + this.directory.getAbsolutePath() + "' is invalid.");
			ModReader.removeMod(this);
			return;
		}
		
		this.globalVariables = new ModGlobalVariables(this);
		this.events = new ModEventHandler(this);
		
		readCommands(new File(this.directory, "commands"));
		readEvents(new File(this.directory, "events"));
		
		Utils.println("Mod loaded '" + name + "' by " + author);
		
		events.trigger("onLoad");
	}
	
	private void readCommands(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			for (File luaScript : dir.listFiles()) {
				if (luaScript.isDirectory()) readCommands(luaScript);
				if (!luaScript.isDirectory() && luaScript.getName().endsWith(".lua")) commands.add(new ModCommand(luaScript, this));
			}
		}
	}
	
	private void readEvents(File dir) {
		if (dir.exists() && dir.isDirectory()) events.enable(dir);
	}
	
	public void textEntered(String text) {
		for (ModCommand cmd : commands) {
			if (text.startsWith(cmd.getPrefix() + " ")) {
				String txt = text.substring(cmd.getPrefix().length() + 1, text.length());
				
				if (!txt.isEmpty()) {
					String[] args = txt.split(" ");
					
					for (String s : args) {
						Utils.println(s);
					}
					
					if (args.length == 1) {
						cmd.trigger(new String[] {});
						continue;
					}
					
					
					
					cmd.trigger(args);
				}
			} else if (text.startsWith(cmd.getPrefix())) {
				cmd.trigger(new String[] {});
				System.out.println("test");
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public File getDirectory() {
		return directory;
	}
	
	protected ModGlobalVariables getGlobalVariables() {
		return globalVariables;
	}
	
	protected ModEventHandler getEventHandler() {
		return events;
	}
	
	protected static class ModCommand {
		
		private File script;
		private LuaValue onTrigger;
		private String prefix;
		private String name;
		private Mod parent;
		
		public ModCommand(File script, Mod parent) {
			this.script = script;
			this.parent = parent;
			
			LuaValue g = JsePlatform.standardGlobals();
			g.get("dofile").call( LuaValue.valueOf( script.getAbsolutePath() ) );
			
			LuaValueRegistrar.registerLuaValue(g);
			
			ModScriptProperties properties = new ModScriptProperties(script, parent);
			
			g.set("info", CoerceJavaToLua.coerce(properties));
			g.set("properties", CoerceJavaToLua.coerce(properties));
			
			g.set("gv", CoerceJavaToLua.coerce(this.parent.getGlobalVariables()));
			g.set("globalVariables", CoerceJavaToLua.coerce(this.parent.getGlobalVariables()));
			
			LuaValue getName = g.get("getName");
			LuaValue getPrefix = g.get("getPrefix");
			
			this.onTrigger = g.get("onTrigger");
			
			this.prefix = getPrefix.call().tojstring(1);
			this.name = getName.call().tojstring(1);
		}
		
		public void trigger(String[] args) {
			String s = "";
			
			boolean b = false;
			for (String arg : args) {
				if (b) s += Utilities.getSplitter();
				
				s += arg;
				b = true;
			}
			
			onTrigger.call(LuaValue.valueOf(s));
		}
		
		public String getPrefix() {
			return prefix;
		}
		
		public String getName() {
			return name;
		}
		
		public String getFileName() {
			return script.getName();
		}
		
		public String getFilePath() {
			return script.getAbsolutePath();
		}
		
		protected Mod getParent() {
			return parent;
		}
		
	}
	
}