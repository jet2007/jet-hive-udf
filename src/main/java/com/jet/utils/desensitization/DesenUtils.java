package com.jet.utils.desensitization;


import org.apache.commons.lang.StringUtils;

public class DesenUtils {

	

		public static final String DESEN_STR="*";
	    
	    /**
	     * 中间脱敏的通用方法
	     * middleDesensitization("abc12345", 2, 3, "*", 0, 0)   --> "ab***345"
	     * middleDesensitization("abc12345", 2, 3, "*", 2, 0)   --> "ab**345"
	     * middleDesensitization("abc12345", 3, 3, "*", 0, 3)   --> "abc***345"
	     * @param str 被脱敏的str
	     * @param leftLen 左侧保留的字符数(>0)
	     * @param rightLen 右侧保留的字符数(>0)
	     * @param DesenStr 脱敏的字符，例如*
	     * @param fixDesenStrLen 脱敏后的*数量；取0值：保留原长度；如abcded--> a****d; 取N=2时,只使用2个*，如abcdef-->a**f(示例：中间4位脱敏)
	     * @param fixDesenStrLenAtLeast 至少包含有*的数量；当fixDesenStrLen=0时有效；取0值表示不保证；取N=1时，至少包含一个*；
	     * @return 脱敏后的str
	     */
	    public static String middleDesensitization(Object strObj,int leftLen,int rightLen,String DesenStr,int fixDesenStrLen,int fixDesenStrLenAtLeast) {
	    	if(strObj!=null)  {
	    	String str=strObj.toString();
	    	if (StringUtils.isBlank(str)) {
	            return "";
	        }
	        else if(str.length()>=leftLen+rightLen){
	        	 String leftStr = StringUtils.left(str, leftLen);
			        String rigthStr = StringUtils.right(str, rightLen);
			        int deseLen;
			        if(fixDesenStrLen==0) {
			        	deseLen=str.length()-leftLen-rightLen;
			        	deseLen=fixDesenStrLenAtLeast>0 ? Math.max(deseLen, fixDesenStrLenAtLeast) : deseLen;
			        }
			        else {deseLen=fixDesenStrLen;}
			        return leftStr+StringUtils.repeat(DesenStr, deseLen)+rigthStr;
	        	
	        }
	        else {
	        	int deseLen;
	        	if(str.length()>=leftLen) {
	        		String leftStr = StringUtils.left(str, leftLen);
			        if(fixDesenStrLen==0) {
			        	deseLen=str.length()-leftLen;
			        	deseLen=fixDesenStrLenAtLeast>0 ? Math.max(deseLen, fixDesenStrLenAtLeast) : deseLen;
			        }
			        else {deseLen=fixDesenStrLen;}
			        return leftStr+StringUtils.repeat(DesenStr, deseLen);
	        	}else{
	        		return str;
	        	}
	        }
	    	}
	    	else {
				return null;
			}
	    }
	    
