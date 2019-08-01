package com.jet.utils.useragent;

import java.util.Date;
import java.util.HashMap;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

import com.jet.hive.udf.useragent.UDFUserAgentParserYauaaStevenkang;


public class UDFUserAgentAnalyzerUtilsTest {
	
	
	public void uat(String userAgentString,String fields) throws HiveException {        
        UDFUserAgentParserYauaaStevenkang udf=new UDFUserAgentParserYauaaStevenkang();
        ObjectInspector ua_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector fields_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector[] arguments = {ua_type, fields_type};
        udf.initialize(arguments);
        GenericUDF.DeferredObject ua_val = new GenericUDF.DeferredJavaObject(userAgentString);
        GenericUDF.DeferredObject fields_val = new GenericUDF.DeferredJavaObject(fields);
        GenericUDF.DeferredObject[] args = {ua_val, fields_val};
        
        HashMap<Text, Text> output = (HashMap<Text, Text>) udf.evaluate(args);
        for (Text key : output.keySet()) {
//			System.out.print(key);
//			System.out.print("=");
//			System.out.print(output.get(key));
//			System.out.println("");
		}
        System.out.println("size="+output.size());
		Assert.assertEquals("a","a");
	}
	
	
	public void uat(String userAgentString) throws HiveException {        
        UDFUserAgentParserYauaaStevenkang udf=new UDFUserAgentParserYauaaStevenkang();
        ObjectInspector ua_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector[] arguments = {ua_type};
        udf.initialize(arguments);
        GenericUDF.DeferredObject ua_val = new GenericUDF.DeferredJavaObject(userAgentString);
        GenericUDF.DeferredObject[] args = {ua_val};
        
        HashMap<Text, Text> output = (HashMap<Text, Text>) udf.evaluate(args);
        for (Text key : output.keySet()) {
//			System.out.print(key);
//			System.out.print("=");
//			System.out.print(output.get(key));
//			System.out.println("");
		}
        System.out.println("size="+output.size());
		Assert.assertEquals("a","a");
	}
	
	@Test
	public void call() throws HiveException{
		
		Date start =new Date();
		Date end =null;
		double delta;
		
		String fields="all";
		String uas="Mozilla/5.0 (Linux; Android 6.0.1; KIW-AL10 Build/HONORKIW-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044304 Mobile Safari/537.36 MicroMessenger/6.6.7.1321(0x26060739) NetType/4G Language/zh_CN";
		
		uat(uas,fields);
		end =new Date();
		delta = (end.getTime()-start.getTime())/1000.0;
		System.out.println(delta);
		
		
		
		System.out.println("#############################-------------------");
		start =new Date();
		uat(uas);
		end =new Date();
		delta = (end.getTime()-start.getTime())/1000.0;
		System.out.println(delta);
		
		
		
		for (int i = 0; i < 100; i++) {
			uas=uas.replace("6.2", "6.2."+i);
			System.out.println("########################################################"+i);
			start=new Date();
			uat(uas);
			end =new Date();
			delta = (end.getTime()-start.getTime())/1000.0;
			System.out.println(delta);
			
		}
		
		 
	}

}
