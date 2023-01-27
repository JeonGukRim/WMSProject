package MyProject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SubBtnListener2 extends JFrame {
	private JPanel mainP = new JPanel(); // 메인패널
	private JPanel northP = new JPanel(); //
	private JPanel testP = new JPanel(); //
	private JPanel centerP = new JPanel(); //
	private JPanel southP = new JPanel(); //
	private PreparedStatement pstmtInsert = null;
	private PreparedStatement pstmtUpdate = null;
	private PreparedStatement pstmtDelete = null;
	private String formatedNow = null;
	private ResultSet rs = null;
	private String text = null;
	private String loginid;
	private LoginUi l;
	private Vector data = null;
	private Vector title = null;
	private JTable table = null;
	private DefaultTableModel model = null;
	private Vector result;

////////////////////////////출고 오더 생성기능//////////////////////////////////////
	private JLabel headname = new JLabel("출고오더 생성");
	private JLabel order = new JLabel("오더번호");
	private JLabel ordernum = new JLabel();
	private JLabel skuCode = new JLabel("SKU코드:");
	private JTextField codeTf = new JTextField(20);
	private JLabel skuName = new JLabel("제품명:");
	private JLabel skuNamedata = new JLabel();
	private JLabel kind = new JLabel("분류:");
	private JLabel kinddata = new JLabel();
	private JLabel sku_location = new JLabel("재고위치:");
	private JLabel locationdata = new JLabel();
	private JLabel finalNum = new JLabel("출고 가능 수량:");
	private JLabel finalNumdata = new JLabel();
	private JLabel outnum = new JLabel("출고예정수량:");
	private JTextField outnumTf = new JTextField(20);
	private JButton creatBtn = new JButton("생성");
	private JButton upBtn = new JButton("새로고침");
	private Boolean exp = true;
////////////////////////////////////출고//////////////////////////////////////
	private JButton view = new JButton("조회");
	private JComboBox<String> orderCombo;
	private JLabel realoutNumJl = new JLabel("실제 출고수량:");
	private JLabel skuCodeJl = new JLabel();
	private JLabel outdata = new JLabel();
	private JTextField realoutNumTf = new JTextField(20);
	private JLabel skuLocation = new JLabel("재고위치:");
	private JLabel skuLocationData = new JLabel();
	private JButton outBtn = new JButton("출고완료");

////////////////////////////////////////////////////////////////////////////	
	public SubBtnListener2(JPanel mainP, String text, String loginid, JFrame frame) {
		this.mainP = mainP;
		this.text = text;
		this.loginid = loginid;
		l = (LoginUi) frame;
		mainP.setLayout(new BorderLayout());
		LocalDateTime now = LocalDateTime.now();
		formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

////////////////////////////출고 오더 생성기능//////////////////////////////////////		
		if (text.equals("출고오더생성")) {
			resetP();
			locationSetting1();
			codeTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JTextField t = (JTextField) e.getSource();
					try {
						rs = l.stmt.executeQuery("select * from listdb where sku_code ='" + t.getText() + "';");
						if (rs.isBeforeFirst()) { // 데이터가 존재하면 true를 반환해줌
							while (rs.next()) {
								skuNamedata.setText(rs.getString("sku_name"));
								kinddata.setText(rs.getString("sku_kind"));
								finalNumdata.setText(rs.getString("sku_finalnum"));
								locationdata.setText(rs.getString("sku_location"));
							}
							exp = false;
							t.setEditable(exp);
						} else {
							resetF();
							JOptionPane.showMessageDialog(null, "존재하지 않는 코드 입니다\n코드를 새로 생성후 진행하세요!", "입력오류", 1);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			upBtn.addActionListener(new ActionListener() {
				// 새로고침
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					exp = true;
					resetF();
					codeTf.setEditable(exp);
				}
			});
			creatBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int num = 0;
//					int outex = 0;
					if (outnumTf.getText().trim().length() != 0 && exp == false) {
						try {
							int outex = Integer.parseInt(outnumTf.getText());
							try {
								rs = l.stmt.executeQuery("select * from listdb where sku_code ='" + codeTf.getText()
										+ "' and sku_location = '" + locationdata.getText() + "'");
								while (rs.next()) {
									num = rs.getInt("sku_finalnum");
								}

							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							if (outex > num) {
								JOptionPane.showMessageDialog(null, "출고가능 수량을 초과하였습니다", "알림",
										JOptionPane.ERROR_MESSAGE);
							} else {
								if (exp == false) {
									creat(codeTf.getText(), skuNamedata.getText(), kinddata.getText(), outex,
											ordernum.getText(), locationdata.getText());
									resetF();
									exp = true;
									codeTf.setText("");
									codeTf.setEditable(exp);
									// 발주내역 추가후 새로운 오더번호 받기
									LocalDateTime now = LocalDateTime.now();
									formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
									ordernum.setText("IO" + formatedNow);

								} else
									JOptionPane.showMessageDialog(null, "입력정보를 확인해주세요", "알림", 1);
							}

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "숫자만 입력해주세요", "알림", 1);
						}

					} else {
						JOptionPane.showMessageDialog(null, "빈칸을 확인 해주세요", "알림", 1);
					}
				}
			});
		}

