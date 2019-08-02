package com.jet.utils.useragent.build;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.jet.utils.string.StringUtils;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.model.GooglePlayStoreSupportedDevice;


public class GooglePlayStoreSupportedDeviceBuild {
	
	private static Logger logger = LoggerFactory.getLogger(GooglePlayStoreSupportedDeviceBuild.class);
	private List<GooglePlayStoreSupportedDevice> supportedDeviceList=null;
	// 型号精确匹配，型号一般匹配，厂商匹配的索引
	private	Map<String,Set<Integer>> vendorIndex= null;  // 形如 huawei=[1001,1002,1003]
    private Map<String,Set<Integer>> deviceModelIndex = null;
    private Map<String,Set<Integer>> deviceModelLowerNoVendorIndex = null;
    
    /**
     * 创建型号精确匹配，型号一般匹配，厂商匹配的索引
     */
    private void buildIndex(){
    	this.vendorIndex=new HashMap<String,Set<Integer>>();
    	this.deviceModelIndex=new HashMap<String,Set<Integer>>();
    	this.deviceModelLowerNoVendorIndex=new HashMap<String,Set<Integer>>();
    	for (int i = 0; i < this.supportedDeviceList.size(); i++) {
    		GooglePlayStoreSupportedDevice supportedDevice = supportedDeviceList.get(i);
    		
    		String vendorKey = supportedDevice.getRetailBranding().toLowerCase();
    		Set<Integer> vendorVauleSet = this.vendorIndex.get(vendorKey);
    		if(vendorVauleSet==null){
    			vendorVauleSet=new HashSet<Integer>();
    		}
    		vendorVauleSet.add(Integer.valueOf(i));
    		this.vendorIndex.put(vendorKey, vendorVauleSet);
    		
    		String deviceModelKey = supportedDevice.getModel();
    		Set<Integer> deviceModelVauleSet = this.deviceModelIndex.get(deviceModelKey);
    		if(deviceModelVauleSet==null){
    			deviceModelVauleSet=new HashSet<Integer>();
    		}
    		deviceModelVauleSet.add(Integer.valueOf(i));
    		this.deviceModelIndex.put(deviceModelKey, deviceModelVauleSet);
    		
    		
    		String deviceModelLowerNoVendorKey = supportedDevice.getModel().toLowerCase().replace(vendorKey.toLowerCase(), "").trim();
    		Set<Integer> deviceModelLowerNoVendorVauleSet = this.deviceModelLowerNoVendorIndex.get(deviceModelLowerNoVendorKey);
    		if(deviceModelLowerNoVendorVauleSet==null){
    			deviceModelLowerNoVendorVauleSet=new HashSet<Integer>();
    		}
    		deviceModelLowerNoVendorVauleSet.add(Integer.valueOf(i));
    		this.deviceModelLowerNoVendorIndex.put(deviceModelLowerNoVendorKey, deviceModelLowerNoVendorVauleSet);
    		
		}
    	
    }
    
	
    /**
     * 读取数据源的资源文件
     */
    private void readResouceFile(String filePath)  {
		try {
	    	ArrayList<String []> List = new ArrayList<String[]>();
	    	CsvReader reader = new CsvReader(filePath,',', Charset.forName("UTF-16"));
	    	reader.readHeaders();// 跳过表头 如果需要表头的话，这句可以忽略  
	    	supportedDeviceList=new ArrayList<GooglePlayStoreSupportedDevice>();
	    	while(reader.readRecord()) {
		           List.add(reader.getValues());
		           String[] record = reader.getValues();
		           if(record!=null && record.length==4){
		        	   GooglePlayStoreSupportedDevice sd = new GooglePlayStoreSupportedDevice(record);
		        	   if(StringUtils.isNotBlank(sd.getRetailBranding())){ //没有厂商的，不考虑
		        		   this.supportedDeviceList.add(sd);
		        	   }
		           }
		       }
	       reader.close();
		} catch (IOException e) {
			logger.error("loadFile error. error is "+ filePath);
		}

    }
    
