package com.jet.hive.udf.desensitization.confuse;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.desensitization.DesenConfuseUtils;


@Description(name = "DesenConfusePositions", value = "_FUNC_(str ,postitions [,randomType] ) - 指定位置混淆，其余部分保持不变(混淆：置换成随机字符如字母a-zA-Z等)\n"
		+"参数1：string,输入的字符串\n"
		+"参数2：string,被混淆的位置列表，混淆位置部分,格式n1,n2,...,n3~n4,分隔符与连续符分别为[,~],示例'1,2,-3~-1'输入的第1,2，倒3至倒1位；\n"
		+"参数3：可选，string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]\n"
, extended = "Example:\n > select _FUNC_('1234567','1,2,-3~-1')  \n " 
+"                    _FUNC_('1234567','1,2,-3~-1','2')  \n "	
+"                    将第1,2位和倒3至倒1位共5个位置混淆(置为随机英文大小写字母)\n "
)
public class UDFDesenConfusePositions extends UDF {
	
	private Text re=new Text();

	public Text evaluate(Text input, Text postitions, Text... args) {
		if(input==null){
			return null;
		}
		String type;
		if(args.length==0){
			type=DesenConfuseUtils.RANDOM_DEFAULT;
		}else{
			type=args[0].toString();
		}
		String value = DesenConfuseUtils.confuseStr(input.toString(), postitions.toString(), type, ConstantDesensitization.DELIMITER, ConstantDesensitization.CONTINUOUS);
		re.set(value);
		return re;
	}
	
}
