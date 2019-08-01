package com.jet.utils.useragent.juan;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

import com.jet.hive.udf.useragent.UDFUserAgentParserJuanSupportDevice;


public class UDFUserAgentParserTest {
	
		
	public void uat(String userAgentString) throws HiveException {        
		UDFUserAgentParserJuanSupportDevice udf=new UDFUserAgentParserJuanSupportDevice();
        ObjectInspector ua_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector[] arguments = {ua_type};
        udf.initialize(arguments);
        GenericUDF.DeferredObject ua_val = new GenericUDF.DeferredJavaObject(userAgentString);
        GenericUDF.DeferredObject[] args = {ua_val};
        
        HashMap<Text, Text> output = (HashMap<Text, Text>) udf.evaluate(args);
        for (Text key : output.keySet()) {
			System.out.print(key);
			System.out.print("=");
			System.out.print(output.get(key));
			System.out.println("");
		}
        System.out.println("size="+output.size());
		Assert.assertEquals("a","a");
	}
	
	@Test
	public void call() throws HiveException{
		
		Date start =new Date();
		Date end =null;
		double delta;
		
		String uas="Mozilla/5.0 (Linux; U; Android 8.0.0; zh-CN; VTR-AL00 Build/HUAWEIVTR-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.1.4.994 Mobile Safari/537.36";
		
		uat(uas);
		end =new Date();
		delta = (end.getTime()-start.getTime())/1000.0;
		System.out.println(delta);
		
 
		
		
		
//		for (int i = 0; i < 100; i++) {
//			uas=uas.replace("6.2", "6.2."+i);
//			System.out.println("########################################################"+i);
//			start=new Date();
//			uat(uas);
//			end =new Date();
//			delta = (end.getTime()-start.getTime())/1000.0;
//			System.out.println(delta);
//			
//		}
		
		 
	}

}
