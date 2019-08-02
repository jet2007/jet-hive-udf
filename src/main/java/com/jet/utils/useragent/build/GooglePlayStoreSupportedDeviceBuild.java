package com.jet.utils.useragent.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;

import com.csvreader.CsvReader;
import com.jet.utils.string.StringUtils;
import com.jet.utils.udf.HiveUdfUtils;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.model.GooglePlayStoreSupportedDevice;


public class GooglePlayStoreSupportedDeviceBuild {
	
	private List<GooglePlayStoreSupportedDevice> supportedDeviceList=null;
	// 型号精确匹配，型号一般匹配，厂商匹配的索引
	private	static Map<String,Set<Integer>> vendorIndex= null;  // 形如 huawei=[1001,1002,1003]
    private static Map<String,Set<Integer>> deviceModelIndex = null;
    private static Map<String,Set<Integer>> deviceModelLowerNoVendorIndex = null;
    
    /**
     * 创建型号精确匹配，型号一般匹配，厂商匹配的索引
     */
    private void buildIndex(){
    	vendorIndex=new HashMap<String,Set<Integer>>();
    	deviceModelIndex=new HashMap<String,Set<Integer>>();
    	deviceModelLowerNoVendorIndex=new HashMap<String,Set<Integer>>();
    	for (int i = 0; i < this.supportedDeviceList.size(); i++) {
    		GooglePlayStoreSupportedDevice supportedDevice = supportedDeviceList.get(i);
    		
    		String vendorKey = supportedDevice.getRetailBranding().toLowerCase();
    		Set<Integer> vendorVauleSet = vendorIndex.get(vendorKey);
    		if(vendorVauleSet==null){
    			vendorVauleSet=new HashSet<Integer>();
    		}
    		vendorVauleSet.add(Integer.valueOf(i));
    		vendorIndex.put(vendorKey, vendorVauleSet);
    		
    		String deviceModelKey = supportedDevice.getModel();
    		Set<Integer> deviceModelVauleSet = deviceModelIndex.get(deviceModelKey);
    		if(deviceModelVauleSet==null){
    			deviceModelVauleSet=new HashSet<Integer>();
    		}
    		deviceModelVauleSet.add(Integer.valueOf(i));
    		deviceModelIndex.put(deviceModelKey, deviceModelVauleSet);
    		
    		
    		String deviceModelLowerNoVendorKey = supportedDevice.getModel().toLowerCase().replace(vendorKey.toLowerCase(), "").trim();
    		Set<Integer> deviceModelLowerNoVendorVauleSet = deviceModelLowerNoVendorIndex.get(deviceModelLowerNoVendorKey);
    		if(deviceModelLowerNoVendorVauleSet==null){
    			deviceModelLowerNoVendorVauleSet=new HashSet<Integer>();
    		}
    		deviceModelLowerNoVendorVauleSet.add(Integer.valueOf(i));
    		deviceModelLowerNoVendorIndex.put(deviceModelLowerNoVendorKey, deviceModelLowerNoVendorVauleSet);
    		
		}
    	
    }
    
    
    private void readResouceByInputStream(InputStream inputStream) throws UDFArgumentException  {
		try {
	    	ArrayList<String []> List = new ArrayList<String[]>();
	    	CsvReader reader = new CsvReader(inputStream,',', Charset.forName("UTF-16"));
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
	       inputStream.close();
		} catch (IOException e) {
			throw new UDFArgumentException(String.format("Read file %s info Error."));
		}

    }
	
    /**
     * 读取数据源的资源文件hdfs_filename,hdfs的文件名，不包含路径
     * @throws UDFArgumentException 
     */
    private void readResouceFile(String hdfs_filename) throws UDFArgumentException  {
		try {
			File f = HiveUdfUtils.getHiveResourceFile(hdfs_filename);
			if(!f.exists()) {
				throw new UDFArgumentException(String.format("The file %s can not found.", hdfs_filename));
			}
			InputStream inputStream = new FileInputStream(f);
			readResouceByInputStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new UDFArgumentException(String.format("Read file %s info Error.", hdfs_filename));
		}

    }
    
    
    /**
     * 读取数据源的资源文件(jar内部)
     * @throws UDFArgumentException 
     */    
    private void readResouceFile() throws UDFArgumentException  {
    	try {
    		InputStream inputStream = GooglePlayStoreSupportedDeviceBuild.class.getResourceAsStream("/"+ConstantsUserAgent.SUPPORT_DEVICE_RESOURCE_FILE);
    		readResouceByInputStream(inputStream);
    		inputStream.close();
		} catch ( IOException e) {
			throw new UDFArgumentException(String.format("Read file %s info error.", ConstantsUserAgent.SUPPORT_DEVICE_RESOURCE_FILE));
		}
    }
	
    
	public GooglePlayStoreSupportedDeviceBuild() throws UDFArgumentException {
		readResouceFile();
        buildIndex();
	}
	
	public GooglePlayStoreSupportedDeviceBuild(String filePath) throws UDFArgumentException {
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
		Set<Integer> deviceModelIndexSet = deviceModelIndex.get(yauaaUserAgentDeviceName);
		Set<Integer> deviceModelLowerNoVendorIndexSet = deviceModelLowerNoVendorIndex.get(yauaaUserAgentDeviceNameLowerNoVendor);
		Set<Integer> vendorIndexSet = vendorIndex.get(yauaaUserAgentDeviceBrandLower);
		
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