////////////////////////////////////출고//////////////////////////////////////		
		if (text.equals("출고")) {
			resetP();
			locationSetting2();
			//출고 오더 조회
			view.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String select = orderCombo.getSelectedItem().toString();
					if (orderCombo != null) {
						try {
							ResultSet rs = l.stmt
									.executeQuery("select * from iohistory where ordernum ='" + select + "'");
							while (rs.next()) {
								skuCodeJl.setText(rs.getString("sku_code"));
								skuNamedata.setText(rs.getString("sku_name"));
								kinddata.setText(rs.getString("sku_kind"));
								skuLocationData.setText(rs.getString("sku_location"));
								finalNumdata.setText(rs.getString("ex_num"));
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
			});
			// 출고완료 저장
			outBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					LocalDateTime now = LocalDateTime.now();
					formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년MM월dd일HH시mm분"));
					String select = orderCombo.getSelectedItem().toString();
					int realnum = 0;
					try {
						// try 입력값의 정확성 판단
						realnum = Integer.parseInt(realoutNumTf.getText());
						// 보유재고가 부족할때
						rs = l.stmt.executeQuery("select sku_finalnum from listdb where sku_code ='"
								+ skuCodeJl.getText() + "'and sku_location ='" + skuLocationData.getText() + "'");
						int getnum = 0;
						while (rs.next()) {
							getnum = rs.getInt("sku_finalnum");
						}
						if (realnum > getnum) {
							JOptionPane.showMessageDialog(null, "현재 해당 재고 보유 수량이 "+getnum+"개 입니다", "알림", 1);
						} else {
							try {
								if (realnum <= Integer.parseInt(finalNumdata.getText()) && realnum >= 0) {
									// 입출고 이력에 작업자 id와 작업일자 저장
									pstmtUpdate = l.conn.prepareStatement(
											"update  iohistory set realnum = ?,complete = ?,worker_id = ?,work_date = ? where ordernum =?");
									pstmtUpdate.setInt(1, realnum);
									pstmtUpdate.setString(2, "over"); // 작업끝났으면 오버로 표시
									pstmtUpdate.setString(3, loginid);
									pstmtUpdate.setString(4, formatedNow);
									pstmtUpdate.setString(5, select);
									pstmtUpdate.executeUpdate();

									//현재 재고 수량에서 출고수량 덜어내기
									pstmtUpdate = l.conn.prepareStatement(
											"update  listdb set sku_finalnum = ? where sku_code =? and sku_location = ?");
									pstmtUpdate.setInt(1, (getnum - realnum));
									pstmtUpdate.setString(2, skuCodeJl.getText());
									pstmtUpdate.setString(3, skuLocationData.getText());
									JOptionPane.showMessageDialog(null, "출고완료 되였습니다", "알림", 1);
									pstmtUpdate.executeUpdate();

									//재고가 0인 데이터 삭제처리
									pstmtDelete = l.conn
											.prepareStatement("delete from listdb where sku_finalnum = '0'");
									pstmtDelete.executeUpdate();

									// 리셋
									orderCombo.removeAll();
									skuCodeJl.setText("");
									skuNamedata.setText("");
									kinddata.setText("");
									outdata.setText("");
									realoutNumTf.setText("");
									skuLocationData.setText("");
									finalNumdata.setText("");
									resetP();
									locationSetting2();
								} else {
									JOptionPane.showMessageDialog(null, "입력값을 확인해주세요", "에러", JOptionPane.ERROR_MESSAGE);
								}

							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "숫자를 확인해주세요", "알림", 1);
					}
				}
			});
		}
	}

