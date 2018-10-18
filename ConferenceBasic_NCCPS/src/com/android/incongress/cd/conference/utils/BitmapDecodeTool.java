package com.android.incongress.cd.conference.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.view.View;

public class BitmapDecodeTool {
	
	public static  Bitmap decodeBitmap(String filename, int width,
			int height, int maxMultiple,Bitmap.Config config,boolean isScale) {
		//只加载基础信息,并不真正解码图片
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 这里进行假解码,获取宽高等信息,此时返回的bitmap是null的,因为并不真正进行解码
		BitmapFactory.decodeFile(filename, options);
		if (options.outWidth < 1 || options.outHeight < 1) {

		}
		options.inPreferredConfig = config;
		//计算缩放率
		options.inSampleSize = calculateOriginal(options, width, height, maxMultiple);
		// 设置为false开始真的解码图片
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[12 * 1024];
		Bitmap bm1 = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(filename);
			bm1 = BitmapFactory.decodeFileDescriptor(is.getFD(),null,options);
//			bm1 = BitmapFactory.decodeFile(filename, options);
			
		} catch (Error e) { 
			//发生错误 在次进行解压 先将前面的key去掉  都按照为小图的来释放
			//发生错误进行再次压缩
			Runtime.getRuntime().runFinalization();
			try {
				Thread.sleep(600);

				options.inSampleSize += 1;
				options.inJustDecodeBounds = false;
				options.inDither = true;
				options.inTempStorage = new byte[12 * 1024];
				options.inPreferredConfig = null;
				try {
						is = new FileInputStream(filename);
						bm1 = BitmapFactory.decodeFileDescriptor(is.getFD(),null,options);
				} catch (Exception e2) {
					//还错,继续压缩并且在sdcard中建立缓存区
					Runtime.getRuntime().runFinalization();
					try {
						Thread.sleep(600);
						options.inSampleSize += 1;
						//内存不足的情况下尝试在sdcard开辟空间存储内存
						options.inTempStorage = new byte[12 * 1024];
						options.inJustDecodeBounds = false;
						options.inDither = true;
						options.inPreferredConfig = null;

						try {
							is = new FileInputStream(filename);
							bm1 = BitmapFactory.decodeFileDescriptor(is.getFD(),null,options);
						} catch (Exception e4) {
							//实在不行了返回null,解码失败
							Runtime.getRuntime().runFinalization();
							bm1 = null;
						}

					} catch (InterruptedException e3) {

					}
				}

			} catch (InterruptedException e1) {
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null !=is){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (bm1 == null) {
			return null;
		}
		if (!isScale) {
			return bm1;
		}
		//等比缩放
		int queryWidth = width;
		int queryHeight = height;
		int resWidth = bm1.getWidth();
		int resHeight = bm1.getHeight();
		float scaleWidth = ((float) queryWidth) / resWidth;
		float scaleHeight = ((float) queryHeight) / resHeight;
		Bitmap bm;
		try {
			if (scaleWidth >= 1 && scaleHeight >= 1) {
				bm = bm1;
			} else if (scaleHeight >= 1 && scaleWidth < 1) {
				int cutH = resHeight;
				int cutW = queryWidth * cutH / queryHeight;
				int cutX = resWidth / 2 - cutW / 2;
				bm = Bitmap.createBitmap(bm1, cutX, 0, cutW, cutH);
			} else if (scaleWidth >= 1 && scaleHeight < 1) {
				int cutW = resWidth;
				int cutH = queryHeight * cutW / queryWidth;
				bm = Bitmap.createBitmap(bm1, 0, 0, cutW, cutH);
			} else {
				float scale = scaleHeight < scaleWidth ? scaleWidth
						: scaleHeight;
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
				bm = Bitmap.createBitmap(bm1, 0, 0, resWidth, resHeight,
						matrix, true);
			}
			if(null !=bm1 && !bm1.isRecycled()){
				bm1.recycle();
			}
		} catch (Exception e) {
			bm = bm1;
		}
		return bm;
	}

	/**
	 * 计算缩放比例
	 * */
	private static int calculateOriginal(BitmapFactory.Options options,
			int reqWidth, int reqHeight, int maxMultiple) {
		int inSampleSize = 1;
		final int height = options.outHeight;
		final int width = options.outWidth;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
			final float totalPixels = width * height;
			final float totalReqPixelsCap = (reqWidth * reqHeight * maxMultiple);

			//按允许的倍数计算缩放倍数
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
			//检测是否有足够的内存对缩放倍数进行缩放,不行则继续缩放
			while(!CheckBitmapFitsInMemory(reqWidth*maxMultiple/inSampleSize, reqHeight*maxMultiple/inSampleSize, options.inPreferredConfig)){
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 按照宽高计算bitmap所占内存大小
	 * */
	public static long GetBitmapSize(long bmpwidth, long bmpheight,
			Bitmap.Config config) {
		return bmpwidth * bmpheight *  getBytesxPixel(config);
	}

	/**
	 * 按照不同格式计算所占字节数
	 * */
	public static int getBytesxPixel(Bitmap.Config config) {
		int bytesxPixel = 1;
		// 3.1或者以上
		switch (config) {
		case RGB_565:
		case ARGB_4444:
			bytesxPixel = 2;
			break;
		case ALPHA_8:
			bytesxPixel = 1;
			break;
		case ARGB_8888:
			bytesxPixel = 4;
			break;
		}
		return bytesxPixel;
	}

	/**
	 * 当前空闲的堆内存
	 * */
	public static long FreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 检测当前是否有足够的内存进行读取bitmap
	 * */
	public static boolean CheckBitmapFitsInMemory(long bmpwidth,
			long bmpheight, Bitmap.Config config) {
		return (GetBitmapSize(bmpwidth, bmpheight, config) < FreeMemory());
	}
}
