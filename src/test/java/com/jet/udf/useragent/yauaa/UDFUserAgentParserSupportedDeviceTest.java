package com.jet.udf.useragent.yauaa;

import java.util.Date;
import java.util.HashMap;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

import com.jet.hive.udf.useragent.UDFUserAgentParserYauaaSupportedDevice;


public class UDFUserAgentParserSupportedDeviceTest {

	public void uat(String userAgentString) throws HiveException {        
		UDFUserAgentParserYauaaSupportedDevice udf=new UDFUserAgentParserYauaaSupportedDevice();
        ObjectInspector ua_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector[] arguments={ua_type};
		udf.initialize(arguments);
        GenericUDF.DeferredObject ua_val = new GenericUDF.DeferredJavaObject(userAgentString);
        GenericUDF.DeferredObject[] args = {ua_val};
        
        HashMap<Text, Text> output = (HashMap<Text, Text>) udf.evaluate(args);
        System.out.println(output);
        
//        for (Text key :  output.keySet()) {
//			System.out.print(key);
//			System.out.print("=");
//			System.out.print(output.get(key));
//			System.out.println("");
//		}
        System.out.println("size="+output.size());
		Assert.assertEquals("a","a");
	}
		
	public void uat(String userAgentString,String fields) throws HiveException {        
		UDFUserAgentParserYauaaSupportedDevice udf=new UDFUserAgentParserYauaaSupportedDevice();
        ObjectInspector ua_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector fields_type = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector[] arguments={ua_type,fields_type};
		udf.initialize(arguments);
        GenericUDF.DeferredObject ua_val = new GenericUDF.DeferredJavaObject(userAgentString);
        GenericUDF.DeferredObject fields_val = new GenericUDF.DeferredJavaObject(fields);
        GenericUDF.DeferredObject[] args = {ua_val,fields_val};
        
        HashMap<Text, Text> output = (HashMap<Text, Text>) udf.evaluate(args);
        System.out.println(output);
        
//        for (Text key :  output.keySet()) {
//			System.out.print(key);
//			System.out.print("=");
//			System.out.print(output.get(key));
//			System.out.println("");
//		}
        System.out.println("size="+output.size());
		Assert.assertEquals("a","a");
	}
	
	@Test
	public void call() throws HiveException{
		Date start =new Date();
		Date end =null;
		double delta;
		String uas="Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4X Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044203 Mobile Safari/537.36 MicroMessenger/6.7.2.1340(0x26070238) NetType/WIFI Language/zh_CN";
		uat(uas,"DeviceClass,DeviceVendor,DeviceBrand,DeviceModel,DeviceName");
		end =new Date();
		delta = (end.getTime()-start.getTime())/1000.0;
		System.out.println(delta);
				
		for (int i = 0; i < 10; i++) {
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
