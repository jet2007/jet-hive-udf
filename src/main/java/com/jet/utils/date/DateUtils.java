package com.jet.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.jet.utils.string.StringUtils;



public class DateUtils {
	
	/**
	 * 
	 * @param date date
	 * @param formatStr exg. yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss
	 * @return date format String
	 */
	public static String getFormatDate(Date date , String formatStr){
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String formatDate = sdf.format(date);
		return formatDate;
	}
	
	/**
	 * 
	 * @param dateStr date format String
	 * @param format exg. yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss
	 * @return date type 
	 */
	public static Date parse(String dateStr , String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	
	/**
	 * parse input value(String) to java Date 
	 * @param dateStr dateStr date format String(Format,yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss,yyyyMMdd,yyyy-MM-dd)
	 * @return Date
	 */
	public static Date parse(String dateStr){
		String formatStr=guessDateFormat(dateStr);
		if(formatStr==null){
			return null;
		}else{
			return parse(dateStr,formatStr);
		}
	}
	
	/**
	 * 猜测输入date string的date format,支持(Format,yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss,yyyyMMdd,yyyy-MM-dd)
	 * @param dateStr
	 * @return format或null值
	 */
	public static String guessDateFormat(String dateStr){
		String ds = dateStr.replace(" ", "").replace("-", "").replace(":", "");
		String formatStr=null;
		boolean f2 = dateStr.contains(" ") || dateStr.contains(":") || dateStr.contains("-"); // 包含[:-空格]任何一个
		if(ds.length()==8){//日期格式
			if (f2){
				formatStr="yyyy-MM-dd";
			}
			else{
				formatStr="yyyyMMdd";
			}
		}
		else if(ds.length()==14){//日期+时间格式
			if (f2){
				formatStr="yyyy-MM-dd HH:mm:ss";
			}
			else{
				formatStr="yyyyMMddHHmmss";
			}
		}
		return formatStr;
	}
	
	
	/**
	 * 仿python relativedelta实现日期相加操作,返回String
	 * 
	 * @param dateStr 支持yyyyMMddHHmmss和yyyy-MM-dd HH:mm:ss
	 * @param offsetsStr delta日期的单位与长度,分隔符分别为逗号与等号
	 *        示例： offsetsStr："year=+2,month=8,day=-16"  年份加2，月份为8，日期减16 
	 *        单位支持有year,month,day,hour,minute,second(或y,m,d,h,n,s)；
	 *        长度支持+N,-N,N
	 * @return 返回String的格式与源参数dateStr相似；
	 *         dateDeltaStr("20180102030405","day=+3")   "20180105030405"
	 *         dateDeltaStr("2018-01-02 03:04:05","day=+3")   "2018-01-05 03:04:05"
	 *         dateDeltaStr("20180102","day=+3")   "20180105"
	 *         dateDeltaStr("2018-01-02","day=+3")   "2018-01-05"
	 */
	public static String dateDeltaStr(String dateStr, String offsetsStr){
		return dateDeltaStr(dateStr,offsetsStr,null);
		
	}
	
	
	/**
	 * 仿python relativedelta实现日期相加操作,返回String
	 * 示例： offsetsStr：[year=+2, month=8, day=-16]  年份加2，月份为8，日期减16 
	 * @param dateStr 支持yyyyMMddHHmmss和yyyy-MM-dd HH:mm:ss
	 * @param offsetsStr delta日期的单位与长度,分隔符分别为逗号与等号
	 * @param formatStr date format string exg.yyyyMMddHHmmss,yyyy-MM-dd HH:mm:ss
	 *        单位支持有year,month,day,hour,minute,second；
	 *        长度支持+N,-N,N
	 *        示例有year=+2, month=8, day=-16
	 */
	public static String dateDeltaStr(String dateStr, String offsetsStr,String formatStr){
		Date dt = relativedelta(dateStr,offsetsStr);
		if(StringUtils.isBlank(formatStr)){
			String ds = dateStr.replace(" ", "").replace("-", "").replace(":", "");
			boolean f2 = dateStr.contains(" ") || dateStr.contains(":") || dateStr.contains("-"); // 包含[:-空格]任何一个
			if(ds.length()==8){//日期或日期+时间格式
				if (f2)
					formatStr="yyyy-MM-dd";
				else
					formatStr="yyyyMMdd";
			}
			else {
				if (f2)
					formatStr="yyyy-MM-dd HH:mm:ss";
				else
					formatStr="yyyyMMddHHmmss";
			}
		}
		return getFormatDate(dt,formatStr);
	}
	
	/**
	 * 仿python relativedelta实现日期相加操作
	 * 示例： offsetsStr：[year=+2, month=8, day=-16]  年份加2，月份为8，日期减16 
	 * @param dateStr 支持yyyyMMddHHmmss和yyyy-MM-dd HH:mm:ss
	 * @param offsetsStr delta日期的单位与长度,分隔符分别为逗号与等号
	 *        单位支持有year,month,day,hour,minute,second；
	 *        长度支持+N,-N,N
	 *        示例有year=+2, month=8, day=-16
	 */
	public static Date relativedelta(String dateStr, String offsetsStr){
		String ds = dateStr.replace(" ", "").replace("-", "").replace(":", "");
		if(ds.length()==8)
			ds=ds+"000000";
		Date date=parse(ds, "yyyyMMddHHmmss");
		return relativedelta(date,offsetsStr);
	}
	
	
	/**
	 * 仿python relativedelta实现日期相加操作
	 * 示例： offsetsStr：[year=+2, month=8, day=-16]  年份加2，月份为8，日期减16   
	 * @param date 日期类型
	 * @param offsetsStr delta日期的单位与长度,分隔符分别为逗号与等号
	 *        单位支持有year,month,day,hour,minute,second；
	 *        长度支持+N,-N,N
	 *        示例有year=+2, month=8, day=-16
	 *        特殊(单个日期差)：-1,+1,1等价于day=-1,day=+1,day=1
	 */
	public static Date relativedelta(Date date, String offsetsStr){
		if(StringUtils.isNotBlank(offsetsStr)){
			String[] offsets=null;
			if (offsetsStr.contains(",") || offsetsStr.contains("=")){
				offsets = offsetsStr.split(",");
			}
			else{//offsetsStr形为-1,+1,1的，默认为日期
				offsets = ("day="+offsetsStr).split(",");
			}

			return relativedelta(date,offsets);
		}
		else {
			return date;
		}
	}
	
	/**
	 * 仿python relativedelta实现日期相加操作
	 * 示例： offsets：[year=+2, month=8, day=-16]  年份加2，月份为8，日期减16   
	 * @param date 日期类型
	 * @param offsets delta日期的单位与长度。
	 *        单位支持有year,month,day,hour,minute,second；
	 *        长度支持+N,-N,N
	 *        示例有year=+2, month=8, day=-16
	 */
	public static Date relativedelta(Date date, String... offsets){
		if (offsets==null || offsets.length==0){
			return date;
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			for (int i = 0; i < offsets.length; i++) {
				String[] offset = offsets[i].trim().split("=");
				int field = offsetUnit(offset[0].trim());
				String value = offset[1];
				if(value.contains("+") || value.contains("-")){
					cal.add( field, Integer.parseInt(offset[1]));
				}
				else{
					int off;
					if (field==Calendar.MONTH) 
							off=Integer.parseInt(offset[1])-1;
					else
						off=Integer.parseInt(offset[1]);
					cal.set( field, off );
				}
			}
			return cal.getTime();
		}
	}
	
	/**
	 * year,month,day,hour,minute,second字符串转化Calendar的常量INT类型
	 * @param offsetStr
	 * @return
	 */
	public static int offsetUnit(String offsetStr) {
		if (offsetStr.equals("year") || offsetStr.equals("y"))
			return Calendar.YEAR;
		else if (offsetStr.equals("month") || offsetStr.equals("m"))
			return Calendar.MONTH;
		else if (offsetStr.equals("day") || offsetStr.equals("d"))
			return Calendar.DAY_OF_MONTH;
		else if (offsetStr.equals("hour") || offsetStr.equals("h"))
			return Calendar.HOUR;
		else if (offsetStr.equals("minute") || offsetStr.equals("n"))
			return Calendar.MINUTE;
		else if (offsetStr.equals("second") || offsetStr.equals("s"))
			return Calendar.SECOND;
		else
			return -1;
	}
	
//	public static void main(String[] args) {
//		String dt="20180102";
//		Date re = relativedelta(dt,"+1");
//		String reStr = getFormatDate(re,"yyyy-MM-dd HH:mm:ss");
//		//System.out.println(reStr); 
//		
//		String dt1=null;
//		dt1="20180102030405";
//		dt1="2018-01-02 03:04:05";
//		dt1="20180102";
//		String re1 = dateDeltaStr(dt1,"month=1,day=1,day=-1");
//		System.out.println(re1);
//		
//		
//	}
	
	
	
}
