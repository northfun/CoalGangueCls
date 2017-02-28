package com.fbs.sys.threads;

import com.fbs.sys.Controlor;
import com.fbs.sys.dtos.ResultDto;
import com.fbs.sys.uis.MainFrame;

public class HandleResult implements Runnable {
	private boolean stop;
	private Controlor control;
	public HandleResult(){
		stop=false;
	}
	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	@Override
	public void run() {
		this.control=Controlor.getInstance();
		MainFrame.showMessage("Ready to Handle Result!");
		while(!stop){
			//while(control.getLengthOfRstQueue() < 1);
			ResultDto result;
			try {
				result = control.takeResult();
			} catch (InterruptedException e) {
				break;
			}
			MainFrame.showMessage(result.toString());
		}
		MainFrame.showMessage("Ïß³ÌHandleResult½áÊø£¡");
	}
}
