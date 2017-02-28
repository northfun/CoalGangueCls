package com.fbs.image;

public class GrayScaleDto {
	private int height,width;
	private int[] image=null;
	public GrayScaleDto(){}
	public GrayScaleDto(int[] image,int width,int height){
		setImage(image,width,height);
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int[] getImage() {
		return image;
	}
	public void setImage(int[] image,int width,int height) {
		if(this.image == null)
			this.image=new int[width*height];
		System.arraycopy(image, 0, this.image, 0, image.length);
		this.width=width;
		this.height=height;
	}
}
