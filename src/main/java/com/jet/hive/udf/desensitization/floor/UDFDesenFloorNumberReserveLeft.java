package com.jet.hive.udf.desensitization.floor;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import com.jet.utils.desensitization.DesenFloorUtils;



@Description(name = "DesenFloorNumberReserveLeft", value = "_FUNC_(input ,reserveLeftLength ) - 从左起保留n位，其余置为0\n"
		+"参数1：输入的数值"
		+"参数2：从左起保留的长度"
		, extended = "Example:\n > select _FUNC_(123456789876543,2)  \n " 
				    +"                    _FUNC_(1234567, 4)  \n "	
				    +"                    结果：  1200000000000000,1234000\n "
)
public class UDFDesenFloorNumberReserveLeft extends UDF {
	
	public LongWritable evaluate(LongWritable input, IntWritable reserveLeftLength) {
		if(input==null){
			return null;
		}
		LongWritable resultLong = new LongWritable();
		long value = DesenFloorUtils.floorNumberReserveLeft(input.get(), reserveLeftLength.get());
		resultLong.set(value);
		return resultLong;
	}
	
	public IntWritable evaluate(IntWritable input, IntWritable reserveLeftLength) {
		if(input==null){
			return null;
		}
		IntWritable resultInt = new IntWritable();
		int value = DesenFloorUtils.floorNumberReserveLeft(input.get(), reserveLeftLength.get());
		resultInt.set(value);
		return resultInt;
	}

	
}
