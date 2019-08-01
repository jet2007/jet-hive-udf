package com.jet.utils.string;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtils {
	/**
	 * 分割拿到形如 \xE9 的16进制数据,返回string
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String hex2Str(String str)   {
		String strArr[] = str.split("\\\\"); // 
		byte[] byteArr = new byte[strArr.length - 1];
		for (int i = 1; i < strArr.length; i++) {
			Integer hexInt = Integer.decode("0" + strArr[i]);
			byteArr[i - 1] = hexInt.byteValue();
		}
		try {
			return new String(byteArr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	/**
	 * 拿到形如 \xE9的16进制数据+正常abc,返回string
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String mixHex2Str(String str){
		String re=str;
		String regex = "(\\\\x\\w\\w)+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(re);
		while (matcher.find()) {
			re=re.replace(matcher.group(0), hex2Str(matcher.group(0)));
		    matcher = pattern.matcher(re);
		}		
		return re;
	}
}
