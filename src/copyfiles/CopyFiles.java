package copyfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFiles {
	
	public static void copy(File inputFile, File outputFile) {
		if (!inputFile.isDirectory()) copyFile(inputFile, outputFile);
		else copyDirectory(inputFile, outputFile);
	}
	
	public static void copyFile(File src, File dest) {
		try {
			mkParent(dest);
			
			InputStream is = new FileInputStream(src);
			OutputStream os = new FileOutputStream(dest);
			
			byte[] buffer = new byte[1024];
			
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// https://www.baeldung.com/java-copy-directory
	
	public static void copyDirectory(File srcDir, File destDir) {
		try {
			if (!destDir.exists()) {
				mkParent(destDir);
				destDir.mkdirs();
			}
			
			for (String f : srcDir.list()) {
				copy(new File(srcDir, f), new File(destDir, f));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void mkParent(File file) {
		File parent = null;
		
		if (file.getParent() == null) {
			String ap = file.getAbsolutePath();
			String daPath = ap.substring(0, ap.lastIndexOf(File.separator));
			
			parent = new File(daPath);
		} else {
			parent = file.getParentFile();
		}
		
		try {
			parent.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}