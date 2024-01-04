package caner.blog.common.util;

import org.springframework.stereotype.Component;

@Component
public class TruncateTextUtil {

    public String truncateText(String text, int maxLength) {
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "... Devamını görmek için tıklayınız";
        }
        return text;
    }

}
