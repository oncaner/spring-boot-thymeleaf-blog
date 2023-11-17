package caner.blog.common.util;

import java.util.Calendar;
import java.util.Date;

import static caner.blog.common.Constant.EXPIRATION_TIME;

public class TokenExpirationTime {

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }

}
