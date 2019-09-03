package com.jet.utils.desensitization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.jet.utils.string.NumsString2ListUtils;

public class DesenConfuseUtils {
	
	public static final String RANDOM_NUMERIC="0";  //数字
	public static final String RANDOM_ALPHABETIC="1"; //字母
	public static final String RANDOM_ALPHABETIC_LOWER="2"; //字母小写
	public static final String RANDOM_ALPHABETIC_UPPER="3";//字母大写
	public static final String RANDOM_ALPHA_NUMERIC="4"; //字母+字母
	public static final String RANDOM_ASCII="5"; //ASCII
	public static final String RANDOM_DEFAULT="4"; //默认值=RANDOM_ALPHA_NUMERIC
	
	/**
	 * 返回指定长度的随机字符串
	 * NUMERIC="0";
	 * ALPHABETIC="1";
	 * ALPHABETIC_LOWER="2";
	 * ALPHABETIC_UPPER="3";
	 * ALPHA_NUMERIC="4";
	 * ASCII="5";
	 * @param type 不同类型的字符集
	 * @param count 长度
	 * @return
	 */
	public static String getRandom(String type,int count){
		if(type.equals(RANDOM_NUMERIC)){
			return RandomStringUtils.randomNumeric(count);
		}else if(type.equals(RANDOM_ALPHABETIC)){
			return RandomStringUtils.randomAlphabetic(count);
		}else if(type.equals(RANDOM_ALPHABETIC_LOWER)){
			return RandomStringUtils.randomAlphabetic(count).toLowerCase();
		}else if(type.equals(RANDOM_ALPHABETIC_UPPER)){
			return RandomStringUtils.randomAlphabetic(count).toUpperCase();
		}else if(type.equals(RANDOM_ALPHA_NUMERIC)){
			return RandomStringUtils.randomAlphanumeric(count);
		}else if(type.equals(RANDOM_ASCII)){
			return RandomStringUtils.randomAscii(count);
		}else {
			return getRandom(RANDOM_DEFAULT,count);
			}
	}
	
	
	/**
	 * 指定位置混淆，其余部分保持不变(混淆：置换成随机字符如字母a-zA-Z等)
	 * 随机字符串类型
	 * NUMERIC="0";
	 * ALPHABETIC="1";
	 * ALPHABETIC_LOWER="2";
	 * ALPHABETIC_UPPER="3";
	 * ALPHA_NUMERIC="4";
	 * ASCII="5";
	 * 示例 fun("1234567890","1,2,-3~-1",",","~","1") = HK34567cTJ
	 * @param str 输入String
	 * @param positions  混淆位置部分,格式n1,n2,...,n3~n4,示例"1,2,-3~-1"输入的第1,2，倒3至倒1位
	 * @param type 随机字符串类型
	 * @param delimiter positions的分隔符，如,
	 * @param continuous positions的连续符，如~
	 * @return 随机字符串类型
	 */
	public static String confuseStr(String str, String positions,String type ,String delimiter,String continuous) {
		if(StringUtils.isBlank(str)){
			return str;
		}
		List<Integer> numsList = NumsString2ListUtils.nums2List(positions,str.length() ,delimiter,continuous);
		Set<Integer> set = new HashSet<Integer>(numsList);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if(set.contains(Integer.valueOf(i+1))){
				sb.append(getRandom(type,1));
			}else{
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
	
	

	

}
