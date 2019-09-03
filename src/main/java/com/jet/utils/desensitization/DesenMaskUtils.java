package com.jet.utils.desensitization;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jet.utils.string.NumsString2ListUtils;

public class DesenMaskUtils {

	public static final String DESEN_STR = "*";
	public static final String DELIMITER=","; 
	public static final String CONTINUOUS="~";

	// 获取脱敏校验字符
	public static String getMaskString(String mask) {
		if (mask == null || StringUtils.isBlank(mask) || mask.length() != 1) {
			mask = DESEN_STR;
		}
		return mask;
	}

	/**
	 * 身份证脱敏的方法,保留前3位和后4位,其他位脱敏成掩码 
	 * maskIDCardUtil("421424199504137211","#") -->"421###########7211" 
	 * maskIDCardUtil("413199003148821","*") -->"413********8821"
	 * @param str 被脱敏的字符串
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskIDCard(String str, String mask) {
		String newStr = DesenMaskUtils.maskMiddle(str, 3, 4, mask, -1);
		return newStr;
	}

	/**
	 * 手机号的脱敏方法 ,保留前3位和后4位,其他位脱敏成掩码 
	 * maskMobilePhoneUtil("13712504312","*") --> "137****4312"
	 * @param str 被脱敏的字符串
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskMobilePhone(String str, String mask) {
		String newStr = DesenMaskUtils.maskMiddle(str, 3, 4, mask, -1);
		return newStr;
	}

	/**
	 * 固定电话的脱敏方法,保留后4位,其他脱敏成掩码,长度小于6位时，不脱敏
	 * maskFixedPhoneUtil("95533","#") --> "95533"
	 * maskFixedPhoneUtil("01076453210","*") --> "*******3120"
	 * @param str 被脱敏的字符串
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskFixedPhone(String str, String mask) {
		String newStr = null;
		if (str != null) {
			int len = str.length();
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else if (len <6 ) {
				newStr = str;
			} else {
				newStr = DesenMaskUtils.maskMiddle(str, 0,4,mask,-1);
			}
		}
		return newStr;
	}

	/**
	 * 银行卡号的脱敏方法 ,保留前6位和后4位,其他脱敏成掩码
	 * maskBankCardUtil("6224121206590423059","#") -->"622412#########3059"
	 * @param str 被脱敏的字符串
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskBankCard(String str, String mask) {
		String newStr = DesenMaskUtils.maskMiddle(str, 6, 4, mask, -1);
		return newStr;
	}

	/**
	 * 密码的脱敏方法,将将字串全部脱敏成掩码
	 * maskPasswordUtil("123456","#") --> "######"
	 * @param str 被脱敏的字符串
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskPassword(String str, String mask) {
		String newStr = maskMiddle(str, 0,0, mask,-1);
		return newStr;
	}

	/**
	 * 中文姓名的脱敏方法 ,只保留第一个汉字，其他脱敏成掩码
	 * maskChineseNameUtil("李小明","#") --> "李##"
	 * @param str 被脱敏的字符
	 * @param mask   替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskChineseName(String str, String mask) {
		String newStr = DesenMaskUtils.maskMiddle(str, 1,0,mask,-1);
		return newStr;
	}

	/**
	 * 邮箱脱敏方法,邮箱前缀仅显示前2个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，
	 * danny@126.com->da**@126.com
	 * @param str 被脱敏的字符
	 * @param mask 替换的字符
	 * @return 脱敏后的str
	 */
	public static String maskEmail(String str, String mask) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr=null;
		if (str != null) {
			String email = str;
			if (StringUtils.isBlank(email)) {
				newStr= "";
			}
			int index = StringUtils.indexOf(email, "@");
			if (index <= 1)
				newStr= email;
			else {
				newStr=DesenMaskUtils.maskLeft(str, 2,mask,2);
			}
		} 
		
