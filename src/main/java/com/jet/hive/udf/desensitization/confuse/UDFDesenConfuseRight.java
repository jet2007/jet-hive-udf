package com.jet.hive.udf.desensitization.confuse;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import com.jet.utils.desensitization.DesenConfuseUtils;


@Description(name = "DesenConfuseRight", value = "_FUNC_(str ,len [,randomType]) - 混淆右侧len长度的字符，其余保持不变\n"
		+"参数1：string,输入的字符串\n"
		+"参数2：int,从右侧被混淆的位置长度\n"
		+"参数3：可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]\n"
, extended = "Example:\n > select _FUNC_('abcd','2')  \n " 
+"                    _FUNC_('abcd','2','4')  \n "	
+"                    _FUNC_('abcd','2','0')  \n "	
)
public class UDFDesenConfuseRight extends UDF {
	
	private static Text re=new Text();	

	public Text evaluate(Text input, IntWritable len, Text... args) {
		if(input==null||len.get()<=0){
			return input;
		}
		String str = input.toString();
		int length = len.get();
		if (length>str.length()){
			length=str.length();
		}
		String cf=DesenConfuseUtils.RANDOM_DEFAULT;
		if(args.length>0){
			cf=args[0].toString();
		}
		String reserveStr = str.substring(0,str.length()-length);
		String confuseStr = DesenConfuseUtils.getRandom(cf, length);
		re.set(reserveStr+confuseStr);
		return re;
	}
	
//	public static void main(String[] args) {
//		UDFDosenConfuseRight udf=new UDFDosenConfuseRight();
//		Text res = udf.evaluate(new Text("abcd"), new IntWritable(6), new Text("0"));
//		System.out.println(res);
//	}
	
}
