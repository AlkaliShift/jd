package cn.shenghui.jd.framework.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 10:32
 */
@Component
@Aspect
@Order(1)
@Slf4j
public class RestControllerLogAdvice {

    @Around("@within(org.springframework.web.bind.annotation.RestController)" +
            " && (@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8)).toEpochMilli();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String uri = servletRequestAttributes.getRequest().getRequestURI();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            long end = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8)).toEpochMilli();
            long cost = end - start;
            log.debug(methodName + ",URI:" + uri + ":执行时间:" + cost + "毫秒");
        }
    }
}
