package com.jet.utils.useragent;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentAnalyzerTest {

	public String uaaTest(String userAgentString) throws Exception {
		UserAgentAnalyzer userAgentAnalyzer = null;
	    List<String> fieldNames = null;

	    if (userAgentAnalyzer == null) {
            userAgentAnalyzer = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .delayInitialization()
                .build();
            fieldNames = userAgentAnalyzer.getAllPossibleFieldNamesSorted();
        }
	    
	    for (String string : fieldNames) {
			System.out.println(string);
		}

	    if (userAgentString == null) {
            return null;
        }
	    System.out.println("-------------------");
        UserAgent userAgent = userAgentAnalyzer.parse(userAgentString);
        List<Object> result = new ArrayList<>(fieldNames.size());
        for (String fieldName : fieldNames) {
            String value = userAgent.getValue(fieldName);
            if (value == null) {
                result.add(null);
            } else {
                result.add(value);
                System.out.println(fieldName+":"+value);
            }
        }
        
	    Assert.assertEquals("a", "ab");
	    
	    return null;
		
	}
	
	@Test
	public void callTest() throws Exception{
		String ua1="Mozilla/5.0 (Linux; Android 8.0.0; SM-G9650 Build/R16NW; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.0.0)";
		uaaTest(ua1);
	}
	
	
}
