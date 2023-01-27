package MyProject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.print.attribute.IntegerSyntax;
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

public class SubBtnListener1 extends JFrame {
	private JPanel mainP = new JPanel(); // 메인패널
	private JPanel northP = new JPanel(); //
	private JPanel testP = new JPanel(); //
	private JPanel centerP = new JPanel(); //
	private JPanel southP = new JPanel(); //
	private ResultSet rs = null;
	private String text = null;
	private PreparedStatement pstmtInsert = null;
	private PreparedStatement pstmtUpdate = null;
	private PreparedStatement pstmtDelete = null;
	private PreparedStatement pstmtDel = null;
	private String formatedNow = null;

////////////////////////////발주서 생성기능//////////////////////////////////////
	private JLabel headname = new JLabel("발주서 생성");
	private JLabel order = new JLabel("오더번호");
	private JLabel ordernum = new JLabel();
	private JLabel skuCode = new JLabel("SKU코드:");
	private JTextField codeTf = new JTextField(20);
	private JLabel skuName = new JLabel("제품명:");
	private JLabel skuNamedata = new JLabel();
	private JLabel kind = new JLabel("분류:");
	private JLabel kinddata = new JLabel();
	private JLabel innum = new JLabel("입고예정수량:");
	private JTextField inTf = new JTextField(20);
//	private NumberField inTf = new NumberField();

	private JButton creatBtn = new JButton("생성");
	private JButton upBtn = new JButton("새로고침");
	private Boolean exp = true;
	private JLabel locationJl = new JLabel("재고위치");
	private JComboBox<String> locationCombo;
	private JLabel numtag = new JLabel("재고위치 적재수량");
	private JLabel holdnum = new JLabel("");
	private JButton addBtn1 = new JButton("<<<<추가");

//////////////////////////////////입고//////////////////////////////////////////
	private JButton view = new JButton("조회");
	private JComboBox<String> orderCombo;
	private JLabel realinNumJl = new JLabel("실제 입고수량:");
	private JLabel skuCodeJl = new JLabel();
	private JLabel indata = new JLabel();
	private JTextField realinNumTf = new JTextField(20);
	private JLabel skuLocation = new JLabel("재고위치:");
	private JLabel skuLocationTf = new JLabel("");
	private JButton inBtn = new JButton("입고완료");

	/////////////////////////// Location정보///////////////////////////////////////
	private JButton addBtn = new JButton("추가");
	private JTextField loTf = new JTextField(20);
	private JButton delBtn = new JButton("삭제");
	private JButton seaBtn = new JButton("검색");
	private Vector data = null;
	private Vector title = null;
	private JTable table = null;
	private DefaultTableModel model = null;
	private Vector result;
	private int row = -1;
	private String loginid;
	private LoginUi l;

	public SubBtnListener1() {
	}

	public SubBtnListener1(JPanel mainP, String text, String loginid, JFrame frame) {
		this.mainP = mainP;
		this.text = text;
		this.loginid = loginid;
		l = (LoginUi) frame;
		mainP.setLayout(new BorderLayout());
		LocalDateTime now = LocalDateTime.now();
		formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//////////////////////////////////////발주서생성////////////////////////////////////////////
		if (text.equals("발주서 생성")) {
			resetP();
			locationSetting();
			codeTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JTextField t = (JTextField) e.getSource();
					try {
						rs = l.stmt.executeQuery("select * from productlist where sku_code ='" + t.getText() + "';");
						if (rs.isBeforeFirst()) { // 데이터가 존재하면 true를 반환해줌
							while (rs.next()) {
								skuNamedata.setText(rs.getString("sku_name"));
								kinddata.setText(rs.getString("sku_kind"));
							}
							exp = false;
							t.setEditable(exp);
						} else {
							skuNamedata.setText("");
							kinddata.setText("");
							JOptionPane.showMessageDialog(null, "존재하지 않는 코드 입니다\n코드를 새로 생성후 진행하세요!", "입력오류", 1);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			upBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					exp = true;
					skuNamedata.setText("");
					kinddata.setText("");
					codeTf.setEditable(exp);
				}
			});
			creatBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int innum = 0;
					if ((inTf.getText().trim().length() != 0 && exp == false)) {
						try {
							innum = Integer.parseInt((inTf.getText().trim()));
							try {
								if (innum > 0) {
									creat(codeTf.getText(), skuNamedata.getText(), kinddata.getText(),
											Integer.parseInt(inTf.getText()), ordernum.getText());
									skuNamedata.setText("");
									kinddata.setText("");
									inTf.setText("");
									exp = true;
									codeTf.setText("");
									codeTf.setEditable(exp);

									// 발주내역 추가후 새로운 오더번호 받기
									LocalDateTime now = LocalDateTime.now();
									formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
									ordernum.setText("IO" + formatedNow);
									JOptionPane.showMessageDialog(null, "발주신청이 생성되였습니다", "알림", 1);
								} else {
									JOptionPane.showMessageDialog(null, "입고예정수량을 확인해주세요", "알림", 1);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "숫자를 입력해주세요", "알림", 1);
							}

						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "숫자만 입력해주세요", "알림", 1);
						}
					} else {
						JOptionPane.showMessageDialog(null, "빈칸을 확인해주세요", "알림", 1);
					}
				}
			});
		}
