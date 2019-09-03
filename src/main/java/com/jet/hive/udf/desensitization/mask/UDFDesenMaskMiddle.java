package com.jet.hive.udf.desensitization.mask;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenMaskUtils;

@Description(name = "DesenMaskMiddle", value = "_FUNC_(input,leftLen,rightLen [,mask][,fixLen]) - 除去左右侧保留的部分，其他全部脱敏成掩码\n"
		+ "参数1:被脱敏的字符串" + "参数2:左侧保留的字符长度" + "参数3:右侧保留的字符长度" + "参数4:可选参数,自定义的掩码字符,默认值为*"
		+ "参数5:可选参数,脱敏成的掩码字符长度,默认保留原有长度", extended = "Example:\n > select _FUNC_('abc1234def',3,3,'#',1)  \n "
				+ ",_FUNC_('abc1234def',3,3,1)  \n " + ",_FUNC_('abc1234def',3,3,'#')  \n "
				+ ",_FUNC_('abc1234def',3,3)  \n " + "结果: abc#def,abc*def,abc####def,abc****def \n ")
public class UDFDesenMaskMiddle extends UDF {
	private Text result = new Text();

	public Text evaluate(Text input, IntWritable leftLen, IntWritable rightLen, Text mask, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskMiddle(input.toString(), leftLen.get(), rightLen.get(), mask.toString(),
				fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen, IntWritable rightLen, IntWritable fixLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskMiddle(input.toString(), leftLen.get(), rightLen.get(),
				ConstantDesensitization.DEN_STR, fixLen.get());
		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen, IntWritable rightLen, Text mask) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskMiddle(input.toString(), leftLen.get(), rightLen.get(), mask.toString(), -1);

		result.set(value);
		return result;
	}

	public Text evaluate(Text input, IntWritable leftLen, IntWritable rightLen) {
		if (input == null) {
			return null;
		}
		String value = DesenMaskUtils.maskMiddle(input.toString(), leftLen.get(), rightLen.get(),
				ConstantDesensitization.DEN_STR, -1);
		result.set(value);
		return result;
	}

}
