package com.fbs.calcu;
/*
 * int[] 代表 统计值数组
 * double[] 代表 样本数组
 * */
public class CalcuTool {
	private double average,variance;
	public CalcuTool(){
		average=0.0d;
		variance=0.0d;
	}
	public CalcuTool(double[] data){
		setData(data);
	}
	public CalcuTool(int[] data){
		setData(data);
	}
	// data[i] tells the count of value i
	public static double getAverageFromSta(int[] data){
		int count=0;
		for(int i:data)
			count+=i;
		double addup=0.0d;
		for(int i=0; i<data.length; i++)
			addup+=data[i]*i;
		return addup/count;
	}
	
	public static double getSampleVarianceFromSta(int[] data){
		double meanValue=getAverageFromSta(data);
		int count=0;
		for(int i:data)
			count+=i;
		double powSum=0.0d;
		for(int i=0; i<data.length; i++)
			powSum += data[i]*Math.pow((i - meanValue), 2);
		return Math.sqrt(powSum/(count-1));
	}
	
	public static double getAverage(double[] data){
		double sum=0.0d;
		for(double i:data)
			sum+=i;
		return sum/data.length;
	}
	public static double getSampleVariance(double[] data){
	    double powSum = 0.0d,meanValue=getAverage(data);
        for(double k:data)
            powSum += Math.pow((k - meanValue), 2); 
        return Math.sqrt(powSum/(data.length-(double)1.0d));  
	}
	public void setData(double[] data){
		this.average=getAverage(data);
		this.variance=getSampleVariance(data,this.average);
	}
	public void setData(int[] data){
		this.average=getAverageFromSta(data);
		this.variance=getSampleVarianceFromSta(data,this.average);
	}
	public double getAverage() {
		return average;
	}
	public double getSampleVariance() {
		return variance;
	}
	private double getSampleVariance(double[] data,double ave){
	    double powSum = 0.0d,meanValue=ave;
        for(double k:data)
            powSum += Math.pow((k - meanValue), 2); 
        return Math.sqrt(powSum/(data.length-(double)1.0d));  
	}
	private double getSampleVarianceFromSta(int[] data, double average) {
		double meanValue=average;
		int count=0;
		for(int i:data)
			count+=i;
		double powSum=0.0d;
		for(int i=0; i<data.length; i++)
			powSum += data[i]*Math.pow((i - meanValue), 2);
		return Math.sqrt(powSum/(count-1));
	}
}
