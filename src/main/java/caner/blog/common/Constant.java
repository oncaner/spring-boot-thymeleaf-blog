package caner.blog.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Constant {

    public static final int EXPIRATION_TIME = 10;
    public static final String VALID = "valid";
    public static final String EXPIRED = "expired";
    public static final String INVALID = "invalid";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
}
