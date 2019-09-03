package com.jet.utils.desensitization;


public class DesenLinearShiftUtils {
	
	
	/**
	 * 返回input的线性变换后的值,y=kx+b
	 * @param input
	 * @param k 默认值=97
	 * @param b 默认值=997
	 * @return
	 */
	public static float linearShift(float input,int k, int b){
		return input*k+b;
	}
	
	
	public static double linearShift(double input,int k, int b){
		return input*k+b;
	}
	
	
	public static long linearShift(long input,int k, int b){
		return input*k+b;
	}
	
	
	public static int linearShift(int input,int k, int b){
		return input*k+b;
	}
	
}