////////////////////////////////////////입고//////////////////////////////////////////////
		if (text.equals("입고")) {
			resetP();
			locationSetting1();
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
								indata.setText(rs.getString("ex_num"));
								skuLocationTf.setText(rs.getString("sku_location"));
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					// 입고완료 저장
					if (!skuCodeJl.getText().equals("")) {
						inBtn.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								LocalDateTime now = LocalDateTime.now();
								formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년MM월dd일HH시mm분"));
								int realnum = 0;
								try {
									realnum = Integer.parseInt(realinNumTf.getText());
								} catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null, "숫자를 확인해주세요", "알림", 1);
								}

								try {
									// 입출고 이력에 실제 입고수량 날짜 작업자 id 저장
									if (realnum <= Integer.parseInt(indata.getText()) && realnum >= 0) {
										pstmtUpdate = l.conn.prepareStatement(
												"update  iohistory set realnum = ?,complete = ?,worker_id = ?,work_date = ? where ordernum =?");
										pstmtUpdate.setInt(1, realnum);
										pstmtUpdate.setString(2, "over"); // 작업끝났으면 오버로 표시
										pstmtUpdate.setString(3, loginid);
										pstmtUpdate.setString(4, formatedNow);
										pstmtUpdate.setString(5, select);
										pstmtUpdate.executeUpdate();

									} else {
										JOptionPane.showMessageDialog(null, "입력값을 확인해주세요", "에러",
												JOptionPane.ERROR_MESSAGE);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								try {
									// 리스트에 같은 제품과 재고위치에 재고가 존재한다면 재고 추가
									if (realnum <= Integer.parseInt(indata.getText()) && realnum >= 0) {
										rs = l.stmt.executeQuery(
												"select * from listdb  where sku_location = '" + skuLocationTf.getText()
														+ "' and sku_code='" + skuCodeJl.getText() + "'");
										if (rs.isBeforeFirst()) {
											int num = 0;
											pstmtUpdate = l.conn.prepareStatement(
													"update listdb set sku_finalnum = ? where sku_location = ? and sku_code = ?");
											while (rs.next()) {
												num = rs.getInt("sku_finalnum");
											}

											num += realnum;
											pstmtUpdate.setInt(1, num);
											pstmtUpdate.setString(2, skuLocationTf.getText());
											pstmtUpdate.setString(3, skuCodeJl.getText());
											pstmtUpdate.executeUpdate();
										} else {
											pstmtInsert = l.conn.prepareStatement(
													"insert into listdb(sku_code,sku_name,sku_kind,sku_location,sku_finalnum) values( ?,? ,"
															+ "? ,?,?)");
//										int num1 = Integer.parseInt(realinNumTf.getText());
											pstmtInsert.setString(1, skuCodeJl.getText());
											pstmtInsert.setString(2, skuNamedata.getText());
											pstmtInsert.setString(3, kinddata.getText());
											pstmtInsert.setString(4, skuLocationTf.getText());
											pstmtInsert.setInt(5, realnum);
											pstmtInsert.executeUpdate();
										}

										orderCombo.removeAll();
										skuCodeJl.setText("");
										skuNamedata.setText("");
										kinddata.setText("");
										indata.setText("");
										realinNumTf.setText("");
										skuLocationTf.setText("");
										resetP();
										locationSetting1();
										JOptionPane.showMessageDialog(null, "입고완료 되였습니다", "알림", 1);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

						});
					}

				}
			});
		}
///////////////////////////////////Location정보//////////////////////////////////////////////		
		if (text.equals("Location정보")) {
			resetP();
			locationSetting2();
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					row = table.getSelectedRow();
					Vector in = (Vector) data.get(row);
					String sku_location = (String) in.get(0);
					loTf.setText(sku_location);
				}
			});
			seaBtn.addActionListener(new btnAction());
			addBtn.addActionListener(new btnAction());
			delBtn.addActionListener(new btnAction());
		}
	}

