package cn.shenghui.jd.framework.config;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 10:30
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, Object> handleValidate(MethodArgumentNotValidException e) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("statusCode", 0);
        map.put("msg", e.getBindingResult().getFieldError().getDefaultMessage());
        return map;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Map<String, Object> handle(RuntimeException e) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("statusCode", 0);
        map.put("msg", e.getMessage());
        return map;
    }
}