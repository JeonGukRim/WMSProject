package NewTest;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.swing.*;

public class DicServerFrame2 extends JFrame {
	private JTextField eng = new JTextField(10);
	private JTextField kor = new JTextField(10);
	private JButton saveBtn = new JButton("저장");
	private JButton searchBtn = new JButton("찾기");
	private JButton delBtn = new JButton("삭제");
	private JTextArea ta = new JTextArea(7, 25);
	private HashMap<String, String> dic = new HashMap<String, String>();
	Connection conn;
	Statement stmt = null;
	ResultSet srs;

	public DicServerFrame2() {
		super("Dic Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 종료 버튼(X)을 클릭하면 프로그램 종료
		setSize(350, 300);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("영어"));
		c.add(eng);
		c.add(new JLabel("한글"));
		c.add(kor);
		c.add(saveBtn);
		c.add(searchBtn);
		c.add(delBtn);

		try {
			Class.forName("com.mysql.jdbc.Driver"); // MySQL 드라이버 로드
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sampledb", "root", "test123"); // JDBC 연결
			System.out.println("DB 연결 완료");
			stmt = conn.createStatement();

			srs = stmt.executeQuery("SELECT * FROM word");
			while (srs.next()) {
				ta.append("목록 (" + srs.getString("eng") + "," + srs.getString("kor") + ")\n");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e) {
			System.out.println("DB 연결 오류");
		}

		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ta.append("삽입 (" + eng.getText() + "," + kor.getText() + ")\n");
				try {
					stmt.executeUpdate("insert into word values('" + eng.getText() + "', '" + kor.getText() + "');");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // 레코드 추가
			}
		});
		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ResultSet srs2 = null;
				try {
					srs2 = stmt.executeQuery("SELECT * FROM word WHERE " + "eng = '" + eng.getText() + "';");
					if (!srs2.next())
						kor.setText("없음");
					else
						kor.setText(srs2.getString("kor"));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ResultSet srs3 = null;
				ta.append("삭제 (" + eng.getText() + "," + kor.getText() + ")\n");
				try {
					stmt.executeUpdate("delete FROM word WHERE " + "eng = '" + eng.getText() + "';");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		c.add(new JScrollPane(ta));
		setVisible(true);

	}

	public static void main(String[] args) {
		new DicServerFrame2();
	}

}