package MyProject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SubBtnListener3 extends JFrame {
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
	private Vector data = new Vector<>();
	private Vector title = new Vector<>();
	private JTable table = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private Vector result;

////////////////////////////상품정보조회//////////////////////////////////////	
	private JLabel skuName1 = new JLabel("제품명:");
	private JLabel skuName2 = new JLabel("제품명:");
	private JLabel check = new JLabel("(Enter로 중복체크)");
	private JLabel skuKind = new JLabel("분류:");
	private JLabel skuCode = new JLabel("SKU번호:");
	private JTextField searchTf = new JTextField(20);
	private JTextField skucodeTf = new JTextField(10);
	private JTextField skunameTf = new JTextField(10);
	private JTextField skukindTf = new JTextField(10);
	private JButton searchBtn = new JButton("검색");
	private JButton addBtn = new JButton("추가");
	private JButton upBtn = new JButton("수정");
	private JButton delBtn = new JButton("삭제");
	private JButton resetBtn = new JButton("초기화");
	private boolean popup = true;
	private boolean popup1 = true;
	private String skucode = null;
	private String skuname = null;
	private String skukind = null;
////////////////////////////ID정보생성//////////////////////////////////////	
	private JLabel id = new JLabel("ID>>", JLabel.CENTER);
	private JLabel pw = new JLabel("PW>>", JLabel.CENTER);
	private JLabel workName = new JLabel("이름>>", JLabel.CENTER);
	private JLabel workAge = new JLabel("나이>>", JLabel.CENTER);
	private JTextField idTf = new JTextField(10);
	private JTextField pwTf = new JTextField(10);
	private JTextField workNameTf = new JTextField(10);
	private JTextField workAgeTf = new JTextField(10);
	private Vector data1 = new Vector<>();
	private JTable table1 = new JTable();
	private JButton saveBtn = new JButton("저장");
	private DefaultTableModel model1 = null;
	private String[] t = { "아이디", "이름" };
	private JComboBox titleCombo = new JComboBox<String>(t);
	private int row = -1; // 행미선택시 디폴트 값이 -1임;

	public SubBtnListener3(JPanel mainP, String text, String loginid, JFrame frame) {
		this.mainP = mainP;
		this.text = text;
		this.loginid = loginid;
		l = (LoginUi) frame;
		mainP.setLayout(new BorderLayout());
	
/////////////////////////////////상품정보조회//////////////////////////////////////		
		if (text.equals("상품정보조회")) {
			resetP();
			locationSetting1();
////////////////////////////////추가 버튼
			addBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String code = skucodeTf.getText();
					String name = skunameTf.getText();
					String kind = skukindTf.getText();
					if (code.trim().isEmpty() || name.trim().isEmpty() || kind.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "입력값을 확인해주세요", "에러", JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						add(code, name, kind, "");
						result = allData();
						model.setDataVector(result, title);
						if (popup) {
							JOptionPane.showMessageDialog(null, "추가 되였습니다", "알림", JOptionPane.DEFAULT_OPTION);
						}
					}
				}
			});
////////////////////////////////검색 버튼
			searchBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String search = searchTf.getText();
					if (search.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "검색할 내용을 입력해주세요", "알림", JOptionPane.DEFAULT_OPTION);
						return;
					} else {
						result = search(search);
						model.setDataVector(result, title);
					}
				}
			});
////////////////////////////////리셋 버튼		
			resetBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					skucodeTf.setText("");
					skunameTf.setText("");
					skukindTf.setText("");
					skucodeTf.setEnabled(true);
					skunameTf.setEnabled(true);
					skukindTf.setEnabled(true);
				}
			});
////////////////////////////////삭제 버튼
			delBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					delete(skucode);
					skucode = null;
					result = allData();
					model.setDataVector(result, title);
				}
			});
////////////////////////////////수정 버튼			
			upBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String code = skucodeTf.getText();
					String name = skunameTf.getText();
					String kind = skukindTf.getText();
					if (code.trim().isEmpty() || name.trim().isEmpty() || kind.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "빈칸을 채워주세요", "알림", JOptionPane.DEFAULT_OPTION);
					} else {
						update(code, name, kind);
						result = allData();
						model.setDataVector(result, title);
						if (popup1) {
							JOptionPane.showMessageDialog(null, "수정되였습니다", "알림", JOptionPane.DEFAULT_OPTION);
						}
					}
				}
			});
		}
