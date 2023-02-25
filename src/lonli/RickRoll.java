package lonli;

import javax.swing.JFrame;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Desktop;
import java.io.File;

import lonliengine.Frames;

public class RickRoll extends JFrame implements Runnable {
	
	private List<BufferedImage> frames;
	private File[] files = new File[2];
	private boolean triggered = false, able = false;
	private int time = 0, pos = 0;
	
	public RickRoll() {
		super(Utils.main.frame.getTitle());
		
		setSize(Utils.main.frame.getWidth(), Utils.main.frame.getHeight());
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		
		frames = Frames.sparrow(
			"lmao",
			Utils.loadFileFromAssets("assets/run/rickroll.xml", Utils.TEMP),
			Utils.loadFileFromAssets("assets/run/rickroll.png", Utils.TEMP)
		);
		
		if (frames == null || frames.size() <= 0) Utils.exit(-1);
		
		files[0] = Utils.loadFileFromAssets("assets/secret_notes/lol.txt");
		
		File gsave = new File(Utils.APPDATA, "com.lunime.gachaclub/Local Store/#SharedObjects/gacha_clubPC.swf/gachaclubpc_save.sol");
		
		if (gsave.exists() && !gsave.isDirectory()) files[1] = Utils.loadFileFromAssets("assets/secret_notes/pls note.txt");
		else files[1] = Utils.loadFileFromAssets("assets/secret_notes/byee.txt");
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (able) Utils.exit(0);
				if (triggered) return;
				
				triggered = true;
				
				try {
					Desktop.getDesktop().open(files[0]);
					
					Timer timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							time++;
							
							if (time == 4) {
								timer.cancel();
								
								try {
									Desktop.getDesktop().open(files[1]);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								
								able = true;
							}
						}
					}, 1000, 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		setVisible(true);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				if (pos < 0 || pos >= frames.size()) pos = 0;
				
				getGraphics().drawImage(frames.get(pos), 0, 0, getWidth(), getHeight(), null);
				
				pos++;
				
				Thread.sleep(1000 / 15);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}