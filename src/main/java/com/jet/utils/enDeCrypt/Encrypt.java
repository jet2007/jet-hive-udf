package com.jet.utils.enDeCrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class Encrypt {
	
	
	public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }
	
	public static String sha256(String text) {
        return DigestUtils.sha256Hex(text);
    }
	
	public static String base64(String text) {
		byte[] b = Base64.encodeBase64(text.getBytes(), true);
        return new String(b);
    }
	

}
