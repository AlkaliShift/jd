package cn.shenghui.jd.controller.system.category;

import cn.shenghui.jd.dao.system.category.model.Category;
import cn.shenghui.jd.dao.system.product.model.Product;
import cn.shenghui.jd.resthttp.system.category.response.CategoryBasicResponse;
import cn.shenghui.jd.resthttp.system.category.response.CategoryResponse;
import cn.shenghui.jd.service.system.category.CategoryService;
import cn.shenghui.jd.service.system.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:26
 */
@Controller
@Api(value = "Category")
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;
    private ProductService productService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 商品种类列表页
     *
     * @return 页面
     */
    @RequestMapping("")
    public ModelAndView categoryPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/category/category");
        return mv;
    }

    /**
     * 增加商品种类页
     *
     * @return 页面
     */
    @RequestMapping("/addCategory")
    public ModelAndView addCategoryPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/category/addCategory");
        return mv;
    }

    /**
     * 更新商品种类页
     *
     * @param categoryId 商品种类ID
     * @return 页面
     */
    @RequestMapping("/updateCategory")
    public ModelAndView updateCategoryPage(@RequestParam(name = "categoryId") String categoryId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/category/updateCategory");
        mv.addObject("category", categoryService.getCategoryById(categoryId));
        return mv;
    }

    /**
     * 模糊查询商品种类信息，若搜索内容为空，则返回所有商品种类
     *
     * @param content 搜索内容
     * @return 商品种类列表和状态码：1
     */
    @ApiOperation(value = "获取商品种类列表", notes = "状态码1:查询成功")
    @RequestMapping(value = "/list")
    @ResponseBody
    public CategoryResponse getCategoryList(@RequestParam(name = "content") String content) {
        CategoryResponse response = new CategoryResponse();
        response.setCategories(categoryService.getCategoryList(content));
        response.setStatusCode(1);
        return response;
    }

    /**
     * 增加单个商品种类
     *
     * @param category 商品种类
     * @return 状态码：1
     */
    @ApiOperation(value = "增加单个商品种类", notes = "状态码1:创建成功")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public CategoryBasicResponse addCategory(@RequestBody Category category) {
        CategoryBasicResponse response = new CategoryBasicResponse();
        categoryService.addCategory(category);
        response.setStatusCode(1);
        return response;
    }

    /**
     * 移除单个商品种类
     *
     * @param categoryId 商品种类ID
     * @return 状态码：1
     */
    @ApiOperation(value = "删除单个商品种类", notes = "状态码1:删除成功")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public CategoryBasicResponse removeCategory(@RequestParam(name = "categoryId") String categoryId) {
        CategoryBasicResponse response = new CategoryBasicResponse();
        if (productService.ifExistProduct(categoryId)) {
            response.setStatusInfo(0, "请先删除该商品种类下的所有商品。");
        } else {
            categoryService.removeCategory(categoryId);
            response.setStatusCode(1);
        }
        return response;
    }

    /**
     * 修改单个商品种类信息
     *
     * @param category 商品种类信息
     * @return 状态码：1
     */
    @ApiOperation(value = "修改单个商品种类信息", notes = "状态码1:修改成功")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public CategoryBasicResponse updateCategory(@RequestBody Category category) {
        CategoryBasicResponse response = new CategoryBasicResponse();
        categoryService.updateCategory(category);
        response.setStatusCode(1);
        return response;
    }
}
