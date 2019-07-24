package com.jet.udf.dncrypt;

import org.junit.Test;

import com.jet.hive.udf.decrypt.UDFDes;

import org.apache.hadoop.io.Text;
import org.junit.Assert;

public class UDFDesTest {
	
	protected void runTest(String value, Text exp) {
		UDFDes udf=new UDFDes();
		Text t;
		if(value==null) t=null;
		else{
			t=new Text(value);
		}
        Text res = udf.evaluate(t);
        
        Assert.assertEquals(exp, res );
        
  
    }
	
	

	@Test
    public void testEncrypt() throws Exception {
		runTest("UpndVp1Yw4WlEO+ams97Bg==",new Text("13612345678"));
		runTest("Yl0udxgsQm4=",new Text("123"));
		runTest(null,null);
		
	}
}
