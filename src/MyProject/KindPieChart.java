package MyProject;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;

public class KindPieChart extends JLabel {
	public Connection conn;
	public Statement stmt = null;
	private ResultSet rs = null;

	public KindPieChart() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new KindPieChart();
	}

}
