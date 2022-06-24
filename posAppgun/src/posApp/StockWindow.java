package posApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StockWindow extends JFrame implements ActionListener{

	JLabel labelId = new JLabel("ID");
	JLabel labelName = new JLabel("상품명");
	JLabel labelStock = new JLabel("재고");
	JLabel labelPrice = new JLabel("가격");
	
	JTextField textFieldId = new JTextField(10);
	JTextField textFieldName = new JTextField(10);
	JTextField textFieldStock = new JTextField(10);
	JTextField textFieldPrice = new JTextField(10);
	
	JButton buttonAccept = new JButton("");
	Item item;
	String text;
	
	public StockWindow(String text) {
		this.text = text;
		display();
		setSize(300, 300);
		setVisible(true);
	}
	
	public StockWindow(String text, Item item) {
		this.text = text;
		this.item = item;
		display();
		setSize(300, 300);
		setVisible(true);
	}
	
	public void display() {
		Container c = getContentPane();
		JPanel p = new JPanel(new GridLayout(4, 2));
		buttonAccept.setText(text);
		textFieldId.setEditable(false);
		
		if (item != null) {
			textFieldId.setText(String.valueOf(item.getId()));
			textFieldName.setText(item.getItem_name());
			textFieldStock.setText(String.valueOf(item.getItem_stock()));
			textFieldPrice.setText(String.valueOf(item.getItem_price()));
		}
		
		p.add(labelId);
		p.add(textFieldId);
		
		p.add(labelName);
		p.add(textFieldName);
		
		p.add(labelStock);
		p.add(textFieldStock);
		
		p.add(labelPrice);
		p.add(textFieldPrice);
		
		c.add(p, BorderLayout.CENTER);
		c.add(buttonAccept, BorderLayout.SOUTH);
		
		buttonAccept.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String state = buttonAccept.getText();
		String id;
		String name;
		String stock;
		String price;
		boolean result = false;
		
		switch(state) {
		case "등록" :
			name = textFieldName.getText();
			stock = textFieldStock.getText();
			price = textFieldPrice.getText();
			
			Item register = new Item();
			register.setItem_name(name);
			register.setItem_stock(Integer.parseInt(stock));
			register.setItem_price(Integer.parseInt(price));
			
			result = ItemDAO.getInstance().insertItem(register);
			dispose();
			break;
		case "수정" :
			id = textFieldId.getText();
			name = textFieldName.getText();
			stock = textFieldStock.getText();
			price = textFieldPrice.getText();
			
			item.setId(Integer.parseInt(id));
			item.setItem_name(name);
			item.setItem_stock(Integer.parseInt(stock));
			item.setItem_price(Integer.parseInt(price));
			
			result = ItemDAO.getInstance().updateitem(item);
			
			if (result) {
				JOptionPane.showMessageDialog(null, "수정에 성공하였습니다.", "성공!", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}else {
				JOptionPane.showMessageDialog(null, "수정에 실패하였습니다.", "경고!", JOptionPane.WARNING_MESSAGE);
			}
			break;
			
		case "삭제" :
			id = textFieldId.getText();
			name = textFieldName.getText();
			stock = textFieldStock.getText();
			price = textFieldPrice.getText();

			item.setId(Integer.parseInt(id));
			item.setItem_name(name);
			item.setItem_stock(Integer.parseInt(stock));
			item.setItem_price(Integer.parseInt(price));
			
			int res;
			
			res = JOptionPane.showConfirmDialog(null, "선택한 상품명" + name + "을 데이터베이스에서 삭제하시겠습니까?");
					
			if (res == 0) {
				ItemDAO.getInstance().deleteItem(Integer.parseInt(id));
				dispose();
				JOptionPane.showMessageDialog(null, "삭제하려는 " + name + "을 삭제했습니다.");
			} else {
				JOptionPane.showMessageDialog(null, "삭제를 취소했습니다.");
			}
			break;
			
			default :
				throw new IllegalStateException("Unexpected Value : " + state);
		}
	}
	
}
