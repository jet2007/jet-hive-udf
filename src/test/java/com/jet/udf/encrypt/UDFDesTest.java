package com.jet.udf.encrypt;

import org.junit.Test;

import com.jet.hive.udf.encrypt.UDFDes;

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
		runTest("13612345678",new Text("UpndVp1Yw4WlEO+ams97Bg=="));
		runTest("123",new Text("Yl0udxgsQm4="));
		runTest(null,null);
		
	}
}
