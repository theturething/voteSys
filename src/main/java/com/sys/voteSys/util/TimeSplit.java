package com.sys.voteSys.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Zlei
 * @date 2021/5/12  16:27
 */
public class TimeSplit {

    public static String date2str(Date ts) {
        DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

            if (null != ts) {
                return sdf.format(ts);
            }
        return null;
    }

    public static Date str2date(String str){
        DateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date date=null;

        try {
            date = (Date) sdf.parse (str);
        } catch (ParseException e) {
            e.printStackTrace ( );
        }

        return date;
    }

}
