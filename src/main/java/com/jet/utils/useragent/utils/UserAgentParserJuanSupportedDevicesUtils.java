package com.jet.utils.useragent.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;

import com.github.codesorcery.juan.ParsedUserAgent;
import com.github.codesorcery.juan.agent.Agent;
import com.github.codesorcery.juan.device.DeviceInfo;
import com.github.codesorcery.juan.os.OperatingSystem;
import com.jet.utils.string.HexUtils;
import com.jet.utils.useragent.constant.ConstantsUserAgent;

public class UserAgentParserJuanSupportedDevicesUtils {
	
	/**
	 * hive udf的map类型值
	 * @param parsedUserAgent
	 * @return
	 */
	public static Map<Text, Text> fromUserAgentParserToTextMap(ParsedUserAgent parsedUserAgent){
		if (parsedUserAgent==null){
			return null;
		}
		
		Agent agent = parsedUserAgent.agent();
		OperatingSystem os = parsedUserAgent.os();
		DeviceInfo device = parsedUserAgent.device();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantsUserAgent.JUAN_AGENT_NAME,agent.getName());
		map.put(ConstantsUserAgent.JUAN_AGENT_VENDOR, agent.getVendor());
		map.put(ConstantsUserAgent.JUAN_AGENT_VERSION, agent.getVersion());
		map.put(ConstantsUserAgent.JUAN_AGENT_TYPE, agent.getType());
		
		map.put(ConstantsUserAgent.JUAN_OS_NAME, os.getName());
		map.put(ConstantsUserAgent.JUAN_OS_VENDOR, os.getVendor());
		map.put(ConstantsUserAgent.JUAN_OS_VERSION, os.getVersion());
		map.put(ConstantsUserAgent.JUAN_OS_TYPE, os.getType());
		
		map.put(ConstantsUserAgent.JUAN_DEVICE_NAME,HexUtils.mixHex2Str(device.getName()) );
		map.put(ConstantsUserAgent.JUAN_DEVICE_VENDOR,HexUtils.mixHex2Str(device.getVendor()) );
		
		Map<Text, Text> re = new HashMap<Text, Text>();
		for (String key : map.keySet()) {
			String value = map.get(key);
			Text t = ( value==null?null:new Text(value) );
			re.put(new Text(key), t);
		}
		
		return re;
	}

}
