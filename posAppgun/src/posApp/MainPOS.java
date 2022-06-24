package posApp;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainPOS extends JFrame {
		public POS_pos pos = null;
		public POS_StockManagerment stockManagerment = null;
	
	public static void main(String[] args) {
		MainPOS mainPOS = new MainPOS();
		mainPOS.setTitle("POS system");
		
		mainPOS.pos = new POS_pos();
		mainPOS.stockManagerment = new POS_StockManagerment();
		
		
		JTabbedPane jtab = new JTabbedPane();
		jtab.add("POS", mainPOS.pos);
		jtab.add("재고관리", mainPOS.stockManagerment);
		
		mainPOS.add(jtab);
		mainPOS.setSize(550, 400);
		mainPOS.setVisible(true);
	}
}
