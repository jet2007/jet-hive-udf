package com.jet.utils.conf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aaronshan.functions.model.ChinaIdArea;
import com.google.common.collect.Maps;


public class ConfigUtils {

	private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
	
	
	
	public static Map<String, ChinaIdArea> loadIp2regionDb() throws IOException {
        String fileName = "/ip2region.db";
        

        
        Map<String, ChinaIdArea> map = Maps.newHashMap();
        List<String> list = null;// loadFile(fileName);
		for (String line : list) {
		    String[] results = line.split("\t", 4);
		    map.put(results[0], new ChinaIdArea(results[1], results[2], results[3]));
		}

        return map;
    }
}
