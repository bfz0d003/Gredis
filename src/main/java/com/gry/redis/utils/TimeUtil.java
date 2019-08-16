package com.gry.redis.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("all")
public class TimeUtil {

	/** 日期格式 yyyyMMdd */
	public static final String DateFormat0 = "yyyyMMdd";

	/** 日期格式 yyyy-MM-dd */
	public static final String DateFormat1 = "yyyy-MM-dd";

	/** 日期格式 yyyy年MM月dd日 */
	public static final String DateFormat2 = "yyyy年MM月dd日";

	/** 日期格式 yyyy-MM-dd HH:mm:ss */
	public static final String DateFormat3 = "yyyy-MM-dd HH:mm:ss";

	/** 日期格式 yyyyMMddhhmmss */
	public static final String DateFormat4 = "yyyyMMddHHmmss";

	public static String convertDate(String datestr, String format1, String format2) {
		return getFormatDate(getDateFromStr(datestr, format1), format2);
	}

	/**
	 * 根据日期字符返回日期对象
	 *
	 * @param datestr
	 *            比如：2006-02-03
	 * @param format
	 *            比如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static Date getDateFromStr(String datestr, String format) {
		if (datestr == null || "".equalsIgnoreCase(datestr)) {
			return null;
		}

		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		Date result = null;
		try {
			result = dateformat.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 格式化日期
	 *
	 * @param indate
	 *            日期对象
	 * @param format
	 *            比如yyyy-MM-dd
	 * @return 比如：2006-02-03
	 */
	public static String getFormatDate(Date indate, String format) {
		if (indate == null) {
			return "";
		}

		SimpleDateFormat dateformat = new SimpleDateFormat(format);

		return dateformat.format(indate);
	}

	/**
	 * 得到当前时间
	 *
	 * @return 当前时间
	 */
	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 日期加减年数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addYear
	 *            要加的年数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addYearS(final Date inDate, final int addYear) {

		return addDateS(getZeroDay(inDate), addYear, Calendar.YEAR);
	}

	/**
	 * 日期加减月数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addMonth
	 *            要加的月数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addMonthS(final Date inDate, final int addMonth) {

		return addDateS(getZeroDay(inDate), addMonth, Calendar.MONTH);
	}

	/**
	 * 日期加减天数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addDay
	 *            要加的天数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addDayS(final Date inDate, final int addDay) {
		return addDateS(getZeroDay(inDate), addDay, Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得对应日期的零点日期时间
	 */
	private static Date getZeroDay(Date date) {
		Calendar day = Calendar.getInstance() ;
		day.setTime(date);
		day.set(Calendar.HOUR_OF_DAY, 0) ;
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return day.getTime() ;
	}

	/**
	 * 日期加减小时数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addDay
	 *            要加的小时数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addHourS(final Date inDate, final int addDay) {

		return addDateS(inDate, addDay, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 日期加减分钟数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addMinute
	 *            要加的小时数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addMinuteS(final Date inDate, final int addMinute) {

		return addDateS(inDate, addMinute, Calendar.MINUTE);
	}

	/**
	 * 日期加减秒数
	 *
	 * @param inDate
	 *            初始日期
	 * @param addSecond
	 *            要加的秒数(负值代表减)
	 * @return 结果日期
	 */
	public static Date addSecondS(final Date inDate, final int addSecond) {

		return addDateS(inDate, addSecond, Calendar.SECOND);
	}

