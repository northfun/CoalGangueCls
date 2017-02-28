package com.fbs.sys;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.fbs.sys.dtos.ReadImageDto;
import com.fbs.sys.dtos.ResultDto;
import com.fbs.sys.threads.HandleImage;
import com.fbs.sys.threads.HandleResult;
import com.fbs.sys.threads.ReadImage;
import com.fbs.sys.threads.TestData;

public class Controlor{
	private static BlockingQueue<ReadImageDto> readImageq;
	private static BlockingQueue<ResultDto> resultq;
	private static final int THRESHOLDOFQ=5;
	private static final int MAXOFQ=10;
	
	private ReadImage readimg;
	private HandleImage handleimg;
	private HandleResult handlerst;
	private TestData testdata;
	
	private Thread rdimgthread;
	private Thread hdimgthread;
	private Thread hdrstthread;
	private Thread tdthread;
	
	private boolean isrun;
	private boolean testrun;
	private boolean rdimgthreadIsWait;
	private boolean hdimgthreadIsWait;
	
	private static Controlor instance=new Controlor();
	
	private Controlor(){
		isrun=false;
		testrun=false;
		rdimgthreadIsWait=false;
		hdimgthreadIsWait=false;
		readImageq=new LinkedBlockingQueue<ReadImageDto>();
		resultq=new LinkedBlockingQueue<ResultDto>();

		readimg=new ReadImage();
		handleimg=new HandleImage();
		handlerst=new HandleResult();
		testdata=new TestData();
	}
	
	public static Controlor getInstance(){
		return instance;
	}
	
	public void putReadImage(ReadImageDto image) throws InterruptedException{
		// System.out.println("***put "+readImageq.size());
		if(readImageq.size() >= MAXOFQ){
			synchronized(rdimgthread){
				rdimgthreadIsWait=true;
				this.rdimgthread.wait();
			}
		}
		readImageq.put(image);
	}
	
	public ReadImageDto takeReadImage() throws InterruptedException{
		// System.out.println("***take "+readImageq.size());
		ReadImageDto rimg=null;
		rimg=readImageq.take();
		if(rdimgthreadIsWait && readImageq.size()<THRESHOLDOFQ)
			synchronized(rdimgthread){
				rdimgthread.notify();
				rdimgthreadIsWait=false;
			}
		return rimg;
	}
	
	public void putResult(ResultDto rst) throws InterruptedException{
		if(resultq.size() >= MAXOFQ){
			synchronized(hdimgthread){
				hdimgthreadIsWait=true;
				hdimgthread.wait();
			}
		}
		resultq.put(rst);
	}
	
	public ResultDto takeResult() throws InterruptedException{
		ResultDto rst=null;
		rst=resultq.take();
		if(hdimgthreadIsWait && resultq.size() < THRESHOLDOFQ){
			synchronized(hdimgthread){
				hdimgthread.notify();
				hdimgthreadIsWait=false;
			}
		}
		return rst;
	}
	
	public int getLengthOfRdImgQueue(){
		return Controlor.readImageq.size();
	}
	
	public int getLengthOfRstQueue(){
		return Controlor.resultq.size();
	}
	
	public void startMachine(){
		if(isrun)
			return;

		synchronized(Controlor.readImageq){
			readImageq.clear();
		}
		synchronized(Controlor.resultq){
			resultq.clear();
		}
		
		isrun=true;
		this.readimg.setStop(false);
		this.handleimg.setStop(false);
		this.handlerst.setStop(false);
		
		rdimgthread=new Thread(readimg);
		hdimgthread=new Thread(handleimg);
		hdrstthread=new Thread(handlerst);
		
		this.rdimgthread.start();
		this.hdimgthread.start();
		this.hdrstthread.start();
	}
	
	public void stopMachine(){
		if(!isrun)
			return;
		isrun=false;
		
		this.readimg.setStop(true);
		this.handleimg.setStop(true);
		this.handlerst.setStop(true);
		
		this.rdimgthread.interrupt();
		this.hdimgthread.interrupt();
		this.hdrstthread.interrupt();
	}
	
	public boolean isRun(){
		return isrun;
	}
	public boolean testRun(){
		return testrun;
	}
	public void testStop(){
		testrun=false;
		if(tdthread != null)
			testdata.stopRun();
	}
	
	public static long getSleepTime(long pretime,
				int presize,int cursize,int times){
		/*pretime/=times;
		//System.out.print("cur:"+cursize+" pre:"+presize);
		if(cursize > THRESHOLDOFQ){
			// bigger than thsh , time quick bigger
			if(cursize > presize) // increase
				pretime=(cursize-THRESHOLDOFQ)+2;
			else //decrease
				pretime=(pretime > 2?(pretime-1):1);
		}
		else{
			// lower than thsh
			if(2 < cursize - presize) // increase
				pretime=cursize-presize;
			else if(cursize < presize) // decrease
				//pretime=(pretime > 1?(pretime-2):0);
				pretime=0;
			//else
				//return pretime;
		}
		return pretime*times;*/
		return 0;
	}
	
	public void testData(){
		testrun=true;
		tdthread=new Thread(testdata);
		tdthread.start();
	}
}