///////////////////////////////////메소드//////////////////////////////////////	

	//출고오더생성
	public void locationSetting1() {
		ordernum = new JLabel("OP" + formatedNow);
		headname.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		mainP.add(headname, BorderLayout.NORTH);
		centerP.setLayout(new GridLayout(14, 2));
		order.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		ordernum.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		centerP.add(order, ordernum);
		centerP.add(ordernum);
		centerP.add(skuCode);
		centerP.add(codeTf);
		centerP.add(skuName);
		centerP.add(skuNamedata);
		centerP.add(kind);
		centerP.add(kinddata);
		centerP.add(sku_location);
		centerP.add(locationdata);
		centerP.add(finalNum);
		centerP.add(finalNumdata);
		centerP.add(outnum);
		centerP.add(outnumTf);

		title = new Vector<>();
		data = new Vector<>();
		model = new DefaultTableModel();
		result = allData();
		title.add("SKU번호");
		title.add("제품명");
		title.add("분류");
		title.add("재고위치");
		title.add("수량");
		title.add("메모");
		model.setDataVector(result, title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
//		sp.setPreferredSize(new Dimension(400, 200));
		testP.setLayout(new BorderLayout());
		testP.add(new JLabel("출고가능 제품리스트(클릭)", JLabel.CENTER), BorderLayout.NORTH);
		testP.add(sp, BorderLayout.CENTER);
		southP.add(creatBtn);
		southP.add(upBtn);
		mainP.add(testP, BorderLayout.EAST);
		mainP.add(centerP, BorderLayout.WEST);
		mainP.add(southP, BorderLayout.SOUTH);

		// 클릭하여 데이터 가져 오기
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 더블 클릭하면 데이터 전송
				if (e.getClickCount() == 1 && exp == true) {
					int index = table.getSelectedRow();
					Vector in = (Vector) data.get(index);
					String sku_code = (String) in.get(0);
					String sku_name = (String) in.get(1);
					String sku_kind = (String) in.get(2);
					String sku_location = (String) in.get(3);
					int sku_finalnum = (int) in.get(4);
					codeTf.setText(sku_code);
					skuNamedata.setText(sku_name);
					kinddata.setText(sku_kind);
					locationdata.setText(sku_location);
					finalNumdata.setText(sku_finalnum + "");
					exp = false;
					codeTf.setEditable(exp);
				}
			}
		});
		result = null;
	}
	//출고
	public void locationSetting2() {
		creatComboBox();
		northP.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
		northP.add(order);
		northP.add(orderCombo);
		northP.add(view);
		mainP.add(northP, BorderLayout.NORTH);

		testP.setLayout(new GridLayout(6, 2, 40, 40));
		testP.add(skuCode);
		testP.add(skuCodeJl);
		testP.add(skuName);
		testP.add(skuNamedata);
		testP.add(kind);
		testP.add(kinddata);
		testP.add(skuLocation);
		testP.add(skuLocationData);
		testP.add(finalNum);
		testP.add(finalNumdata);
		testP.add(realoutNumJl);
		testP.add(realoutNumTf);
		centerP.add(testP, BorderLayout.NORTH);
		mainP.add(centerP, BorderLayout.CENTER);

		southP.add(outBtn);
		mainP.add(southP, BorderLayout.SOUTH);
	}
	//패널 리셋
	public void resetP() {
		mainP.removeAll();
		northP.removeAll();
		southP.removeAll();
		centerP.removeAll();
		testP.removeAll();
		validate();
		mainP.repaint();
		northP.repaint();
		southP.repaint();
		centerP.repaint();
		testP.repaint();
	}

	public void resetF() {
		skuNamedata.setText("");
		outnumTf.setText("");
		kinddata.setText("");
		locationdata.setText("");
		finalNumdata.setText("");
		outdata.setText("");
	}

	public Vector allData() {
		data.clear();
		try {
			rs = l.stmt.executeQuery("select * from listdb order by sku_code");
			while (rs.next()) {
				Vector in = new Vector<String>(); //
				String sku_code = rs.getString("sku_code");
				String sku_name = rs.getString("sku_name");
				String sku_kind = rs.getString("sku_kind");
				String sku_location = rs.getString("sku_location");
				int sku_finalnum = rs.getInt("sku_finalnum");
				String menmo = rs.getString("memo");
				in.add(sku_code);
				in.add(sku_name);
				in.add(sku_kind);
				in.add(sku_location);
				in.add(sku_finalnum);
				in.add(menmo);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

	public void creat(String sku, String name, String kind, int num, String order, String location) {

		try {
			pstmtInsert = l.conn.prepareStatement(
					"insert into iohistory(sku_code,sku_name,sku_kind,ordernum,ex_num,oder_kind,complete,sku_location) values( ?,? ,"
							+ "? ,?,?,?,?,?)");
			pstmtInsert.setString(1, sku);
			pstmtInsert.setString(2, name);
			pstmtInsert.setString(3, kind);
			pstmtInsert.setString(4, order);
			pstmtInsert.setInt(5, num);
			pstmtInsert.setString(6, "출고");
			pstmtInsert.setString(7, "yet");
			pstmtInsert.setString(8, location);
			pstmtInsert.executeUpdate();
			JOptionPane.showMessageDialog(null, "출고오더 생성되였습니다", "알림", 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void creatComboBox() {
		Vector orderlist = new Vector<>();
		try {
			rs = l.stmt.executeQuery("select * from iohistory where complete ='yet' and oder_kind ='출고'");
			while (rs.next()) {
				String orderNum = rs.getString("ordernum");
				orderlist.add(orderNum);
			}
			orderCombo = new JComboBox<String>(orderlist);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
