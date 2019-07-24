package com.jet.utils.string;

public class StringUtils {
	
	/**
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	/**
	 * 
	 * @param str the String to check, may be null
	 * @return <code>false</code> if the String is null, empty or whitespace
	 */
	public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	
	
//	
//	public static void main(String[] args) {
//		System.out.println(
//				isNotBlank("    ")
//				);
//	}
	
}
