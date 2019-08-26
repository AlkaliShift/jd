package cn.shenghui.jd.service.system.category;

import cn.shenghui.jd.dao.system.category.mapper.CategoryMapper;
import cn.shenghui.jd.dao.system.category.model.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
public class CategoryService {
    private CategoryMapper categoryMapper;
    private static int ID = 0;

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 查询特定商品种类或返回所有商品种类列表
     *
     * @param categoryId 商品种类ID
     * @return 商品种类列表
     */
    public List<Category> selectCategoryList(String categoryId) {
        return categoryMapper.selectCategoryList(categoryId);
    }

    /**
     * 增加一种商品种类
     *
     * @param categoryName 商品种类名称
     * @param warehouseId 仓库ID
     */
    public void addCategory(String categoryName, String warehouseId) {
        ID = ID + 1;
        String categoryId = ID + "";
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setWarehouseId(warehouseId);
        categoryMapper.addCategory(category);
    }

    /**
     * 移除一种商品种类
     *
     * @param categoryId 商品种类ID
     */
    public void removeCategory(String categoryId) {
        categoryMapper.removeCategory(categoryId);
    }

    /**
     * 更新商品种类信息
     *
     * @param category 商品种类
     */
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }
}
