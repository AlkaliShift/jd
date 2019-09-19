package cn.shenghui.jd.service.system.category;

import cn.shenghui.jd.dao.system.category.dto.CategoryDetails;
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

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 根据商品种类ID查找商品种类
     *
     * @param categoryId 商品种类ID
     * @return 商品种类
     */
    public Category getCategoryById(String categoryId) {
        return categoryMapper.getCategoryById(categoryId);
    }

    /**
     * 模糊查询商品种类信息，若搜索内容为空，则返回所有商品种类
     *
     * @param content 搜索内容
     * @return 商品种类列表
     */
    public List<CategoryDetails> getCategoryList(String content) {
        return categoryMapper.getCategoryList(content);
    }

    /**
     * 增加一种商品种类
     *
     * @param category 商品种类
     */
    public void addCategory(Category category) {
        String categoryId = String.valueOf(categoryMapper.countCategory() + 1);
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

    /**
     * 判断指定仓库下是否存在商品种类
     *
     * @param warehouseId 仓库ID
     * @return 存在：true/不存在：false
     */
    public boolean ifExistCategory(String warehouseId) {
        return categoryMapper.ifExistCategory(warehouseId) != 0;
    }
}
