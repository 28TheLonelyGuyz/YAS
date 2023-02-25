package lonli;

import javax.swing.JLabel;
import java.io.IOException;
import java.io.File;
import java.awt.Desktop;
import java.util.Timer;
import java.util.TimerTask;

public class Secret {
	
	int seconds = 0;
	JLabel text;
	
	public Secret(JLabel text) {
		this.text = text;
	}
	
	public boolean check(String input) {
		switch (input) {
			// Rickroll
			case "func_69420_":
				text.setText(":)))");
				rickroll();
				return true;
			case "func_666_":
				secretFunc();
				return true;
			// Secret notes
			case "secretNote1":
				sn(1);
				return true;
			case "secretNote2":
				sn(2);
				return true;
			case "secretNote3":
				sn(3);
				return true;
			case "secretNote4":
				sn(4);
				return true;
			default:
				return false;
		}
	}
	
	void rickroll() {
		
	}
	
	void sn(int suffix) {
		try {
			Desktop.getDesktop().open(Utils.loadFileFromAssets("assets/secret_notes/Secret Note " + String.valueOf(suffix) + ".txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	int glitch = 0, delayedExit = 0;
	
	void secretFunc() {
		if (Utils.main.run.limit) {
			Utils.main.text.setText("No. I'm too tired to do that rn.");
			return;
		}
		
		Utils.main.button.removeActionListener(Utils.main);
		Utils.main.text.setText("...");
		randomButtonGlitch();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				glitch++;
				
				if (glitch == 3) Utils.main.text.setText("WHAT HAVE YOU DONE");
				if (glitch == 5) Utils.main.text.setText("STOP");
				if (glitch == 7) {
					timer.cancel();
					
					glitchText();
					glitch = 0;
					
					new Timer().scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							glitch++;
							
							if (glitch >= 1) {
								Utils.main.run.run = 0;
								Utils.main.run.save();
								Utils.exit(0);
							}
						}
					}, 1000, 1000);
				}
			}
		}, 1000, 1000);
	}
	
	void glitchText() {
		String[] answers = new String[] {
			"âÌ#37ì:šËé1",
			"~0‘Ê±HKt$‚A±",
			"x­{|†Us©ð0×",
			"/HÌ°}z#Æ»o"
		};
		
		String[] chars = new String[] {
			"â","Ì","#","3","7","ì",":","š","Ë","é","1",
			"~","0","‘","Ê","±","H","K","t","$","‚","A","±",
			"x","­","{","|","†","U","s","©","ð","0","×",
			"/","H","Ì","°","}","z","#","Æ","»","o"
		};
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Utils.main.text.setText(answers[Utils.main.random.nextInt(answers.length)]);
			}
		}, 1, 1);
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Utils.main.textbox.setText(
					Utils.main.textbox.getText() + chars[Utils.main.random.nextInt(chars.length)]
				);
			}
		}, 30, 30);
	}
	
	void randomButtonGlitch() {
		String[] answers = new String[] {
			"âÌ#37ì:šËé1",
			"~0‘Ê±HKt$‚A±",
			"x­{|†Us©ð0×",
			"/HÌ°}z#Æ»o"
		};
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Utils.main.button.setText(answers[Utils.main.random.nextInt(answers.length)]);
			}
		}, 1, 1);
	}
	
}