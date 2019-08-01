/*
 * Yet Another UserAgent Analyzer
 * Copyright (C) 2013-2019 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jet.hive.udf.useragent;

import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

import com.jet.utils.string.StringUtils;
import com.jet.utils.useragent.build.StevenkangMobileModelBuild;
import com.jet.utils.useragent.constant.ConstantsUserAgent;
import com.jet.utils.useragent.utils.UserAgentAnalyzerYauaaStevenkangUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参考nl.basjes.parse.useragent.hive.UserAgentParse
 *
 */

@Description(
    name = "UserAgentParse",
    value = "_FUNC_(str,[str]) - Parses the UserAgent into a map type.",
    extended = "Example:\n" +
        "> SELECT UserAgentParse(useragent), \n" +
        "         UserAgentParse(useragent,'all'), \n" +
        "         UserAgentParse(useragent,'key1,key2,...,keyN') \n" +
        "  FROM   clickLogs;\n" )
public class UDFUserAgentParserYauaaStevenkang extends GenericUDF {

	//private PrimitiveObjectInspector inputOI;
    private StringObjectInspector stringOI = null;
    private static UserAgentAnalyzer userAgentAnalyzer = null;
    private static StevenkangMobileModelBuild mobileModelParse = null;
    private List<String> fieldNames = null;
    private boolean isNonInit = true; //未被init过

    private static synchronized void constructAnalyzer(){
        if (userAgentAnalyzer == null) {
            userAgentAnalyzer = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .delayInitialization()
                .build();
        }
        if (mobileModelParse == null) {
        	mobileModelParse = new StevenkangMobileModelBuild();
        }
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        if (args.length < 1) {
            throw new UDFArgumentException("The argument list must be exactly 1 or 2 element");
        }
        // ================================
        // Initialize the parser
        constructAnalyzer();
        if (args.length == 1) {
        	fieldNames=getFieldNames(null);
        	isNonInit=false;
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
        if(isNonInit){//未被初始化过，有2个参数的情况
        	if(args.length>1){ //根据第2个参数，得到map的key列表
        		String inputFields = stringOI.getPrimitiveJavaObject(args[1].get());
        		fieldNames=getFieldNames(inputFields);
        		isNonInit=false;
        	}
        }

        if (StringUtils.isBlank(userAgentString) ) {
            return null;
        }
        
        Map<Text, Text> reMap = new HashMap<Text, Text>();
        Map<String, String> uavs = UserAgentAnalyzerYauaaStevenkangUtils.getUserAgentVaules(userAgentAnalyzer, mobileModelParse, userAgentString, fieldNames);
        for (String key : uavs.keySet()) {
        	String value = uavs.get(key);
        	Text t = ( value==null?null:new Text(value) );
        	reMap.put(new Text(key), t);
		}
        return reMap;
    }

    @Override
    public String getDisplayString(String[] args) {
        return "Parses the UserAgent into all possible pieces.";
    }
    
    /**
     * 返回输入列名的List类型值
     * @param inputFields 形为col1,col2,...,colN
     * @return
     */
    private List<String> getFieldNames(String inputFields) {
    	List<String> fieldNamesNative = userAgentAnalyzer.getAllPossibleFieldNamesSorted();//原工具的所有展出的列名
    	//处理入参列名，支持default或all或c1,c2,c3
		
		if(StringUtils.isBlank(inputFields)){
			inputFields=ConstantsUserAgent.YAUAA_FIELD_DEFAULT;
		}
		if(inputFields.equals(ConstantsUserAgent.YAUAA_FIELD_ALL)){
			fieldNamesNative.add(ConstantsUserAgent.YAUAA_DEVICE_MODEL_NAME);
			fieldNamesNative.add(ConstantsUserAgent.YAUAA_DEVICE_VENDOR);
			return fieldNamesNative;
    		
    	}else {
    		String fs=inputFields;
    		if(inputFields.equals(ConstantsUserAgent.YAUAA_FIELD_DEFAULT)){
    			fs=ConstantsUserAgent.YAUAA_FIELD_DEFAULT_FIELD_NAMES;
    			}
    		String[] es = fs.split(ConstantsUserAgent.FIELD_SPLIT_BY);
    		List<String> fieldNames=new ArrayList<String>();
    		for (String e : es) {
    			if(fieldNamesNative.contains(e.trim()))
    				fieldNames.add(e.trim());
			}
    		return fieldNames;
    	}
		
	}
    
    
    
}
