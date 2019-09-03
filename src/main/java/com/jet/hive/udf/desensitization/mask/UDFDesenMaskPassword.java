package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskPassword", value = "_FUNC_(input [,mask] ) - 将密码全部脱敏成掩码\n" + "参数1:输入的密码"
		+ "参数2:可选参数,自定义的掩码字符,默认值为*", extended = "Example:\n > select _FUNC_('abc36dshjf','#')  \n "
				+ ",_FUNC_('abc123')  \n " + "结果: ##########,****** \n ")
public class UDFDesenMaskPassword extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskPassword(input.toString(), mask.toString());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskPassword(input.toString(), ConstantDesensitization.DEN_STR);
		result.set(value);
		return result;
	}

}
