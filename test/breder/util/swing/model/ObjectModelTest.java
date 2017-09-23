package breder.util.swing.model;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.model.IObjectModel;
import breder.util.swing.table.BFilterTable;

public class ObjectModelTest {

	@SuppressWarnings("unchecked")
	public static void main(IObjectModel<?> model) {
		LookAndFeel.getInstance().installNative();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		BFilterTable table = new BFilterTable(model);
		frame.add(table.getComponent(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