///////////////////////////////ID정보조회//////////////////////////////////////
		if (text.equals("ID정보관리")) {
			resetP();
			locationSetting2();
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 마우스 더블 클릭하면 데이터 전송
					row = table.getSelectedRow();
				}
			});
////////////////////////////////검색 버튼			
			searchBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String search = searchTf.getText();
					if (search.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "검색할 내용을 입력해주세요", "알림", JOptionPane.DEFAULT_OPTION);
						return;
					} else {
						result = search(search);
						model1.setDataVector(result, title);
						tableCheckBox();
					}
				}
			});
////////////////////////////////수정 버튼	
			upBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					update("", "", "");
					result = getData();
					model1.setDataVector(result, title);
					tableCheckBox();
				}
			});
////////////////////////////////추가 버튼
			addBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String id = idTf.getText();
					String pw = pwTf.getText();
					String name = workNameTf.getText();
					String age = workAgeTf.getText();
					if (id.trim().isEmpty() || pw.trim().isEmpty() || name.trim().isEmpty() || age.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "입력값을 확인해주세요", "에러", JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						add(id, pw, name, age);
						result = getData();
						model1.setDataVector(result, title);
						tableCheckBox();
					}
				}
			});
////////////////////////////////삭제 버튼			
			delBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					delete("");
					result = getData();
					model1.setDataVector(result, title);
					tableCheckBox();
				}
			});
			
		}

	}

////////////////////////////////상품리스트 조회 세팅
	public void locationSetting1() {
		title.clear();
		table.removeAll();
		northP.add(skuName1);
		northP.add(searchTf);
		northP.add(searchBtn);
		mainP.add(northP, BorderLayout.NORTH);
		title.add("SKU번호");
		title.add("제품명");
		title.add("분류");
		title.add("보유 재고수량");
		result = allData();
		model.setDataVector(result, title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		mainP.add(sp, BorderLayout.CENTER);
		southP.add(skuCode);
		southP.add(skucodeTf);
		southP.add(skuName2);
		southP.add(skunameTf);
		southP.add(skuKind);
		southP.add(skukindTf);
		southP.add(addBtn);
		southP.add(upBtn);
		southP.add(delBtn);
		southP.add(resetBtn);
		mainP.add(southP, BorderLayout.SOUTH);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 더블 클릭하면 데이터 전송
				int index = table.getSelectedRow();
				Vector in = (Vector) data.get(index);
				skucode = (String) in.get(0);
				skuname = (String) in.get(1);
				skukind = (String) in.get(2);
				skucodeTf.setText(skucode);
				skunameTf.setText(skuname);
				skukindTf.setText(skukind);
				skucodeTf.setEnabled(false);
			}
		});
		result = null;

	}

////////////////////////////////작업자 id생성 세팅
	public void locationSetting2() {
		title.clear();
		table.removeAll();
		title.add("아이디");
		title.add("비밀번호");
		title.add("이름");
		title.add("나이");
		title.add("발주서생성권한");
		title.add("출고오더생성권한");
		result = getData();
		model1 = new DefaultTableModel();

		model1.setDataVector(result, title);
		table = new JTable(model1) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return false; // 컬럼 첫열만 수정가능으로 설정
				} else {
					return true; // 기타 컬럼열은 수정불가
				}
			}
		};

//		TableColumn tc = table.getColumnModel().getColumn(0);
//		tc.setCellEditor(table.getDefaultEditor(Boolean.class));
//		tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));

		tableCheckBox();

		JScrollPane sp = new JScrollPane(table);
		mainP.add(sp, BorderLayout.CENTER);

		northP.setLayout(new FlowLayout(FlowLayout.LEFT));
		northP.add(titleCombo);
		northP.add(searchTf);
		northP.add(searchBtn);
		northP.add(upBtn);
