package com.fbs.sys.threads;

import com.fbs.sys.Controlor;
import com.fbs.sys.dtos.ReadImageDto;
import com.fbs.sys.dtos.ResultDto;
import com.fbs.sys.tools.Operations;
import com.fbs.sys.uis.MainFrame;

public class HandleImage implements Runnable {
	private boolean stop;
	private Controlor control;
	private Operations operations;
	
	public HandleImage(){
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
		this.operations=Operations.getInstance();
		
		MainFrame.showMessage("Ready to Handle Image!");
		int presize=0,cursize=0;
		long time=0;
		while(!stop){
			// 有可能是空图，边缘找不到
			ReadImageDto image;
			try {
				image = control.takeReadImage();
			} catch (InterruptedException e1) {
				//e1.printStackTrace();
				break;
			}
			ResultDto result=operations.getResult(image);
			cursize=control.getLengthOfRstQueue();
			//System.out.print("handleImageSize***");
			time=Controlor.getSleepTime(time, presize, cursize, 5);
			//System.out.println(" handleImage time:"+time);
			if(time != 0)
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			
			try {
				control.putResult(result);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				break;
			}
			presize=cursize;
		}
		MainFrame.showMessage("线程HandleImage结束！");
	}

}
