package cn.shenghui.jd.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/10/8 17:04
 */
public class ApplicationContextUtil {

    private static ApplicationContext ctx;

    public static void setApplicationContext(ApplicationContext applicationContext){
        ctx = applicationContext;
    }

    public static ApplicationContext getCtx(){
        return ctx;
    }
}
