import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BankerAlgorithm {
	private int[][] allocated;
	private int[][] maximum;
	private int[][] need;
	private int[] available;
	private int numberOfProcess;
	private int numberOfResource;
	private int numberOfSafeProcess;
	private int[] result;
	private int[] finish;
	private int[] work;
	
	public BankerAlgorithm() {
	}
	
//	public static void main(String[] args) {
//		String path="";
//		try {
//			path = new java.io.File( "." ).getCanonicalPath() + "\\src\\";
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		BankerAlgorithm bk =  new BankerAlgorithm(1, 1);
//		//bk.initDataByFile(path+"input.txt");
//		bk.initDataByRandom(true);
//		if(bk.calAndCheckNeed()) {
//			bk.displayInput();
//			System.out.println();
//			bk.method();
//			if(bk.numberOfSafeProcess == bk.numberOfProcess) 
//				System.out.println("The full process in system is in safe state ");
//			else System.out.println("The  process in system is not in safe state ");
//		}
//		else System.out.println("data error");
//	}
	
	public void init(int nop, int nor) {
		this.allocated = new int[this.numberOfProcess][this.numberOfResource];
		this.maximum = new int[this.numberOfProcess][this.numberOfResource];
		this.need = new int[this.numberOfProcess][this.numberOfResource];
		this.available = new int[this.numberOfResource];
		this.result = new int[this.numberOfProcess];
		this.work = new int[this.numberOfResource];
		this.finish = new int[this.numberOfProcess];
		this.numberOfSafeProcess = 0;
	}
	
	public void initDataByFile(String path) {
		int index = 0;
		try {
		     File f = new File(path);
		     FileReader fr = new FileReader(f);
		     BufferedReader br = new BufferedReader(fr);
		     String line;
		     while ((line = br.readLine()) != null){
	    	 	String[] arr = line.trim().split(" ");
				if(index++==0) {
					this.numberOfProcess = Integer.parseInt(arr[0]);
					this.numberOfResource = Integer.parseInt(arr[1]);
					init(this.numberOfProcess, this.numberOfProcess);
					
				}
		       if(index <= this.numberOfProcess+1 && index > 1) {
		    	   for(int i=0;i<this.numberOfResource;i++) {
		    		   this.allocated[index-2][i] = Integer.parseInt(arr[i]);
		    	   }
		    	   continue;
		       }
			       
		       if(index <= 2*this.numberOfProcess+1 && index > this.numberOfProcess+1) {
		    	   for(int i=0;i<this.numberOfResource;i++) {
		    		   this.maximum[index-2-this.numberOfProcess][i] = Integer.parseInt(arr[i]);
		    	   }
		    	   continue;
		       }
		       if(index == 2*this.numberOfProcess+2) {
		    	   for(int i=0;i<this.numberOfResource;i++) {
		    		   this.available[i] = Integer.parseInt(arr[i]);
		    	   }
		       }
		     }
		     fr.close();
		     br.close();
		    } catch (Exception ex) {
		      System.out.println("Loi doc file: "+ex);
		  }
	}
	
	public void displayInput() {
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				System.out.print(this.allocated[i][j]+ " ");
			}
			System.out.println();
		}
		System.out.println("-----------------");
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				System.out.print(this.maximum[i][j]+ " ");
			}
			System.out.println();
		}
		System.out.println("-----------------");
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				System.out.print(this.need[i][j]+ " ");
			}
			System.out.println();
		}
		System.out.println("-----------------");
		for(int j=0;j<this.numberOfResource;j++) {
			System.out.print(this.available[j]+" ");
		}
	}
	
	public boolean calAndCheckNeed() {
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				if((this.need[i][j] = this.maximum[i][j] - this.allocated[i][j]) < 0)
					return false;
			}
		}
		return true;
	}
	
	public void initDataByRandom(boolean isCheckError) {
		//number of processes : 2->10;
		//number of resources : 2->10;
		//Maximun and alocated : 2->20;
		Random rand = new Random();
		
		this.numberOfProcess = rand.nextInt(9)+2;
		this.numberOfResource = rand.nextInt(9)+2;
		init(this.numberOfProcess, this.numberOfResource);
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				this.maximum[i][j] = rand.nextInt(19)+1;
			}
		}
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				if(isCheckError) this.allocated[i][j] = rand.nextInt(this.maximum[i][j]+1);
				else this.allocated[i][j] = rand.nextInt(19)+1;
			}
		}
		
		for(int i=0;i<this.numberOfResource;i++) {
			this.available[i] = rand.nextInt(20);
		}
	}
	
	public void method() {
		int i = 0;
		boolean flag = false;

		while (true) {
			if (this.finish[i] == 0 && this.isLowerAvailable(i)) {
				this.result[this.numberOfSafeProcess++] = i;
				//System.out.println("Process "+ i +" is executing......");
				//System.out.println("The process"+ i +" is in safe state.");
				this.finish[i] = 1;
				for (int j = 0; j < this.numberOfResource; j++)
					this.available[j] += this.allocated[i][j];
			}
			
			if (++i == this.numberOfProcess) {
				flag = false;
				for (int j = 0; j < this.numberOfResource; j++)
					if (this.available[j] != this.work[j]) flag = true;
				for (int j = 0; j < this.numberOfResource; j++)
					this.work[j] = this.available[j];
				if (flag) i = 0;
				else break;
			}
		}
	}
	
	public boolean isLowerAvailable(int i) {
		for (int j = 0; j < this.numberOfResource; j++)
			if (this.need[i][j] > this.available[j])
				return false;
		return true;
	}
	
	public JTable displayTable() {
		String[] columnNames = new String[3]; 
		for(int i=0;i<3;i++) {	
				columnNames[i] = "R"+i;
		}
		Object[][] data = new Object[3][3];
		for(int i=0;i<3;i++) {	
			for(int j=0;j<3;j++) {
				data[i][j] = 3;
			}
		}
		JTable table = new JTable(data, columnNames);
		
			return table;
	}
	
	public DefaultTableModel addDataForTable(int[][] data) {
		DefaultTableModel dftm = new DefaultTableModel();
		String[] columnNames = new String[numberOfResource]; 
		
		for(int i=0;i<numberOfResource;i++) {	
				columnNames[i] = "R"+i;
		}
		dftm.setColumnIdentifiers(columnNames);
		
		dftm.setRowCount(0);
		dftm.addRow(columnNames);
		
	
		String[] dataRow = new String[this.numberOfResource];
		
		for(int i=0;i<this.numberOfProcess;i++) {
			for(int j=0;j<this.numberOfResource;j++) {
				dataRow[j] = Integer.toString(data[i][j]);
			}
			dftm.addRow(dataRow);
		}
		return dftm;
	}
	
	public DefaultTableModel addDataAvailable() {
		DefaultTableModel dftm = new DefaultTableModel();
		String[] columnNames = new String[numberOfResource]; 
		
		for(int i=0;i<numberOfResource;i++) {	
				columnNames[i] = "R"+i;
		}
		dftm.setColumnIdentifiers(columnNames);
		
		dftm.setRowCount(0);
		dftm.addRow(columnNames);
		
	
		String[] dataRow = new String[this.numberOfResource];
		
		
			for(int j=0;j<this.numberOfResource;j++) 
				dataRow[j] = Integer.toString(this.available[j]);
			dftm.addRow(dataRow);
		
		return dftm;
	}
	
	public DefaultTableModel addDataProcesses() {
		DefaultTableModel dftm = new DefaultTableModel();
		String[] columnNames = new String[1]; 
		columnNames[0] = "P";
		
		dftm.setColumnIdentifiers(columnNames);
		
		dftm.setRowCount(0);
		dftm.addRow(columnNames);
		
	
		String[] dataRow = new String[1];
		
		
		for(int j=0;j<this.numberOfProcess;j++) {
			dataRow[0] = "P"+j;
			dftm.addRow(dataRow);
		}
			
		
		return dftm;
	}
	
	public int[][] getAllocatedData() {
		return this.allocated;
	}
	public int[][] getMaximumData() {
		return this.maximum;
	}
	public int[][] getNeedData() {
		return this.need;
	}
	public String getResult() {
		String sForReturn = "";
		for(int i=0;i<this.numberOfSafeProcess;i++) {
			if(i!=this.numberOfSafeProcess-1) {
				sForReturn += ("P"+Integer.toString(result[i]) + " -> ");
			}
			else sForReturn += ("P"+Integer.toString(result[i]));
		}
		return sForReturn;
	}
	public boolean isFullSafe() {
		return this.numberOfSafeProcess == this.numberOfProcess;
	}
	public int getSafeProcesses() {
		return this.numberOfSafeProcess;
	}
}
