package com.lepu.algorithm.ecg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.lepu.algorithm.R;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description: <DateUtil><br>
 * Author: mxdl<br>
 * Date: 2018/6/11<br>
 * Version: V1.0.0<br>
 * Update: <br>
 */
public class DateUtil {

    public static final String DATE_ALL_ALL = "yyyy-MM-dd H:mm:ss";
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String YEAR_MONTH = "yyyy-MM";
    public static final String DATE_ALL = "yyyy-MM-dd H:mm";
    public static final String DATE_ALL_12 = "yyyy-MM-dd h:mm";
    public static final String DATE_TIME = "MM-dd H:mm";
    public static final String DATE_HOUR_MINUTE = "H:mm";
    public static final String DATE_HOUR_MINUTE_12 = "h:mm";
    public static final String DATE_HOUR_MINUTE_SEC = "H:mm:ss";
    public static final String DATE_HOUR_MINUTE_SEC_12 = "h:mm:ss";
    public static final long WEEK_MILLIS = 604800000L;
    public static final long MONTH_MILLIS = 2592000000L;
    public static final long DAY_MILLIS = 86400000L;
    public static String mCurrentDateFormat;
    private static final long timeZone = TimeZone.getDefault().getRawOffset();
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    public enum FormatType {
        yyyy, yyyyMM, yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss, MMdd, HHmm,MM,dd,MMddHHmm,ddMMyyyy,HHmmss;
    }
    /**
     * 格式化时间字符串
     */
    public static String formatDate(String time, String formatStr) {
        Date setdate = parseTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(setdate);
    }

