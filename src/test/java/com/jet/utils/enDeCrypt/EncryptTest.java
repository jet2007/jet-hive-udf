package com.jet.utils.enDeCrypt;

import org.junit.Test;

import java.util.Date;

import org.junit.Assert;

public class EncryptTest {

	@Test
    public void testEncrypt() throws Exception {
		String text="abc";
		String textEncodemd5= Encrypt.base64(text);
		String textEncodesha256 = Encrypt.sha256(text);
		String textEncodemd512 = Encrypt.sha512(text);
		String textEncodemdbase64 = Encrypt.base64(text);
		
		System.out.println(textEncodemd5);
		System.out.println(textEncodesha256);
		System.out.println(textEncodemd512);
		System.out.println("|"+textEncodemdbase64+"|");
		System.out.println(Decrypt.base64(textEncodemdbase64));
		
		String key = "xUHdKxzVCbsgVIwTnc1jtpWn";  
        String idcard = "13612345678";  
        String encode = Encrypt.des3(key, idcard);  
        System.out.println("原串：" + idcard);  
        System.out.println("加密后的串：" + encode);  
		
		
		int len=1000000;
		System.out.println(new Date());
		for (int i = 0; i < len; i++) {
			textEncodemd5 = Decrypt.des3(key, "UpndVp1Yw4WlEO+ams97Bg==");  
		}
		System.out.println(textEncodemd5);
		System.out.println(new Date());
		
		
		

		
		
		Assert.assertEquals(textEncodemd5, "900150983cd24fb0d6963f7d28e17f72");
	}
}
