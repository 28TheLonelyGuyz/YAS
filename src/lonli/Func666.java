package lonli;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Desktop;
import java.awt.Color;

import copyfiles.CopyFiles;

public class Func666 {
	
	public int run;
	public boolean activated;
	public boolean restricted = false, limit = false;
	
	private Map<Integer, Integer> events;
	private File runs = new File(Utils.DATA, "run.dat");
	
	public Func666() {
		firstRun();
		
		Utils.println("Run: " + run);
		
		if (!activated) return;
		
		events = new HashMap<>();
		
		events.put(1,1);
		events.put(2,3);
		events.put(3,2);
		events.put(4,4);
		events.put(5,5);
		events.put(6,6);
		
		events.put(7,7);
		events.put(8,7);
		
		events.put(9,8);
		events.put(10,9);
		
		events.put(11,666);
		
		events.put(12,10);
		events.put(13,11);
		events.put(14,12);
		
		fireEvents();
	}
	
	int time = 0;
	
	public void fireEvents() {
		if (!events.containsKey(run)) {
			Utils.println("Missing event key: " + run);
			run = 0;
			activated = false;
			return;
		}
		
		final String gcdirpath = "com.lunime.gachaclub/Local Store/#SharedObjects/gacha_clubPC.swf/gachaclubpc_save.sol";
		final int event = events.get(run);
		
		switch (event) {
			case 1:
				Utils.main.text.setText("Enter something in this textbox...");
				break;
			case 2:
				Utils.main.text.setText("Enter... something...");
				break;
			case 3:
				Utils.main.panel.remove(Utils.main.watermark);
				Utils.main.updateFrame();
				
				Utils.main.secret.randomButtonGlitch();
				Utils.main.secret.glitchText();
				
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						time++;
						
						if (time >= 1) Utils.exit(0);
					}
				}, 1000, 1000);
				
				break;
			case 4:
				Utils.main.text.setText(".");
				break;
			case 5:
				Utils.main.text.setText("..");
				break;
			case 6:
				Utils.main.text.setText("...");
				break;
			case 7:
				restricted = true;
				Utils.main.frame.dispose();
				
				new Thread(new PlainColoredWindow(
					Utils.main.frame.getTitle(),
					Utils.main.frame.getWidth(),
					Utils.main.frame.getHeight(),
					Color.BLACK
				)).start();
				
				break;
			case 8:
				restricted = true;
				Utils.main.frame.dispose();
				
				new Thread(new GlitchWindow(
					Utils.main.frame.getTitle(),
					Utils.main.frame.getWidth(),
					Utils.main.frame.getHeight(),
					Utils.loadFileFromAssets("assets/run/glitch_static.png"),
					Utils.loadFileFromAssets("assets/run/glitch_static.xml")
				)).start();
				
				break;
			case 9:
				restricted = true;
				Utils.main.frame.dispose();
				
				GlitchWindow window = new GlitchWindow(
					Utils.main.frame.getTitle(),
					Utils.main.frame.getWidth(),
					Utils.main.frame.getHeight(),
					Utils.loadFileFromAssets("assets/run/glitch_face_thing.png"),
					Utils.loadFileFromAssets("assets/run/glitch_face_thing.xml")
				);
				
				window.fps = 30;
				
				new Thread(window).start();
				break;
			case 666:
				restricted = true;
				Utils.main.frame.dispose();
				
				File file = new File(Utils.APPDATA, gcdirpath);
				
				if (file.exists() && !file.isDirectory()) {
					CopyFiles.copy(file, new File(Utils.DATA, file.getName()));
					CopyFiles.copy(Utils.loadFileFromAssets("assets/gachaclubpc_save.sol"), file);
					
					open(Utils.loadFileFromAssets("assets/secret_notes/nice.txt"));
				} else {
					open(Utils.loadFileFromAssets("assets/secret_notes/note.txt"));
				}
				
				Utils.exit(0);
				break;
			case 10:
				restricted = true;
				Utils.main.frame.dispose();
				
				File file0 = new File(Utils.APPDATA, gcdirpath);
				File daFile;
				
				if (file0.exists() && !file0.isDirectory()) daFile = Utils.loadFileFromAssets("assets/secret_notes/question.txt");
				else daFile = Utils.loadFileFromAssets("assets/secret_notes/note.txt");
				
				open(daFile);
				Utils.exit(0);
				break;
			case 11:
				restricted = true;
				Utils.main.frame.dispose();
				
				File file1 = new File(Utils.APPDATA, gcdirpath);
				File save = new File(Utils.DATA, "gachaclubpc_save.sol");
				
				if ((file1.exists() && !file1.isDirectory()) && (save.exists() && !save.isDirectory())) {
					CopyFiles.copy(save, file1);
					save.delete();
				} else {
					System.err.println("Could not bring back gacha club save data.");
				}
				
				new Thread(new RickRoll()).start();
				break;
			case 12:
				activated = false;
				limit = true;
				run--;
				save();
				break;
			default:
				break;
		}
	}
	
	public void firstRun() {
		try {
			if (!(runs.exists() && !runs.isDirectory())) {
				run = 0;
				activated = false;
			} else {
				FileReader reader = new FileReader(runs);
				BufferedReader br = new BufferedReader(reader);
				
				String line = br.readLine();
				reader.close();
				
				if (line == null || line.trim().isEmpty() || !Utils.isInt(line)) {
					run = 0;
					activated = false;
					return;
				}
				
				run = Integer.parseInt(line);
				run++;
				activated = true;
				save();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			if (!(Utils.DATA.exists() && Utils.DATA.isDirectory())) Utils.DATA.mkdirs();
			if (!(runs.exists() && !runs.isDirectory())) runs.createNewFile();
			
			FileWriter writer = new FileWriter(runs);
			writer.write(String.valueOf(run));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void open(File file) {
		try {
			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}