package com.jet.utils.useragent.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jet.utils.useragent.build.GooglePlayStoreSupportedDeviceBuild;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.model.GooglePlayStoreSupportedDevice;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentAnalyzerYauaaSupportedDevicesUtils {

	
	/**
	 * 解析userAgentString，得到Map<String,String>的解析后结果
	 * @param userAgentAnalyzer 已实例过的userAgentAnalyzer
	 * @param supportedDevicesParser  已实例过的supportedDevicesParser
	 * @param userAgentString   userAgent字符串
	 * @param inputFields       获取值的多个列
	 * @return Map<String,String> 预结果
	 */
	public static Map<String,String> getVaulesByParser(UserAgentAnalyzer userAgentAnalyzer
														,GooglePlayStoreSupportedDeviceBuild supportedDevicesParser
														,String userAgentString
														,List<String> fieldNames) {
		UserAgent userAgent = userAgentAnalyzer.parse(userAgentString);
		Map<String,String> target=new HashMap<String,String>();
		for (String key : fieldNames) {
			target.put(key,userAgent.getValue(key));
        }//以上为原UserAgentParse解析出来的所有信息
		
		// 以下为自定义的修改输出内容,若有修改，到此处来哦
		// 解析厂商，品牌，型号
		String deviceClass = target.get(UserAgent.DEVICE_CLASS);
		if(deviceClass.equals("Phone") || deviceClass.equals("Tablet") ||deviceClass.equals("Mobile") ){//只针对，下面的场景进行处理
			String yauaaUserAgentDeviceName = target.get(UserAgent.DEVICE_NAME);
			String yauaaUserAgentDeviceBrand = target.get(UserAgent.DEVICE_BRAND);
			
			GooglePlayStoreSupportedDevice supportedDevice = supportedDevicesParser.getSupportDeviceBy(yauaaUserAgentDeviceName, yauaaUserAgentDeviceBrand);
			
			if(supportedDevice!=null){
				target.put(ConstantsUserAgent.JUAN_DEVICE_VENDOR, supportedDevice.getRetailBranding()); //厂商
				target.put(ConstantsUserAgent.JUAN_DEVICE_MODEL_NAME, supportedDevice.getMarketingName());//型号
			}else{
				target.put(ConstantsUserAgent.JUAN_DEVICE_VENDOR, yauaaUserAgentDeviceBrand);
				target.put(ConstantsUserAgent.JUAN_DEVICE_MODEL_NAME, UserAgent.UNKNOWN_VALUE);
			}
		}
		else{
			target.put(ConstantsUserAgent.JUAN_DEVICE_VENDOR, target.get(UserAgent.DEVICE_BRAND));
			target.put(ConstantsUserAgent.JUAN_DEVICE_MODEL_NAME, UserAgent.UNKNOWN_VALUE);
		}

		 
		return target;
    }
	
}
