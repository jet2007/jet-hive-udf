package com.jet.utils.useragent.juan;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import com.github.codesorcery.juan.ParsedUserAgent;
import com.github.codesorcery.juan.UserAgentParser;


public class UserAgentParserTest {
	
	@Test
    public void withDeviceList() throws IOException {
		
		
		URL url = UserAgentParserTest.class.getResource("/supported_devices.csv");
		Charset ch = Charset.forName("UTF-16");
		
        final UserAgentParser userAgentParser = UserAgentParser
                .withPlayStoreDeviceList(
                		url,ch
                        
                );
                //.withTokenizedUALogger(LOG::info);

        final String input = "Mozilla/5.0 (Linux; Android 8.0; FIG-AL10 Build/HUAWEIFIG-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044304 Mobile Safari/537.36 MicroMessenger/6.7.3.1360(0x26070333) NetType/4G Language/zh_CN Process/tools";
        final ParsedUserAgent result = userAgentParser.parse(input);
        System.out.println(result);
        
        Assert.assertEquals("a", "a");
		
		
	}

}
