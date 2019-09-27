package cn.shenghui.jd.controller.system.product;

import cn.shenghui.jd.constants.system.product.ProductConstants;
import cn.shenghui.jd.dao.system.product.dto.ProductDetails;
import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.resthttp.system.product.response.ProductBasicResponse;
import cn.shenghui.jd.resthttp.system.product.response.ProductResponse;
import cn.shenghui.jd.service.system.product.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:27
 */
@Controller
@Api(value = "Product")
@RequestMapping("/product")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProductController {

    @Autowired
    ProductService productService;

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
    public ModelAndView listProductPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/product/product");
        return mv;
    }

    /**
     * 更新商品页面
     *
     * @param productId 商品ID
     * @return 页面
     */
    @RequestMapping("/updateProduct")
    public ModelAndView updateProductPage(@RequestParam(name = "productId") String productId) {
        ModelAndView mv = new ModelAndView();
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        List<Product> products = productService.getProductsByIds(productIds);
        if (!CollectionUtils.isEmpty(products)) {
            mv.addObject("product", products.get(0));
        }
        mv.setViewName("system/product/updateProduct");
        return mv;
    }

    /**
     * 上传图片页
     *
     * @param productId 商品ID
     * @return 页面
     */
    @RequestMapping("/uploadImage")
    public ModelAndView uploadPage(@RequestParam(name = "productId") String productId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("productId", productId);
        mv.setViewName("system/product/uploadImage");
        return mv;
    }

    /**
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表
     *
     * @param content 搜索内容
     * @param page    页数
     * @param limit   每页条数
     * @return 匹配的商品列表和状态码：1
     */
    @ApiOperation(value = "获取商品列表", notes = "状态码1:搜索成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public ProductResponse getProductList(@RequestParam("content") String content,
                                          @RequestParam(name = "page") int page,
                                          @RequestParam(name = "limit") int limit) {
        ProductResponse response = new ProductResponse();
        PageInfo<ProductDetails> pages = productService.getProductList(content, page, limit);
        response.setProducts(pages.getList());
        response.setTotal(pages.getTotal());
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
    public ProductBasicResponse addProduct(@RequestBody Product product) {
        ProductBasicResponse response = new ProductBasicResponse();
        if (checkIfNegative(product)) {
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
    public ProductBasicResponse updateProduct(@RequestBody Product product) {
        ProductBasicResponse response = new ProductBasicResponse();
        if (checkIfNegative(product)) {
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
     * @param productIds    商品ID集
     * @param productStatus 商品上下架状态
     * @return 状态码：1
     */
    @ApiOperation(value = "修改单个商品上下架信息", notes = "状态码1:修改成功")
    @RequestMapping(value = "/setProductStatus", method = RequestMethod.POST)
    @ResponseBody
    public ProductBasicResponse setProductStatus(@RequestBody List<String> productIds,
                                                 @RequestParam("productStatus") String productStatus) {
        ProductBasicResponse response = new ProductBasicResponse();
        productService.setProductStatus(productIds, productStatus);
        response.setStatusCode(1);
        return response;
    }

    /**
     * 根据商品ID删除商品
     *
     * @param productId 商品ID
     * @return 状态码：1
     */
    @ApiOperation(value = "根据商品ID删除商品", notes = "状态码1:修改成功")
    @RequestMapping(value = "/removeProduct", method = RequestMethod.POST)
    @ResponseBody
    public ProductBasicResponse removeProduct(@RequestParam(name = "productId") String productId) {
        ProductBasicResponse response = new ProductBasicResponse();
        productService.removeProduct(productId);
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
        if (unitPrice == null) {
            unitPrice = initial;
            product.setUnitPrice(initial);
        }
        return availableNum < 0 || frozenNum < 0 || initial.compareTo(unitPrice) >= 1;
    }

    /**
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表（用户页）
     *
     * @param content 搜索内容
     * @param page    页数
     * @param limit   每页条数
     * @return 匹配的商品列表和状态码：1
     */
    @ApiOperation(value = "获取商品列表", notes = "状态码1:搜索成功")
    @RequestMapping(value = "/listUser")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ProductResponse getProductListUser(@RequestParam("content") String content,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "limit") int limit) {
        ProductResponse response = new ProductResponse();
        PageInfo<ProductDetails> pages = productService.getProductListUser(content, page, limit);
        response.setProducts(pages.getList());
        response.setTotal(pages.getTotal());
        response.setStatusCode(1);
        return response;
    }

    /**
     * 根据商品ID查找商品信息
     *
     * @param productId 商品ID
     * @return 带商品信息的页面页面
     */
    @RequestMapping("/productCart")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ModelAndView productCartPage(@RequestParam(name = "productId") String productId) {
        ModelAndView mv = new ModelAndView();
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        List<ProductDetails> products = productService.getProductsByIdsUser(productIds);

        if (!ObjectUtils.isEmpty(products)) {
            mv.addObject("product", products.get(0));
        }
        mv.setViewName("system/product/addToCart");
        return mv;
    }

    /**
     * 上传图片
     *
     * @param file      图片文件
     * @param productId 商品ID
     * @return 状态码：1
     */
    @ApiOperation(value = "上传图片", notes = "状态码1:上传成功")
    @RequestMapping(value = "/uploadPath", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ProductBasicResponse uploadImage(@RequestParam(name = "file") MultipartFile file,
                                            @RequestParam(name = "id") String productId) {
        ProductBasicResponse response = new ProductBasicResponse();
        if (file.isEmpty()) {
            response.setStatusInfo(0, "请选择一张图片");
            return response;
        } else {
            String filename = productId + "_" + file.getOriginalFilename();
            String path = ProductConstants.IMAGE_ROOT_PATH + "/" + filename;
            File dest = new File(path);
            InputStream in = null;
            FileOutputStream out = null;

            //保存文件
            try {
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                if (!dest.exists()) {
                    dest.createNewFile();
                }
                in = file.getInputStream();
                out = new FileOutputStream(dest);
                byte[] b = new byte[1024];
                int length;
                while ((length = in.read(b)) > 0) {
                    out.write(b, 0, length);
                }
                productService.updateImagePath(productId, path);
                response.setStatusInfo(1, "上传成功");
                out.flush();
                return response;
            } catch (Exception e) {
                response.setStatusInfo(0, "上传失败：" + e.getMessage());
                return response;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 下载图片
     *
     * @param productId           商品ID
     * @param httpServletResponse http返回值
     */
    @ApiOperation(value = "下载图片", notes = "状态码1:上传成功")
    @RequestMapping(value = "/downloadImage")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public void downloadImage(@RequestParam(name = "productId") String productId,
                              HttpServletResponse httpServletResponse) {
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        List<Product> list = productService.getProductsByIds(productIds);
        String path = list.get(0).getPath();
        File file = new File(path);
        InputStream in = null;
        OutputStream out = null;
        if (file.exists()) {
            try {
                in = new FileInputStream(file);
                out = httpServletResponse.getOutputStream();
                int length;
                byte[] b = new byte[1024];
                while ((length = in.read(b)) > 0) {
                    out.write(b, 0, length);
                }
                httpServletResponse.setContentType("image/jpg");
                out.flush();
            } catch (Exception e) {
                e.getMessage();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        ioe.getMessage();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ioe) {
                        ioe.getMessage();
                    }
                }
            }
        }
    }
}
