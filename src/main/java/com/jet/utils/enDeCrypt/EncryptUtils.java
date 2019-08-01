package com.jet.utils.enDeCrypt;

import org.apache.commons.codec.digest.DigestUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;  


public class EncryptUtils {

	
	public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }
	
	public static String sha256(String text) {
        return DigestUtils.sha256Hex(text);
    }
	
	public static String sha512(String text) {
        return DigestUtils.sha512Hex(text);
    }
	
	public static String base64(String text) {
		byte[] b = Base64.encodeBase64(text.getBytes(), true);
        return new String(b);
    }
	

	/** 
     * 转换成十六进制字符串 
     * @param username 
     * @return 
     * 
     * lee on 2017-08-09 10:54:19 
     */  
    public static byte[] hex(String key){    
        String f = DigestUtils.md5Hex(key);    
        byte[] bkeys = new String(f).getBytes();    
        byte[] enk = new byte[24];    
        for (int i=0;i<24;i++){    
            enk[i] = bkeys[i];    
        }    
        return enk;    
    }  
	
    /** 
     * 3DES加密 
     * @param key 密钥，24位 
     * @param srcStr 将加密的字符串 
     * @return 
     * 
     * lee on 2017-08-09 10:51:44 
     */  
    public static String  des3(String key,String srcStr){    
        byte[] keybyte = hex(key);  
        byte[] src = srcStr.getBytes();  
        try {    
           //生成密钥    
           SecretKey deskey = new SecretKeySpec(keybyte, "DESede");  
           //加密    
           Cipher c1 = Cipher.getInstance("DESede");  
           c1.init(Cipher.ENCRYPT_MODE, deskey);    
             
           String pwd = Base64.encodeBase64String(c1.doFinal(src));  
//           return c1.doFinal(src);//在单一方面的加密或解密    
           return pwd;  
       } catch (java.security.NoSuchAlgorithmException e1) {    
           // TODO: handle exception    
            e1.printStackTrace();    
       }catch(javax.crypto.NoSuchPaddingException e2){    
           e2.printStackTrace();    
       }catch(java.lang.Exception e3){    
           e3.printStackTrace();    
       }    
       return null;    
   }  
    
    
    
}
