package com.jet.utils.udf;

import java.io.File;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;

public class HiveUdfUtils {
	
	/**
	 * 获取hiveql add file/jar的文件名
	 * @param filename 文件名，不包含路径；如add file hdfs://path/xxx.txt当中的xxx.txt
	 * @return
	 */
	public static File getHiveResourceFile(String filename) {
		/* distributed cache */
		File f = new File("./"+filename); // file available locally
		if (!f.exists()) {
			/* local resources (non-MR mode) */
			File resourceDir = new File( SessionState.get().getConf().getVar(HiveConf.ConfVars.DOWNLOADED_RESOURCES_DIR));
			f = new File(resourceDir, filename);
		}
		return f;
	}
	
}
