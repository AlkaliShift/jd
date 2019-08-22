package cn.shenghui.jd.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 11:01
 */
public class CurrentUserUtils {

    private CurrentUserUtils() {

    }

    public static String getUserName() {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getName();
    }
}