//		northP.add(saveBtn);
		northP.add(delBtn);
		mainP.add(northP, "North");

		southP.add(id);
		southP.add(idTf);
		southP.add(pw);
		southP.add(pwTf);
		southP.add(workName);
		southP.add(workNameTf);
		southP.add(workAge);
		southP.add(workAgeTf);
		southP.add(addBtn);
		mainP.add(southP, BorderLayout.SOUTH);

		result = null;
	}

	// 체크박스 정렬을 지정해준다
	DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent //
		(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JCheckBox box = new JCheckBox();
			box.setSelected(((Boolean) value).booleanValue());
			box.setHorizontalAlignment(JLabel.CENTER);
			return box;
		}
	};
////////////////////////////전체 패널 리셋
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

////////////////////////////////추가 메소드
	public void add(String a, String b, String c, String age) {
		if (text.equals("상품정보조회")) {
			try {
				pstmtInsert = l.conn
						.prepareStatement("insert into productlist(sku_code,sku_name,sku_kind) values(?,?,?)");
				pstmtInsert.setString(1, a);
				pstmtInsert.setString(2, b);
				pstmtInsert.setString(3, c);
				pstmtInsert.executeUpdate();
				popup = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "이미존재하는 SKU번호입니다", "에러", JOptionPane.ERROR_MESSAGE);
				popup = false;
				e.printStackTrace();
			}
		} else {
			try {
				pstmtInsert = l.conn.prepareStatement(
						"insert into workerid (id,pw,worker_name,worker_age,pow_inorder,pow_outorder) values(?,?,?,?,?,?)");
				pstmtInsert.setString(1, a);
				pstmtInsert.setString(2, b);
				pstmtInsert.setString(3, c);
				pstmtInsert.setString(4, age);
				pstmtInsert.setBoolean(5, false);
				pstmtInsert.setBoolean(6, false);
				pstmtInsert.executeUpdate();
//				popup = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "이미존재하는 아이디입니다", "에러", JOptionPane.ERROR_MESSAGE);
//				popup = false;
				e.printStackTrace();
			}
		}
	}

