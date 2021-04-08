package project;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Subject extends JPanel {
	
	private JLabel name = new JLabel();
	private JLabel description = new JLabel();		
	JPanel panel = new JPanel();
	
	/**
	 * Create the panel.
	 */
	public Subject(String name, String description) {	
				
		this.name.setText(name);
		this.name.setHorizontalTextPosition(JLabel.CENTER);
		this.name.setHorizontalTextPosition(JLabel.TOP);
		this.name.setFont(new Font("MV Boli", Font.BOLD, 25));
		this.name.setHorizontalAlignment(JLabel.CENTER);
		

		
		this.description.setText(description);
		this.description.setHorizontalTextPosition(SwingConstants.LEFT);
		this.description.setHorizontalTextPosition(JLabel.LEFT);
		this.description.setFont(new Font("MV Boli", Font.PLAIN, 20));
		this.description.setHorizontalAlignment(JLabel.LEFT);
		this.description.setBounds(250, 10, 100, 100);
		
		this.add(this.description);
		this.add(this.name);
	}

}
