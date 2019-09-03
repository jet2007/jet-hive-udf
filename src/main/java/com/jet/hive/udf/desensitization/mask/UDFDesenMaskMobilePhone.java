package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskMobilePhone", value = "_FUNC_(input [,mask] ) - 手机号保留前3和后4位,其他脱敏成掩码\n" + "参数1：输入的手机号"
		+ "参数2:可选参数,自定义的掩码字符,默认值为*", extended = "Example:\n > select _FUNC_('13700012345','#')  \n "
				+ ",_FUNC_('18545678900')  \n " + "结果: 137####2345,185****8900 \n ")
public class UDFDesenMaskMobilePhone extends UDF {
	private Text result = new Text();
	
	public Text evaluate(Text input, Text mask) {
		if(input==null){
			return null;
		}
		String value = DesenMaskUtils.maskMobilePhone(input.toString(), mask.toString());
		result.set(value);
		return result;
	}
	
	public Text evaluate(Text input) {
		if(input==null){
			return null;
		}
		String value = DesenMaskUtils.maskMobilePhone(input.toString(), ConstantDesensitization.DEN_STR);
		result.set(value);
		return result;
	}
	
	

	
}