////////////////////////////////삭제 메소드
	public void delete(String code) {
		if (text.equals("상품정보조회")) {
			try {
				rs = l.stmt.executeQuery("select * from listdb where sku_code ='" + code + "'");
				if (rs != null && rs.isBeforeFirst()) {
					JOptionPane.showMessageDialog(null, "현재 사용중인 코드입니다", "에러", JOptionPane.ERROR_MESSAGE);
				} else {
					pstmtDelete = l.conn.prepareStatement("delete from productlist where sku_code = ?");
					pstmtDelete.setString(1, code);
					pstmtDelete.executeUpdate();
					rs = l.stmt.executeQuery("select * from productlist where sku_code ='" + code + "'");
					if (rs != null && rs.isBeforeFirst()) {
						return;
					} else {
						JOptionPane.showMessageDialog(null, "삭제되였습니다", "알림", JOptionPane.DEFAULT_OPTION);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (row >= 0) { //
				Vector in = (Vector) data.get(row);
				try {
					pstmtDelete = l.conn.prepareStatement("delete from workerid where id = ?");
					pstmtDelete.setString(1, (String) in.get(0));
					pstmtDelete.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

////////////////////////////////수정 메소드
	public void update(String code, String name, String kind) {
		if (text.equals("상품정보조회")) {
			try {
				pstmtUpdate = l.conn
						.prepareStatement("update productlist set sku_name = ?, sku_kind = ? where sku_code = ?");
				pstmtUpdate.setString(3, code);
				pstmtUpdate.setString(1, name);
				pstmtUpdate.setString(2, kind);
				pstmtUpdate.executeUpdate();
				popup1 = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "없는 SKU번호입니다", "에러", JOptionPane.ERROR_MESSAGE);
				popup1 = false;
				e.printStackTrace();
			}
		} else {
			try {
				boolean exp = true;
				if (row >= 0) { //
					Vector in = (Vector) data.get(row);
					pstmtUpdate = l.conn.prepareStatement("update workerid set pw = ?, worker_name = ? ,worker_age = ?,"
							+ "pow_inorder= ?,pow_outorder = ? where id = ?");
					pstmtUpdate.setString(1, ((String) in.get(1)).trim());
					pstmtUpdate.setString(2, ((String) in.get(2)).trim());
					pstmtUpdate.setString(3, ((String) in.get(3)).trim());
					pstmtUpdate.setBoolean(4, (boolean) in.get(4));
					pstmtUpdate.setBoolean(5, (boolean) in.get(5));
					pstmtUpdate.setString(6, (String) in.get(0));

					for (int i = 0; i < in.size(); i++) {
						if (in.get(i).equals("")) {
							exp = false;
						}
					}
					if (exp) {
						pstmtUpdate.executeUpdate();
					} else {
						JOptionPane.showMessageDialog(null, "빈칸을 채줘주세요", "알림", 1);
					}
				}
//				popup1 = true;
			} catch (Exception e) {
//				popup1 = false;
				JOptionPane.showMessageDialog(null, "입력한내용을 학인해주세요", "알림", 1);
				e.printStackTrace();
			}

		}
	}

////////////////////////////////검색 메소드
	public Vector search(String search) {
		data.clear();
		try {
			if (text.equals("상품정보조회")) {
				rs = l.stmt.executeQuery("select * from productlist p left join (select sku_code, SUM(sku_finalnum) as "
						+ "total from listdb group by sku_code) s on p.sku_code = s.sku_code where sku_name like '%"
						+ search + "%'");
				while (rs.next()) {
					Vector in = new Vector<String>(); //
					String sku_code = rs.getString("sku_code");
					String sku_name = rs.getString("sku_name");
					String sku_kind = rs.getString("sku_kind");
					String total_num = rs.getString("total");
					in.add(sku_code);
					in.add(sku_name);
					in.add(sku_kind);
					in.add(total_num);
					data.add(in);
				}
			} else { // if (text.equals("ID정보관리"))
				String select = titleCombo.getSelectedItem().toString();
				if (select.equals("아이디"))
					rs = l.stmt.executeQuery("select * from workerid  where id like '%" + search + "%'");
				else
					rs = l.stmt.executeQuery("select * from workerid  where worker_name like '%" + search + "%'");
				while (rs.next()) {
					Vector in = new Vector<Object>(); //
					String id = rs.getString("id");
					String pw = rs.getString("pw");
					String name = rs.getString("worker_name");
					String age = rs.getString("worker_age");
					boolean powin = rs.getBoolean("pow_inorder");
					boolean powout = rs.getBoolean("pow_outorder");
					in.add(id);
					in.add(pw);
					in.add(name);
					in.add(age);
					in.add(powin);
					in.add(powout);
					data.add(in);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

////////////////////////////////전체 데이터 가져오기 메소드
	public Vector allData() {
		data.clear();
		try {
			rs = l.stmt.executeQuery("select * from productlist p left join (select sku_code, SUM(sku_finalnum) as "
					+ "total from listdb group by sku_code) s on p.sku_code = s.sku_code");
			while (rs.next()) {
				Vector in = new Vector<String>(); //
				String sku_code = rs.getString("sku_code");
				String sku_name = rs.getString("sku_name");
				String sku_kind = rs.getString("sku_kind");
				String total_num = rs.getString("total");
				in.add(sku_code);
				in.add(sku_name);
				in.add(sku_kind);
				in.add(total_num);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

//////////////////////////////// 작업자 아이디정보 가져오기 메소드	
	public Vector getData() {
		data.clear();
		try {
			rs = l.stmt.executeQuery("select * from workerid order by id ");
			while (rs.next()) {
				Vector in = new Vector<Object>(); //
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String name = rs.getString("worker_name");
				String age = rs.getString("worker_age");
				boolean powin = rs.getBoolean("pow_inorder");
				boolean powout = rs.getBoolean("pow_outorder");
//				in.add(new Boolean(false));
				in.add(id);
				in.add(pw);
				in.add(name);
				in.add(age);
				in.add(powin);
				in.add(powout);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

/////////////////////////////////테이블체크박스
	public void tableCheckBox() {

		table.getColumn("발주서생성권한").setCellRenderer(dcr);
		JCheckBox box1 = new JCheckBox();
		box1.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("발주서생성권한").setCellEditor(new DefaultCellEditor(box1));

		table.getColumn("출고오더생성권한").setCellRenderer(dcr);
		JCheckBox box2 = new JCheckBox();
		box2.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("출고오더생성권한").setCellEditor(new DefaultCellEditor(box2));
	}
}
