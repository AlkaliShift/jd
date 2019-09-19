package cn.shenghui.jd.controller.system.user;

import cn.shenghui.jd.constants.system.user.UserConstants;
import cn.shenghui.jd.utils.CurrentUserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:46
 * index
 */
@Controller
public class IndexController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        String temp;
        Boolean admin = false;
        for (Object role : CurrentUserUtils.getUserRole()){
            temp = role.toString();
            if (UserConstants.ROLE_ADMIN.equals(temp)){
                admin = true;
            }
        }
        if (admin){
            mv.setViewName("index");
        }else{
            mv.setViewName("system/product/productUser");
        }
        return mv;
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home");
        mv.addObject("sourceAmount", 123);
        mv.addObject("typeAmount", 321);
        mv.addObject("infoAmount", 4556);
        mv.addObject("sendAmount", 112);
        return mv;
    }
}
