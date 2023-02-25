package lonli.lua;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
	
	private File file;
	
	protected Clip clip;
	
	public Sound(File file) {
		this.file = file;
		
		try {
			clip = AudioSystem.getClip();
			
			AudioInputStream in = AudioSystem.getAudioInputStream(this.file);
			
			clip.open(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void play() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void loop(int amount) {
		clip.loop(amount);
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void setMillisecondPosition(int ms) {
		setMicrosecondPosition(ms * 1000);
	}
	
	public void setMicrosecondPosition(int ms) {
		clip.setMicrosecondPosition(ms);
	}
	
	public String getFilePath() {
		return file.getAbsolutePath();
	}
	
	public void flush() {
		clip.drain();
		stop();
	}
	
}