	/**
	 * 计算日期加减
	 *
	 * @param inDate
	 *            初始日期
	 * @param addDate
	 *            要加的日期
	 * @param field
	 *            要加的域
	 * @return 结果日期
	 */
	private static Date addDateS(final Date inDate, final int addDate,
                                 final int field) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inDate);

		calendar.add(field, addDate);

		return calendar.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static Date parseDate(String dateString) {
		try {
			return DateUtils.parseDate(dateString, new String[] { "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd",
					"yyyyMMdd", "yyyyMM", "yyyy-MM", "yyyy/MM", });
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转换为字符串
	 *            日期
	 * @param format
	 *            日期格式 yyyy-MM-dd HH:mm:ss
	 * @return 字符串
	 */
	public static String getFormatedDate(String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 获得当前时间是一年的第几周
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getWeekOfYear(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * calendar2Str
	 * 
	 * @param calendar
	 * @param formatStr
	 * @return
	 */
	public static String calendar2Str(Calendar calendar, String formatStr) {
		if (StringUtils.isEmpty(formatStr)) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat strFormater = new SimpleDateFormat(formatStr);

		return strFormater.format(calendar.getTime());
	}

	/**
	 * str2Calendar
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Calendar str2Calendar(String dateStr, String formatStr) {
		Calendar calendar = Calendar.getInstance();
		try {

			if (StringUtils.isEmpty(dateStr)) {
				// dateStr = calendar2Str(calendar, null);
				return calendar;
			}

			if (StringUtils.isEmpty(formatStr)) {
				formatStr = "yyyy-MM-dd HH:mm:ss";
			}

			SimpleDateFormat strFormater = new SimpleDateFormat(formatStr);
			Date date = strFormater.parse(dateStr);

			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendar;
	}

	/**
	 * date2Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar date2Calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	/**
	 * 获得当前年月
	 * 
	 * @return
	 */
	public static String curYearMonth() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMM");
		Calendar lastDate = Calendar.getInstance();
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获得当前年月
	 * 
	 * @param dateString
	 * @return
	 */
	public static String curYearMonth(String dateString) {
		if (StringUtils.isEmpty(dateString)) {
			return curYearMonth();
		} else {
			Date date = TimeUtil.parseDate(dateString);
			SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMM");
			return "_" + strFormater.format(date);
		}
	}

	/**
	 * 获得当前年月日
	 * 
	 * @return
	 */
	public static String curYearMonthDay() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMMdd");
		Calendar lastDate = Calendar.getInstance();
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获得当前年月日
	 * 
	 * @param dateString
	 * @return
	 */
	public static String curYearMonthDay(String dateString) {
		if (StringUtils.isEmpty(dateString)) {
			return curYearMonthDay();
		} else {
			Date date = TimeUtil.parseDate(dateString);
			SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMMdd");
			return "_" + strFormater.format(date);
		}
	}

	/**
	 * 获得当前年
	 * 
	 * @return
	 */
	public static String curYear() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyy");
		Calendar lastDate = Calendar.getInstance();
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获得当前年
	 * 
	 * @param dateString
	 * @return
	 */
	public static String curYear(String dateString) {
		if (StringUtils.isEmpty(dateString)) {
			return curYear();
		} else {
			Date date = TimeUtil.parseDate(dateString);
			SimpleDateFormat strFormater = new SimpleDateFormat("yyyy");
			return "_" + strFormater.format(date);
		}
	}

	/**
	 * 获得下一个年月
	 * 
	 * @return
	 */
	public static String nextYearMonth() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMM");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获得下一个年
	 * 
	 * @return
	 */
	public static String nextYear() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyy");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获得下一天
	 * 
	 * @return
	 */
	public static String nextYearMonthDay() {
		SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMMdd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DAY_OF_MONTH, 1);
		return "_" + strFormater.format(lastDate.getTime());
	}

	/**
	 * 获取今天在本年的第几天
	 * 
	 * @return
	 */
	public static int getDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 今天是否是一年的第一天
	 * 
	 * @return
	 */
	public static boolean firstDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == 1 ? true : false;
	}

	/**
	 * 获取今天在本月的第几天
	 * 
	 * @return
	 */
	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今天是否是一月的第一天
	 * 
	 * @return
	 */
	public static boolean firstDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1 ? true : false;
	}

	public static String getFirstDayOfMonth(String dateformat) {
		String str = "";
		if (StringUtils.isBlank(dateformat)) {
			dateformat = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.HOUR_OF_DAY, 0);// 设为当前时间为0
		lastDate.set(Calendar.MINUTE, 0);// 设为当前分钟为0
		lastDate.set(Calendar.SECOND, 0);// 设为当前秒为0
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获得当前时间
	 * 
	 * @param dateformat
	 * @return
	 */
	public static String getNowTime(String dateformat) {
		if (StringUtils.isBlank(dateformat)) {
			dateformat = "yyyy-MM-dd HH:mm:ss";
		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String nowStr = dateFormat.format(now);
		return nowStr;
	}

	/**
	 * 获得距离calendar的某天时间
	 * 
	 * @param calendar
	 *            日期
	 * @param dateformat
	 *            dateformat
	 * @param amount
	 *            相差天数
	 * @param hourOfDay
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return
	 */
	public static String getSomedayTime(Calendar calendar, String dateformat, int amount, int hourOfDay, int minute, int second) {
		if (StringUtils.isBlank(dateformat)) {
			dateformat = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		calendar.add(Calendar.DATE, amount);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);// 设为当前时间为0
		calendar.set(Calendar.MINUTE, minute);// 设为当前分钟为0
		calendar.set(Calendar.SECOND, second);// 设为当前秒为0
		String str = dateFormat.format(calendar.getTime());

		return str;
	}

	/**
	 * 获得当天00:00:00
	 * 
	 * @param dateformat
	 * @return
	 */
	public static String getTodayTime(String dateformat) {
		if (StringUtils.isBlank(dateformat)) {
			dateformat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.HOUR_OF_DAY, 0);// 设为当前时间为0
		lastDate.set(Calendar.MINUTE, 0);// 设为当前分钟为0
		lastDate.set(Calendar.SECOND, 0);// 设为当前秒为0
		String str = dateFormat.format(lastDate.getTime());

		return str;
	}

	/**
	 * 获得昨天00:0:00
	 * 
	 * @return
	 */
	public static Calendar getYesterdayCalendar() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);// 设为当前时间为0
		calendar.set(Calendar.MINUTE, 0);// 设为当前分钟为0
		calendar.set(Calendar.SECOND, 0);// 设为当前秒为0

		return calendar;
	}

	/**
	 * 获得距离calendar的某天
	 * 
	 * @param calendar
	 *            日期
	 * @param amount
	 *            相差天数
	 * @param hourOfDay
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return
	 */
	public static Calendar getSomedayCalendar(Calendar calendar, int amount, int hourOfDay, int minute, int second) {
		calendar.add(Calendar.DATE, amount);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);// 设为当前时间为0
		calendar.set(Calendar.MINUTE, minute);// 设为当前分钟为0
		calendar.set(Calendar.SECOND, second);// 设为当前秒为0

		return calendar;
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String date2Str(Date date, String format) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
	 * 
	 * @param str
	 *            字符串
	 * @param format
	 *            日期格式
	 * @return 日期
	 * @throws ParseException
	 */
	public static Date str2Date(String str, String format) {
		if (null == str || "".equals(str)) {
			return null;
		}
		// 如果没有指定字符串转换的格式，则用默认格式进行转换
		if (null == format || "".equals(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串转换时间戳
	 * 
	 * @param str
	 * @return
	 */
	public static Timestamp str2Timestamp(String str) {
		Date date = str2Date(str, "yyyy-MM-dd HH:mm:ss");
		return new Timestamp(date.getTime());
	}

	/**
	 * 时间戳转换为字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String timestamp2Str(Timestamp time, String format) {
		Date date = null;
		if (null != time) {
			date = new Date(time.getTime());
		}

		if (StringUtils.isBlank(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		return date2Str(date, format);
	}

	public static String getTimesConvert(long beginTime, long endTime) {
		long between = endTime - beginTime;
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		if (day != 0) {
			return day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
		} else if (hour != 0) {
			return hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
		} else if (min != 0) {
			return min + "分" + s + "秒" + ms + "毫秒";
		} else if (s != 0) {
			return s + "秒" + ms + "毫秒";
		} else {
			return ms + "毫秒";
		}
		// String result = day + "天" + hour + "小时" + min + "分" + s + "秒" + ms +
		// "毫秒";
	}

	public static int getDate4int(Date date) {
		return Integer.parseInt(date2Str(date, "yyyyMMdd"));
	}

	public static SimpleDateFormat strFormater = new SimpleDateFormat("yyyyMMdd");

	public static int getDate4int(int i) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DAY_OF_MONTH, i);
		return Integer.parseInt(strFormater.format(lastDate.getTime()));
	}

	public static String getDate4String(int i) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DAY_OF_MONTH, i);
		return strFormater.format(lastDate.getTime());
	}

	public static String getDate4String(int i, String format) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DAY_OF_MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(lastDate.getTime());
	}

	/**
	 * 计算两个日期之间的天数
	 *
	 * @param fromDate
	 *            开始日
	 * @param toDate
	 *            结束日
	 * @param blnAbs
	 *            是否取绝对值
	 * @return 天数
	 */
	public static int calcDays(Date fromDate, Date toDate, boolean blnAbs) {

		long miliSeconds1 = fromDate.getTime();
		long miliSeconds2 = toDate.getTime();

		if (fromDate.compareTo(toDate) > 0 && blnAbs == false) {
			// 不足2天算1天
			return (int) ((miliSeconds2 - miliSeconds1) / 86400000);
		}

		// 超过1天算2天
		return (int) (Math.abs(miliSeconds2 - miliSeconds1 - 1) / 86400000) + 1;
	}

	/**
	 * 计算两个日期之间的天数 （过一个0点算一天）
	 *
	 * @param fromDate
	 *            开始日期
	 * @param toDate
	 *            结束日期
	 * @return 天数
	 */
	public static int calcDays(Date fromDate, Date toDate) {
		long miliSeconds1 = getFirstTimeOfDay(fromDate).getTime();
		long miliSeconds2 = getFirstTimeOfDay(toDate).getTime();

		return (int) ((miliSeconds2 - miliSeconds1) / 86400000);

	}

	/**
	 * 计算两个日期之间的年数
	 *
	 * @param fromDate
	 *            开始日期
	 * @param toDate
	 *            结束日期
	 * @return 年数
	 */
	public static int calcYears(Date fromDate, Date toDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		int years = 0;
		Date temp = null;
		if (fromDate.before(toDate)) {
			temp = addYearS(fromDate, 1);
			while (!temp.after(toDate)) {
				temp = addYearS(temp, 1);
				years++;
			}
		} else {
			temp = addYearS(fromDate, -1);
			while (!temp.before(toDate)) {
				temp = addYearS(temp, -1);
				years--;
			}
		}

		return years;

	}

	/**
	 * 得到本年的开始时间
	 *
	 * @param date
	 *            日期对象
	 * @return 本年开始时间
	 */
	public static Date getFirstDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 得到本月的开始时间
	 *
	 * @param date
	 *            日期对象
	 * @return 本月开始时间
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 得到本天的开始时间
	 *
	 * @param date
	 *            日期对象
	 * @return 本天的开始时间
	 */
	public static Date getFirstTimeOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 得到日期的年份
	 *
	 * @param date
	 *            日期对象
	 * @return 日期的年份
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 得到日期的月份 (1-12)
	 *
	 * @param date
	 *            日期对象
	 * @return 日期的月份
	 */
	public static int getMonthOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到日期是年中的第几周
	 *
	 * @param date
	 *            日期对象
	 * @return 年中的第几周
	 */
	public static int getWeekOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到日期是一年中的第几天 (1-366)
	 *
	 * @param date
	 *            日期对象
	 * @return 年中的第几天
	 */
	public static int getDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 得到日期是一月中的第几天 (1-31)
	 *
	 * @param date
	 *            日期对象
	 * @return 月中的第几天
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到日期是一周中的第几天(星期日开始，是1)
	 *
	 * @param date
	 *            日期对象
	 * @return 周中的第几天
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到日期所在星期的周一日期
	 *
	 * @param date
	 *            日期对象
	 * @return 本周一的日期
	 */
	public static Date getMondayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

		return calendar.getTime();
	}

	/**
	 * 得到日期所在星期的周天日期(周一做开始,周天做结束模式)
	 *
	 * @param date
	 *            日期对象
	 * @return 本周天的日期
	 */
	public static Date getSundayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);

		return calendar.getTime();
	}

	/**
	 * 得到时间是一天中的哪个小时
	 *
	 * @param date
	 *            日期对象
	 * @return 天中的哪个小时
	 */
	public static int getHourOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到时间是多少分钟
	 *
	 * @param date
	 *            日期对象
	 * @return 天中的哪个小时
	 */
	public static int getMinuteOfHour(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 设置小时
	 *
	 * @param date
	 *            日期对象
	 * @return 天中的哪个小时
	 */
	public static Date setHourOfDay(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, hour);

		return calendar.getTime();
	}

	/**
	 * 设置分钟
	 *
	 * @param date
	 *            日期对象
	 * @return 天中的哪个小时
	 */
	public static Date setMinuteOfHour(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.set(Calendar.MINUTE, minute);

		return calendar.getTime();
	}

	/**
	 * 根据输入的毫秒数，得到 "星期几",如“星期二”
	 *
	 * @param date
	 *            日期对象
	 * @return 周中的第几天
	 */
	public static String getWeekDay(Date date) {
		SimpleDateFormat weekFormatter = new SimpleDateFormat("E");

		return weekFormatter.format(date);
	}

	/**
	 * 根据数字得到对应的星期名字
	 *
	 * @param i
	 *            数字
	 * @return 对应的星期名字
	 */
	public static String getWeekName(int i) {
		switch (i) {
			case 1:
				return "星期日";
			case 2:
				return "星期一";
			case 3:
				return "星期二";
			case 4:
				return "星期三";
			case 5:
				return "星期四";
			case 6:
				return "星期五";
			case 7:
				return "星期六";

			default:
				return "无效数字";
		}
	}

	/**
	 * 根据数字得到对应的时间段名字
	 *
	 * @param i
	 *            数字
	 * @return 对应的时间段名字
	 */
	public static String getHourInterzone(int i) {
		switch (i) {
			case 0:
				return "00:00-01:00";
			case 1:
				return "01:00-02:00";
			case 2:
				return "02:00-03:00";
			case 3:
				return "03:00-04:00";
			case 4:
				return "04:00-05:00";
			case 5:
				return "05:00-06:00";
			case 6:
				return "06:00-07:00";
			case 7:
				return "07:00-08:00";
			case 8:
				return "08:00-09:00";
			case 9:
				return "09:00-10:00";
			case 10:
				return "10:00-11:00";
			case 11:
				return "11:00-12:00";
			case 12:
				return "12:00-13:00";
			case 13:
				return "13:00-14:00";
			case 14:
				return "14:00-15:00";
			case 15:
				return "15:00-16:00";
			case 16:
				return "16:00-17:00";
			case 17:
				return "17:00-18:00";
			case 18:
				return "18:00-19:00";
			case 19:
				return "19:00-20:00";
			case 20:
				return "20:00-21:00";
			case 21:
				return "21:00-22:00";
			case 22:
				return "22:00-23:00";
			case 23:
				return "23:00-24:00";

			default:
				return "无效数字";
		}
	}

	public static Date getBeijingDate() throws Exception {
		URL url=new URL("http://www.baidu.com");//取得资源对象
		URLConnection uc=url.openConnection();//生成连接对象
		uc.connect(); //发出连接
		long ld=uc.getDate(); //取得网站日期时间
		return new Date(ld); //转换为标准时间对象
	}

	public static void main(String[] args) {

		int today = TimeUtil.getDate4int(0);
		int yestody = TimeUtil.getDate4int(-1);
		System.out.println(today);
		System.out.println(yestody);
	}
}
