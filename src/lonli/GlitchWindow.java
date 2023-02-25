package lonli;

import javax.swing.JFrame;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.io.File;

import lonliengine.Frames;

public class GlitchWindow extends JFrame implements Runnable {
	
	private List<BufferedImage> frames;
	private int time = 0, pos = 0;
	public int fps = 60;
	
	public GlitchWindow(String title, int width, int height, File picFile, File xmlFile) {
		super(title);
		
		setSize(width, height);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		
		frames = Frames.sparrow("glitch", xmlFile, picFile);
		
		if (frames == null || frames.size() <= 0) Utils.exit(-1);
		
		setVisible(true);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				time++;
				
				if (time >= 3) {
					timer.cancel();
					Utils.exit(0);
				}
			}
		}, 1000, 1000);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				if (pos < 0 || pos >= frames.size()) pos = 0;
				
				BufferedImage frame = frames.get(pos);
				
				getGraphics().drawImage(frame, 0, 0, frame.getWidth(null), frame.getHeight(null), null);
				
				pos++;
				
				Thread.sleep(1000 / fps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}