package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskBankCard", value = "_FUNC_(input [,mask]) - 银行卡号保留前6和后4位,其他脱敏为掩码\n" + "参数1：输入的银行卡号"
		+ "参数2:可选参数,自定义的掩码字符,默认值为*", extended = "Example:\n > select _FUNC_('6224121206590423059','#')  \n "
				+ ",_FUNC_('6224121206590423059')  \n " + "结果: 622412#########3059,622412*********3059 \n ")
public class UDFDesenMaskBankCard extends UDF {

	private Text result = new Text();

	public Text evaluate(Text input, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskBankCard(input.toString(), mask.toString());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskBankCard(input.toString(), ConstantDesensitization.DEN_STR);
		result.set(value);
		return result;
	}

}
