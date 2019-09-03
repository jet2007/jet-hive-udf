package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskEmail", value = "_FUNC_(input [,mask] ) - 电子邮箱账号前缀只保留前2位,其他脱敏成掩码\n" + "参数1：输入的电子邮箱账号"
		+ "参数2:可选参数,自定义的掩码字符,默认值为*", extended = "Example:\n > select _FUNC_('danny@126.com','#')  \n "
				+ ",_FUNC_('steven91@yeah.net')  \n " + "结果:da###@126.com,st******@yeah.net \n ")
public class UDFDesenMaskEmail extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskEmail(input.toString(), mask.toString());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskEmail(input.toString(), ConstantDesensitization.DEN_STR);
		result.set(value);
		return result;
	}

}
