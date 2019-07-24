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



import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import com.jet.utils.date.DateUtils;

/**
 * <code>DateDelta(date,offsets)</code>. 
 * 仿python relativedelta实现日期相加操作,返回日期String
 *  
 */

@Description(name = "DateDelta",
    value = "_FUNC_(date,offsets) - 仿python relativedelta实现日期相加操作,返回日期String,String的格式与参数date相似",
    extended = "Params:\n"
    		+ "	> date: String类型,支持yyyyMMddHHmmss和yyyy-MM-dd HH:mm:ss\n"
    		+ "	> offsets: delta日期的单位与长度,分隔符分别为逗号与等号\n"
    		+ "		单位支持有year,month,day,hour,minute,second(或y,m,d,h,n,s)\n"
    		+ "		长度支持+N,-N,N\n"
    		+ "		示例： offsetsStr：'year=+2,month=8,day=-16'  年份加2，月份为8，日期减16\n"
    		+ " > format: 可选；输出的日期格式，如yyyyMMddHHmmss和yyyy-MM-dd HH:mm:ss\n"
    + "Example:\n"
    + "  > SELECT DateDelta('20180102030405','day=+3') FROM src LIMIT 1;\n" + "  '20180105030405'\n"
    + "  > SELECT DateDelta('2018-01-02 03:04:05','day=+3') FROM src LIMIT 1;\n" + "  '2018-01-05 03:04:05'\n"
    + "  > SELECT DateDelta('20180102','day=+3') FROM src LIMIT 1;\n" + "  '20180105'\n"
    + "  > SELECT DateDelta('2018-01-02','day=+3') FROM src LIMIT 1;\n" + "  '2018-01-05'\n"
    + "  > SELECT DateDelta('2018-01-02','day=+3' , 'yyyyMMdd') FROM src LIMIT 1;\n" + "  '20180105'\n"
    )



public class DateDelta extends UDF {
	private Text result = new Text();
	
	/**
	 * 接受3个参数
	 * @param args
	 * @return
	 */
	public Text evaluate(Text... args) {
		if(args.length==2){
			String dt = args[0].toString();
			String offsets = args[1].toString();
			String format = null;
			String re = DateUtils.dateDeltaStr(dt,offsets,format);
			result.set(re);
			return result;
		}else if(args.length>2){
			String dt = args[0].toString();
			String offsets = args[1].toString();
			String format = args[2].toString();
			String re = DateUtils.dateDeltaStr(dt,offsets,format);
			result.set(re);
			return result;
		}
		else if(args.length==1){
			if (args[0]==null) {
				return null;
			}else{
				result.set(args[0].toString());
				return result;
			}
		}else{
			return null;
		}
		
	}
}
