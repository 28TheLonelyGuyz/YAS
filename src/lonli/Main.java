package lonli;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.File;

import lonli.lua.Window;
import lonli.lua.Utilities;
import lonli.lua.YAS;
import lonli.lua.SoundPool;
import lonli.lua.LuaValueRegistrar;
import lonli.modsupport.ModReader;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Main implements ActionListener {
	
	public JFrame frame;
	public JPanel panel;
	public Point location;
	public JTextField textbox;
	public JButton button, sayButton;
	public JLabel text, watermark, loadingText;
	
	//public Secret secret;
	public Func666 run;
	public Random random;
	public Secret secret;	
	public boolean windowShaking = false;
	
	public Main() {
		button = new JButton("Go");
		sayButton = new JButton("Say It");
		textbox = new JTextField(20);
		text = new JLabel("Ask something", JLabel.CENTER);
		watermark = new JLabel("Made by LonliHH");
		loadingText = new JLabel("Loading...");
		loadingText.setFont(new Font(loadingText.getFont().getName(), loadingText.getFont().getStyle(), 40));
		loadingText.setForeground(new Color(80, 80, 80));
		
		random = new Random();
		frame = new JFrame("Your Annoying Assistant v1.7");
		panel = new JPanel();
		
		frame.setUndecorated(true);
		frame.setBackground(new Color(1f, 1f, 1f, 0f));
		frame.add(loadingText);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Utils.main = this;
		Utils.checkPak();
		SpecificResponses.cache();
		
		frame.remove(loadingText);
		frame.dispose();
		frame.setVisible(false);
		frame.setBackground(new Color(1f, 1f, 1f, 1f));
		frame.setUndecorated(false);
		
		
		frame.setSize(500, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (run.activated) {
					run.run--;
					run.save();
				}
				
				Utils.exit(0);
			}
		});
		
		panel.setLayout(null);
		
		button.setFocusable(false);
		sayButton.setFocusable(false);
		button.setFocusPainted(false);
		sayButton.setFocusPainted(false);
		
		final int center = Utils.center(frame.getWidth(), 280);
		
		button.setBounds(center, 300, 280, 25);
		sayButton.setBounds(center, 300, 280, 25);
		textbox.setBounds(center, 120, 280, 25);
		text.setBounds(center - 5, -40, 280, 280);
		
		button.addActionListener(this);
		sayButton.addActionListener(new SayButton(this));
		
		panel.add(text);
		panel.add(textbox);
		panel.add(button);
		
		
		watermark.setBounds(10, 435, 280, 25);
		panel.add(watermark);
		
		frame.add(panel);
		
		secret = new Secret(text);
		run = new Func666();
		
		try {
			/*
			final int tsize = text.getFont().getSize();
			text.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/sans.ttf")));
			text.setFont(new Font(text.getFont().getName(), text.getFont().getStyle(), tsize));
			*/
			
			File dir = new File(Utils.DATA, "cached");
			
			if (!(dir.exists() && dir.isDirectory())) dir.mkdirs();
			
			File iconCount = Utils.loadFileFromAssets("assets/icons_count.dat", Utils.TEMP);
			String line = Utils.getFileLines(iconCount)[0];
			
			if (Utils.isInt(line)) {
				int count = Integer.parseInt(line);
				List<File> files = new ArrayList<>();
				
				for (int i = 0; i < count; i++) {
					files.add(Utils.loadFileFromAssets("assets/icons/" + i, dir));
					
					int percent = (int) Math.round((i * 100) / (count - 1));
					
					Utils.println("Caching icons... (" + percent + "%)");
				}
				
				File iconFile = files.get(random.nextInt(files.size()));
				Image icon = ImageIO.read(iconFile);
				
				frame.setIconImage(icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame.setFocusable(true);
		
		if (!run.restricted) {
			frame.setVisible(true);
			
			frame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentMoved(ComponentEvent e) {
					if (!windowShaking) location = frame.getLocationOnScreen();
				}
				
				@Override
				public void componentResized(ComponentEvent e) {
					location = frame.getLocationOnScreen();
				}
			});
			
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowActivated(WindowEvent e) {
					ModReader.triggerModEvent("onFocus");
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
					ModReader.triggerModEvent("onLostFocus");
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					ModReader.triggerModEvent("onMinimizeWindow");
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
					ModReader.triggerModEvent("onUnminimizeWindow");
				}
			});
			
			LuaValueRegistrar.register("Window", new Window());
			LuaValueRegistrar.register("Utils", new Utilities());
			LuaValueRegistrar.register("YAS", new YAS());
			LuaValueRegistrar.register("SoundPool", new SoundPool());
			
			ModReader.readDir("mods/");
		}
	}
	
	public void say() {
		final String input = textbox.getText();
		
		final int r = random.nextInt(5) + 1;
		final int a = random.nextInt(5) + 1;
		final int b = random.nextInt(5) + 1;
		final int c = random.nextInt(5) + 1;
		
		if (r == a || r == b || r == c) text.setText(Utils.getAssetRandomString("assets/commands/say_refuse.txt"));
		else text.setText(input);
		
		panel.remove(sayButton);
		panel.add(button);
		
		updateFrame();
	}
	
	public void updateFrame() {
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	int delayExit = 0;
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		final String input = textbox.getText();
		
		if (run.activated) {
			if (input.equals("func_666_")) Utils.exit(0);
			else {
				panel.remove(watermark);
				updateFrame();
				
				run.run--;
				run.save();
				
				secret.randomButtonGlitch();
				secret.glitchText();
				
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						delayExit++;
						
						if (delayExit >= 1) {
							timer.cancel();
							Utils.exit(0);
						}
					}
				}, 1000, 1000);
			}
			
			return;
		}
		
		
		// Secret
		if (!secret.check(input)) {
			ModReader.triggerModEvent("onInput");
			
			// Empty text box
			if (input.trim().isEmpty()) text.setText(Utils.getAssetRandomString("assets/empty.txt"));
			
			// Commands
			if (input.startsWith("/")) {
				final String command = input.substring(1, input.length()).toLowerCase();
				
				if (command.trim().isEmpty()) {
					text.setText(Utils.getAssetRandomString("assets/empty_command.txt"));
					return;
				}
				
				if (command.equals("fortune") || command.equals("fortunecookie")) {
					text.setText(Utils.getAssetRandomString("assets/commands/fortune_cookie.txt"));
				} else if (command.equals("say")) {
					textbox.setText("");
					text.setText(Utils.getAssetRandomString("assets/commands/say.txt"));
					
					panel.remove(button);
					panel.add(sayButton);
					
					updateFrame();
				} else if (command.equals("luck")) {
					text.setText(
						Utils.getAssetRandomString("assets/commands/luck.txt") + " " +
						random.nextInt(101) + "% lucky"
					);
				} else {
					text.setText("Invalid command.");
				}
			} else if (SpecificResponses.containsQuestion(input)) {
				text.setText(SpecificResponses.getAnswer(input));
			} else if (input.contains("?")) {
				text.setText(Utils.getAssetRandomString("assets/answers.txt"));
			} else if (input.startsWith("--")) {
				final String command = input.substring(2, input.length());
				
				if (command.startsWith("changeResourcesPath ")) {
					String path = command.substring("changeResourcesPath".length(), command.length());
					File rsc = new File(path);
					
					if (rsc.exists() && !rsc.isDirectory()) {
						Utils.PACKED_ASSETS = rsc.getAbsolutePath();
						
						button.setEnabled(false);
						
						try {
							FileWriter writer = new FileWriter(new File(Utils.DATA, "resourcesPath.dat"));
							writer.write(Utils.PACKED_ASSETS + System.getProperty("line.separator") + "1");
							writer.flush();
							writer.close();
							
							text.setText("Attempt sent. Please restart this program.");
						} catch (Exception e) {
							e.printStackTrace();
							text.setText("An error occurred.");
						}
					} else {
						text.setText("The file does not exist.");
					}
				} else if (command.equals("resetResourcesPath")) {
					Utils.PACKED_ASSETS = "assistant.pak";
					
					button.setEnabled(false);
					
					try {
						FileWriter writer = new FileWriter(new File(Utils.DATA, "resourcesPath.dat"));
						writer.write(Utils.PACKED_ASSETS + System.getProperty("line.separator") + "2");
						writer.flush();
						writer.close();
						
						text.setText("Attempt sent. Please restart this program.");
					} catch (Exception e) {
						e.printStackTrace();
						text.setText("An error occurred.");
					}
				} else if (command.equals("reloadSpecificResponses")) {
					text.setText("Reloading specific responses...");
					SpecificResponses.reload();
					text.setText("Reloaded specific responses.");
				} else if (command.equals("clearCache")) {
					Utils.deleteDir(new File(Utils.DATA, "cached"));
					text.setText("Attempted to clear cached files.");
				} else if (command.equals("memoryUsageCheckPercent")) {
					text.setText(MemoryUsageCheck.percent() + "%");
				} else if (command.equals("memoryUsageCheck")) {
					text.setText(String.valueOf(MemoryUsageCheck.used()));
				} else if (command.equals("modCount")) {
					text.setText(String.valueOf(ModReader.getAmount()));
				}
			} else {
				ModReader.textEntered(input);
			}
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}