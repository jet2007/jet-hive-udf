package com.jet.utils.enDeCrypt;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DecryptUtils {

	public static String base64(String text) {
		byte[] b = Base64.decodeBase64(text.getBytes());
        return new String(b);
        
    }
	
	
	/** 
	    * 3DES解密 
	    * @param key 加密密钥，长度为24字节   
	    * @param desStr 解密后的字符串 
	    * @return 
	    * 
	    * lee on 2017-08-09 10:52:54 
	    */   
	    public static String des3(String key, String desStr){    
	        Base64 base64 = new Base64();  
	        byte[] keybyte = EncryptUtils.hex(key);  
	        byte[] src = base64.decode(desStr);  
	                  
	        try {    
	            //生成密钥    
	            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");    
	            //解密    
	            Cipher c1 = Cipher.getInstance("DESede");    
	            c1.init(Cipher.DECRYPT_MODE, deskey);    
	            String pwd = new String(c1.doFinal(src));  
	            return pwd;  
	        } catch (java.security.NoSuchAlgorithmException e1) {    
	            e1.printStackTrace();    
	        }catch(javax.crypto.NoSuchPaddingException e2){    
	            e2.printStackTrace();    
	        }catch(java.lang.Exception e3){    
	            e3.printStackTrace();    
	        }    
	        return null;            
	    }  
	
}
