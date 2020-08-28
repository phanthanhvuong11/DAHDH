import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

public class GUiBankerAlgorithm extends JFrame {

	private JPanel contentPane;
	private JTextField txtResult;
	private JTable tbNeed;
	private JTable tbMaximun;
	private JTable tbAllocated;
	private JTable tbAvailable;
	
	JButton btnResult;
	
	private static BankerAlgorithm bk ;
	private JTable tbP;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					bk = new BankerAlgorithm();
					GUiBankerAlgorithm frame = new GUiBankerAlgorithm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUiBankerAlgorithm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1106, 749);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		txtResult = new JTextField();
		txtResult.setEditable(false);
		txtResult.setBounds(12, 629, 1016, 36);
		contentPane.add(txtResult);
		txtResult.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Allocation");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(198, 13, 173, 37);
		contentPane.add(lblNewLabel);
		
		JLabel lblMaximum = new JLabel("Maximum");
		lblMaximum.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMaximum.setBounds(538, 13, 112, 37);
		contentPane.add(lblMaximum);
		
		JLabel lblNeed = new JLabel("Need");
		lblNeed.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNeed.setBounds(904, 13, 84, 37);
		contentPane.add(lblNeed);
		
		tbNeed = new JTable();
		tbNeed.setBounds(763, 52, 313, 274);
		contentPane.add(tbNeed);
		
		tbMaximun = new JTable();
		tbMaximun.setBounds(421, 52, 330, 274);
		contentPane.add(tbMaximun);
		
		tbAllocated = new JTable();
		tbAllocated.setBounds(66, 52, 343, 274);
		contentPane.add(tbAllocated);
		
		tbAvailable = new JTable();
		tbAvailable.setBounds(24, 396, 333, 68);
		contentPane.add(tbAvailable);
		
		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("C:\\Users\\DAVID-DIEP\\eclipse-workspace\\DANLHDH"));
				fc.setDialogTitle("Choose File");
				fc.setFileFilter(new FileNameExtensionFilter("txt file","txt"));
				fc.showOpenDialog(btnChooseFile);
				bk.initDataByFile(fc.getSelectedFile().getAbsolutePath());
				if(bk.calAndCheckNeed()) {
					tbAllocated.setModel(bk.addDataForTable(bk.getAllocatedData()));	
					tbNeed.setModel(bk.addDataForTable(bk.getNeedData()));	
					tbMaximun.setModel(bk.addDataForTable(bk.getMaximumData()));	
					tbAvailable.setModel(bk.addDataAvailable());
					tbP.setModel(bk.addDataProcesses());
					btnResult.setEnabled(true);
				}
				else {
					tbAllocated.setModel(bk.addDataForTable(bk.getAllocatedData()));	
					tbMaximun.setModel(bk.addDataForTable(bk.getMaximumData()));
					btnResult.setEnabled(false);
					JOptionPane.showMessageDialog(null, "Error data. Try again");
				}
			}
		});
		btnChooseFile.setBounds(316, 477, 104, 45);
		contentPane.add(btnChooseFile);
		
		JRadioButton radrandom = new JRadioButton("Random with exactly valies");
		radrandom.setBounds(789, 487, 183, 25);
		contentPane.add(radrandom);
		
		JButton btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tbAllocated.removeAll();
				tbNeed.removeAll();
				tbMaximun.removeAll();
				tbAvailable.removeAll();
				tbP.removeAll();
				bk.initDataByRandom(radrandom.isSelected());
				if(bk.calAndCheckNeed()) {
					tbAllocated.setModel(bk.addDataForTable(bk.getAllocatedData()));	
					tbNeed.setModel(bk.addDataForTable(bk.getNeedData()));	
					tbMaximun.setModel(bk.addDataForTable(bk.getMaximumData()));	
					tbAvailable.setModel(bk.addDataAvailable());
					tbP.setModel(bk.addDataProcesses());
					btnResult.setEnabled(true);
				}
				else {
					tbAllocated.setModel(bk.addDataForTable(bk.getAllocatedData()));	
					tbMaximun.setModel(bk.addDataForTable(bk.getMaximumData()));
					btnResult.setEnabled(false);
					JOptionPane.showMessageDialog(null, "Error data. Try again");
				}
				
			}
		});
		btnRandom.setBounds(640, 477, 104, 45);
		contentPane.add(btnRandom);
		
		
		
		
		
		
		btnResult = new JButton("Result");
		btnResult.setEnabled(false);
		btnResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bk.method();
				if(bk.isFullSafe()) {
					txtResult.setText(bk.getResult());
					JOptionPane.showMessageDialog(null, "Trang thai he thong an toan");
				}
				else {
					String s = "Khoong an toan! Co" + bk.getSafeProcesses() + " tien trinh duoc thuc thi: "+bk.getResult();
					txtResult.setText(s);
					JOptionPane.showMessageDialog(null, "Trang thai he thong khong toan");
				}
			}
		});
		btnResult.setBounds(482, 546, 104, 45);
		contentPane.add(btnResult);
		
		JLabel lblAvailable = new JLabel("Available");
		lblAvailable.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblAvailable.setBounds(134, 357, 173, 37);
		contentPane.add(lblAvailable);
		
		tbP = new JTable();
		tbP.setBounds(12, 52, 48, 274);
		contentPane.add(tbP);
		
		
		
		
	}
}