    public static String formatDate(String time, FormatType type) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Date date = parseTime(time);
        return formatDate(date, type);
    }
    public static String formatDate(String time, FormatType fromtype,FormatType totype) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Date date = parseTime(time,fromtype);
        return formatDate(date, totype);
    }
    public static String formatDate(Date time, FormatType type) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = getSimpleDateFormat(type);
        return sdf.format(time);
    }

    private static SimpleDateFormat getSimpleDateFormat(FormatType type) {
        SimpleDateFormat sdf;
        switch (type) {
            case yyyy:
                sdf = new SimpleDateFormat("yyyy");
                break;
            case yyyyMM:
                sdf = new SimpleDateFormat("yyyy-MM");
                break;
            case yyyyMMdd:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case yyyyMMddHHmm:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            case yyyyMMddHHmmss:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case MMdd:
                sdf = new SimpleDateFormat("MM-dd");
                break;
            case HHmm:
                sdf = new SimpleDateFormat("HH:mm");
                break;
            case MM:
                sdf = new SimpleDateFormat("MM");
                break;
            case dd:
                sdf = new SimpleDateFormat("dd");
                break;
            case MMddHHmm:
                sdf = new SimpleDateFormat("MM-dd HH:mm");
                break;
            case ddMMyyyy:
                sdf=new SimpleDateFormat("dd/MM/yyyy");
                break;
            case HHmmss:
                sdf=new SimpleDateFormat("HH:mm:ss");
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }
        return sdf;
    }
    /**
     * 将时间字符串转化为date
     */
    public static Date parseTime(String dateStr, String formatStr) {
        Date date = null;
        try {
            DateFormat sdf = new SimpleDateFormat(formatStr);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串转换成date
     */
    public static Date parseTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 将字符串转换成date
     */
    public static Date parseTime(String time, FormatType type) {
        Date date = null;
        SimpleDateFormat sdf = getSimpleDateFormat(type);
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 增/减 offset 后的日期
     */
    public static Date addAndSubtractDate(int offset, Date date, int unit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (unit == Calendar.MONTH) {
            calendar.set(Calendar.DATE, 1);
        }
        calendar.set(unit, calendar.get(unit) + offset);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 比较日期大小
     */
    public static int compareDate(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        if (time1 == time2) {
            return 0;
        } else if (time1 > time2) {
            return -1;
        } else {
            return 1;
        }

    }

    /**
     * 根据日期算周几
     */
    public static String whatDay(Date date) {
        Calendar calendar = Calendar.getInstance();// 获得一个日历
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day);// 设置当前时间,月份是从0月开始计算
        int number = calendar.get(Calendar.DAY_OF_WEEK);// 周表示1-7，是从周日开始，
        String[] str = {"", "周日", "周一", "周二", "周三", "周四", "周五", "周六",};
        return str[number];
    }

    public static String formatSecondToHourMinuteSecond(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return h + "时" + d + "分" + s + "秒";
    }

    public static String formatSecondToHourMinute(int duration) {
        if (duration < 60) {
            return duration + "秒";
        } else if (duration < 60 * 60) {
            return Math.round((float) duration / 60) + "分钟";
        } else {
            int second = duration % (60 * 60);
            if (second == 0) {
                return (duration / (60 * 60)) + "小时";
            } else {

                int round = Math.round((float) second / 60);
                if (round == 0) {
                    return (duration / (60 * 60)) + "小时";
                } else {
                    if (round == 60) {
                        return ((duration / (60 * 60)) + 1) + "小时";
                    } else {
                        return (duration / (60 * 60)) + "小时" + round + "分钟";
                    }
                }
            }
        }
    }

    /**
     * 说明 小于1分钟：”刚刚“ 小于1小时：”X分钟前“ 小于一天：”X小时前“ 小于一月：”X天前“ 小于一年：6-23 大于一年：2015-6-23
     *
     * @param dateStr
     * @return
     */
    public static String formatTimeToDay(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat sdf2 = new SimpleDateFormat("MM-dd");
        DateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);

            int minute = (int) ((System.currentTimeMillis() - date.getTime()) / 1000 / 60);
            if (minute <= 0) {
                return "刚刚";

            } else if (minute / 60 == 0) {
                return minute + "分钟前";

            } else if (minute / (60 * 24) == 0) {
                return minute / 60 + "小时前";

            } else if (minute / (60 * 24 * 30) == 0) {
                return minute / (60 * 24) + "天前";
            } else if (minute / (60 * 24 * 30 * 12) == 0) {
                return sdf2.format(date);
            } else {
                return sdf3.format(date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 获取几个小时以后的时间戳
     *
     * @param hour
     * @return
     */
    public static String getLaterTimeByHour(int hour) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hour);
        // now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now.getTime());
    }

    /**
     * 获取几天以后的时间戳
     *
     * @param day
     * @return
     */
    public static String getLaterTimeByDay(int day) {
        return getLaterTimeByHour(day * 24);
    }

    /**
     * 获取给定时间以后几天的时间戳
     * @param date
     * @param day
     * @return
     */
    public static String getLaterTimeByDay(String date,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseTime(date, FormatType.yyyyMMdd));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + day * 24);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
    /**
     * 获取当前时间的位置：一天24小时以半小时为单位划分为48个单元格
     * @return
     */
    public static int getCurrTimePosition() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return (int)Math.ceil((double)(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) / 30);
    }

    public static String getPeroidText(Context context,long timeStamp) {
        String am = context.getString(R.string.app_am);
        String pm = context.getString(R.string.app_pm);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.get(Calendar.HOUR_OF_DAY)>=12?pm:am;
        /*String am_pm = Calendar.getInstance().getTime().getHours() >= 12 ? pm : am;
        return am_pm;*/
    }

    public static String stringFromDate(Date date, String formatString) {
        DateFormat df = new SimpleDateFormat(formatString);
        return df.format(date);
    }

    public static Date dateFromString(String dateStr, String formatString) {
        DateFormat df = new SimpleDateFormat(formatString);
        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
//            KLog.e(Log.getStackTraceString(e));
        }
        return date;
    }

    public static Date addDateOfDay(Date date, int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        Date d = calendar.getTime();

        return d;
    }

    public static Date addDateOfYear(Date date, int addYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, addYear);
        Date d = calendar.getTime();

        return d;
    }

    public static Date addDateOfMonth(Date date, int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonth);
        Date d = calendar.getTime();

        return d;
    }

    public static Date addDateOfHour(Date date, int addHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, addHour);
        Date d = calendar.getTime();

        return d;
    }

    public static Date addDateOfMinute(Date date, int addMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinute);
        Date d = calendar.getTime();

        return d;
    }

    public static long getNowTimeInMillis() {
        return System.currentTimeMillis();
    }

    public static int getTadayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressLint("DefaultLocale")
    public static String getStringUseTime(long useTime) {
        int sec = 0;
        int minute = 0;
        int hour = 0;
        String timeText = "";
        sec = (int) (useTime / 1000);
        minute = sec / 60;
        hour = minute / 60;
        timeText = String.format("%02d:%02d:%02d", hour, minute % 60, sec % 60);
        return timeText;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {

        String returntime = "";
        DateFormat formart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String currentDateString = stringFromDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss");
            Date begindate = formart.parse(sdate);
            Date enddate = dateFromString(currentDateString,
                    "yyyy-MM-dd HH:mm:ss");

            long times = enddate.getTime() - begindate.getTime();// 这样得到的差值是微秒级别

            long days = times / (1000 * 60 * 60 * 24); // 换算成天数
            long hours = times / (1000 * 60 * 60);
            long minutes = times / (1000 * 60);

            if (minutes > 60) {

                if (days >= 365) {
                    returntime = String.format("%d年前", days / 365);
                } else if (days >= 30) {
                    returntime = String.format("%d月前", days / 30);
                } else if (hours >= 24) {
                    returntime = String.format("%d天前", days);
                } else {
                    returntime = String.format("%d小时前", hours);
                }
            } else {
                if (minutes <= 0) {
                    returntime = "刚刚";
                } else {
                    returntime = String.format("%d分钟前", minutes);
                }
            }
        } catch (Exception e) {

        }
        return returntime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = dateFromString(sdate, DATE_ALL);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    public static int getThisYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getWeekOfYear(long times) {
        Time mTime = new Time();
        mTime.set(times);
        return mTime.getWeekNumber();
    }

    public static long getThisWeekOfSunday() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.set(year, month, dayOfMonth + (7 - (dayOfWeek - 1)));
        return cal.getTimeInMillis();
    }

    public static long getThisWeekOfMonday() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.set(year, month, dayOfMonth - (dayOfWeek - 2));
        return cal.getTimeInMillis();
    }

    public static int longTimeToYear(long times) {
        Time mTime = new Time();
        mTime.set(times);
        return mTime.year;
    }

    public static int longTimeToMonth(long times) {
        Time mTime = new Time();
        mTime.set(times);
        return mTime.month;
    }

    public static long getLongOfFirstDayOfMonth(int beforeOfMonth) {
        Calendar localCalendar = Calendar.getInstance();
        int latestMonth = localCalendar.get(Calendar.MONTH);
        localCalendar.set(Calendar.MONTH, latestMonth - beforeOfMonth);
        int latestDay = localCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        localCalendar.set(Calendar.DATE, latestDay);
        localCalendar.set(Calendar.HOUR, 0);
        localCalendar.set(Calendar.MINUTE, 0);
        localCalendar.set(Calendar.SECOND, 0);
        localCalendar.set(Calendar.MILLISECOND, 0);
        return localCalendar.getTimeInMillis();
    }

    public static long getLongOfLatestDayOfMonth(int beforeOfMonth) {

        Calendar localCalendar = Calendar.getInstance();
        int latestMonth = localCalendar.get(Calendar.MONTH);
        localCalendar.set(Calendar.MONTH, latestMonth - beforeOfMonth);
        int latestDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        localCalendar.set(Calendar.DATE, latestDay);
        localCalendar.set(Calendar.HOUR, 0);
        localCalendar.set(Calendar.MINUTE, 0);
        localCalendar.set(Calendar.SECOND, 0);
        localCalendar.set(Calendar.MILLISECOND, 0);
        return localCalendar.getTimeInMillis();
    }

    public static long getLongTimesByString(String time, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        long tempTime = 0L;
        Date date = null;
        try {
            date = format.parse(time);
            tempTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempTime;
    }

    /**
     * 获取当月有多少天
     */
    public static int getDaysOfMonth(int year, int month) {
        int daysOfMonth = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapYear(year)) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }
                break;
            default:
                break;
        }
        return daysOfMonth;
    }

    /**
     * 判断当前年份是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }


    // 获取从参数月份开始到现在的年月份集合
    public static ArrayList<String> getToNowMonths(String startDate) {
        ArrayList<String> months = new ArrayList<String>();
        String startYearmonth = startDate.substring(0, 7);
        int startYear = Integer.valueOf(startYearmonth.substring(0, 4));
        int startMonth = Integer.valueOf(startYearmonth.substring(5, 7));

        String nowYearMonth = "2015-04";
        int nowYear = Integer.valueOf(nowYearMonth.substring(0, 4));
        int nowMonth = Integer.valueOf(nowYearMonth.substring(5, 7));

        if (startYear == nowYear && startMonth == nowMonth) {
            months.add(nowYearMonth);
        } else {
            months.add(startYearmonth);
            while (!startYearmonth.equals(nowYearMonth)) {

                if (startMonth == 12) {
                    startYear += 1;
                    startMonth = 1;
                } else {
                    startMonth += 1;
                }

                if (startMonth < 10) {
                    startYearmonth = startYear + "-0" + startMonth;
                } else {
                    startYearmonth = startYear + "-" + startMonth;
                }

                // 添加到集合
                months.add(startYearmonth);

            }
        }

        return months;
    }
    /**
     * 字符串转换成日期
     * yyyy-MM-dd'T'HH:mm:ss
     * @param str
     * @return date
     */
    public static Date StrToDateHasT(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
            date.setTime(date.getTime()+timeZone);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 时间戳 转化 bcd 数组。ymdhms  数组8个长度对应
     *
     * @param timeStamp
     * @return
     */
    public static byte[] timeToBcd(long timeStamp) {
        byte[] dataArray = new byte[8];

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        final int yearCurrent = calendar.get(Calendar.YEAR);
        final int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        final int dateCurrent = calendar.get(Calendar.DATE);
        final int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        final int minuteCurrent = calendar.get(Calendar.MINUTE);
        final int secondCurrent = calendar.get(Calendar.SECOND);

        char[] yearArray = String.valueOf(yearCurrent).toCharArray();
        //datetime
        dataArray[0] =  Byte.parseByte(String.format("%s%s",yearArray[0],yearArray[1]));
        dataArray[1] = Byte.parseByte(String.format("%s%s",yearArray[2],yearArray[3]));
        dataArray[2] = (byte) monthCurrent;
        dataArray[3] = (byte) dateCurrent;
        dataArray[4] = (byte) hourCurrent;
        dataArray[5] = (byte) minuteCurrent;
        dataArray[6] = (byte) secondCurrent;
        dataArray[7] = 0X00;//保留

        return dataArray;
    }

    /**
     * 获取网络时间
     * @return
     */
    public static Date getNetTime(){
        Date date = new Date(System.currentTimeMillis());
        URL url= null;//取得资源对象
        try {
            //url = new URL("http://www.bjtime.cn");//这个以前可以，最近连接成功后，getDate 为0
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            if(ld > 0){
                date = new Date(ld); //转换为标准时间对象
            }else{
//                KLog.d("获取网络时间失败，设置系统当前时间");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取utc time
     * @return
     */
    public static CharSequence getUTCTime() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTimeInMillis(System.currentTimeMillis() + 8 * 3600 * 1000);//+8个小时
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return android.text.format.DateFormat.format("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'sss'Z'", cal);
    }

}