/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jet.hive.udf.date;

import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import com.jet.utils.date.DateUtils;

/**
 * <code>CurrentDateTimeDelta2(offsets)</code>. 
 * 当前时间，进行日期时间计算,返回yyyy-MM-dd HH:mm:ss格式
 */




public class CurrentDateTimeDelta extends UDF {
	private Text result = new Text();
	
	/**
	 * @param args 参数1：offsets；返回yyyy-MM-dd HH:mm:ss格式
	 * @return
	 */
	public Text evaluate(Text... args) {
		Date now = new Date();
		String offsets = args.length>=1 ? args[0].toString():null;
		//String format = args.length>=2 ? args[1].toString():"yyyy-MM-dd";
		Date reDate=DateUtils.relativedelta(now,offsets);
		String re = DateUtils.getFormatDate(reDate,"yyyy-MM-dd HH:mm:ss");
		result.set(re);
		return result;
		
	}
}
