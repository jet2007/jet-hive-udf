package com.jet.utils.useragent.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jet.utils.useragent.build.StevenkangMobileModelBuild;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.model.StevenkangMobileModel;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentAnalyzerYauaaStevenkangUtils {

	
	/**
	 * 解析userAgentString
	 * @param userAgentAnalyzer 已实例过的userAgentAnalyzer
	 * @param mobileModelParse  已实例过的mobileModelParse
	 * @param userAgentString   userAgent字符串
	 * @param inputFields       获取值的多个列
	 * @return
	 */
	public static Map<String,String> getUserAgentVaules(UserAgentAnalyzer userAgentAnalyzer
														,StevenkangMobileModelBuild mobileModelParse
														,String userAgentString
														,List<String> fieldNames) {
		UserAgent userAgent = userAgentAnalyzer.parse(userAgentString);
		Map<String,String> target=new HashMap<String,String>();
		//List<String> fieldNamesNative = userAgentAnalyzer.getAllPossibleFieldNamesSorted();
		for (String key : fieldNames) {
			target.put(key,userAgent.getValue(key));
        }//以上为原UserAgentParse解析出来的所有信息
		
		// 以下为自定义的修改输出内容,若有修改，到此处来哦
		// 解析厂商，品牌，型号
		String deviceClass = target.get(UserAgent.DEVICE_CLASS);
		if(deviceClass.equals("Phone") || deviceClass.equals("Tablet") ||deviceClass.equals("Mobile") ){
			StevenkangMobileModel mm = mobileModelParse.getMobileModel(target.get(UserAgent.DEVICE_NAME), target.get(UserAgent.DEVICE_BRAND));
			if(mm!=null){
				target.put(ConstantsUserAgent.YAUAA_DEVICE_VENDOR, mm.getManufacturerEn()); //将mm解析出来的厂商信息
				target.put(UserAgent.DEVICE_BRAND, mm.getBrandEn()); //覆盖写入到DEVICE_BRAND品牌中
				target.put(ConstantsUserAgent.YAUAA_DEVICE_MODEL_NAME, mm.getModelName());//型号
			}else{
				target.put(ConstantsUserAgent.YAUAA_DEVICE_VENDOR, target.get(UserAgent.DEVICE_BRAND));
				//UserAgent.DEVICE_BRAND不改变
				target.put(ConstantsUserAgent.YAUAA_DEVICE_MODEL_NAME, UserAgent.UNKNOWN_VALUE);
			}
		}
		else{
			target.put(ConstantsUserAgent.YAUAA_DEVICE_VENDOR, target.get(UserAgent.DEVICE_BRAND));
			//UserAgent.DEVICE_BRAND不改变
			target.put(ConstantsUserAgent.YAUAA_DEVICE_MODEL_NAME, UserAgent.UNKNOWN_VALUE);
		}

		 
		return target;
    }
	
}
