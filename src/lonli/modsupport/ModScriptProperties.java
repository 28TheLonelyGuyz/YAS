package lonli.modsupport;

import java.io.File;

public class ModScriptProperties {
	
	private Mod parent;
	private File script;
	private String modPath;
	private String modName;
	private String modAuthor;
	private String scriptFileName;
	private String scriptFilePath;
	
	public ModScriptProperties(File script, Mod parent) {
		this.script = script;
		this.parent = parent;
		
		this.modPath = this.parent.getDirectory().getAbsolutePath();
		this.modName = this.parent.getName();
		this.modAuthor = this.parent.getAuthor();
		this.scriptFileName = this.script.getName();
		this.scriptFilePath = this.script.getAbsolutePath();
	}
	
	public String modPath() {
		return modPath;
	}
	
	public String modName() {
		return modName;
	}
	
	public String modAuthor() {
		return modAuthor;
	}
	
	public String scriptFileName() {
		return scriptFileName;
	}
	
	public String scriptFilePath() {
		return scriptFilePath;
	}
	
}