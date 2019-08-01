package com.jet.utils.useragent.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jet.utils.string.StringUtils;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.model.StevenkangMobileModel;

/**
 * 解析资源文件的mobile-model.txt内容
 * @author JET
 *
 */
public class StevenkangMobileModelBuild {
	private static Logger logger = LoggerFactory.getLogger(StevenkangMobileModelBuild.class);
	public Map<String,StevenkangMobileModel> mobileModels=new HashMap<String,StevenkangMobileModel>();
	
	/**
	 * 构造函数：加载手机型号资源文件的内容
	 * @throws IOException
	 */
	public StevenkangMobileModelBuild() {
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
        try {
            inputStream = StevenkangMobileModelBuild.class.getResourceAsStream("/"+ConstantsUserAgent.STEVENKANG_RESOURCE_FILE);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtils.isBlank(line) || line.startsWith("#") ) {
                    continue;
                }
                StevenkangMobileModel mm=new StevenkangMobileModel(line);
                String key = mm.getModel(); //当前设置：型号=主键，更严格为厂商+型号=主键
                if(!mobileModels.containsKey(key)){
                	mobileModels.put(key, mm);
                };
                
            }
        } catch (IOException e) {
            logger.error("loadFile {} error. error is {}.", ConstantsUserAgent.STEVENKANG_RESOURCE_FILE, e);
        } finally {
        	try {
				bufferedReader.close();
				inputStream.close();
			} catch (IOException e) {
			}
        	
        }
	}
	
	/**
	 * 读取手机型号文件的相关信息
	 * @param uaDeviceName  手机型号-useragent; 
	 * @param uaDeviceBrand 手机品牌-useragent
	 * @return 手机型号文件的相关信息
	 */
    public StevenkangMobileModel getMobileModel(String uaDeviceName,String uaDeviceBrand){
    	if(mobileModels.containsKey(uaDeviceName)){
    		return mobileModels.get(uaDeviceName);
    	}else {
    		String key=uaDeviceName.replace(uaDeviceBrand, "").trim();
    		if(mobileModels.containsKey(key)){
    			return mobileModels.get(key);
    			}
    		
    	}
    	return null;
    }
   
	/**
	 * 手机型号文件的相关信息
	 * @param deviceName  手机型号-useragent
	 * @return 手机型号文件的相关信息
	 */
    public StevenkangMobileModel getMobileModel(String uaDeviceName){
    	return getMobileModel(uaDeviceName,"");
    }
    
}
