package lonli;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SayButton implements ActionListener {
	
	private Main main;
	
	public SayButton(Main main) {
		this.main = main;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String input = main.textbox.getText();
		
		if (input.trim().isEmpty()) main.text.setText("You want me to speak air???");
		else main.say();
	}
	
}