package imgsplit;


import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JOptionPane;

import imgsplit.func.Function;
import misc.Properties;

public class WorkThread implements Runnable{

	private static WorkThread instance;
	private static Thread thread;
	
	
	public static Thread createThread() {
		return thread = new Thread(new WorkThread());
	}
	
	public static Thread getThread() {
		return thread;
	}
	
	public static WorkThread getInstance() {
		return instance;
	}
	
	private WorkThread() {
		instance = this;
	}
	
	private Function function;
	private Properties p;
	
	public void run() {
		instance = this;
		while(true) {
			try {
				Thread.sleep(1000000);
			} catch (Exception e) {}
			
			if(function != null) {
				try{
					String ret = function.execute(p);
					if(ret == null) {
						ret = "Completed without errors.";
					}
					JOptionPane.showMessageDialog(null, ret, "Done", JOptionPane.INFORMATION_MESSAGE);
				}catch (Exception ex) {
					String msg = "An Error occured.\n";
					msg += "Type:\n    "+ex.getClass().getName()+"\n";
					msg += "Message:\n    "+ex.getMessage()+"\n";
					msg += "StackTrace:\n";
					
					for(StackTraceElement el : ex.getStackTrace()) {
						msg += "    "+el+"\n";
					}
					
					
					
					
					JOptionPane.showMessageDialog(null, msg);
					
					ex.printStackTrace();
				}
				
				function = null;
			}
		}
	}
	
	public void setAction(Function function, Properties properties) {
		try{
			if(this.function != null)throw new RuntimeException("Already Running!");
			this.p = properties;
			this.function = function;
			thread.interrupt();
		}catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(null, Utils.fancyErrorString(ex, 8), "Unexpected Error", 0);
		}
	}
	
	public boolean isFinished() {
		return function == null;
	}

}
