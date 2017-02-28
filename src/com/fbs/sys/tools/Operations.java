package com.fbs.sys.tools;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.fbs.calcu.CalcuTool;
import com.fbs.image.GrayScaleDto;
import com.fbs.image.GrayScaleTools;
import com.fbs.sys.dtos.ReadImageDto;
import com.fbs.sys.dtos.ResultDto;

public class Operations {
	private final String PARAFILENAME="parameters.dat";
	
	private String imagedir=null;
	private int threshold=0;
	private int filterwidth=0;
	private double maxmeanvalue=0.0d;
	private double maxvariance=0.0d;
	private int mincount=0;
	private double minmeanvalue=0.0d;
	private double minvariance=0.0d;
	private double mvdeviation=0.0d;
	private double vrdeviation=0.0d;
	
	private int mincountbinary=0;
	private int mincountedge=0; 
	
	private boolean canUse;
	private static Operations instance=new Operations(); 
	private Operations() {
		this.canUse=false;
		this.readParas();
	}
	
	public static Operations getInstance(){
		return instance;
	}
	/*public Operations(String dir,int fw,int tsh,int mc,
						double minmv,double maxmv,double minvr,double maxvr){
		this.setFilterWidth(fw);
		this.setImageDir(dir);
		this.setMaxMeanValue(maxmv);
		this.setMaxVariance(maxvr);
		this.setMincount(mc);
		this.setMinMeanValue(minmv);
		this.setMinVariance(minvr);
	}*/
	
	public ReadImageDto readImage(final String name){
		if(imagedir == null)
			return null;
		BufferedImage bimg=null;
		long cur=new Date().getTime();
		try {
			bimg=ImageIO.read(new File(imagedir,name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bimg == null)
			return null;
		GrayScaleDto gray=GrayScaleTools.toGrayScaleDto(bimg);
		bimg.flush();
		return new ReadImageDto(name,gray,cur);
	}
	
	public ResultDto getResult(ReadImageDto image){
		if(filterwidth == 0 || mincount == 0 || threshold == 0 || 
				maxmeanvalue == 0 || maxvariance == 0 || image == null)
			return null;
		
		// 如果目标找不到，会返回null
		int[] objpixels=GrayScaleTools.getGrays(image.getImage(), 
						filterwidth, threshold, mincountbinary,mincountedge);
		if(objpixels == null)
			return new ResultDto(image.getNum(),false,-1);
		/*
		 * int[] 代表 统计值数组
		 * double[] 代表 样本数组
		 * */
		CalcuTool calcu=new CalcuTool(objpixels);
		double theMev=calcu.getAverage();
		double theVar=calcu.getSampleVariance();
		
		// System.out.println(image.getNum()+" 均值："+theMev+" 样本方差："+theVar);
		long time=new Date().getTime()-image.getTime();
		return new ResultDto(image.getNum(),check(theMev,theVar),time);
	}

	private boolean check(double theMev,double theVar){
		if(theMev >= this.minmeanvalue && theMev <= this.maxmeanvalue)
			return true;
		if(theMev >= (this.minmeanvalue-mvdeviation)
				&& theMev <= (this.maxmeanvalue+mvdeviation)
				&& (theVar >= this.minvariance
					&& theVar <= this.maxvariance))
			return true;
		return false;
	}

	public String getImageDir() {
		return imagedir;
	}

	public void setImageDir(String imageDir) {
		this.imagedir = new String(imageDir);
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getFilterWidth() {
		return filterwidth;
	}

	public void setFilterWidth(int filterWidth) {
		this.filterwidth = filterWidth;
	}

	public double getMaxMeanValue() {
		return maxmeanvalue;
	}

	public void setMaxMeanValue(double meanValue) {
		this.maxmeanvalue = meanValue;
	}

	public double getMaxVariance() {
		return maxvariance;
	}

	public void setMaxVariance(double maxvariance) {
		this.maxvariance = maxvariance;
	}

	public int getMincount() {
		return mincount;
	}

	public void setMincount(int mincount) {
		this.mincount = mincount;
		mincountbinary=(int) (mincount*mincount*Math.PI);
		mincountedge=(int)(mincount*2*Math.PI);
	}

	public double getMinMeanValue() {
		return minmeanvalue;
	}

	public void setMinMeanValue(double minmeanvalue) {
		this.minmeanvalue = minmeanvalue;
	}

	public double getMinVariance() {
		return minvariance;
	}

	public void setMinVariance(double minVariance) {
		this.minvariance = minVariance;
	}
	
	public double getMvdeviation() {
		return mvdeviation;
	}

	public void setMvdeviation(double mvdeviation) {
		this.mvdeviation = mvdeviation;
	}

	public double getVrdeviation() {
		return vrdeviation;
	}

	public void setVrdeviation(double vrdeviation) {
		this.vrdeviation = vrdeviation;
	}

	public void writeParas(String dir,int fw,int tsh,int mc,
			double minmv,double maxmv,double mvde,
			double minvr,double maxvr,double vrde){
		this.setFilterWidth(fw);
		this.setImageDir(dir);
		this.setMincount(mc);
		this.setMaxMeanValue(maxmv);
		this.setMaxVariance(maxvr);
		this.setMinMeanValue(minmv);
		this.setMinVariance(minvr);
		this.setMvdeviation(mvde);
		this.setVrdeviation(vrde);
		this.setThreshold(tsh);
		
		File file=new File(PARAFILENAME);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			DataOutputStream out=
					new DataOutputStream(new FileOutputStream(file));
			out.writeUTF(dir);
			out.writeInt(fw);
			out.writeInt(tsh);
			out.writeInt(mc);
			out.writeDouble(minmv);
			out.writeDouble(maxmv);
			out.writeDouble(mvde);
			out.writeDouble(minvr);
			out.writeDouble(maxvr);
			out.writeDouble(vrde);
			out.close();
			canUse=true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readParas(){
		File file=new File(PARAFILENAME);
		if(file.exists()){
			try {
				DataInputStream in=
						new DataInputStream(new FileInputStream(file));
				imagedir=in.readUTF();
				filterwidth=in.readInt();
				threshold=in.readInt();
				mincount=in.readInt();
				minmeanvalue=in.readDouble();
				maxmeanvalue=in.readDouble();
				mvdeviation=in.readDouble();
				minvariance=in.readDouble();
				maxvariance=in.readDouble();
				vrdeviation=in.readDouble();
				
				mincountbinary=(int) (mincount*mincount*Math.PI);
				mincountedge=(int)(mincount*2*Math.PI);
				
				in.close();
				canUse=true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public CalcuTool testData(ReadImageDto image){
		if(filterwidth == 0 || mincount == 0
				|| threshold == 0 || image == null)
			return null;
		
		// 如果目标找不到，会返回null
		int[] objpixels=GrayScaleTools.getGrays(image.getImage(), 
						filterwidth, threshold, mincountbinary,mincountedge);
		if(objpixels == null)
			return null;
		return new CalcuTool(objpixels);
	}
	
	public boolean canUse(){
		return canUse;
	}
}
