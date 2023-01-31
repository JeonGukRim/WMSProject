package MyProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ProjectDialog extends JDialog {
	private JButton[] menuBtn = new JButton[5];
	private JPanel menuL, pNorth, pSouth, subMenuContainer;
	private JLabel modeJl = new JLabel(); // 관리자 혹은 작업자 구분 라벨
	// 처음화면 돌아기기
	private JButton back = new JButton("메인화면 돌아가기", new ImageIcon("images/back.png"));
	// 로그아웃 버튼
	private JButton logoutBtn = new JButton("로그아웃", new ImageIcon("images/logout1.png"));
	// 테이블 메인 스크롤 창
	private JScrollPane pCenter;
	// 서브 버튼
	private JButton[] subBtn = new JButton[9];
	private String[] btnname = { "재고현황조회", "입출고 이력조회", "발주서 생성", "입고", "Location정보", "출고오더생성", "출고", "상품정보조회",
			"ID정보관리" }; // "검색",
	// 메인메뉴 펼침 닫침 판단
	private static boolean expand = false;
	private static boolean expand1 = false;
	private static boolean expand2 = false;
	private static boolean expand3 = false;
	private static boolean expand4 = false;

	// 로그인시 입력 아이디
	private String loginid;
	// 상속받은 프레임
	public LoginUi frame;
	private ResultSet rs = null;

	public ProjectDialog(LoginUi frame, String title, String loginid) {
		super(frame, title, true);
		// 로그인 아이디 상속받아옴
		this.loginid = loginid;
		// 프레임 상속받아옴
		this.frame = frame;
		
		//서브버튼 익명
		for (int i = 0; i < subBtn.length; i++) {
			subBtn[i] = new JButton(btnname[i]);
			subBtn[i].setBackground(Color.white);
		}
		//관리자모드 여부 확인
		if (frame.ck.isSelected()) {
			modeJl.setText("관리자 모드");
		} else {
			String name = "";
			try {
				rs = frame.stmt.executeQuery("select worker_name from workerid where id = '" + loginid + "'");
				while (rs.next()) {
					name = rs.getString("worker_name");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modeJl.setText(name + "님 환영합니다!");
		}
		
		
		modeJl.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		modeJl.setSize(300, 200);
		modeJl.setLocation(90, 20);
		//메인 화면 돌아기기
		back.setLocation(75, 150);
		back.setSize(200, 40);
		back.setBorderPainted(false);
		back.setContentAreaFilled(false);
		back.setFocusPainted(false);
		back.setOpaque(false);
		back.setHorizontalAlignment(SwingConstants.LEFT);
		
		pNorth = new JPanel();
		menuL = new JPanel();
		setBackground(Color.WHITE);
		//창크기 고정
		setResizable(false);
		setLocation(100, 100);
		setLayout(null);
		//메인 메뉴 담을 패널
		menuL.setSize(200, 500);
		menuL.setLocation(90, 200);
		menuL.setLayout(new BorderLayout());
		menuL.setBackground(Color.LIGHT_GRAY);

		
		//메인 메뉴명 설정
		menuBtn[0] = new JButton("조회");
		menuBtn[0].setFont(new Font("맑은 고딕", Font.BOLD, 20));
		menuBtn[0].setForeground(Color.white);
		menuBtn[0].setBackground(Color.BLACK);

		menuBtn[1] = new JButton("입고");
		menuBtn[1].setFont(new Font("맑은 고딕", Font.BOLD, 20));
		menuBtn[1].setForeground(Color.white);
		menuBtn[1].setBackground(Color.BLACK);

		menuBtn[2] = new JButton("출고");
		menuBtn[2].setFont(new Font("맑은 고딕", Font.BOLD, 20));
		menuBtn[2].setForeground(Color.white);
		menuBtn[2].setBackground(Color.BLACK);

		menuBtn[3] = new JButton("상품리스트");
		menuBtn[3].setFont(new Font("맑은 고딕", Font.BOLD, 20));
		menuBtn[3].setForeground(Color.white);
		menuBtn[3].setBackground(Color.BLACK);

		menuBtn[4] = new JButton("설정");
		menuBtn[4].setFont(new Font("맑은 고딕", Font.BOLD, 20));
		menuBtn[4].setForeground(Color.white);
		menuBtn[4].setBackground(Color.BLACK);

		pNorth.setLayout(new GridLayout(0, 1));
		
		//메인 메뉴를 north패널에 담고 각각 열림 담힘 액션리스널를 부가한다
		//관리자 아니면 id생성기능 제외
		for (int i = 0; i < menuBtn.length; i++) {
			if (i == 4 && frame.ck.isSelected()) {
				pNorth.add(menuBtn[i]);
				menuBtn[i].addActionListener(new ActionHandlerR());
			}
			if (i != 4) {
				pNorth.add(menuBtn[i]);
				menuBtn[i].addActionListener(new ActionHandlerR());
			}
			if (i == 4) {
				for (int j = 0; j < subBtn.length; j++)
					subBtn[j].addActionListener(new ActionHandlerR());
			}
		}
		//패널에 각 버튼 추가
		menuL.add(pNorth, "North");
		
		
		//서브 패널 제작
		subMenuContainer = new JPanel();
		subMenuContainer.setSize(900, 500);
		subMenuContainer.setLocation(350, 200);
		// 서브 패널에 초기 화면 미완료건 카운트 창
		defPanel();
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				subMenuContainer.removeAll();
				defPanel();
			}
		});

		add(menuL);
		add(new textPanel());
		add(modeJl);
		add(back);

		//로그아웃버튼 정의
		logoutBtn.setLocation(1200, 150);
		logoutBtn.setSize(200, 40);
		logoutBtn.setBorderPainted(false);
		logoutBtn.setContentAreaFilled(false);
		logoutBtn.setFocusPainted(false);
		logoutBtn.setOpaque(false);
		logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
		add(logoutBtn);
		setSize(1500, 900);
		logoutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setVisible(true);
				ProjectDialog.this.setVisible(false);
			}
		});
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				try {
					frame.stmt.close(); // Statement 객체 닫기
					frame.conn.close(); // Connection 객체 닫기
					setVisible(false); // 화면 닫기
					dispose(); // 자원 반납
					System.exit(0); // 종료 처리
				} catch (Exception e) {

				}
			}
		});
	}

	private class ActionHandlerR implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < subBtn.length; i++) {
				//서브버튼 확인 하고 각 클래스에 생성자 제공
				if (subBtn[i] == e.getSource()) {
					if (i == 0) {
						new SubBtnListener(subMenuContainer, "재고현황조회", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 1) {
						new SubBtnListener(subMenuContainer, "입출고 이력조회", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 2) {
						new SubBtnListener1(subMenuContainer, "발주서 생성", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 3) {
						new SubBtnListener1(subMenuContainer, "입고", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 4) {
						new SubBtnListener1(subMenuContainer, "Location정보", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 5) {
						new SubBtnListener2(subMenuContainer, "출고오더생성", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 6) {
						new SubBtnListener2(subMenuContainer, "출고", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else if (i == 7) {
						new SubBtnListener3(subMenuContainer, "상품정보조회", loginid, frame);
						getContentPane().add(subMenuContainer);
					} else {
						new SubBtnListener3(subMenuContainer, "ID정보관리", loginid, frame);
						getContentPane().add(subMenuContainer);
					}
				}
				validate();
			}
		
			//메인 메뉴판 클릭시 펼침 닫힘 
			if (menuBtn[0] == e.getSource()) {
				expand = !expand;
				expand1 = false;
				expand2 = false;
				expand3 = false;
				expand4 = false;
				wind2();
				wind3();
				wind4();
				wind5();
				wind1();
			} else if (menuBtn[1] == e.getSource()) {
				expand = false;
				expand1 = !expand1;
				expand2 = false;
				expand3 = false;
				expand4 = false;
				wind1();
				wind3();
				wind4();
				wind5();
				wind2();
			} else if (menuBtn[2] == e.getSource()) {
				expand = false;
				expand1 = false;
				expand2 = !expand2;
				expand3 = false;
				expand4 = false;
				wind1();
				wind2();
				wind4();
				wind5();
				wind3();
			} else if (menuBtn[3] == e.getSource()) {
				expand = false;
				expand1 = false;
				expand2 = false;
				expand3 = !expand3;
				expand4 = false;
				wind1();
				wind2();
				wind3();
				wind5();
				wind4();
			} else if (menuBtn[4] == e.getSource()) {
				expand = false;
				expand1 = false;
				expand2 = false;
				expand3 = false;
				expand4 = !expand4;
				wind1();
				wind2();
				wind3();
				wind4();
				wind5();
			}

		}

	}

	// 접이식 메뉴
	public void wind1() {
		if (expand) {// 펼침
			for (int i = 1; i < 5; i++) {
				pNorth.remove(menuBtn[i]);
			}
			for (int i = 0; i < 2; i++) {
				pNorth.add(subBtn[i]);
			}
			for (int i = 1; i < 5; i++) {
				pNorth.add(menuBtn[i]);
			}
		} else {// 접힘
			for (int i = 0; i < 2; i++) {
				pNorth.remove(subBtn[i]);
			}
		}
		masterOnoff();
		validate();
		menuL.repaint();
	}

	/// 작업자 서브 2버튼 권한 여부
	public void wind2() {
		if (expand1) {// 펼침
			for (int i = 2; i < 5; i++) {
				pNorth.remove(menuBtn[i]);
			}

			// 관리자 모드 선택되면 모드 서브메뉴 추가
			try {
				if (frame.ck.isSelected()) {
					for (int i = 2; i < 5; i++) {
						pNorth.add(subBtn[i]);
					}
				}
				// 관리자 모드 아닐시 권한 받은 메뉴만 추가
				else {
					rs = frame.stmt.executeQuery("select * from workerid where id = '" + loginid + "'");
					boolean powin = true;
					while (rs.next()) {
						powin = rs.getBoolean("pow_inorder");
					}
					if (powin) {
						for (int i = 2; i < 5; i++) {
							pNorth.add(subBtn[i]);
						}
					} else {
						for (int i = 3; i < 5; i++) {
							pNorth.add(subBtn[i]);
						}
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 2; i < 5; i++) {
				pNorth.add(menuBtn[i]);
			}
		} else {// 접힘
			for (int i = 2; i < 5; i++) {
				pNorth.remove(subBtn[i]);
			}

		}
		masterOnoff();
		validate();
		menuL.repaint();
	}

	// 작업자 서브 5번 권한 체크
	public void wind3() {
		if (expand2) {// 펼침
			for (int i = 3; i < 5; i++) {
				pNorth.remove(menuBtn[i]);
			}
			try {
				if (frame.ck.isSelected()) {
					for (int i = 5; i < 7; i++) {
						pNorth.add(subBtn[i]);
					}
				}
				// 관리자 모드 아닐시 권한 받은 메뉴만 추가
				else {
					rs = frame.stmt.executeQuery("select * from workerid where id = '" + loginid + "'");
					boolean powout = true;
					while (rs.next()) {
						powout = rs.getBoolean("pow_outorder");
					}
					if (powout) {
						for (int i = 5; i < 7; i++) {
							pNorth.add(subBtn[i]);

						}
					} else {
						pNorth.add(subBtn[6]);
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 3; i < 5; i++) {
				pNorth.add(menuBtn[i]);
			}
		} else {// 접힘
			for (int i = 5; i < 7; i++) {
				pNorth.remove(subBtn[i]);
			}

		}
		masterOnoff();
		validate();
		menuL.repaint();
	}

	public void wind4() {
		if (expand3) {// 펼침
			pNorth.remove(menuBtn[4]);
			pNorth.add(subBtn[7]);
			pNorth.add(menuBtn[4]);
		} else {// 접힘
			pNorth.remove(subBtn[7]);
		}
		masterOnoff();
		validate();
		menuL.repaint();
	}

	public void wind5() {
		if (frame.ck.isSelected()) {
			if (expand4) {// 펼침
				pNorth.add(subBtn[8]);
			} else {// 접힘
				pNorth.remove(subBtn[8]);
			}
		}
		//관리자 아니면 설정기능 제외
		masterOnoff();
		validate();
		menuL.repaint();
	}

	public void masterOnoff() {
		if (!frame.ck.isSelected()) {
			pNorth.remove(menuBtn[4]);
			pNorth.remove(subBtn[8]);
		}
	}

	//초기 서브패널에 화면 정의 미완료 작업건 및 재고 점유 수량 분류별로 통계
	public void defPanel() {
		JLabel cal1 = new JLabel(new ImageIcon("images/cal2.png"));
		JLabel cal2 = new JLabel(new ImageIcon("images/cal2.png"));
		String in = null;
		String out = null;
		try {
			rs = frame.stmt.executeQuery(
					"SELECT COUNT(CASE WHEN oder_kind='입고' THEN 1 END AND CASE WHEN complete = 'yet' THEN 1 END) AS cnt FROM iohistory");
			while (rs.next())
				in = rs.getString("cnt");
			rs = frame.stmt.executeQuery(
					"SELECT COUNT(CASE WHEN oder_kind='출고' THEN 1 END AND CASE WHEN complete = 'yet' THEN 1 END) AS cnt FROM iohistory");
			while (rs.next())
				out = rs.getString("cnt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel yetinJl = new JLabel(in);
		JLabel yetoutJl = new JLabel(out);
		yetinJl.setFont(new Font("맑음 고딕", Font.BOLD, 30));
		yetoutJl.setFont(new Font("맑음 고딕", Font.BOLD, 30));
		JPanel nP = new JPanel();
		JPanel cP = new JPanel();
		nP.add(cal1);
		nP.add(new JLabel("미완료 입고 작업 :"));
		nP.add(yetinJl);
		nP.add(new JLabel(" 건"));
		nP.add(cal2);
		nP.add(new JLabel("미완료 출고 작업 :"));
		nP.add(yetoutJl);
		nP.add(new JLabel(" 건"));
		subMenuContainer.setLayout(new BorderLayout());
		subMenuContainer.add(nP, BorderLayout.NORTH);
		subMenuContainer.add(cP, BorderLayout.CENTER);
		add(subMenuContainer);
		// 파이차트 추가
		KindPieChart pie = new KindPieChart(cP);
		validate();
		repaint();
	}
}

class textPanel extends JPanel {
	textPanel() {
		setSize(900, 100);
		setLocation(380, 5);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(132, 203, 52));
		g.setFont(new Font("Arial", Font.ITALIC, 40));
		g.drawString("This too shall pass.", 100, 70);
		g.setColor(Color.BLACK);
		g.setFont(new Font("휴먼매직체", Font.BOLD, 30));
		g.drawString("  Et hoc transibit", 540, 100);
	}

}
