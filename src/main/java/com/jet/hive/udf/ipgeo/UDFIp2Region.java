package com.jet.hive.udf.ipgeo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
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
import org.lionsoul.ip2region.DbSearcher;
import org.apache.hadoop.hive.ql.session.SessionState;

@Description(name = "ip2geo", value = "_FUNC_(array) - Returns map of the ip address.\n"
		+ "Based on https://github.com/lionsoul2014/ip2region\n"
		+ "  > Para1: Ipadress\n"
		+ "  > Para2:(Option,default value=ip2region.db),'ip2region.db' filename in hdfs path\n"
		+ "Example:\n"  
        + "  > add file hdfs://quickstart.cloudera:8020/user/hive/warehouse/func.db/ip2region.db ;\n"
        + "  > ADD jar /home/cloudera/tmp/chm/udf/ip2region-1.7.jar; \n"
        + "  > ADD jar /home/cloudera/tmp/chm/udf/hive-udf-0.2.jar; \n"
        + "  > CREATE TEMPORARY FUNCTION ip2geo AS \"com.ct.dw.udf.geoip.UDFIp2RegionByHDFS\" \n"
        + "  > SELECT ip2geo('221.226.1.30','ip2region.db') \n"
        + "  > equals to SELECT ip2geo('221.226.1.30') \n"
        + "") 
public class UDFIp2Region extends GenericUDF{
	
	PrimitiveObjectInspector inputOI;
	private static DbSearcher searcher=null;
	private static boolean isNonInit = true; //未被init过
	
	@Override
	public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
		
		isNonInit=true;
		if(isNonInit){
	        File file = getLookupFile();
			try {
				DbConfig config = new DbConfig();
				searcher = new DbSearcher(config, file.getAbsolutePath());
			} catch (Exception e) {
				throw new UDFArgumentException("Error: Invalid file:"+file.getAbsolutePath());
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
		if(ip!=null && isIpV4(ip)){
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
		
		String keyArray[] = {ConstantsGeoIp.KEY_CONTINENT_NAME
					 ,ConstantsGeoIp.KEY_REGION_NAME
					 ,ConstantsGeoIp.KEY_PROVINCE_NAME
				     ,ConstantsGeoIp.KEY_CITY_NAME
				     ,ConstantsGeoIp.KEY_ISP_NAME};
		
		Map<Text, Text> reMap = new HashMap<Text, Text>();
		for (int i = 0; i < keyArray.length; i++) {
			Text t = ipArray[i].equals("0")?null:new Text(ipArray[i]);
			reMap.put(new Text(keyArray[i]), t);
		}
		return reMap;
	}
	
	
	/**
	 * 获取文件
	 * @param lookupFile，'add file hfds//xxxxx/filename'加载后的filename值
	 * @return
	 */
	protected File getLookupFile() {
		
		URL fileurl = UDFIp2Region.class.getResource("/"+ConstantsGeoIp.FILE_IP2REGION);
		
		/* distributed cache */
		File f = new File(fileurl.toString()); // file available locally

		if (!f.exists()) {
			/* local resources (non-MR mode) */
			File resourceDir = new File(
					SessionState.get().getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR));
			f = new File(resourceDir, ConstantsGeoIp.FILE_IP2REGION);
		}
		return f;
	}
	
	protected static boolean isIpV4(String ipAddress) {  
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";   
        Pattern pattern = Pattern.compile(ip);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
    }
	

	@Override
	public String getDisplayString(String[] arg0) {
		return arg0[0];
	}





}
