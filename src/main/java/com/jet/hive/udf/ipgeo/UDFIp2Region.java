package com.jet.hive.udf.ipgeo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.io.Text;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import com.jet.utils.file.FilesUtils;
import com.jet.utils.ip.IpUtils;

import org.apache.hadoop.hive.ql.session.SessionState;

@Description(name = "ip2geo", value = "_FUNC_(array) - Returns map type of the ip address.\n"
		+ "Based on https://github.com/lionsoul2014/ip2region.\nThe ip address database has stored in local resource. \n"
		+ "  > Para1: Ipadress\n"
		+ "Example:\n"  
        + "  > CREATE TEMPORARY FUNCTION ip2geo AS 'com.jet.hive.udf.ipgeo.UDFIp2Region' \n"
        + "  > SELECT ip2geo('221.226.1.30' ),ip2geo('221.226.1.30' )['provinceName'] \n"
        + "") 
public class UDFIp2Region extends GenericUDF{
	
	PrimitiveObjectInspector inputOI;
	private static DbSearcher searcher=null;
	private static boolean isNonInit = true; //未被init过
	private static List<String> fieldNames = null;
	
	 
	/**
	 * 构造searcher
	 * @throws UDFArgumentException
	 */
    private static synchronized void constructUDFIp2Region() throws UDFArgumentException {
    	File inputFile = null;
		try {
			InputStream inputStream = UDFIp2Region.class.getResourceAsStream("/"+ConstantsGeoIp.FILE_IP2REGION);
			String inputFilePath = SessionState.get().getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR)+"/"+UUID.randomUUID().toString().replaceAll("-", "")+".db";
	    	inputFile = FilesUtils.inputStream2File(inputStream,inputFilePath);
	    	inputStream.close();
	    	if(searcher==null){
	    		DbConfig config = new DbConfig();
				searcher = new DbSearcher(config, inputFile.getAbsolutePath());
			} 
	    	isNonInit=false;
		}catch (DbMakerConfigException | IOException e) {
			throw new UDFArgumentException("Error: read file:"+ConstantsGeoIp.FILE_IP2REGION);
		}finally {
			if(inputFile.exists()){
	    		inputFile.delete();
	    	}
		}
    }
	
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
		if(isNonInit){
			constructUDFIp2Region();
			if(fieldNames==null){
	        	fieldNames=new ArrayList<String>();
	        	fieldNames.add(ConstantsGeoIp.KEY_COUNTRY_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_REGION_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_PROVINCE_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_CITY_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_ISP_NAME);
	        }
			isNonInit=false;
		}
		
		// 存储在全局变量的ObjectInspectors元素的输入
		inputOI = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
		// 返回变量输出类型
		return ObjectInspectorFactory.getStandardMapObjectInspector(  
                PrimitiveObjectInspectorFactory.writableStringObjectInspector,   
                PrimitiveObjectInspectorFactory.writableStringObjectInspector);  
	}
	
	/**
	 * 解析IP,返回map类型的地址信息
	 * @param ipText ip地址
	 * @return map类型的地址信息
	 * @throws HiveException
	 */
	public Object evaluate(DeferredObject[] arguments) throws HiveException {
		String ip = PrimitiveObjectInspectorUtils.getString(arguments[0].get(), inputOI).trim();
		String reString=null;
		if(ip!=null && IpUtils.isIpV4(ip)){
			try {
				DataBlock dataBlock = searcher.memorySearch(ip);
				reString=dataBlock.getRegion();
			} catch (IOException e) {
				reString="0|0|0|0|0";
			}
		}
		else{
			reString="0|0|0|0|0";
		}
		String[] ipArray = reString.split(ConstantsGeoIp.SEP_IP2REGION);
		
		
		Map<Text, Text> reMap = new HashMap<Text, Text>();
		for (int i = 0; i < fieldNames.size(); i++) {
			Text t = ipArray[i].equals("0")?null:new Text(ipArray[i]);
			reMap.put(new Text(fieldNames.get(i)), t);
		}
		return reMap;
	}
	
	

	@Override
	public String getDisplayString(String[] arg0) {
		return arg0[0];
	}



}
