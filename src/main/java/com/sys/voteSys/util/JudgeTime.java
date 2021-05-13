package com.sys.voteSys.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Zlei
 * @date 2021/5/5  18:32
 */
public class JudgeTime {

    public static boolean judge1(Timestamp beginTime, Timestamp endTime, Timestamp currentTime){
        if (currentTime.getTime ()==beginTime.getTime ()||currentTime.getTime ()==endTime.getTime ()){
            return true;
        }
        if (beginTime.getTime ()<currentTime.getTime ()&&currentTime.getTime ()<endTime.getTime ()){
            return true;
        }
        return false;
    }

    public static boolean judge( Date beginTime, Date endTime,Date nowTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }



}
