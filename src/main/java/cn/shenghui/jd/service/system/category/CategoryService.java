package cn.shenghui.jd.service.system.category;

import cn.shenghui.jd.dao.system.category.mapper.CategoryMapper;
import cn.shenghui.jd.dao.system.category.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
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
    public List<Category> getCategoryList(String categoryId) {
        return categoryMapper.getCategoryList(categoryId);
    }

    /**
     * 增加一种商品种类
     *
     * @param category 商品种类
     */
    public void addCategory(Category category) {
        ID = ID + 1;
        String categoryId = category.getWarehouseId() + "-" + ID;
        category.setCategoryId(categoryId);
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
