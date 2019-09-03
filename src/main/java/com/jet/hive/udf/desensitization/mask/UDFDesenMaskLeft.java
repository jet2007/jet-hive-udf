package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskLeft", value = "_FUNC_(input,leftLen [,mask][,fixLen]) - 将左侧的前leftLen位脱敏成掩码\n" + "参数1:被脱敏的字符串"
		+ "参数2:左侧脱敏的字符长度" + "参数3:可选参数,自定义的掩码字符,默认值为*"
		+ "参数4:可选参数,脱敏成的掩码字符长度,默认保留原有长度", extended = "Example:\n > select _FUNC_('abc1234def',3,'#',1)  \n "
				+ ",_FUNC_('abc1234def',4,1)  \n " + ",_FUNC_('abc1234def',4,'#')  \n " + ",_FUNC_('abc1234def',4)  \n "
				+ "结果: #1234def,*234def,####234def,****234def \n ")
public class UDFDesenMaskLeft extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, IntWritable leftLen, Text mask, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskLeft(input.toString(), leftLen.get(), mask.toString(), fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskLeft(input.toString(), leftLen.get(), ConstantDesensitization.DEN_STR,
				fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskLeft(input.toString(), leftLen.get(), mask.toString(), leftLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskLeft(input.toString(), leftLen.get(), ConstantDesensitization.DEN_STR, leftLen.get());
		result.set(value);
		return result;
	}

}
