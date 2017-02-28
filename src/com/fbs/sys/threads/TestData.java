package com.fbs.sys.threads;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import com.fbs.calcu.CalcuTool;
import com.fbs.sys.Controlor;
import com.fbs.sys.dao.SQLDao;
import com.fbs.sys.dtos.ReadImageDto;
import com.fbs.sys.tools.Operations;
import com.fbs.sys.uis.MainFrame;

public class TestData implements Runnable {
	private boolean testrun=false;

	public TestData() {
	}

	public void stopRun(){
		testrun=false;
	}
	
	@Override
	public void run() {
		
		SQLDao dao = new SQLDao();
		dao.getConnect();
		
		Operations operation=Operations.getInstance();
		MainFrame.showMessage("文件路径："+operation.getImageDir());
		MainFrame.showMessage("中值滤波窗口："+operation.getFilterWidth()+
				"*"+operation.getFilterWidth());
		MainFrame.showMessage("二值化阈值："+operation.getThreshold());
		MainFrame.showMessage("");
		MainFrame.showMessage("正在计算...\n");
		
		int i;
		ReadImageDto img=null;
		CalcuTool culcu=null;
		long time;
		testrun=true;
		for(i=0; i<
				new File(operation.getImageDir()).list().length && testrun;i++){
				img=operation.readImage((i+1)+".png");
				culcu=operation.testData(img);
				if(culcu == null){
					MainFrame.showMessage(img.getNum()+"未检测到区域");
					dao.insertCoalTest(img.getNum(), 0, 0, 0);
					continue;
				}
				MainFrame.showMessage(img.getNum()+"*均值*"+culcu.getAverage()+
						" *样本方差*"+culcu.getSampleVariance());
				time=(new Date().getTime())-img.getTime();
				MainFrame.showMessage("用时："+time+"ms");
				MainFrame.showMessage("");
				
				dao.insertCoalTest(img.getNum(),
						culcu.getAverage(), culcu.getSampleVariance(), time);
		}
		MainFrame.showMessage("线程testdata结束！");
		Controlor.getInstance().testStop();
		try {
			dao.CutConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
