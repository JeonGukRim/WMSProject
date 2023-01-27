package NewTest;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ScoreClientFrame extends JFrame {
	private JTextField nameTf = new JTextField(7);
	private JLabel resLabel = new JLabel("점수");
	private Socket socket = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	
	public ScoreClientFrame() {
		super("클라이언트");
		setSize(300, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //프레임 종료 버튼(X)을 클릭하면 프로그램 종료
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("이름 입력 "));
		c.add(nameTf);
		c.add(resLabel);
		
		setVisible(true);
	
		setupConnection();
		
		nameTf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					String name = tf.getText().trim();					
					if(name.length() == 0)
						return; // 입력되지 않았음
					
					out.write(name + "\n");
					out.flush();
					
					String score = in.readLine();
					resLabel.setText("성적 " + score);
				} catch (IOException e1) {
					System.out.println("클라이언트 : 서버로부터 연결 종료");
					return;
					// e.printStackTrace();
				}
				
			}
			
		});
	}
	
	public void setupConnection() {
		try {
			socket = new Socket("localhost", 9997);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ScoreClientFrame();
	}

}
