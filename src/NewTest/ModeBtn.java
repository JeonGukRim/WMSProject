package NewTest;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ModeBtn extends JFrame {

	public ModeBtn(Container f) {
		JButton btn1 = new JButton("관리자");
		JButton btn2 = new JButton("직원");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c = f ;
		btn1.setSize(200, 40);
		btn2.setSize(200, 40);
		btn1.setLocation(800, 370);
		btn2.setLocation(870, 370);
		c.add(btn1);
		c.add(btn2);

		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btn1.setEnabled(false);
				btn1.setBackground(Color.ORANGE);
				btn1.setForeground(Color.WHITE);
				btn2.setEnabled(true);
				btn2.setBackground(null);
				btn2.setForeground(null);
			}
		});
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btn2.setEnabled(false);
				btn2.setBackground(Color.ORANGE);
				btn2.setForeground(Color.WHITE);
				btn1.setEnabled(true);
				btn1.setBackground(null);
				btn1.setForeground(null);
			}
		});

	}

	public static void main(String[] args) {
//		new Tessss();
	}
}
