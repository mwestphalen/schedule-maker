
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI {
	public GUI() {
		JPanel panel = new JPanel();
		JFrame frame = new JFrame();
		frame.setSize(350,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		
		JLabel rNumb = new JLabel("R Number:");
		rNumb.setBounds(10, 5, 80, 25);
		panel.add(rNumb);
		
		JTextField rNumbText = new JTextField(9);
		rNumbText.setBounds(100, 20, 165, 25);
		panel.add(rNumbText);
		
		JLabel firstName = new JLabel("First Name:");
		firstName.setBounds(10, 50, 80, 25);
		panel.add(firstName);
		
		JTextField firstNameText = new JTextField(9);
		firstNameText.setBounds(100, 20, 165, 25);
		panel.add(firstNameText);
		
		
		
		
		frame.setTitle("Rollins Freshmen Schedule Maker");
		frame.setVisible(true);
	}
	
}
