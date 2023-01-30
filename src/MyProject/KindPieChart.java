package MyProject;

import java.awt.Color;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KindPieChart extends JFrame {
	public Connection conn;
	public Statement stmt = null;
	private ResultSet rs = null;
	private Vector data1 = new Vector();
	private Vector<Integer> data2 = new Vector();
	private Vector result;
	private MyPanel panel = new MyPanel();

	public KindPieChart() {
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 2020);
		setVisible(true);
	}

	class MyPanel extends JPanel {
		private int sum = 0;
		private Color[] color = {Color.RED,
				Color.BLUE,Color.YELLOW,Color.GRAY,Color.CYAN,Color.ORANGE,Color.GREEN};
		private int start = 0;
		private int over =0;
		private	Graphics g = null;
		public MyPanel() {  
			dbclass();
			allData();
			for(int i =0 ; i < data1.size();i++) {
					sum += data2.get(i);
			}
			
			for(int i = 0;i<data1.size();i++) {
				over =((data2.get(i)/sum)*360);
				paintComponent(g,color[i],start,over);
				start += over;
			}
			
		}
		
		public void paintComponent(Graphics g,Color color,int start,int over) {
			super.paintComponent(g);
			g.setColor(color);
			g.fillArc(0,0,100,100,start,over);
		}
	}

	// SELECT COUNT(DISTINCT sku_kind) from listdb group by sku_kind; 종류
//	select sku_kind, SUM(sku_finalnum) as total from listdb group by sku_kind;
	public void allData() {
		data1.clear();
		data2.clear();
		try {
			rs = stmt.executeQuery("select sku_kind, SUM(sku_finalnum) as total from listdb group by sku_kind");
			while (rs.next()) {
				Vector in1 = new Vector<String>(); //
				Vector in2 = new Vector<Integer>(); //
				String kind = rs.getString("sku_kind");
				int total = rs.getInt("total");
				in1.add(kind);
				in2.add(total);
				data1.add(in1);
				data2.addAll(in2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return data;
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
