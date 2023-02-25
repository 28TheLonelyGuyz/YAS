package lonli;

import javax.swing.JFrame;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class PlainColoredWindow extends JFrame implements Runnable {
	
	int time = 0;
	Color color;
	
	public PlainColoredWindow(String title, int width, int height, Color color) {
		super(title);
		
		this.color = color;
		
		setSize(width, height);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
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
				getGraphics().setColor(color);
				getGraphics().fillRect(0, 0, getWidth(), getHeight());
				
				Thread.sleep(1000 / 60);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.exit(-1);
		}
	}
	
}