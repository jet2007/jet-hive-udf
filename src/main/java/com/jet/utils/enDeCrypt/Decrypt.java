package com.jet.utils.enDeCrypt;

import org.apache.commons.codec.binary.Base64;

public class Decrypt {

	public static String base64(String text) {
		byte[] b = Base64.decodeBase64(text.getBytes());
        return new String(b);
        
    }
}
