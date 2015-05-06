package com.piaojin.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final int ONE = 1;
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String LOG_DATE_PATTERN = "dd/MMM/yyyy";
    public static final String HOUR_PATTERN = "yyyy-MM-dd-HH";
    public static final String MIN_PATTERN = "yyyy-MM-dd-HH-mm";
    public static final String SIMPLE_SECOND_PATTERN = "yyyyMMddHHmmss";
    public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String TIME_MIN_PATTERN = "HH:mm";
    public static final Locale US = Locale.US;
    public static final String CHART_DATE_PATTERN = "%Y-%m-%d";
    public static final String CHART_HOUR_PATTERN = "%Y-%m-%d-%H";
    public static final String RFC3339 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    public static long yesterdayBeginTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String CurrentTime(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(new Date());
    }

    public static String CurrentTime2(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	return sdf.format(new Date());
    }

    public static Date dayBeforeBeginTime(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -num);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date dayBeforeEndTime(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -num);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static long todayBeginTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Date hourEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date hourStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isSameDay(Long start, Long end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTimeInMillis(end);
        return date == calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameDay(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int date = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(end);
        return date == calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameByDatePattern(Date start, Date end) {
        String formatDate = formatDate(start, DATE_PATTERN);
        String endDate = formatDate(end, DATE_PATTERN);
        return formatDate.equals(endDate);
    }

    public static boolean isSameWeek(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int date = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(end);
        return date == calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isSameMonth(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int date = calendar.get(Calendar.MONTH);
        calendar.setTime(end);
        return date == calendar.get(Calendar.MONTH);
    }

    public static long lastHour() {
        return lastHours(1);
    }

    public static long last2Hours() {
        return lastHours(2);
    }

    public static long lastHours(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, (-1) * n);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long nextDays(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, n);
        return calendar.getTimeInMillis();
    }

    public static Date nextHours(int n, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, n);
        return calendar.getTime();
    }

    public static Date nextMinute(int n, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, n);
        return calendar.getTime();
    }

    public static Date nextDate(int n, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    public static long lastHalfHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Date lastMonth(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -n);
        return new Date(calendar.getTimeInMillis());
    }

    public static String formatDate(Date time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, US);
        return format.format(time);
    }

    public static String dayPattern(long time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        return format.format(new Date(time));
    }

    public static String logDayPattern(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(LOG_DATE_PATTERN, US);
        return format.format(date);
    }

    public static String hourPattern(long time) {
        SimpleDateFormat format = new SimpleDateFormat(HOUR_PATTERN);
        return format.format(new Date(time));
    }

    public static String minutePattern(long time) {
        SimpleDateFormat format = new SimpleDateFormat(MIN_PATTERN);
        return format.format(new Date(time));
    }

    public static String simpleSecondPattern(long time) {
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_SECOND_PATTERN);
        return format.format(new Date(time));
    }

    public static String fiveMinutePattern(long time) {
        time = (time / (300000)) * 300000;
        SimpleDateFormat format = new SimpleDateFormat(MIN_PATTERN);
        return format.format(new Date(time));
    }

    public static String fiveMinutePattern(long time, String pattern) {
        time = (time / (300000)) * 300000;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    public static Date parseDate(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time);
        } catch (ParseException e) {
        }
        return null;
    }

    public static Date parseDate(String time, String pattern, Locale local) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, local);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] last2HourPattern() {
        return new String[]{hourPattern(System.currentTimeMillis()), hourPattern(lastHour()), hourPattern(last2Hours())};
    }

    public static String[] last1HourPattern() {
        return new String[]{hourPattern(System.currentTimeMillis()), hourPattern(lastHour())};
    }

    public static Date minutesBefore(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -num);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * ȡָ�����ڵĿ�ʼ����ʱ��
     *
     * @param date ���������
     * @return �������ڵĿ�ʼʱ��
     */
    @SuppressWarnings("deprecation")
    public static Date getStartDate(Date date) {
        try {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_MONTH, ONE);
        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static long dateDiff(Date startTime, Date endTime, int type) {
        long result = 0;
        long nd = 1000 * 24 * 60 * 60;// һ��ĺ�����
        long nh = 1000 * 60 * 60;// һСʱ�ĺ�����
        long nm = 1000 * 60;// һ���ӵĺ�����
        long ns = 1000;// һ���ӵĺ�����
        long diff;
        try {
            // �������ʱ��ĺ���ʱ�����
            diff = endTime.getTime() - startTime.getTime();
            switch (type) {
                case Calendar.DATE:
                    result = diff / nd;// ����������
                    break;
                case Calendar.HOUR:
                    result = diff % nd / nh;// ��������Сʱ
                    break;
                case Calendar.MINUTE:
                    result = diff % nd % nh / nm;// �������ٷ���
                    break;
                case Calendar.SECOND:
                    result = diff % nd % nh % nm / ns;// ����������
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String toRFC3339(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        return sdf.format(date).replaceAll("(\\d\\d)(\\d\\d)$", "$1:$2");
    }

    public static Date fromRFC3339(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        date = date.substring(0, date.lastIndexOf(':')) + date.substring(date.lastIndexOf(":") + 1);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }

    public static int getDayOfWeek(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week - 1;
    }

    public static int getWeekOfYear(Date date) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        return cl.get(Calendar.WEEK_OF_YEAR);
    }

    public static void main(String[] args) {
        Date d = new Date();
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.nextDate(i, d));
            int week = (calendar.get(Calendar.DAY_OF_WEEK));
            System.out.println(week);
        }
    }
}
