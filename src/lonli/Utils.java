package lonli;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;
import java.awt.Desktop;
import java.text.SimpleDateFormat;

import lonli.modsupport.ModReader;

public class Utils {
	
	public static String PACKED_ASSETS = "assistant.pak";
	
	public static final File APPDATA = new File(System.getenv("APPDATA"));
	public static final File DATA = new File(APPDATA, "LonliHH/YourAnnoyingAssistant");
	public static final File TEMP = new File(DATA, "temp");
	public static final File ERRORS = new File(DATA, "errors");
	
	public static Main main;
	
	public static File loadFileFromAssets(String fileName, File destDir) {
		try {
			if (!(destDir.exists() && destDir.isDirectory())) destDir.mkdirs();
			else {
				File file = new File(destDir, fileName);
				
				if (file.exists()) return file;
			}
			
			FileInputStream fis = new FileInputStream(PACKED_ASSETS);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry entry = zis.getNextEntry();
			
			byte[] buffer = new byte[1024];
			
			while (entry != null) {
				if (entry.getName().equals(fileName)) {
					File newFile = new File(destDir, entry.getName());
					
					if (entry.isDirectory()) {
						if (!newFile.isDirectory() && !newFile.mkdirs()) throw new IOException("Failed to create directory " + newFile);
					} else {
						// fix for Windows-created archives
						File parent = newFile.getParentFile();
						if (!parent.isDirectory() && !parent.mkdirs()) throw new IOException("Failed to create directory " + parent);
						
						// write file content
						FileOutputStream fos = new FileOutputStream(newFile);
						
						int length;
						while ((length = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
						
						fos.close();
						
						return newFile;
					}
				}
				
				entry = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static File loadFileFromAssets(String fileName) {
		return loadFileFromAssets(fileName, TEMP);
	}
	
	public static void extractAllFromAssets(File destDir) {
		try {
			if (!(destDir.exists() && destDir.isDirectory())) destDir.mkdirs();
			
			FileInputStream fis = new FileInputStream(PACKED_ASSETS);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry entry = zis.getNextEntry();
			
			byte[] buffer = new byte[1024];
			
			while (entry != null) {
				File newFile = new File(destDir, entry.getName());
				
				if (entry.isDirectory()) {
					if (!newFile.isDirectory() && !newFile.mkdirs()) throw new IOException("Failed to create directory " + newFile);
				} else {
					// fix for Windows-created archives
					File parent = newFile.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) throw new IOException("Failed to create directory " + parent);
					
					// write file content
					FileOutputStream fos = new FileOutputStream(newFile);
					
					int length;
					while ((length = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, length);
					}
					
					fos.close();
				}
				
				entry = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileRandomString(File file) {
		if (!(file.exists() && !file.isDirectory())) return null;
		
		String[] lines = getFileLines(file);
		return lines[main.random.nextInt(lines.length)];
	}
	
	public static String getAssetRandomString(String asset) {
		return getFileRandomString(loadFileFromAssets(asset, TEMP));
	}
	
	public static String[] getFileLines(File file) {
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			List<String> lines = new ArrayList<>();
			
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			
			br.close();
			reader.close();
			
			return lines.toArray(new String[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void deleteDir(File dir) {
		if (!(dir.exists() && dir.isDirectory())) return;
		
		for (File idk : dir.listFiles()) {
			if (idk.isDirectory()) deleteDir(idk);
			else idk.delete();
		}
		
		dir.delete();
	}
	
	public static void checkPak() {
		try {
			if (!(ERRORS.exists() && ERRORS.isDirectory())) ERRORS.mkdirs();
			if (!(DATA.exists() && DATA.isDirectory())) DATA.mkdirs();
			File rscPathFile = new File(DATA, "resourcesPath.dat");
			
			if (rscPathFile.exists() && !rscPathFile.isDirectory()) {
				String[] lines = getFileLines(rscPathFile);
				File rsc = new File(lines[0]);
				
				if (rsc.exists() && !rsc.isDirectory()) PACKED_ASSETS = lines[0];
				
				if (lines.length >= 2) {
					if (isInt(lines[1])) {
						int bool = Integer.parseInt(lines[1]);
						
						if (bool > 0) deleteDir(new File(DATA, "cached/assets/icons"));
						
						switch (bool) {
							case 1:
								main.text.setText("Resources Path changed.");
								break;
							case 2:
								main.text.setText("Resources Path reset.");
								break;
							default:
								break;
						}
					}
				}
			}
			
			FileWriter w = new FileWriter(rscPathFile);
			
			w.write(PACKED_ASSETS + System.getProperty("line.separator") + "0");
			w.flush();
			w.close();
			
			File pak = new File(PACKED_ASSETS);
			
			if (!(pak.exists() && !pak.isDirectory())) {
				File file = new File(ERRORS, "bruh.txt");
				if (!(file.exists() && file.isDirectory())) file.createNewFile();
				
				FileWriter writer = new FileWriter(file);
				writer.write(
					"Program can't run because \"" + pak.getName() + "\" is missing." +
					System.getProperty("line.separator") +
					System.getProperty("line.separator") +
					":innocent:"
				);
				writer.flush();
				writer.close();
				
				Desktop.getDesktop().open(file);
				exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
			exit(-1);
		}
	}
	
	public static void exit(int code) {
		ModReader.triggerModEvent("onClose");
		deleteDir(TEMP);
		System.exit(code);
	}
	
	public static int center(int arg0, int arg1) {
		return (arg0 / 2) - (arg1 / 2);
	}
	
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void println(Object o) {
		System.out.println("[" + (new SimpleDateFormat("kk:mm:ss").format(new Date())) + "]: " + String.valueOf(o));
	}
	
}