package cn.shenghui.jd.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 11:01
 */
public class CurrentUserUtils {

    private CurrentUserUtils(){

    }

    public static String getUserName(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public static Object[] getUserRole(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAuthorities().toArray();
    }
}