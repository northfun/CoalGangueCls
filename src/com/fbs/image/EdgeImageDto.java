package com.fbs.image;

public class EdgeImageDto {
	private int count;
	private int[] mark;
	private int width;
	private int height;
	
	public EdgeImageDto(){
		count=0;
		mark=null;
		width=0;
		height=0;
	}
	public EdgeImageDto(int[] mark,int width,int height,int count){
		setMark(mark);
		setWidth(width);
		setHeight(height);
		setCount(count);
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int[] getMark() {
		return mark;
	}
	public void setMark(int[] mark) {
		this.mark=new int[mark.length];
		System.arraycopy(mark, 0, this.mark, 0, mark.length);
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
