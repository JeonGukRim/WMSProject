package MyProject;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class KindPieChart extends JLabel {
	public Connection conn;
	public Statement stmt = null;
	private ResultSet rs = null;
	private Vector data = null;
	private Vector result;

	public KindPieChart() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.fillArc(0, 0, 100, 100, 0, 30);
	}

	// SELECT COUNT(DISTINCT sku_kind) from listdb group by sku_kind; 종류
//	select sku_kind, SUM(sku_finalnum) as total from listdb group by sku_kind;
	public Vector allData() {
		data.clear();
		try {
			rs = stmt.executeQuery("select sku_kind, SUM(sku_finalnum) as total from listdb group by sku_kind");
			while (rs.next()) {
				Vector in = new Vector<String>(); //
				String kind = rs.getString("sku_kind");
				String total = rs.getString("total");
				in.add(kind);
				in.add(total);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
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
