package com.jet.hive.udf.desensitization.shift;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import com.jet.utils.desensitization.DesenLinearShiftUtils;



@Description(name = "DesenDosenLinearShift", value = "_FUNC_(input [,k] [,b]) - 进行线性变化\n"
		+"参数1：输入的数值"
		+"参数2：kx+b的斜率k,默认值为7"
		+"参数3：kx+b的b为常数，默认值为997"
		, extended = "Example:\n > select _FUNC_(1),_FUNC_(1,7),_FUNC_(1,7,997)  \n " 
)
public class UDFDesenLinearShift extends UDF {
	private static final int B=997; // 1000内质数
	private static final int K=7;  // 100内质数
	private int k ;
	private int b ;
	
	private void initLinearKey(IntWritable... args){
		if(args.length==1){
			k=args[0].get();
			b=B;
		}else if(args.length>=2){
			k=args[0].get();
			b=args[1].get();
		}else{
			k=K;
			b=B;
		}
	}
	
	public LongWritable evaluate(LongWritable input, IntWritable... args) {
		if(input==null){
			return null;
		}
		initLinearKey(args);
		LongWritable result = new LongWritable();
		result.set(DesenLinearShiftUtils.linearShift(input.get(), k, b));
		return result;
	}
	
	
	public IntWritable evaluate(IntWritable input, IntWritable... args) {
		if(input==null){
			return null;
		}
		initLinearKey(args);
		IntWritable result = new IntWritable();
		result.set(DesenLinearShiftUtils.linearShift(input.get(), k, b));
		return result;
	}
	
	
	public DoubleWritable evaluate(DoubleWritable input, IntWritable... args) {
		if(input==null){
			return null;
		}
		initLinearKey(args);
		DoubleWritable result = new DoubleWritable();
		result.set(DesenLinearShiftUtils.linearShift(input.get(), k, b));
		return result;
	}
	
	
	public FloatWritable evaluate(FloatWritable input, IntWritable... args) {
		if(input==null){
			return null;
		}
		initLinearKey(args);
		FloatWritable result = new FloatWritable();
		result.set(DesenLinearShiftUtils.linearShift(input.get(), k, b));
		return result;
	}
	
	
}
