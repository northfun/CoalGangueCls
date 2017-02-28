package com.fbs.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class GrayScaleTools {
	/*
	 * 1 转8bit灰度图
	 * 
	 * 读取灰度图保存成imageDto
	 * 8bit灰度图：
	 * 2 中值滤波
	 * 3 转二值图
	 * 4 边缘追踪
	 * 5 标记区域
	 * 6 得到标记区域对应原图灰度
	 * */
	//private GrayScaleDto image=null;
	
	/* 由8bit灰度图，生成GrayScaleDto */
	static public GrayScaleDto toGrayScaleDto(final BufferedImage image){
		int[] pixels=new int[image.getWidth()*image.getHeight()];
		int x,y;
		for(y=0; y<image.getHeight(); y++)
			for(x=0; x<image.getWidth(); x++)
				pixels[y*image.getWidth()+x]=image.getRGB(x,y)&0xff;
		return new GrayScaleDto(pixels,image.getWidth(),image.getHeight());
	}
	
	/* 中值滤波 滤波器:filterWidth*filterWidth */
	private static int getMedValue(int[] arr,int width){
		Arrays.sort(arr);
		return arr[(int) Math.floor(width*width/2)];
	}

	public static GrayScaleDto medianFilter(final GrayScaleDto origeImage,final int filterWidth){
		int[] filter=new int[filterWidth*filterWidth];
		// 深度复制，origeImage不会被改变
		int[] oldImage=new int[origeImage.getImage().length];
		System.arraycopy(origeImage.getImage(), 0,
				oldImage, 0, origeImage.getImage().length);
		// 新图 去掉边缘的
		int[] newImage=new int[(origeImage.getWidth())
		                 *(origeImage.getHeight())];
		Arrays.fill(newImage, 0x000000ff);	// 全白
		int y,x,fy,fx,cur,hfw=filterWidth/2;
		// 边缘像素不处理
		for(y=0; y<origeImage.getHeight()-filterWidth; y++){
			for(x=0; x<origeImage.getWidth()-filterWidth; x++){
				cur=y*origeImage.getWidth()+x; //选区左上角像素,新图当前像素

				// 将image中的待处理块放filter中
				for(fy=0; fy<filterWidth; fy++) // y
					for(fx=0; fx<filterWidth; fx++) // x
						filter[fy*filterWidth+fx]=oldImage[cur+fy*origeImage.getWidth()+fx];
				//将原图的 待处理块 中心点 赋中间值
				oldImage[cur+hfw*origeImage.getWidth()+hfw]=getMedValue(filter,filterWidth);
				//将此中心点赋给新图
				newImage[cur+hfw*origeImage.getWidth()+hfw]=oldImage[cur+hfw*origeImage.getWidth()+hfw];
			}
		}
		return new GrayScaleDto(newImage,
				origeImage.getWidth(),origeImage.getHeight());
	}

	/* 生成二值图 */
	public static GrayScaleDto getBinaryImage(final GrayScaleDto image,
			final int threshold,final int mincount){
		int y,x,count=0;
		int[] oldImage=image.getImage();
		int[] newImage=new int[oldImage.length];
		for(y=0; y<image.getHeight(); y++)
			for(x=0; x<image.getWidth(); x++)
				/*	灰度=0xff 白色，灰度=0 黑色
				 *	令较深色区取全1，背景取0
				 */
				if(oldImage[y*image.getWidth()+x] < threshold){
					newImage[y*image.getWidth()+x]=0;
					count++;
				}
				else
					newImage[y*image.getWidth()+x]=0xff;
		if(count<mincount)
			return null;
		return new GrayScaleDto(newImage,image.getWidth(),image.getHeight());
	}
	
	/* 边缘置1 边缘个数>=mincount
	 * 经过中值滤波图片的最外一圈像素一定为0xff
	 */
	public static EdgeImageDto getEdge(final GrayScaleDto image,final int mincount){
		int beginloc=0;
		EdgeImageDto edge=null;
		while(beginloc <= (image.getImage().length-mincount)){
			edge=getEdgeFromLoc(image,beginloc);
			if(edge == null)
				break;
			if(edge.getCount() >= mincount)
				return edge;
			beginloc+=image.getWidth(); // 下一行开始 可以优化
		}
		return null;
	}

	/* 从beginloc开始 边缘置1
	 *经过中值滤波图片的最外一圈像素一定为0xff
	 * 边缘追踪,边缘置-1，左手法则，左手为黑色，右手为白色外界
	 */
	private static EdgeImageDto getEdgeFromLoc(final GrayScaleDto image,final int beginloc){
		int[] oldImage=image.getImage();
		// 标记的边缘数组,初始化为0xff,找到边缘赋0
		int[] marked=new int[image.getHeight()*image.getWidth()];
		Arrays.fill(marked, 0xff);
		/* count计算边缘长度，
		*若小于某值则放弃从first+1继续开始的步骤
		*直到图片遍历完 
		*/
		final int[] wwalk={image.getWidth()-1,image.getWidth(),image.getWidth()+1,1,
				1-image.getWidth(),-image.getWidth(),-1-image.getWidth(),-1};
		int first=beginloc,prewhite,curblack,count=0;
		int wdirector=0;

		/* 找到某黑区边缘的位置,0是黑色 */
		while(first<oldImage.length && oldImage[first++] != 0)
			;
		// 图片中木有物体(黑区)
		if(first == marked.length)
			return null;
		first--;
		// 将第一个边缘赋0
		marked[first]=0;
		curblack=first;
		prewhite=first-1;
		wdirector=0;
		int c=0;
		do{
			// 左手法则 白的运动方向为逆时针
			if(prewhite+image.getWidth() == curblack){
				// 白上黑下，相对黑，白的方向 左上
				wdirector=6;
			}else if(prewhite+1 == curblack){
				// 白左黑右，相对黑，白的方向 左下
				wdirector=0;
			}else if(prewhite-1 == curblack){
				// 白右黑左，相对黑，白的方向 右上
				wdirector=4;
			}else if(prewhite-image.getWidth() == curblack){
				// 白下黑上，相对黑，白的方向 右下
				wdirector=2;
			}
			c=0;
			while(oldImage[curblack+wwalk[wdirector]] != 0 && c<9){
				// 未碰到黑色边缘
				prewhite=curblack+wwalk[wdirector];
				wdirector=(wdirector+1)%8;
				c++;
			}
			if(c == 8)
				break;
			curblack+=wwalk[wdirector];
			count++;
			marked[curblack]=0; // 边缘=0
		}while(curblack != first);
		return new EdgeImageDto(marked,image.getWidth(),image.getHeight(),count);
	}

	/* 边缘图得到 原图 边缘内部的目标区域 
	 * 经过中值滤波，图像周围一圈一定是0xff
	 * 此算法返回的目标不包括边缘
	 * 目标区域=0xff
	 */
	public static GrayScaleDto getMarkedImage(final EdgeImageDto edge){
		// 目标区域数组
		int[] target=new int[edge.getWidth()*edge.getHeight()];
		// review:mark[]初始化为0xff 边缘部分置0
		System.arraycopy(edge.getMark(),0,target,0,target.length);
		// 方向：上 右 下 左
		final int[] directions={-edge.getWidth(),1,edge.getWidth(),-1};
		/* 赋0直到碰到0,余下的图即为目标区域
		 * willview存放待访问内容下标
		 */ 
		int cur=0,i=0; // 当前像素下标
		Queue<Integer> willview=new LinkedList<Integer>();
		target[cur]=0;
		willview.add(0);
		while(!willview.isEmpty()){
			cur=willview.poll();
			i=0;
			while(i<4){
				int tem=cur+directions[i];
				if(tem<target.length && tem>0 && target[tem] != 0){
					// 未被访问过 访问赋0
					target[tem]=0;
					// 添加 待 访问临近像素 的像素的下标
					willview.add(tem);
				}
				i++;
			}
		}
		return new GrayScaleDto(target,edge.getWidth(),edge.getHeight());
	}

	/* 由标记图和灰度图 得到标记区域 int[]灰度值数组
	 */
	public static int[] getObjectImage(final GrayScaleDto image,
			final GrayScaleDto marked){
		int[] grays=new int[256]; // 初始化默认0
		int[] oriImage=image.getImage(),objMark=marked.getImage();
		int cur;
		for(cur=0; cur<image.getImage().length; cur++){
			if(objMark[cur] != 0) // 略去背景，留下mark选中的区域
				++(grays[ (oriImage[cur] & objMark[cur]) ]);
		}
		return grays;
	}

	public static BufferedImage toBufferedImage(final GrayScaleDto image){
		 BufferedImage grayImage = 
				new BufferedImage(image.getWidth(),image.getHeight(),
							BufferedImage.TYPE_BYTE_GRAY);
		 int i=0;
		 int[] img=image.getImage();
		 int[] bimg=new int[img.length];
		 while(i < img.length){
			 bimg[i]=new Color(img[i],img[i],img[i]).getRGB();
			 i++;
		 }
		grayImage.setRGB(0, 0, image.getWidth(), 
			image.getHeight(), bimg, 0, image.getWidth());
		 return grayImage;
	}

	public static int[] getGrays(GrayScaleDto dto,int fw,int ts,int mcb,int mce){
		GrayScaleDto img=GrayScaleTools.medianFilter(dto,fw);
		
		// 目标可能找不到
		GrayScaleDto bin=GrayScaleTools.getBinaryImage(img, ts, mcb);
		if(bin == null)
			return null;
		
		// 边缘有可能找不到
		EdgeImageDto edge=GrayScaleTools.getEdge(bin, mce);
		if(edge == null)
			return null;
		
		GrayScaleDto objmark=GrayScaleTools.getMarkedImage(edge);
		
		return GrayScaleTools.getObjectImage(img, objmark);
	}
	
	/*private static String showXY(int loc,int width){
		int x=loc%width;
		int y=loc/width;
		return new String("("+x+","+y+")");
	}*/
	
	public static GrayScaleDto getObj(final GrayScaleDto image,
						final GrayScaleDto mark){
		int i=0;
		int[] o=image.getImage();
		int[] m=mark.getImage();
		int[] newimage=new int[o.length];
		while(i<m.length){
			newimage[i]=o[i] & m[i];
			i++;
		}
		return new GrayScaleDto(newimage,image.getWidth(),image.getHeight());
	}
}
