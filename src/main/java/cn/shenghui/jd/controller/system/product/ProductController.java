package cn.shenghui.jd.controller.system.product;

import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.service.system.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Controller
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    /**
     * 返回商品列表
     * @return 商品列表页
     */
    @RequestMapping("/listProduct")
    public ModelAndView listProduct() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/product/product");
        return mv;
    }
}
