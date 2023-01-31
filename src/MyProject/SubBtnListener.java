package MyProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class SubBtnListener extends JFrame {
	private JPanel jp = new JPanel();// 메인 패널
	private ResultSet rs = null;
	private String text = null;
	private Vector data = null;
	private Vector title = null;
	private JTable table = new JTable();
	private DefaultTableModel model = null;
	private Vector result;

	// 조회 메뉴하 재고현황조회,입출고이력,메모 수정 레이블 과 버튼
	private JLabel searchJl1 = new JLabel("SKU코드");
	private JLabel searchJl2 = new JLabel("제품명");
	private JTextField searchTf1 = new JTextField(20);
	private JTextField searchTf2 = new JTextField(20);
	private JButton searchBtn = new JButton("검색");
	private JButton memoupBtn = new JButton("메모수정");
	private JButton excelBtn = new JButton("리스트 다운로드");
	private JTextField memoupTf = new JTextField(20);
	private JPanel upPanel = new JPanel();
	private PreparedStatement pstmtUpdate = null;
	private LoginUi l;
	// 입출고 내역 필요 객체 선언;
	private JRadioButton[] radio = new JRadioButton[3];
	private String[] colName = { "전체", "입고", "출고" };
	private JPanel centerPanel = new JPanel();
	private JPanel northP = new JPanel();
	private JPanel southP = new JPanel();
	private String[] t = { "SKU코드", "제품명" };
	private JComboBox titleCombo = new JComboBox<String>(t);
	private JCheckBox yetCk = new JCheckBox("작업미완료");
	private JCheckBox overCk = new JCheckBox("작업완료");
	private int row = -1; // 행미선택시 디폴트 값이 -1임;

	public SubBtnListener(JPanel jp, String text, String loginid, JFrame frame) {
		// TODO Auto-generated constructor stub
		this.jp = jp;
		this.text = text;
		l = (LoginUi) frame;
		jp.setLayout(new BorderLayout());
		title = new Vector<>();
		data = new Vector<>();
		model = new DefaultTableModel();

		
		if (text.equals("재고현황조회")) {
			resetP();
			title.clear();
			locationSetting1();
			// 매번 행 클릭시 데이터 가져오기
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					row = table.getSelectedRow();
				}
			});
			//검색 필드 액션
			searchTf1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String search = searchTf1.getText();
					if (search.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "검색할 내용을 입력해주세요", "알림", JOptionPane.DEFAULT_OPTION);
						result = allData();
						model.setDataVector(result, title);
						return;
					} else {
						result = search(search);
						model.setDataVector(result, title);
					}
				}
			});
			//메모수정 버튼
			memoupBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					update();
				}
			});
			//엑셀 출력
			excelBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {  //출력완료후 알림이 필요함
					// TODO Auto-generated method stub
					File f = new File("data.xls"); //출력 위치 
					String[] str = {"SKU번호", "제품명", "분류", "재고위치", "수량", "메모"};
					//파일객체는 파일에대한 정보만 넘겨주는거기 때문에 예외처리 할필요 없음;
					try{
						WritableWorkbook wb = Workbook.createWorkbook(f);
						WritableSheet s1 = wb.createSheet("output data", 0); //시트명
						
						for(int i = 0; i < str.length; i++){
							Label label = new Label(i, 0, str[i]);       //카테고리
							s1.addCell(label);
//							Label label1 = new Label(1, i, "데이터.." + i);
//							s1.addCell(label1);
						}
						int i = 0;
						int j = 1;
						rs = l.stmt.executeQuery("select * from listdb order by sku_code");
						while (rs.next()) {
							Label sku_code = new Label(i++, j, rs.getString("sku_code"));  //i는각 셀 j행 
							s1.addCell(sku_code);									
							Label sku_name = new Label(i++, j, rs.getString("sku_name"));
							s1.addCell(sku_name);									
							Label sku_kind = new Label(i++, j, rs.getString("sku_kind"));
							s1.addCell(sku_kind);									
							Label sku_location = new Label(i++, j, rs.getString("sku_location"));
							s1.addCell(sku_location);									
							Label sku_finalnum = new Label(i++, j, rs.getString("sku_finalnum"));
							s1.addCell(sku_finalnum);									
							Label memo = new Label(i++, j, rs.getString("memo"));
							s1.addCell(memo);									
							i = 0;
							j++;
						}
						wb.write();		//반드시 적어줘야 엑셀에 적용이 됨;
						wb.close();
						JOptionPane.showMessageDialog(null, "엑셀 다운로드 완료 되였습니다", "알림", 1);
					} catch(Exception e2){
						System.out.println("Err : " + e2.getMessage());
					}
				}
			});
			
		} else if (text.equals("입출고 이력조회")) {
			resetP();
			locationSetting2();
		}

	}

	//데이터베이스에 수정내용 업그레이드 
	private void update() {
		try {
			if (row >= 0) {
				Vector in = (Vector) data.get(row);
				pstmtUpdate = l.conn.prepareStatement("update listdb set memo = ? where sku_code = ?");
				pstmtUpdate.setString(1, (String) in.get(5));
				pstmtUpdate.setString(2, (String) in.get(0));
				pstmtUpdate.executeUpdate();
				JOptionPane.showMessageDialog(null, "메모수정이 완료되였습니다", "알림", 1);
			} else {
				JOptionPane.showMessageDialog(null, "메모수정을 원하는 행을 선택해주세요", "알림", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 재고현황 모든 데이터 조회
	public Vector allData() {
		data.clear();
		try {
			if (text.equals("재고현황조회")) {
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
			} else { //레디오 버튼 작업롼료,미완료,입고,출고 구분
				if (radio[0].isSelected() && yetCk.isSelected() && overCk.isSelected())
					rs = l.stmt.executeQuery("select * from iohistory order by sku_code");
				else if ((radio[0].isSelected() && !yetCk.isSelected() && overCk.isSelected()))
					rs = l.stmt.executeQuery("select * from iohistory where complete = 'over' ");
				else if ((radio[0].isSelected() && yetCk.isSelected() && !overCk.isSelected()))
					rs = l.stmt.executeQuery("select * from iohistory where complete = 'yet'");

				else if (radio[1].isSelected() && yetCk.isSelected() && overCk.isSelected())
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '입고'");
				else if (radio[1].isSelected() && !yetCk.isSelected() && overCk.isSelected())
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '입고' and complete = 'over'");
				else if (radio[1].isSelected() && yetCk.isSelected() && !overCk.isSelected())
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '입고' and complete = 'yet'");

				else if ((radio[2].isSelected() && yetCk.isSelected() && overCk.isSelected()))
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '출고'");
				else if ((radio[2].isSelected() && !yetCk.isSelected() && overCk.isSelected()))
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '출고'and complete = 'over'");
				else if ((radio[2].isSelected() && yetCk.isSelected() && !overCk.isSelected()))
					rs = l.stmt.executeQuery("select * from iohistory where oder_kind = '출고' and complete = 'yet'");
				while (rs.next()) {
					Vector in = new Vector<String>(); //
					String sku_code = rs.getString("sku_code");
					String sku_name = rs.getString("sku_name");
					String sku_kind = rs.getString("sku_kind");
					String ordernum = rs.getString("ordernum");
					String orderkind = rs.getString("oder_kind");
					String work_date = rs.getString("work_date");
					String ex_num = rs.getString("ex_num");
					String realnum = rs.getString("realnum");
					String worker_id = rs.getString("worker_id");
					String complete = rs.getString("complete");
					String sku_location = rs.getString("sku_location");
					in.add(sku_code);
					in.add(sku_name);
					in.add(sku_kind);
					in.add(ordernum);
					in.add(orderkind);
					in.add(work_date);
					in.add(ex_num);
					in.add(realnum);
					in.add(worker_id);
					in.add(complete);
					in.add(sku_location);
					data.add(in);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

	// 재고현황 테이블 세팅 
	public void locationSetting1() {
		table.removeAll();
		title.add("SKU번호");
		title.add("제품명");
		title.add("분류");
		title.add("재고위치");
		title.add("수량");
		title.add("메모");

		northP.add(titleCombo);
		northP.add(searchTf1);
		northP.add(excelBtn);
		northP.add(memoupBtn);

		result = allData();// 데이터 값 가져오기
		model.setDataVector(result, title);
		table = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 5) {
					return true; // 메모컬럼만 수정가능
				} else {
					return false; // 기타 컬럼열은 수정불가
				}
			}
		};
		JScrollPane sp = new JScrollPane(table);
		jp.add(northP, BorderLayout.NORTH);
		jp.add(sp, BorderLayout.CENTER);
		result = null;
	}

	// 입출고 이력 테이블 세팅
	public void locationSetting2() {
		ButtonGroup g = new ButtonGroup();
		for (int i = 0; i < radio.length; i++) {
			radio[i] = new JRadioButton(colName[i]);
			g.add(radio[i]);
			upPanel.add(radio[i]);
			radio[i].addItemListener(new radioListener());
		}
		radio[0].setSelected(true);
		yetCk.setSelected(true);
		overCk.setSelected(true);
		yetCk.addItemListener(new checkBoxListener());
		overCk.addItemListener(new checkBoxListener());

		upPanel.add(overCk);
		upPanel.add(yetCk);
		jp.add(upPanel, BorderLayout.NORTH);
		title.clear();
		table.removeAll();
		String[] colName1 = { "SKU코드", "제품명", "종류", "오더번호", "입/출", "작업일자", "예정수량", "실제수량", "작업자", "작업현황", "재고위치" };
		for (int i = 0; i < colName1.length; i++) {
			title.add(colName1[i]);
		}
		result = allData();
		model.setDataVector(result, title);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		jp.add(sp, BorderLayout.CENTER);
	}

	public Vector search(String search) {
		data.clear();
		try {
			String select = titleCombo.getSelectedItem().toString();
			if (select.equals("SKU코드"))
				rs = l.stmt.executeQuery("select * from listdb  where sku_code like '%" + search + "%'");
			else
				rs = l.stmt.executeQuery("select * from listdb  where sku_name like '%" + search + "%'");
			while (rs.next()) {
				Vector in = new Vector<Object>(); //
				String code = rs.getString("sku_code");
				String name = rs.getString("sku_name");
				String kind = rs.getString("sku_kind");
				String location = rs.getString("sku_location");
				int num = rs.getInt("sku_finalnum");
				String memo = rs.getString("memo");
				in.add(code);
				in.add(name);
				in.add(kind);
				in.add(location);
				in.add(num);
				in.add(memo);
				data.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}

	// 레디오 리스너
	class radioListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				return;
			}
			if (radio[0].isSelected()) {
				result = allData();
				model.setDataVector(result, title);
			} else if (radio[1].isSelected()) {
				result = allData();
				model.setDataVector(result, title);
			} else {
				result = allData();
				model.setDataVector(result, title);
			}
		}
	}

	//작업 완료 미완료 체크박스 리스너
	class checkBoxListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if (yetCk.isSelected() && overCk.isSelected()) {
				yetCk.setEnabled(true);
				overCk.setEnabled(true);
				result = allData();
				model.setDataVector(result, title);
			} else if (yetCk.isSelected() && !overCk.isSelected()) {
				yetCk.setEnabled(false);
				result = allData();
				model.setDataVector(result, title);
			} else {
				overCk.setEnabled(false);
				result = allData();
				model.setDataVector(result, title);
			}
		}

	}
	//모든 패널 리셋
	public void resetP() {
		jp.removeAll();
		upPanel.removeAll();
		northP.removeAll();
		centerPanel.removeAll();
		southP.removeAll();
		validate();
		jp.repaint();
		upPanel.repaint();
		centerPanel.repaint();
		northP.repaint();
		southP.repaint();
	}

}
