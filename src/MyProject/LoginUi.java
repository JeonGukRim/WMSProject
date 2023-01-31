package MyProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginUi extends JFrame {
	private MyPanel panel = new MyPanel();
	private JLabel idjl = new JLabel(new ImageIcon("images/id.png"));
	private JLabel pwjl = new JLabel(new ImageIcon("images/pw.png"));
	private JTextField loginTf = new JTextField(10); // 아이디 입력 필드
	private JPasswordField pwTf = new JPasswordField(10); // 비밀번호 입력 필드
	private JButton login = new JButton("로 그 인");
	public JCheckBox ck, radio2;
	private ButtonGroup g = new ButtonGroup();
	private ImageIcon image2 = new ImageIcon("images/m1.png");
	private ImageIcon image3 = new ImageIcon("images/m2.png");
	private ProjectDialog dialog;
	public Connection conn;
	public Statement stmt = null;
	private ResultSet rs = null;

	public LoginUi() {
		// 관리자 모드 여부 체크박스
		ck = new JCheckBox("", image3);
		ck.setSelectedIcon(image2);
		ck.setSize(300, 80);
		ck.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		ck.setLocation(800, 380);
		ck.setSelected(true);

		// 아이디 입력 정보
		idjl.setLocation(740, 390);
		idjl.setSize(200, 200);
		loginTf.setSize(400, 40);
		loginTf.setFont(new Font("맑음 고딕", Font.BOLD, 20));
		loginTf.setBackground(Color.LIGHT_GRAY);
		loginTf.setLocation(880, 471);
		// 비밀번호 입력 정보
		pwjl.setLocation(740, 460);
		pwjl.setSize(200, 200);
		pwTf.setSize(400, 40);
		pwTf.setFont(new Font("맑음 고딕", Font.BOLD, 20));
		pwTf.setBackground(Color.LIGHT_GRAY);
		pwTf.setLocation(880, 540);
		// 로그인 버튼
		login.setSize(150, 30);
		login.setForeground(Color.WHITE);
		login.setBackground(new Color(73, 182, 155));
		login.setFont(new Font("맑음 고딕", Font.BOLD, 20));
		login.setLocation(880, 620);
		// 데이터베이스 연결
		dbclass();

		// 로그인버튼 아이디 비번 확인
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (pwTf.getText().length() == 0 && loginTf.getText().trim().length() != 0) {
					JOptionPane.showMessageDialog(null, "비밀번호을 입력 해주세요", "알림", 1);
				} else if (pwTf.getText().length() != 0 && loginTf.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "아이디을 입력 해주세요", "알림", 1);
				} else if (pwTf.getText().length() == 0 && loginTf.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "입력정보가 없습니다", "알림", 1);
				} else {
					loginAction();
				}
			}
		});
		// 패스워드 입력후 enter키로 로그인
		pwTf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (pwTf.getText().length() == 0 && loginTf.getText().trim().length() != 0) {
					JOptionPane.showMessageDialog(null, "비밀번호을 입력 해주세요", "알림", 1);
				} else if (pwTf.getText().length() != 0 && loginTf.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "아이디을 입력 해주세요", "알림", 1);
				} else if (pwTf.getText().length() == 0 && loginTf.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "입력정보가 없습니다", "알림", 1);
				} else {
					loginAction();
				}
			}
		});
		panel.add(ck); // 체크박스 추가
		panel.add(idjl);
		panel.add(pwjl);
		panel.add(loginTf);
		panel.add(pwTf);
		panel.add(login);

		setTitle("W.M.S 로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setContentPane(panel);

		setLayout(null);
		setSize(1500, 900);
		setVisible(true);

	}

	class MyPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("images/warehouse.png");
		private Image img = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// 이미지
			g.drawImage(img, 200, 250, 450, 450, this);
			// 명언
			g.setColor(new Color(132, 203, 52));
			g.setFont(new Font("Arial", Font.ITALIC, 50));
			g.drawString("Being happy never goes out of style", 100, 80);
			g.setColor(Color.BLACK);
			g.setFont(new Font("휴먼매직체", Font.ITALIC, 30));
			g.drawString("즐거움은 영원히 유행에 뒤떨어지지 않는다", 620, 180);
			// 로고 위치
			g.setColor(new Color(73, 182, 155));
			g.fillRoundRect(800, 250, 500, 100, 40, 60);
			g.setColor(Color.WHITE);
			g.setFont(new Font("휴먼매직체", Font.BOLD, 50));
			g.drawString("더 조은  W . M . S", 860, 322);
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new LoginUi();
	}

	public void loginAction() {
		String mode = null;
		String id = null;
		String pw = null;
		if (ck.isSelected())
			mode = "masterid";
		else
			mode = "workerid";
		dialog = new ProjectDialog(LoginUi.this, "로그인 성공", loginTf.getText());
		try {
			rs = stmt.executeQuery("select * from " + mode + " where id = '" + loginTf.getText() + "'");
			while (rs.next()) {
				id = rs.getString("id");
				pw = rs.getString("pw");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (loginTf.getText().equals(id) && pwTf.getText().equals(pw)) {
			setVisible(false);
			dialog.setVisible(true);

		} else {
			JOptionPane.showMessageDialog(null, "아이디 비밀번호가 일치하지 않습니다", "로그인실패", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void dbclass() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myproject", "root", "test123"); // JDBC 연결
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 오류");
		} catch (SQLException e1) {
			System.out.println("실행오류");
		}
	}
}
