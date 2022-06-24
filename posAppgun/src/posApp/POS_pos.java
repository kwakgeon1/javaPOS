package posApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class POS_pos extends JPanel implements ActionListener {

	private JButton buttonAdd, btnDB, btnPay, btnCancel;
	private JLabel lblItem, lblStock, lblTotal;
	private JTextField txtStock, txtTotal;
	private JTable tableModel;
	private JComboBox comboModel;
	DefaultComboBoxModel<String> dcm;
	DefaultTableModel model;
	int total;

	public POS_pos() {

		// 자동 배치 레이아웃 비활성화
		setLayout(null);
		// 각 컴포넌트 객체 생성 및 화면 배치/크기 조정
		btnDB = new JButton("제품 불러오기");
		btnDB.setBounds(20, 20, 140, 40);
		// JPanel에 추가
		add(btnDB);
		// 이벤트 처리를 위한 리스너 등록
		btnDB.addActionListener(this);

		lblItem = new JLabel("상품");
		lblItem.setBounds(20, 90, 100, 30);
		add(lblItem);

		DefaultTableModel model = new DefaultTableModel() {
			public boolean inCellEditable(int i, int c) {
				return false;
			}
		};
		model.addColumn("이름");
		model.addColumn("수량");
		model.addColumn("가격");
		model.addColumn("총가격");

		tableModel = new JTable(model);
		JScrollPane jscroll = new JScrollPane(tableModel);
		jscroll.setBounds(300, 20, 210, 200);
		add(jscroll);

		comboModel = new JComboBox();
		comboModel.setBounds(70, 90, 200, 30);
		add(comboModel);

		lblItem = new JLabel("수량");
		lblItem.setBounds(20, 140, 100, 30);
		add(lblItem);

		txtStock = new JTextField();
		txtStock.setBounds(70, 140, 200, 30);
		add(txtStock);

		lblTotal = new JLabel("총가격");
		lblTotal.setBounds(20, 250, 100, 40);
		add(lblTotal);

		txtTotal = new JTextField();
		txtTotal.setBounds(70, 250, 200, 40);
		add(txtTotal);
		txtTotal.setEditable(false);

		buttonAdd = new JButton("추가");
		buttonAdd.setBounds(170, 190, 100, 40);
		add(buttonAdd);
		buttonAdd.addActionListener(this);

		btnPay = new JButton("결제");
		btnPay.setBounds(300, 250, 100, 40);
		add(btnPay);
		btnPay.addActionListener(this);

		btnCancel = new JButton("취소");
		btnCancel.setBounds(410, 250, 100, 40);
		add(btnCancel);
		btnCancel.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 이벤트 객체로부터 텍스트 가져오기
		String gun = e.getActionCommand();
		// 제품명, 재고량, 가격 저장할 지역변수 선언 및 초기화
		String name, stock, price = "";
		// [제품 불러오기] 버튼 클릭 시
		if (gun == "제품 불러오기") {
			// comboBox의 모든 데이터 요소 삭제(removeAllItems());
			comboModel.removeAllItems();
			// DB로부터 상품명 전체 검색 및 Vector에 저장
			dcm = (DefaultComboBoxModel<String>) comboModel.getModel();
			try {
				Vector<Item> itemlist = ItemDAO.getInstance().getAllItem();
				for (Item item : itemlist) {
					String item_name = item.getItem_name();
					Vector<String> in = new Vector<String>();
					in.add(item_name);
					dcm.addElement(in.get(0));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Vector<String> vdb = new Vector<String>();
			// Vector에 저장한 상품명을 comboBox에 추가

		} // [추가] 버튼 클릭 시
		else if (gun == "추가") {
			model = (DefaultTableModel) tableModel.getModel();
			// comboBox에서 선택한 상품명과 텍스트필드에 입력한 수량 저장
			String totalprice;
			name = comboModel.getSelectedItem().toString();
			stock = txtStock.getText();
			// DB로부터 사용자가 선택한 상품명의 단가 불러오기
			price = ItemDAO.getInstance().getprice(name);
			// 사용자가 선택한 상품의 구매가격(단가*수량)과 누적 총액 연산하기
			totalprice = String.valueOf(Integer.parseInt(stock) * Integer.parseInt(price));
			total += Integer.parseInt(totalprice);
			txtTotal.setText(total + "");
			// 상품명, 구매수량, 구매가격, 누적총액을 Vector에 저장
			Vector<String> in1 = new Vector<String>();
			in1.add(name);
			in1.add(stock);
			in1.add(price);
			in1.add(totalprice);
			model.addRow(in1);
			// Vector 객체를 tableModel에 추가

		} // [결재] 버튼 클릭 시
		else if (gun == "결제") {
			// "결재하시겠습니까?"라는 다이얼로그 창 출력(JOptionPane.showConfirmDialog())
			int gun2 = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?");
			if (gun2 == 0) {
				int gun3 = Integer.parseInt(JOptionPane.showInputDialog(null, "총금액은" + total + "입니다"));
				if (gun3 >= total) {
					JOptionPane.showMessageDialog(null, "지불하신 금액은" + gun3 + "이고\n" + "상품의 합계는" + total + "이며,\n"
							+ "거스름돈은" + (gun3 - total) + "입니다");
					stockUpdate(model);
				} else {
					JOptionPane.showMessageDialog(null, "금액이 적습니다");
				}
				clean();
			}
			// "YES"를 누르면 "총금액은 ~입니다"를 출력한 후 사용자로부터 숫자 입력받기(JOptionPane.showInputDialog())

			// 사용자 입력금액이 총금액보다 크면 "지불금액,거스름돈"을 출력한 후 DB 업데이트(stockUpdate), 모든 컴포넌트 내의 데이터
			// 초기화(clean())

			// 그렇지 않으면 "금액이 적습니다" Dialog 창 출력

		} // [취소] 버튼 클릭 시
		else {
			int gun2 = JOptionPane.showConfirmDialog(null, "주문을 취소하시겠습니까");
			if (gun2 == 0) {
				clean();
			}
			// "주문을 취소하시겠습니까?" Dialog 창 출력
			// 모든 컴포넌트의 데이터 초기화
		}
	}

	// JTable, 수량과 총가격의 JTextField 내 데이터 초기화
	public void clean() {
		int rows = model.getRowCount();
		for (int i = rows - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		total = 0;
		txtStock.setText("");
		txtTotal.setText("0원");
	}

	// JTable에 출력된 모든 데이터의 상품명, 재고량, 가격을 이용하여 DB 데이터 업데이트
	public void stockUpdate(DefaultTableModel model) {
		int inputMoney;

		String product_stock[] = new String[model.getRowCount()];

		// "YES"를 누르면 "총금액은 ~입니다"를 출력한 후 사용자로부터 숫자 입력받기(JOptionPane.showInputDialog())

		int count = model.getRowCount();

		for (int i = 0; i < count; i++) {
			try {
				product_stock[i] = ItemDAO.getInstance().getStock(model.getValueAt(i, 0).toString());
				ItemDAO.getInstance().updateStock(product_stock[i], model.getValueAt(i, 1).toString(),model.getValueAt(i, 0).toString());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}