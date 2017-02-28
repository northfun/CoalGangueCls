package com.fbs.sys.dtos;
import com.fbs.image.GrayScaleDto;

/*
 * Image that have bean read.
 * */
public class ReadImageDto {
	private GrayScaleDto image;
	private long time;
	private String num; // In real environment,it can be omitted.
	public ReadImageDto(String no,GrayScaleDto image,long time) {
		this.setImage(image);
		this.setTime(time);
		this.setNum(no);
	}
	public GrayScaleDto getImage() {
		return image;
	}
	public void setImage(GrayScaleDto image) {
		this.image = image;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
}
