package cn.shenghui.jd.controller.system.product;

import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.restHttp.system.product.response.ProductBasicResponse;
import cn.shenghui.jd.restHttp.system.product.response.ProductResponse;
import cn.shenghui.jd.service.system.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Controller
@Api(value = "Product")
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 商品列表页
     *
     * @return 页面
     */
    @RequestMapping("")
    public ModelAndView productPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/product/product");
        return mv;
    }

    /**
     * 增加商品页
     *
     * @return 页面
     */
    @RequestMapping("/addProduct")
    public ModelAndView addProductPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/product/addProduct");
        return mv;
    }

    /**
     * 返回商品列表
     *
     * @return 商品列表页
     */
    @RequestMapping("/getProductList")
    public ModelAndView listProduct() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/product/product");
        return mv;
    }

    /**
     * 根据输入的商品名称模糊搜索商品
     *
     * @param productName 输入的商品名称
     * @return 匹配的商品列表和状态码：1
     */
    @ApiOperation(value = "模糊搜索商品", notes = "状态码1:搜索成功")
    @RequestMapping(value = "/fuzzySearch")
    @ResponseBody
    public ProductResponse fuzzySearch(String productName) {
        ProductResponse response = new ProductResponse();
        response.setProducts(productService.fuzzySearch(productName));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 增加单个商品
     *
     * @param product 商品信息
     * @return 状态码：0及错误信息/状态码：1
     */
    @ApiOperation(value = "增加单个商品", notes = "状态码1:创建成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ProductBasicResponse addProduct(Product product) {
        ProductBasicResponse response = new ProductBasicResponse();
        if (!checkIfNegative(product)) {
            response.setStatusInfo(0, "输入的数值不能小于0");
        } else {
            productService.addProduct(product);
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 修改单个商品
     *
     * @param product 商品信息
     * @return 状态码：0及错误信息/状态码：1
     */
    @ApiOperation(value = "修改单个商品", notes = "状态码1:修改成功")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ProductBasicResponse updateProduct(Product product) {
        ProductBasicResponse response = new ProductBasicResponse();
        if (!checkIfNegative(product)) {
            response.setStatusInfo(0, "输入的数值不能小于0");
        } else {
            productService.updateProduct(product);
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 修改单个商品上下架信息
     *
     * @param productId     商品ID
     * @param productStatus 商品上下架状态
     * @return 状态码：1
     */
    @ApiOperation(value = "修改单个商品上下架信息", notes = "状态码1:修改成功")
    @RequestMapping(value = "/setProductStatus")
    @ResponseBody
    public ProductBasicResponse setProductStatus(@RequestParam("productId") String productId,
                                                 @RequestParam("productStatus") char productStatus) {
        ProductBasicResponse response = new ProductBasicResponse();
        productService.setProductStatus(productId, productStatus);
        response.setStatusCode(1);
        return response;
    }

    /**
     * 判断输入的商品相关数值是否小于0，若小于0为false，反之为true
     *
     * @param product 商品信息
     * @return true/false
     */
    private boolean checkIfNegative(Product product) {
        int availableNum = product.getAvailableNum();
        int frozenNum = product.getFrozenNum();
        BigDecimal unitPrice = product.getUnitPrice();
        BigDecimal initial = new BigDecimal(0);
        return availableNum >= 0 && frozenNum >= 0 && unitPrice.compareTo(initial) != -1;
    }
}
