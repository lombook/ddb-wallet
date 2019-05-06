package com.jinglitong.wallet.ddbserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;



public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	

	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	

	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	

	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	public static Date format(String viewCtime) {
		Date date = new Date();
		viewCtime = viewCtime.replace("Z", " UTC");
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

		try {
			date = utcFormat.parse(viewCtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  date;
	}


	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}


	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTimeStr() {
		return formatDate(new Date(), "yyyyMMddmm");
	}


	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}


	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}


	public static String getDay() {
		return formatDate(new Date(), "dd");
	}


	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	

	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}


	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}


	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	

	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	

    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	

	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}



	/**
	 * 日期格式化 日期格式为：yyyy-MM-dd
	 * @param date  日期
	 * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
	 * @return  返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date, String pattern) {
		if(date != null){
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd�? E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}

    public static String getUnixTime(String createTime) {
		String formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(createTime) * 1000;
		return  new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
    }
}
