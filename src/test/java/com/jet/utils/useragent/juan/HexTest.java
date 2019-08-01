package com.jet.utils.useragent.juan;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class HexTest {

	public static String str2Hex(String str) throws UnsupportedEncodingException {
		String hexRaw = String.format("%x", new BigInteger(1, str.getBytes("UTF-8")));
		char[] hexRawArr = hexRaw.toCharArray();
		StringBuilder hexFmtStr = new StringBuilder();
		final String SEP = "\\x";
		for (int i = 0; i < hexRawArr.length; i++) {
			hexFmtStr.append(SEP).append(hexRawArr[i]).append(hexRawArr[++i]);
		}
		return hexFmtStr.toString();
	}

	public static String hex2Str(String str) throws UnsupportedEncodingException {
		String strArr[] = str.split("\\\\"); // 分割拿到形如 xE9 的16进制数据
		byte[] byteArr = new byte[strArr.length - 1];
		for (int i = 1; i < strArr.length; i++) {
			Integer hexInt = Integer.decode("0" + strArr[i]);
			byteArr[i - 1] = hexInt.byteValue();
		}

		return new String(byteArr, "UTF-8");
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		System.out.println(str2Hex("中国1a23"));
		System.out.println(hex2Str("G9\\xe9\\x9d\\x92\\xe6\\x98\\xa5\\xe7\\x89\\x88"));
		System.out.println(hex2Str("aaaaaaa\\xE9\\xBB\\x84\\xE8\\x8A\\xB1\\xE6\\xA2\\xA8\\xE5\\xAE\\xB6\\xE5\\x85\\xB7\\xE8\\xBD\\xAC\\xE8\\xAE\\xA9"));
	}

}