/////////////////////////////////////이하 메소드 구역/////////////////////////////////////////////////
	public void locationSetting() { // 발주서생성 세팅
		// 현재 시간으로 오더번호 생성 입고는 IO,출고는 OP로 시작;
		creatCombo();
		ordernum = new JLabel("IO" + formatedNow);
		headname.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		mainP.add(headname, BorderLayout.NORTH);
		centerP.setLayout(new GridLayout(14, 2));
		order.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		ordernum.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		centerP.add(order, ordernum);
		centerP.add(ordernum);
		skuNamedata = new JLabel();
		kinddata = new JLabel();
		centerP.add(skuCode);
		centerP.add(codeTf);
		centerP.add(skuName);
		centerP.add(skuNamedata);
		centerP.add(kind);
		centerP.add(kinddata);
		centerP.add(innum);
		centerP.add(inTf);
		centerP.add(locationJl);
		centerP.add(locationCombo);
		centerP.add(numtag);
		centerP.add(holdnum);

		title = new Vector<>();
		data = new Vector<>();
		model = new DefaultTableModel();
		result = allData();
		title.add("SKU번호");
		title.add("제품명");
		title.add("분류");
		model.setDataVector(result, title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
//		sp.setPreferredSize(new Dimension(300, 300));
		testP.add(addBtn1);
		testP.add(sp);

		southP.add(creatBtn);
		southP.add(upBtn);

		mainP.add(testP, BorderLayout.EAST);
		mainP.add(centerP, BorderLayout.WEST);
		mainP.add(southP, BorderLayout.SOUTH);
		// 클릭하여 데이터 가져 오기
//		table.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				// 마우스 더블 클릭하면 데이터 전송
//
//				if (exp) {
//					int index = table.getSelectedRow();
//					Vector in = (Vector) data.get(index);
//					String sku_code = (String) in.get(0);
//					String sku_name = (String) in.get(1);
//					String sku_kind = (String) in.get(2);
//					codeTf.setText(sku_code);
//					skuNamedata.setText(sku_name);
//					kinddata.setText(sku_kind);
//					exp = false;
//					codeTf.setEditable(exp);
//				}
//			}
//		});
		locationCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String select = locationCombo.getSelectedItem().toString();
				if (locationCombo != null) {
					try {
						ResultSet rs = l.stmt.executeQuery(
								"select * from locationdb l left join (select sku_location, SUM(sku_finalnum) as total from listdb group by sku_location) "
										+ "s on l.sku_location = s.sku_location where l.sku_location = '" + select
										+ "'");
						while (rs.next()) {
							holdnum.setText(rs.getString("total"));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		addBtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (exp) {
					int index = table.getSelectedRow();
					Vector in = (Vector) data.get(index);
					String sku_code = (String) in.get(0);
					String sku_name = (String) in.get(1);
					String sku_kind = (String) in.get(2);
					codeTf.setText(sku_code);
					skuNamedata.setText(sku_name);
					kinddata.setText(sku_kind);
					exp = false;
					codeTf.setEditable(exp);
				}
			}
		});

		result = null;
	}

	public void locationSetting1() { // 입고 세팅
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
		testP.add(innum);
		testP.add(indata);
		testP.add(skuLocation);
		testP.add(skuLocationTf);
		testP.add(realinNumJl);
		testP.add(realinNumTf);
		centerP.add(testP, BorderLayout.NORTH);
		mainP.add(centerP, BorderLayout.CENTER);

		southP.add(inBtn);
		mainP.add(southP, BorderLayout.SOUTH);

	}

	public void locationSetting2() {// 재고위치정보 세팅

		northP.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 0));
		northP.add(new JLabel("추가/삭제 재고위치 >>"));
		northP.add(loTf);
		northP.add(seaBtn);
		northP.add(addBtn);
		northP.add(delBtn);
		mainP.add(northP, BorderLayout.NORTH);

		title = new Vector<>();
		data = new Vector<>();
		model = new DefaultTableModel();
		result = getData();
		title.add("재고위치");
		title.add("적재재고수량");
		model.setDataVector(result, title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		mainP.add(sp, BorderLayout.WEST);
		result = null;
	}

	// 모드 패널 리셋
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

	public void creatComboBox() {
		Vector orderlist = new Vector<>();
		try {
			rs = l.stmt.executeQuery("select * from iohistory where complete ='yet' and oder_kind ='입고'");
			while (rs.next()) {
				String orderNum = rs.getString("ordernum");
				orderlist.add(orderNum);
			}
			orderCombo = new JComboBox<String>(orderlist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Vector getData() { // 재고위치 정보 저장
		data.clear();
		try {
//			rs = l.stmt.executeQuery("select * from locationdb");
			rs = l.stmt.executeQuery(
					"select * from locationdb l left join (select sku_location, SUM(sku_finalnum) as total from listdb group by sku_location) "
							+ "s on l.sku_location = s.sku_location");
			while (rs.next()) {
				Vector in = new Vector<String>();
				String sku_location = rs.getString("sku_location");
				int total = rs.getInt("total");
				in.add(sku_location);
				in.add(total);
				data.add(in);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public Vector searchData() { // 재고위치 정보 저장
		data.clear();
		try {
//			rs = l.stmt.executeQuery("select * from locationdb where sku_location like '%" + loTf.getText() + "%'");
			rs = l.stmt.executeQuery(
					"select * from locationdb l left join (select sku_location, SUM(sku_finalnum) as total from listdb group by sku_location) "
							+ "s on l.sku_location = s.sku_location where l.sku_location like '%" + loTf.getText()
							+ "%'");
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					Vector in = new Vector<String>();
					String sku_location = rs.getString("sku_location");
					int total = rs.getInt("total");
					in.add(sku_location);
					in.add(total);
					data.add(in);
				}
			} else {
				JOptionPane.showMessageDialog(null, "검색한 정보가 없습니다", "알림", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public void addData() {
		if (loTf.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "입력값을 확인 해주세요", "알림", 1);
		} else {
			try {
				rs = l.stmt.executeQuery("select * from locationdb where sku_location = '" + loTf.getText() + "'");
				if (rs.isBeforeFirst()) {
					JOptionPane.showMessageDialog(null, "이미 존재하는 정보입니다", "알림", JOptionPane.ERROR_MESSAGE);
				} else {
					pstmtInsert = l.conn.prepareStatement("insert into locationdb values (?)");
					pstmtInsert.setString(1, loTf.getText());
					pstmtInsert.executeUpdate();
					loTf.setText("");
					JOptionPane.showMessageDialog(null, "새로운 재고위치가 추가 되였습니다", "알림", 1);
					result = getData();
					model.setDataVector(result, title);
					result = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void delData() {
		try {

			rs = l.stmt.executeQuery("select * from listdb where sku_location = '" + loTf.getText() + "'");
			if (rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(null, "현재 사용중인 재고위치입니다", "알림", JOptionPane.ERROR_MESSAGE);
			} else {
				rs = l.stmt.executeQuery("select * from locationdb where sku_location = '" + loTf.getText() + "'");
				if (rs.isBeforeFirst()) {
					pstmtDelete = l.conn.prepareStatement("delete from locationdb where sku_location = ?");
					pstmtDelete.setString(1, loTf.getText());
					pstmtDelete.executeUpdate();
					loTf.setText("");
					JOptionPane.showMessageDialog(null, "삭제가 되였습니다", "알림", 1);
					result = getData();
					model.setDataVector(result, title);
					result = null;
				} else {
					JOptionPane.showMessageDialog(null, "삭제할 내용이 존재하지 않습니다", "알림", JOptionPane.ERROR_MESSAGE);
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Vector allData() {
		data.clear();
		try {
			rs = l.stmt.executeQuery("select * from productlist order by sku_code");
			while (rs.next()) {
				Vector in = new Vector<String>(); //
				String sku_code = rs.getString("sku_code");
				String sku_name = rs.getString("sku_name");
				String sku_kind = rs.getString("sku_kind");
				in.add(sku_code);
				in.add(sku_name);
				in.add(sku_kind);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

	private class btnAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (seaBtn == e.getSource()) {
				result = searchData();
				model.setDataVector(result, title);
				result = null;
			} else if (addBtn == e.getSource()) {
				addData();
			} else if (delBtn == e.getSource()) {
				delData();
			}

		}

	}

	// 발주서 추가
	public void creat(String sku, String name, String kind, int num, String order) {
		try {
			pstmtInsert = l.conn.prepareStatement(
					"insert into iohistory(sku_code,sku_name,sku_kind,ordernum,ex_num,oder_kind,complete,sku_location) values( ?,? ,"
							+ "? ,?,?,?,?,?)");
			pstmtInsert.setString(1, sku);
			pstmtInsert.setString(2, name);
			pstmtInsert.setString(3, kind);
			pstmtInsert.setString(4, order);
			pstmtInsert.setInt(5, num);
			pstmtInsert.setString(6, "입고");
			pstmtInsert.setString(7, "yet");
			pstmtInsert.setString(8, locationCombo.getSelectedItem().toString());
			pstmtInsert.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "입고예정수량을 확인해주세요", "알림", JOptionPane.ERROR);
			e.printStackTrace();
		}

	}

	// 콤보박스 제작
	public void creatCombo() {
		Vector list = new Vector<>();
		try {
			rs = l.stmt.executeQuery("select * from locationdb order by sku_location");
			while (rs.next()) {
				String location = rs.getString("sku_location");
				list.add(location);
			}
			locationCombo = new JComboBox<String>(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
