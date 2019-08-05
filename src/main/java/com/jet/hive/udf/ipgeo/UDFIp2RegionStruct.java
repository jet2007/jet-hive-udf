package com.jet.hive.udf.ipgeo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.session.SessionState;
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


@Description(name = "ip2geo2", value = "_FUNC_(array) - Returns struct type of the ip address.\n"
		+ "Based on https://github.com/lionsoul2014/ip2region.\nThe ip address database has stored in local resource. \n"
		+ "  > Para1: Ipadress\n"
		+ "Example:\n"  
        + "  > CREATE TEMPORARY FUNCTION ip2geo AS 'com.jet.hive.udf.ipgeo.UDFIp2RegionStruct' \n"
        + "  > SELECT ip2geo2('221.226.1.30' ),ip2geo2('221.226.1.30' ).provinceName \n"
        + "") 
public class UDFIp2RegionStruct extends GenericUDF{
	PrimitiveObjectInspector inputOI;
	private static DbSearcher searcher=null;
	private static boolean isNonInit = true; //未被init过
	private static List<String> fieldNames = null;
	
//	public static void main(String[] args) throws IOException {
//		InputStream is = UDFIp2RegionStruct.class.getResourceAsStream("/supported_devices.csv");
//		inputStream2File(is,"d:\\aaaaa.txt");
//		is.close();
//	}
//	

	/**
	 * 构造searcher
	 * @throws UDFArgumentException
	 */
    private static synchronized void constructUDFIp2Region() throws UDFArgumentException {
    	File inputFile = null;
		try {
			InputStream inputStream = UDFIp2RegionStruct.class.getResourceAsStream("/"+ConstantsGeoIp.FILE_IP2REGION);
			String inputFilePath = SessionState.get().getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR)+"/"+UUID.randomUUID().toString().replaceAll("-", "")+".db";
			System.out.println(inputFilePath);
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
	        	fieldNames.add(ConstantsGeoIp.KEY_CONTINENT_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_REGION_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_PROVINCE_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_CITY_NAME);
	        	fieldNames.add(ConstantsGeoIp.KEY_ISP_NAME);
	        }
			isNonInit=false;
		}
		
		// 存储在全局变量的ObjectInspectors元素的输入
		// 返回变量输出类型struct
		 // Define the output
        // https://stackoverflow.com/questions/26026027/how-to-return-struct-from-hive-udf
        // Define the field names for the struct<> and their types
        List<ObjectInspector> fieldObjectInspectors = new ArrayList<ObjectInspector>(fieldNames.size());
        for (String ignored : fieldNames) {
            fieldObjectInspectors.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        }
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldObjectInspectors);
        
	}
	
	/**
	 * 解析IP,返回map类型的地址信息
	 * @param ipText ip地址
	 * @return struct类型的地址信息
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
		List<Object> result = new ArrayList<Object>(fieldNames.size());
		for (int i = 0; i < fieldNames.size(); i++) {
			String string = ipArray[i];
			Text t = string.equals("0")?null:new Text(string);
			result.add(t);
		}
		return result.toArray();
	}

	@Override
	public String getDisplayString(String[] arg0) {
		return arg0[0];
	}

}
