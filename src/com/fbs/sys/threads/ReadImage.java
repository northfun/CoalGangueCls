package com.fbs.sys.threads;


import java.io.File;

import com.fbs.sys.Controlor;
import com.fbs.sys.dtos.ReadImageDto;
import com.fbs.sys.tools.Operations;
import com.fbs.sys.uis.MainFrame;

public class ReadImage implements Runnable {
	private boolean stop;
	private Controlor control;
	private Operations operations;
	
	public ReadImage(){
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
		
		MainFrame.showMessage("Ready to read file under Dir\""+
				this.operations.getImageDir());
		long i=0,time=0;
		int presize=0,cursize=0;
		ReadImageDto rdimg=null;
		while(!stop){
			rdimg=operations.readImage((i+1)+".png");
			if(rdimg == null){
				MainFrame.showMessage("null in readImage");
				break;
			}
			cursize=control.getLengthOfRdImgQueue();
			//System.out.print("readImageSize***");
			time=Controlor.getSleepTime(time, presize, cursize, 15);
			//System.out.println(" readImage time:"+time+"\n");
			if(time != 0)
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			
			try {
				control.putReadImage(rdimg);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				if(control.getLengthOfRdImgQueue() == 0)
					break;
			}
			i=(i+1)%(new File(
					this.operations.getImageDir()).listFiles().length);
			presize=cursize;
		}
		MainFrame.showMessage("Ïß³ÌReadImage½áÊø£¡");
	}

}
