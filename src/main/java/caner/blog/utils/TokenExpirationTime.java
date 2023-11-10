package caner.blog.utils;

import java.util.Calendar;
import java.util.Date;

public class TokenExpirationTime {

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, Constant.EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }

}
