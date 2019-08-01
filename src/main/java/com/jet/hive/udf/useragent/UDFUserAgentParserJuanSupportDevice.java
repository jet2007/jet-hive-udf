package com.jet.hive.udf.useragent;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

import com.github.codesorcery.juan.ParsedUserAgent;
import com.github.codesorcery.juan.UserAgentParser;
import com.jet.utils.string.StringUtils;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.utils.UserAgentParserJuanSupportedDevicesUtils;



/**
 * 
 * @author JET
 * useragent内容解析，核心使用https://github.com/codesorcery/juan项目
 * juan项目：A fast, thread-safe and dependency-free user agent parser for Java 8+.
 */
@Description(
    name = "UserAgentParser：A fast, thread-safe and dependency-free user agent parser for Java 8+.",
    value = "_FUNC_(str) - Parses the UserAgent into a map type.",
    extended = "Example:\n" +
        "> SELECT UserAgentParser(useragent)\n" )
public class UDFUserAgentParserJuanSupportDevice extends GenericUDF {

    private StringObjectInspector stringOI = null;
    private static UserAgentParser userAgentParser = null;

    private static synchronized void constructAnalyzer() throws IOException{
    	URL url = UDFUserAgentParserJuanSupportDevice.class.getResource(ConstantsUserAgent.SUPPORT_DEVICE_RESOURCE_FILE);
        if (userAgentParser == null) {
        	userAgentParser = UserAgentParser.withPlayStoreDeviceList(url,Charset.forName("UTF-16LE"));
        }
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        if (args.length < 1) {
            throw new UDFArgumentException("The argument list must be exactly 1 element");
        }
        // ================================
        // Initialize the parser
        try {
			constructAnalyzer();
		} catch (IOException e) {
			throw new UDFArgumentException("Read the Resourse file[supported_devices.csv] Error");
		}
        
		// 存储在全局变量的ObjectInspectors元素的输入
        ObjectInspector inputOI = args[0]; // The first argument must be a String
        if (!(inputOI instanceof StringObjectInspector)) {
            throw new UDFArgumentException("The argument must be a string");
        }
        stringOI = (StringObjectInspector) inputOI;
		// 返回变量输出类型
		return ObjectInspectorFactory.getStandardMapObjectInspector(  
                PrimitiveObjectInspectorFactory.writableStringObjectInspector,   
                PrimitiveObjectInspectorFactory.writableStringObjectInspector);  
    }


	@Override
    public Object evaluate(DeferredObject[] args) throws HiveException {
        String userAgentString = stringOI.getPrimitiveJavaObject(args[0].get());
        if (StringUtils.isBlank(userAgentString) ) {
            return null;
        }
        ParsedUserAgent parsedUserAgent = userAgentParser.parse(userAgentString);
        Map<Text, Text> re = UserAgentParserJuanSupportedDevicesUtils.fromUserAgentParserToTextMap(parsedUserAgent);
        return re;
    }

    @Override
    public String getDisplayString(String[] args) {
        return "Parses the UserAgent into all possible pieces.";
    }
    
}
