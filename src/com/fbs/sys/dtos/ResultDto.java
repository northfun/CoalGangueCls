package com.fbs.sys.dtos;

public class ResultDto {
	private String num;
	private boolean isCoal;
	private long time;
	public ResultDto(String num,boolean isCoal,long time) {
		this.setCoal(isCoal);
		this.setNum(num);
		this.setTime(time);
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public boolean isCoal() {
		return isCoal;
	}
	public void setCoal(boolean isCoal) {
		this.isCoal = isCoal;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String toString(){
		if(this.time == -1)
			return num+"未检测到区域";
		String is=isCoal?"是":"不是";
		return "**"+num+":"+is+"煤块**"+"("+this.time+"ms)";
	}
}
