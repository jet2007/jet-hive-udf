package com.jet.hive.udf.desensitization.hiding;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


@Description(name = "DesenHiding", value = "_FUNC_(input [,desen_string] ) - 脱敏成一个固定值\n"
											+"参数2：desen_string，可选，输出的指定脱敏字符或数值"
, extended = "Example:\n > select _FUNC_(123)  \n " 
			+"                    _FUNC_(123, 0)  \n "	
			+"                    _FUNC_(123.45, 0)  \n "	
			+"                    _FUNC_(1233333345435, 0)  \n "	
			+"                    _FUNC_('abc123')  \n "
			+"                    _FUNC_('abc123', '')  \n "
			)
public class UDFDesenHiding extends GenericUDF {
	PrimitiveObjectInspector inputOI;//入参1的数据类型
	PrimitiveObjectInspector outputOI;//返回值的数据类型
	Object inputDefault=null;//默认值
	private boolean isNonInit = true; //未被init过
	
	private static final int CONST_INT=0;
	private static final long CONST_LONG=0L;
	private static final float CONST_FLOAT=0F;
	private static final double CONST_DOUBLE=0D;
	private static final String CONST_STRING="";

	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {		
		if (args.length > 2 || args.length<1 ) {
			String msg = "此函数需要1、2个参数";
            throw new UDFArgumentException(msg);
        }
		inputOI = (PrimitiveObjectInspector) args[0];
		
		if(args.length==2){
			if(inputOI.getPrimitiveCategory() == PrimitiveCategory.INT){
				outputOI = PrimitiveObjectInspectorFactory.writableIntObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.LONG){
				outputOI = PrimitiveObjectInspectorFactory.writableLongObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.FLOAT){
				outputOI = PrimitiveObjectInspectorFactory.writableFloatObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.DOUBLE){
				outputOI = PrimitiveObjectInspectorFactory.javaDoubleObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.STRING){
				outputOI = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
			}else{
				String msg = "此函数支持的数据类型int/bigint/float/double/decimal/string";
	            throw new UDFArgumentException(msg);
			}
			
		}else{
			if(inputOI.getPrimitiveCategory() == PrimitiveCategory.INT){
				inputDefault=CONST_INT;
				outputOI = PrimitiveObjectInspectorFactory.writableIntObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.LONG){
				inputDefault=CONST_LONG;
				outputOI = PrimitiveObjectInspectorFactory.writableLongObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.FLOAT){
				inputDefault=CONST_FLOAT;
				outputOI = PrimitiveObjectInspectorFactory.writableFloatObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.DOUBLE){
				inputDefault=CONST_DOUBLE;
				outputOI = PrimitiveObjectInspectorFactory.javaDoubleObjectInspector;
			}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.STRING){
				inputDefault=CONST_STRING;
				outputOI = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
			}else{
				String msg = "此函数支持的数据类型int/bigint/float/double/decimal/string";
	            throw new UDFArgumentException(msg);
			}
			isNonInit=false;
		}
		return outputOI;
	}

	public Object evaluate(DeferredObject[] args) throws HiveException {
		if (args.length > 2 || args.length<1)
			return null;
		Object objInput1 = args[0].get();
		if (objInput1 == null)
			return null;
		//初始化操作，只执行1次
		if(isNonInit){
			if(args.length==2){
				Object input2 = args[1].get();
				inputDefault = inputOI.getPrimitiveJavaObject(input2);
			}
			isNonInit=false;
		}
		
		if(inputOI.getPrimitiveCategory() == PrimitiveCategory.INT){
			int value = (Integer) inputDefault;
			return new IntWritable(value);
		}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.LONG){
			long value = (Long) inputDefault;
			return new LongWritable(value);
		}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.FLOAT){
			float value = (Float) inputDefault;
			return new FloatWritable(value);
		}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.DOUBLE){
			double value = (Double) inputDefault;
			return new DoubleWritable(value);
		}else if(inputOI.getPrimitiveCategory() == PrimitiveCategory.STRING){
			String value = (String) inputDefault;
			return new Text(value);
		}else{
			String msg = "此函数支持的数据类型int/bigint/float/double/decimal/string";
            throw new UDFArgumentException(msg);
		}
		

	}

	@Override
	public String getDisplayString(String[] args) {
		return "Here, write a nice description";
	}
}