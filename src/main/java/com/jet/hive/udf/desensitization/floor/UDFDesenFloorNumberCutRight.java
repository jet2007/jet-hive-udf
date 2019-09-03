package com.jet.hive.udf.desensitization.floor;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import com.jet.utils.desensitization.DesenFloorUtils;


@Description(name = "DesenFloorNumberCutRight", value = "_FUNC_(input ,cutRightLength ) - 从右起截断后n位且置为0\n"
		+"参数1：输入的数值"
		+"参数2：从右起被截断且置为0的长度"
, extended = "Example:\n > select _FUNC_(123456789876543,2)  \n " 
+"                    _FUNC_(1234567,2, 4)  \n "	
+"                    结果： 123456789876500,1230000\n "
)
public class UDFDesenFloorNumberCutRight extends UDF {
	
	public LongWritable evaluate(LongWritable input, IntWritable cutRightLength) {
		if(input==null){
			return null;
		}
		LongWritable resultLong = new LongWritable();
		long value = DesenFloorUtils.floorNumberCutRight(input.get(), cutRightLength.get());
		resultLong.set(value);
		return resultLong;
	}
	
	public IntWritable evaluate(IntWritable input, IntWritable cutRightLength) {
		if(input==null){
			return null;
		}
		IntWritable resultInt = new IntWritable();
		int value = DesenFloorUtils.floorNumberCutRight(input.get(), cutRightLength.get());
		resultInt.set(value);
		return resultInt;
	}

	
}
