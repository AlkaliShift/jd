package cn.shenghui.jd.dao.system.category.mapper;

import cn.shenghui.jd.dao.system.category.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:30
 */
@Mapper
public interface CategoryMapper {

    /**
     * 根据商品种类ID查找商品种类
     *
     * @param categoryId 商品种类ID
     * @return 商品种类
     */
    Category getCategoryById(@Param("categoryId") String categoryId);

    /**
     * 模糊查询商品种类信息，若搜索内容为空，则返回所有商品种类
     *
     * @param content 商品种类ID
     * @return 商品种类列表
     */
    List<Category> getCategoryList(@Param("content") String content);

    /**
     * 增加商品种类
     *
     * @param category 商品种类信息
     */
    void addCategory(Category category);

    /**
     * 移除单个商品种类
     *
     * @param categoryId 商品种类ID
     */
    void removeCategory(@Param("categoryId") String categoryId);

    /**
     * 更新单个商品种类信息
     *
     * @param category 商品种类信息
     */
    void updateCategory(Category category);

    /**
     * 获得仓库表行数
     *
     * @return 行数
     */
    int countCategory();
}
