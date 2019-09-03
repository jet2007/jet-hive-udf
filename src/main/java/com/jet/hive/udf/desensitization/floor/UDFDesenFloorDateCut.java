package com.jet.hive.udf.desensitization.floor;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.serde2.io.TimestampWritable;
import org.apache.hadoop.io.Text;

import com.jet.hive.udf.desensitization.ConstantDesensitization;
import com.jet.utils.date.DateUtils;


@Description(name = "DesenFloorDateCut", value = "_FUNC_(date_or_datetime ,cuts ) - 将日期或时间，指定部分截断置'零'\n"
		+"置'零':从年到秒,被置零依次为'1970,1,1,0,0,0'"
		+"参数1：日期或时间值,支持string(yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss,yyyyMMdd,yyyy-MM-dd),和date/timestamp类型"
		+"参数2：被截断的单位，逗号隔开；从年到秒依次为YYMMDDHHMISS，示例MI,SS为将分秒置为0分0秒"
, extended = "Example:\n > select _FUNC_('2019-01-02 03:04:05','MI,SS')  \n " 	
+"                               ,_FUNC_('20190102030405','MI,SS')  \n "
+"                    结果： '2019-01-02 03:00:00','20190102030000' \n "
)
public class UDFDesenFloorDateCut extends UDF {
	
	//yyyy-MM-dd HH:mm:ss
	
	private static final String DATE_FORMAT_DEFAULT="yyyy-MM-dd HH:mm:ss";
	private static final String S_YEAR="YY";
	private static final String S_MOTHN="MM";
	private static final String S_DAY="DD";
	private static final String S_HOUR="HH";
	private static final String S_MINUTE="MI";
	private static final String S_SECOND="SS";
	
	private static final int FLOOR_YEAR=1970;
	private static final int FLOOR_MOTHN=0;//相当1月份,减去1
	private static final int FLOOR_DAY=1;
	private static final int FLOOR_HOUR=0;
	private static final int FLOOR_MINUTE=0;
	private static final int FLOOR_SECOND=0;
	
	private Calendar cal = Calendar.getInstance();
	private Text result = new Text();
	
	/**
	 * 将日期或时间，指定部分截断置为"零"
	 * @param inputDate 输入时间，格式(yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss,yyyyMMdd,yyyy-MM-dd)
	 * @param cuts 被截断的单位，逗号隔开；从年到秒依次为YYMMDDHHMISS,被置为"零"依次为"1970,1,1,0,0,0"; 示例MI,SS为将分秒置为0分0秒
	 * @return 与输入格式相同的日期值，STRING类型
	 */
	public Text evaluate(Text inputDate, Text cuts) {
		if(inputDate==null){
			return null;
		}
		String input = inputDate.toString();
		if(StringUtils.isBlank(input)){
			return null;
		}
		Date dt = DateUtils.parse(input);
		this.cal.setTime(dt);
		for (String s : cuts.toString().split(ConstantDesensitization.DELIMITER)) {
			changeCal(s);
		}
		String formatStr = DateUtils.guessDateFormat(input);
		String value = DateUtils.getFormatDate(this.cal.getTime(), formatStr);
		result.set(value);
		return result;
	}
	
	/**
	 * 将日期或时间，指定部分截断置为"零"
	 * @param inputDate 输入时间，Timestamp类型
	 * @param cuts 被截断的单位，逗号隔开；从年到秒依次为YYMMDDHHMISS,被置为"零"依次为"1970,1,1,0,0,0"; 示例MI,SS为将分秒置为0分0秒
	 * @return 与输入格式相同的日期值，STRING类型
	 */
	public Text evaluate(TimestampWritable t, Text cuts) {
		this.cal.setTime(t.getTimestamp());
		for (String s : cuts.toString().split(ConstantDesensitization.DELIMITER)) {
			changeCal(s);
		}
		String value = DateUtils.getFormatDate(this.cal.getTime(), DATE_FORMAT_DEFAULT);
		result.set(value);
		return result;
	}
	

	
	private void changeCal(String offsetStr) {
		String s = offsetStr.toUpperCase();
		if (s.equals(S_YEAR)){
			this.cal.set(Calendar.YEAR, FLOOR_YEAR);
		}
		else if (s.equals(S_MOTHN)){
			this.cal.set(Calendar.MONTH, FLOOR_MOTHN);
		}
		else if (s.equals(S_DAY)){
			this.cal.set(Calendar.DAY_OF_MONTH, FLOOR_DAY);
		}
		else if (s.equals(S_HOUR)){
			this.cal.set(Calendar.HOUR, FLOOR_HOUR);
		}
		else if (s.equals(S_MINUTE)){
			this.cal.set(Calendar.MINUTE, FLOOR_MINUTE);
		}
		else if (s.equals(S_SECOND)){
			this.cal.set(Calendar.SECOND, FLOOR_SECOND);
		}
	}

	
}