		return newStr;
	}

	/**
	 * 左侧脱敏的通用方法 ,将左侧的N位脱敏成掩码
	 * @param str      被脱敏的str
	 * @param lefttLen 左侧脱敏的字符长度,大于字符串长度时，返回mask+str
	 * @param fixLen   脱敏后的脱敏字符长度 -1时 保持原有长度
	 * @param mask     脱敏字符
	 * @return 脱敏后的str
	 * maskLeft("abc12345",3,"*",1) --> "*12345"
	 * maskLeft("abc",5,"*",1)->"*abc"
	 */
	public static String maskLeft(String str, int leftLen, String mask, int fixLen) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr=null;
		if (str != null) {
			int len=str.length();
			if (StringUtils.isBlank(str)) {
				newStr= "";
			} else if (len> leftLen) {
				newStr= StringUtils.repeat(mask,fixLen)+StringUtils.right(str, len-leftLen);
			} else {
				newStr= mask+str;
			}
		} 
			return newStr;
	}

	/**
	 * 右侧脱敏的通用方法 ,将右侧的N位脱敏成掩码
	 * @param str      被脱敏的str
	 * @param righttLen 右侧脱敏的字符长度,大于字符串长度时,返回str+mask
	 * @param fixLen   脱敏后的脱敏字符长度  -1时 保持原有长度
	 * @param mask     脱敏字符
	 * @return 脱敏后的str
	 * maskRight("abc12345",3,"*",1) --> "abc12*"
	 * maskRight("abc",5,"*",1)->"abc*"
	 */
	public static String maskRight(String str, int rightLen, String mask, int fixLen) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr = null;
		if (str != null) {
			int len = str.length();
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else if (len > rightLen) {
				newStr = StringUtils.left(str, len - rightLen) + StringUtils.repeat(mask, fixLen);
			} else {
				newStr = str + mask;
			}
		}
		return newStr;
	}

	/**
	 * 中间脱敏的通用方法 ,左右两边各保留一部分，中间部分脱敏成掩码
	 * @param str                   被脱敏的str
	 * @param leftLen               左侧保留的字符数(>0)
	 * @param rightLen              右侧保留的字符数(>0)
	 * @param mask                  脱敏的字符，例如*
	 * @param fixDesenStrLen        脱敏后的*数量；取-1值：保留原长度；如abcded--> a****d;
	 *                              取N=2时,只使用2个*，如abcdef-->a**f(示例：中间4位脱敏)
	 * @return 脱敏后的str
	 * maskMiddle("abc12345", 2, 3, "*", -1) --> "ab***345"
	 * maskMiddle("abc12345", 2, 3, "*", 2) --> "ab**345"
	 * maskMiddle("abc12345", 3, 3, "*", 0) --> "abc***345"
	 */
	public static String maskMiddle(String str, int leftLen, int rightLen, String mask, int fixDesenStrLen) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr = null;
		if (str != null) {
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else if (str.length() >= leftLen + rightLen) {
				String leftStr = StringUtils.left(str, leftLen);
				String rigthStr = StringUtils.right(str, rightLen);
				int deseLen;
				if (fixDesenStrLen == -1) {
					deseLen = str.length() - leftLen - rightLen;
				} else {
					deseLen = fixDesenStrLen;
				}
				newStr = leftStr + StringUtils.repeat(mask, deseLen) + rigthStr;

			} else {

				newStr = str;
			}
		}
		return newStr;
	}

	/**
	 * 将字符串中的某一段脱敏的通用方法 
	 * @param str    被脱敏的str
	 * @param start  开始脱敏的字符位数 start<=end 否则返回原字符串
	 * @param end    结束脱敏的字符位数(包含)
	 * @param fixLen 脱敏后的脱敏字符长度 -1时 保持原有长度
	 * @param mask   脱敏字符
	 * @return 脱敏后的str
	 * maskSubUtil("abc12345",2,3,"*",1) --> "a*12345"
	 * maskSubUtil("abc12345",5,6,"*",-1) --> "abc1**45"
	 */
	public static String maskSub(String str, int start, int end, String mask, int fixLen) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr = null;
		if (str != null) {
			int len = str.length();
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else {
				if (start > end) {
					newStr = str;
				} else {
					if (start < 1) {
						start = 1;
					}
					if (end > len) {
						end = len;
					}
					if (fixLen == -1) {
						fixLen = end - start + 1;
					}
					newStr = StringUtils.left(str, start - 1) + StringUtils.repeat(mask, fixLen)
							+ StringUtils.right(str, len - end);
				}
			}
		}
		return newStr;
	}

	/**
	 * 将字符串中的某些位脱敏的通用方法
	 * @param str        被脱敏的str
	 * @param numsStr    脱敏的字符串位数"1~3,4,5,-1"表示第1,2,3,4,5和最后1位
	 * @param mask       脱敏字符
	 * @return 脱敏后的str
	 * maskInclude("a1b2c3@rg564ty%","1~-10,5,7,-6,-1","*") --> "#######rg#64ty#"
	 */
	public static String maskInclude(String str, String numsStr, String mask) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr = null;
		if (str != null) {
			int len = str.length();
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else {
				newStr = str;
				List<Integer> numsList = NumsString2ListUtils.nums2List(numsStr, len, DELIMITER, CONTINUOUS);
				for (Integer num : numsList) {
					int index = num.intValue();
					newStr = DesenMaskUtils.maskSub(newStr, index, index, mask, -1);

				}
			}
		}
		return newStr;
	}

	/**
	 * 将字符串中的某些位以外的脱敏的通用方法
	 * @param str        被脱敏的str
	 * @param numsStr    不脱敏的字符串位数"1~3,4,5,-1"表示第1,2,3,4,5和最后1位
	 * @param mask       脱敏字符
	 * @return 脱敏后的str
	 * maskInclude("a1b2c3@rg564ty%","1~-10,5,7,-6,-1","*") -->"a1b2c3@##5####%"
	 */
	public static String maskExclude(String str, String numsStr, String mask) {
		mask = DesenMaskUtils.getMaskString(mask);
		String newStr = null;
		if (str != null) {
			if (StringUtils.isBlank(str)) {
				newStr = "";
			} else {
				int len = str.length();
				newStr = StringUtils.repeat(mask, len);
				char[] str_array = str.toCharArray();
				List<Integer> numsList = NumsString2ListUtils.nums2List(numsStr, len, DELIMITER, CONTINUOUS);
				for (Integer num : numsList) {
					int index = num.intValue();
					newStr = DesenMaskUtils.maskSub(newStr, index, index, str_array[index - 1]+"", -1);
				}
			}
		}
		return newStr;
	}
	
	
}
