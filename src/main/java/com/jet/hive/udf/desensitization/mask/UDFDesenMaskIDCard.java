package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskIDCard", value = "_FUNC_(input [,mask] ) - 身份证号保留前3和后4位,其他脱敏成掩码\n" + "参数1:输入的身份证号"
		+ "参数2:可选参数,自定义的掩码字符,默认值为*", extended = "Example:\n > select _FUNC_('411678199204188911','#')  \n "
				+ ",_FUNC_('512199204187834')  \n " + "结果: 411###########8911,512********7834 \n ")
public class UDFDesenMaskIDCard extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskIDCard(input.toString(), mask.toString());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskIDCard(input.toString(), ConstantDesensitization.DEN_STR);
		result.set(value);
		return result;
	}

}