	    /**
	     * 右侧脱敏的通用方法
	     * rightDesensitization("abc12345",2,"*")     -->  "ab******"
	     * rightDesensitization("abc12345",5,"*")     -->  "abc12***"
	     * rightDesensitization("abc12345",10,"*")    -->  "abc12345*"
	     * rightDesensitization("abc12345",0,"*")     -->  "********"
	     * @param str 被脱敏的str
	     * @param leftLen 保留左侧不脱敏的字符个数
	     * @param DesenStr 脱敏字符
	     * @return 脱敏后的str
	     */
	    public static String rightDesensitization(Object strObj,int leftLen ,String DesenStr){
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		        if (StringUtils.isBlank(str)) {
		            return "";
		        }
		        else if(str.length()>leftLen){
			        int length = StringUtils.length(str);
			        String leftStr=StringUtils.left(str, leftLen);
			        return StringUtils.rightPad(leftStr, length, DesenStr);
		        }
		        else {
		        	return str+DesenStr;
		        }
	        }
	    	else {
	    		return null;
	    	}
	    }
	    
	    /**
	     * 左侧脱敏的通用方法
	     * leftDesensitization("abc12345",2,"*")     -->  "******45"
	     * leftDesensitization("abc12345",5,"*")     -->  "***12345"
	     * leftDesensitization("abc12345",10,"*")    -->  "abc12345*"
	     * leftDesensitization("abc12345",0,"*")     -->  "********"
	     * @param str 被脱敏的str
	     * @param rightLen 保留右侧不脱敏的字符个数
	     * @param DesenStr 脱敏字符
	     * @return 脱敏后的str
	     */
	    public static String leftDesensitization(Object strObj,int rightLen ,String DesenStr){
	    	if(strObj!=null)  {
	    	String str=strObj.toString();
		        if (StringUtils.isBlank(str)) {
		            return "";
		        }
		        else if(str.length()>rightLen){
			        int length = StringUtils.length(str);
			        String rightStr=StringUtils.right(str, rightLen);
			        return StringUtils.leftPad(rightStr, length, DesenStr);
		        }
		        else {
		        	return str+DesenStr;
		        }
	    	}
	    	else {
	    		return null;
	    	}
	    }
	    
	    /**
	     * 左侧脱敏的通用方法,与leftDesensitization不同的是，固定为fixLen脱敏字符
	     * @param strObj
	     * @param rightLen
	     * @param fixLen
	     * @param DesenStr
	     * @return
	     */
	    public static String leftFixedDesensitization(Object strObj,int rightLen ,int fixLen,String DesenStr){
	    	if(strObj!=null)  {
	    	String str=strObj.toString();
		        if (StringUtils.isBlank(str)) {
		            return "";
		        }
		        else if(str.length()>rightLen){
			        int length = rightLen+fixLen;
			        String rightStr=StringUtils.right(str, rightLen);
			        return StringUtils.leftPad(rightStr, length, DesenStr);
		        }
		        else {
		        	return str+DesenStr;
		        }
	    	}
	    	else {
	    		return null;
	    	}
	    }
		
	    /**
	     * 右侧脱敏的通用方法,与rightDesensitization不同的是，固定为fixLen脱敏字符
	     * @param strObj
	     * @param leftLen
	     * @param fixLen
	     * @param DesenStr
	     * @return
	     */
	    public static String rightFixedDesensitization(Object strObj,int leftLen,int fixLen ,String DesenStr){
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		        if (StringUtils.isBlank(str)) {
		            return "";
		        }
		        else if(str.length()>leftLen){
			        int length = leftLen+fixLen;
			        String leftStr=StringUtils.left(str, leftLen);
			        return StringUtils.rightPad(leftStr, length, DesenStr);
		        }
		        else {
		        	return str+DesenStr;
		        }
	        }
	    	else {
	    		return null;
	    	}
	    }
		
		
	    /**
	     * 【中文姓名】显示第一个汉字+*+最后一个字，比如：李小明-->李*明
	     * 
	     * @param fullName 中文姓名
	     * @return 脱敏后的中文姓名
	     */
	    public static String chineseName(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		    	return middleDesensitization(str,1,1,DesenStr,1,1);
	    	}
	    	else {
	    		return null;
	    	}
	    }
	

	    /**
	     * 【身份证号】显示前6后4位，其他隐藏。共计18位或者15位，比如：350122*******1234
	     * @param id
	     * @return
	     */
	    public static String idCardNum(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		    	return middleDesensitization(str,6,4,DesenStr,0,0);
	    	}
	    	else {
	    		return null;
	    	}
	    }

	    /**
	     * 【固定电话 后四位，其他隐藏，比如1234
	     *
	     * @param num
	     * @return
	     */
	    public static String fixedPhone(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		    	return leftDesensitization(str, 4, DesenStr);
	    	}
	    	else {
	    		return null;
	    	}
	    }

	    /**
	     * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
	     *
	     * @param mobile
	     * @return
	     */
	    public static String mobilePhone(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String str=strObj.toString();
		    	return middleDesensitization(str,3,4,DesenStr,0,0);
	    	}
	    	else {
	    		return null;
	    	}
	    	
	    	
	    	}

	    /**
	     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	     *
	     * @param address
	     * @param sensitiveSize 敏感信息长度
	     * @return
	     */
	    public static String address(Object strObj, int sensitiveSize,String DesenStr) {
	    	if(strObj!=null)  {
		    	String address=strObj.toString();
		        if (StringUtils.isBlank(address)) {
		            return "";
		        }
		        int length = StringUtils.length(address);
		        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, DesenStr);
	    	}
	    	else {
	    		return null;
	    	}
	    }

	    /**
	     * 【电子邮箱 邮箱前缀仅显示前2个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：da**@126.com>
	     *
	     * @param email
	     * @return
	     */
	    public static String email(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String email=strObj.toString();
	    	
		        if (StringUtils.isBlank(email)) {
		            return "";
		        }
		        int index = StringUtils.indexOf(email, "@");
		        if (index <= 1)
		            return email;
		        else {
		        	String leftStr = StringUtils.left(email, 2);
		        	return StringUtils.rightPad(leftStr, index, DesenStr).concat(StringUtils.mid(email, index, StringUtils.length(email)));
		        }
	    	}
	    	else {
	    		return null;
	    	}
	 }

	    /**
	     * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234>
	     *
	     * @param cardNum
	     * @return
	     */
	    public static String bankCard(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String cardNum=strObj.toString();
		    	return middleDesensitization(cardNum,6,4,DesenStr,0,0);
	    	}
	    	else {
	    		return null;
	    	}
	    }

	    /**
	     * 【密码】密码的全部字符都用*代替，比如：******
	     *
	     * @param password
	     * @return
	     */
	    public static String password(Object strObj,String DesenStr) {
	    	if(strObj!=null)  {
		    	String password=strObj.toString();
		    	return StringUtils.repeat(DesenStr, password.toString().length());
	    	}
	    	else {
	    		return null;
	    	}
	    }


	    
	    
	    
 
	  
	
}
