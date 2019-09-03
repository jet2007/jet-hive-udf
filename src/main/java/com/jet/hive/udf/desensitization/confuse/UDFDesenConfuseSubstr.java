package com.jet.hive.udf.desensitization.confuse;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import com.jet.utils.desensitization.DesenConfuseUtils;


@Description(name = "DesenConfuseSubstr", value = "_FUNC_(str ,begin,end [,randomType]) - 混淆从begin到end位置的字符，其余字符保持不变\n"
		+"参数1：string,输入的字符串\n"
		+"参数2：int,从1起，被混淆的开始位置\n"
		+"参数3：int,从1起，被混淆的结束位置\n"
		+"参数4：可选,string,随机字符串类型(范围：NUMERIC=0,ALPHABETIC=1,ALPHABETIC_LOWER=2,ALPHABETIC_UPPER=3,ALPHA_NUMERIC=4,ASCII=5)，默认值为[ALPHABETIC]\n"
, extended = "Example:\n > select _FUNC_('abcd','2')  \n " 
+"                    _FUNC_('abcd','2','4')  \n "	
+"                    _FUNC_('abcd','2','0')  \n "	
)
public class UDFDesenConfuseSubstr extends UDF {
	
	private static Text re=new Text();	

	public Text evaluate(Text input, IntWritable beginIW,IntWritable endIW, Text... args) {
		if(input==null){
			return null;
		}
		String str = input.toString();
		int strLen = str.length();
		int begin=beginIW.get();
		int end=endIW.get();
		
		if(begin<0) begin=strLen+begin+1;
		else if(begin==0) return input;
		if(end<0) end=strLen+end+1;
		else if(end==0) return input;
		if(begin>end) return null;
		
		String cf=DesenConfuseUtils.RANDOM_DEFAULT;
		if(args.length>0){
			cf=args[0].toString();
		}
		String reserveStrLeft = str.substring(0,begin-1);
		String reserveStrRight = str.substring(end,strLen);
		String confuseStr = DesenConfuseUtils.getRandom(cf, end-begin+1);
		re.set(reserveStrLeft+confuseStr+reserveStrRight);
		return re;
	}
	
//	public static void main(String[] args) {
//		UDFDosenConfuseSubstr udf=new UDFDosenConfuseSubstr();
//		Text res = udf.evaluate(new Text("1234567"), new IntWritable(2),new IntWritable(7), new Text("2"));
//		System.out.println(res);
//	}
	
}
