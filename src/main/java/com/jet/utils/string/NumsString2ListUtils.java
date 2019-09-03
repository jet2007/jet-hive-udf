package com.jet.utils.string;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class NumsString2ListUtils {
	
	
	public static final String DELIMITER="\\s*,\\s*"; //分隔符,如逗号
	public static final String CONTINUOUS="\\s*~\\s*"; //连续符号，如1~4当中的~符号
	
	/**
	 * numsStr当中是否包含有负数
	 * @param numsStr 如"1~4,6,7,-1"返回false<Integer>
	 * @param delimiter 分隔符,如逗号
	 * @param continuous 连续符号，如1~4当中的~符号
	 * @return 如"1~4,6,7"返回true
	 */
	public static boolean isNnums2ListStatic(String numsStr,String delimiter,String continuous) {
    	if(StringUtils.isBlank(numsStr)){
    		return true;
    	}
    	else{
	    	String[] cc = numsStr.split(delimiter);
    		for (int i = 0; i < cc.length; i++) {
    			String s=cc[i];
				if(s.split(continuous).length>1){
					String[] t = s.split(continuous);
					int start=Integer.parseInt(t[0]);
					int end=Integer.parseInt(t[1]);
					if( start<0  ||  end<0){
						return false;
					}
				}
				else{
					if( Integer.parseInt(s)<0 ){
						return false;
					}
				}
			}
    	}
    	return true;
	}
	
	/**
	 * numsStr当中是否包含有负数(连续符号为[~])
	 * @param numsStr 如"1~4,6,7,-1"返回false<Integer>
	 * @param delimiter 分隔符,如逗号
	 * @return 如"1~4,6,7"返回true
	 */
	public static boolean isNnums2ListStatic(String numsStr,String delimiter) {
		return isNnums2ListStatic(numsStr,delimiter,CONTINUOUS);
	}
	
	/**
	 * numsStr当中是否包含有负数(分隔符为[,],连续符号为[~])
	 * @param numsStr 如"1~4,6,7,-1"返回false<Integer>
	 * @return 如"1~4,6,7"返回true
	 */
	public static boolean isNnums2ListStatic(String numsStr) {
		return isNnums2ListStatic(numsStr,DELIMITER,CONTINUOUS);
	}

	/**
	 * 将如"1~4,6,7,-1"返回List<Integer>类型的列表<1,2,3,4,6,7,8>
	 * @param numsStr 形如1~4,6,7; 3,2,1,4~-1(-n代表倒数第几个，比如-1为最后一个)
	 * @param delimiter 分隔符,如逗号
	 * @param input_string_length 总长度，当len>0时，numsStr的负数-n代表len倒数第n个; 当len<=0时,numsStr当中不能出现-n型的值
	 * @param continuous 连续符号，如1~4当中的~符号
	 * @return
	 */
	public static List<Integer> nums2List(String numsStr,int input_string_length,String delimiter,String continuous) {
    	if(StringUtils.isBlank(numsStr)){
    		return null;
    	}
    	else{
	    	String[] cc = numsStr.split(delimiter);
    		List<Integer> list=new ArrayList<Integer>();
    		for (int i = 0; i < cc.length; i++) {
    			String s=cc[i];
				if(s.split(continuous).length>1){
					String[] t = s.split(continuous);
					int start=Integer.parseInt(t[0]);
					int end=Integer.parseInt(t[1]);
					if((input_string_length<=0 && start<0 )||(input_string_length<=0 && end<0)){
						return null;
					}
					if(start<0) start=input_string_length+1+start;
					if(end<0) end=input_string_length+1+end;
					for (int j = start; j <= end; j++) {
						list.add(j);
					}
				}
				else{
					int ss = Integer.parseInt(s);
					if(input_string_length<=0 && ss<0 ){
						return null;
					}
					if(ss<0) ss=input_string_length+1+ss;
					list.add(ss);
				}
			}
    		return list;
    	}
		
	}
    
    /**
	 * 将如"1~4,6,7,-1"返回List<Integer>类型的列表<1,2,3,4,6,7,8>
	 * @param numsStr 形如1~4,6,7或3,2,1,4~-1   其中连续符号为~
	 * @param delimiter 分隔符,如逗号
	 * @param input_string_length 总长度，当len>0时，numsStr的负数-n代表len倒数第n个. 示例为len=8
	 */
    public static List<Integer> nums2List(String numsStr,String delimiter,int input_string_length) {
    	return nums2List(numsStr,input_string_length,delimiter,CONTINUOUS);
    	}
    
    /**
     * 将如"1~4,6,7"返回List<Integer>类型的列表<1,2,3,4,6,7>
     * @param numsStr 形如1~4,6,7  其中数值只能为正整数
	 * @param delimiter 分隔符,如逗号
	 * @param continuous 连续符号，如1~4当中的~符号
     * @return
     */
    public static List<Integer> nums2List(String numsStr,String delimiter,String continuous) {
    	return nums2List(numsStr,-1,delimiter,continuous);
    }
    
    /**
     * 将如"1~4,6,7,-1"返回List<Integer>类型的列表<1,2,3,4,6,7,8>
     * @param numsStr 形如1~4,6,7,-1  (其中-n代表len倒数第几个，比如-1等于8)
     * @param input_string_length 总长度，当len>0时，numsStr的负数-n代表len倒数第n个. 示例为len=8
     * @return
     */
    public static List<Integer> nums2List(String numsStr,int input_string_length) {
    	return nums2List(numsStr,input_string_length,DELIMITER,CONTINUOUS);
    	}
    
    /**
     * 将如"1~4,6,7"返回List<Integer>类型的列表<1,2,3,4,6,7>
     * @param numsStr 形如1~4,6,7 其中分隔符为逗号，连续符为"~", 数值只能为正整数
     * @return
     */
    public static List<Integer> nums2List(String numsStr) {
    	return nums2List(numsStr,-1,DELIMITER,CONTINUOUS);
    	}
    
	
}
