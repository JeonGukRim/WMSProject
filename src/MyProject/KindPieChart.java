package MyProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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
//	private Vector result;
	private MyPanel panel = new MyPanel();
	private JPanel jp =new JPanel();

	public KindPieChart(JPanel jp){
		this.jp = jp;
		jp.setLayout(new BorderLayout());
		jp.add(panel);
	}

	class MyPanel extends JPanel {
		private int sum = 0;
		private Color[] color = { Color.RED, Color.BLUE, Color.YELLOW, Color.PINK, Color.CYAN, Color.ORANGE,
				Color.GREEN ,Color.MAGENTA,Color.LIGHT_GRAY};
		private double start = 0;
		private double over = 0;

		public MyPanel() {
			dbclass(); // db연결
			allData(); // 데이터 받아오기
			for (int i = 0; i < data1.size(); i++) {
				sum += data2.get(i);
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int n = 0; //사격형 간격 조절
			int m = 0; //폰트 간격 조절
			int o = 0; //사격형 길이 초과시 줄바꿈
			int p = 0; //폰트 길이 초과시 줄바꿈
			for (int i = 0; i < data1.size(); i++) {
				over = ((double) data2.get(i) / (double) sum * 360);
//				g.setColor(color[i]);  //개수가 초과시 오류 날 가능성 있음
				g.setColor( new Color((int)(Math.random()*256), 
						(int)(Math.random()*256),(int)(Math.random()*256)));
				g.fillOval(n, 310 + o, 15, 15); // 사각형
				g.fillArc(270, 0, 300, 300, (int) start, (int) over);
				n += 130;
				start += over;
				g.setColor(Color.black);
				g.setFont(new Font("맑음 고딕", Font.BOLD, 14));
				g.drawString(data1.get(i).toString(), 30 + m, 320 + p);
				g.drawString(data2.get(i).toString(), 100+m,320+p);
				m += 127;
				if (n > 400) {
					o += 50;
					p += 50;
					n = 0;
					m = 0 ;
					
				}
				g.setColor(Color.black);
				g.setFont(new Font("맑음 고딕", Font.BOLD, 30));
				g.drawString("재고 종류별 보유 현황", 0,30);
//				repaint();
			}
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
//		new KindPieChart();
	}

}
