package cn.shenghui.jd;

import cn.shenghui.jd.utils.ApplicationContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 10:06
 * 主程序入口
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.shenghui.jd.dao.system")
public class JdApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(JdApplication.class, args);
        ApplicationContextUtil.setApplicationContext(ctx);
    }
}
