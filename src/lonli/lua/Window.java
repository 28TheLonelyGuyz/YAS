package lonli.lua;

import java.awt.Point;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.util.TimerTask;
import java.util.Timer;
import java.io.File;

import lonli.Utils;

public class Window {
	
	private Timer windowShake = new Timer();
	private Timer windowShakeFX = new Timer();
	private boolean windowShakeX = true;
	private boolean windowShakeY = true;
	private int windowShakeMS = 1000;
	
	public void shake(int strength, float durationSeconds) {
		if (Utils.main.windowShaking) {
			windowShake.cancel();
			windowShakeFX.cancel();
		}
		
		Utils.main.windowShaking = true;
		windowShakeMS = 1000;
		
		final int ms = (int) Math.round(durationSeconds * 1000f);
		
		if (ms <= 0) return;
		
		windowShake = new Timer();
		windowShake.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (windowShakeMS >= ms) {
					windowShakeFX.cancel();
					windowShake.cancel();
					Utils.main.windowShaking = false;
					Utils.main.frame.setLocation(Utils.main.location);
				}
				
				windowShakeMS++;
			}
		}, 1, 1);
		
		windowShakeFX = new Timer();
		windowShakeFX.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				windowShakeX = Utilities.randomBool();
				windowShakeY = !windowShakeY;
				
				int px, py;
				int ts = strength;
				
				if (!windowShakeX) ts = -ts;
				px = Utils.main.location.x + ts;
				
				ts = strength;
				
				if (!windowShakeY) ts = -ts;
				py = Utils.main.location.y + ts;
				
				Utils.main.frame.setLocation(new Point(px, py));
			}
		}, 10, 10);
	}
	
	public void setIcon(String imagePath) {
		File file = new File(imagePath);
		
		if (file.exists() && !file.isDirectory()) {
			try {
				Utils.main.frame.setIconImage(ImageIO.read(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isShaking() {
		return Utils.main.windowShaking;
	}
	
}