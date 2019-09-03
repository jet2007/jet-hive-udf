package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskLeft", value = "_FUNC_(input,rightLen [,mask][,fixLen]) - 将右侧的前rightLen位脱敏成掩码\n"
		+ "参数1:被脱敏的字符串" + "参数2:右侧脱敏的字符长度" + "参数3:可选参数,自定义的掩码字符,默认值为*"
		+ "参数4:可选参数,脱敏成的掩码字符长度,默认保留原有长度", extended = "Example:\n > select _FUNC_('abc1234def',3,'#',1)  \n "
				+ ",_FUNC_('abc1234def',4,1)  \n " + ",_FUNC_('abc1234def',4,'#')  \n " + ",_FUNC_('abc1234def',4)  \n "
				+ "结果: abc1234#,abc123*,abc123####,abc123**** \n ")
public class UDFDesenMaskRight extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, IntWritable rightLen, Text mask, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskRight(input.toString(), rightLen.get(), mask.toString(), fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable rightLen, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskRight(input.toString(), rightLen.get(), ConstantDesensitization.DEN_STR,
				fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable rightLen, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskRight(input.toString(), rightLen.get(), mask.toString(), rightLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable rightLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskRight(input.toString(), rightLen.get(), ConstantDesensitization.DEN_STR, rightLen.get());
		result.set(value);
		return result;
	}

}
