package com.jet.utils.desensitization;

import org.apache.commons.lang.StringUtils;

public class DesenFloorUtils {
	
	public static final String ZERO="0";

	
	/**
	 * 与substring类似，把inputString中从begin位置的长度len的值置换成0
	 * @param inputString 
	 * @param begin 从0位置开始
	 * @param len 长度
	 * @return
	 */
	public static String floorNumberByString(String inputString,int begin, int len){
			if(len>0){
				int input_lenth = inputString.length();
				if(input_lenth>=len && len>=0){
					return inputString.substring(0, inputString.length()-len)+StringUtils.repeat(ZERO, len);
				}else if(len>input_lenth){
					return StringUtils.repeat(ZERO, input_lenth);
				}
			} 
			return inputString;
		}
	
	/**
	 * 整数值的截断，从右开始cur_right_length截断后置换成0；如func(123456,2)=123400
	 * @param input long的输入值，
	 * @param cutRightLength 长度
	 * @return 
	 */
	public static long floorNumberCutRight(long input,int cutRightLength){
		String s=String.valueOf(input);
		String result = floorNumberByString(s,s.length()-cutRightLength,cutRightLength);
		return Long.valueOf(result);
	}
	
	
	/**
	 * 整数值的截断，从右开始cur_right_length截断后置换成0；如func(123456,2)=123400
	 * @param input 输入值
	 * @param cutRightLength 长度
	 * @return
	 */
	public static int floorNumberCutRight(int input,int cutRightLength){
		String s=String.valueOf(input);
		String result = floorNumberByString(s,s.length()-cutRightLength,cutRightLength);
		return Integer.valueOf(result);
	}
	
	/**
	 * 整数值的截断，从左开始保留reserveLeftLength长度，其余位置置换成0；如func(123456,2)=120000
	 * @param input
	 * @param reserveLeftLength 长度
	 * @return
	 */
	public static int floorNumberReserveLeft(int input,int reserveLeftLength){
		String s=String.valueOf(input);
		String result;
		if(reserveLeftLength>0){
			result= floorNumberByString(s,0,s.length()-reserveLeftLength);
		}else{
			result=s;
		}
		return Integer.valueOf(result);
	}
	
	/**
	 * 整数值的截断，从左开始保留reserveLeftLength长度，其余位置置换成0；如func(123456,2)=120000
	 * @param input
	 * @param reserveLeftLength 长度
	 * @return
	 */
	public static long floorNumberReserveLeft(long input,int reserveLeftLength){
		String s=String.valueOf(input);
		String result;
		if(reserveLeftLength>0){
			result= floorNumberByString(s,0,s.length()-reserveLeftLength);
		}else{
			result=s;
		}
		return Long.valueOf(result);
	}
	

}
