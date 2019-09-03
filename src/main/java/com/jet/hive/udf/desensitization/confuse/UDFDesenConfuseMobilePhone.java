package com.jet.hive.udf.desensitization.confuse;


import org.apache.commons.lang.RandomStringUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

@Description(name = "DesenConfuseMobilePhone", value = "_FUNC_(date_or_datetime ,cuts ) - 手机号后4位混淆(后4位置成随机数值)\n"
		+"参数1：手机号,string类型"
, extended = "Example:\n > select _FUNC_('18912345678' )  \n " 	
+"                    结果： '18912349642' \n "
)
public class UDFDesenConfuseMobilePhone extends UDF {
	private Text result = new Text();
	
	/**
	 * 将手机号的后4位进行混淆
	 * @param mobilePhone 手机号，示例18912345678
	 * @return 18912343295(后4位被置成随机值)
	 */
	public Text evaluate(Text mobilePhone) {
		if(mobilePhone==null){
			return null;
		}
		String str = mobilePhone.toString();
		String value =str.substring(0, str.length()-4)+RandomStringUtils.randomNumeric(4);
		result.set(value);
		return result;
	}
}