    private void readResouceFile()  {
    	
    	try {
    		
    		URL url2=getClass().getClassLoader().getResource("/");
        	System.out.println("base fold2:"+url2.toURI().getPath());
        	
    		URL url=getClass().getClassLoader().getResource("/"+ConstantsUserAgent.SUPPORT_DEVICE_RESOURCE_FILE);
    		System.out.println("base fold1:"+url.toURI().getPath());
        	
        	String filePath;
    		filePath = url.toURI().getPath();
    		System.out.println("######filePath="+filePath);
			readResouceFile(filePath);
		} catch (URISyntaxException e) {
			logger.error("loadFile error. error is "+ ConstantsUserAgent.SUPPORT_DEVICE_RESOURCE_FILE);
		}
    }
	
    
	public GooglePlayStoreSupportedDeviceBuild() {
		readResouceFile();
        buildIndex();
	}
	
	public GooglePlayStoreSupportedDeviceBuild(String filePath) {
		readResouceFile(filePath);
        buildIndex();
	}
	
	/**
	 * 查找规则：
	 * 		定义：型号精确匹配--入参型号在数据库中找到完全一样
	 *           型号一般匹配--入参型号(小写化+去除厂商)在数据库中找到类似的型号(小写化+去除厂商)
	 *           厂商匹配--入参(小写化)在数据库中找到相同的厂商(小写化)
	 * 规则：满足以下规则之一，即返回相应的GooglePlayStoreSupportedDevice；若不满足返回null
	 * 		1.型号精确匹配且厂商匹配
	 *      2.型号精确匹配且厂商不匹配，型号字符长度>1
	 *      3.型号一般匹配且厂商匹配
	 *      4.型号一般匹配且厂商不匹配，型号字符长度>2
	 *      
	 * @param yauaaUserAgentDeviceName   型号
	 * @param yauaaUserAgentDeviceBrand  厂商
	 * @return
	 */
	public GooglePlayStoreSupportedDevice getSupportDeviceBy(String yauaaUserAgentDeviceName,String yauaaUserAgentDeviceBrand){
		String yauaaUserAgentDeviceBrandLower = yauaaUserAgentDeviceBrand.toLowerCase();//厂商小写
		String yauaaUserAgentDeviceNameLowerNoVendor =  yauaaUserAgentDeviceName.toLowerCase().replace(yauaaUserAgentDeviceBrandLower, "").trim();//型号小写(除去厂商)
		Set<Integer> deviceModelIndexSet = this.deviceModelIndex.get(yauaaUserAgentDeviceName);
		Set<Integer> deviceModelLowerNoVendorIndexSet = this.deviceModelLowerNoVendorIndex.get(yauaaUserAgentDeviceNameLowerNoVendor);
		Set<Integer> vendorIndexSet = this.vendorIndex.get(yauaaUserAgentDeviceBrandLower);
		
		Integer indexNum=-1;
		if(deviceModelIndexSet!=null){ // 型号精确匹配
			if(vendorIndexSet!=null){  // 厂商匹配
				deviceModelIndexSet.retainAll(vendorIndexSet);
				indexNum=deviceModelIndexSet.iterator().next();
			}else{// 厂商不匹配
				if(yauaaUserAgentDeviceName.length()>=2){//厂商不匹配，型号精确匹配要求>=2位
					indexNum=deviceModelIndexSet.iterator().next();
				}else{ 
					indexNum=-1;
				}
			}
		}
		else{//型号不精确匹配
			if(deviceModelLowerNoVendorIndexSet!=null){  //型号一般匹配
				if(vendorIndexSet!=null){  // 厂商匹配
					deviceModelLowerNoVendorIndexSet.retainAll(vendorIndexSet);
					
					indexNum=deviceModelLowerNoVendorIndexSet.iterator().next();
				}else{// 厂商不匹配
					if(yauaaUserAgentDeviceName.length()>=3){//厂商不匹配，型号一般匹配要求>=3位
						indexNum=deviceModelLowerNoVendorIndexSet.iterator().next();
					}else{  
						indexNum=-1;
					}
				}
			}
			else{
				indexNum=-1;
			}
		}
		if(indexNum==Integer.valueOf(-1)){
			return null;
		}
		else{
			return this.supportedDeviceList.get(indexNum);
		}
			
	}
	
}
