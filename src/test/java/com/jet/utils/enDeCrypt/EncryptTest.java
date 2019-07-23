package com.jet.utils.enDeCrypt;

import org.junit.Test;
import org.junit.Assert;

public class EncryptTest {

	@Test
    public void testEncrypt() throws Exception {
		String text="abc";
		String textEncode = Encrypt.md5(text);
		Assert.assertEquals(textEncode, text);
	}
}
