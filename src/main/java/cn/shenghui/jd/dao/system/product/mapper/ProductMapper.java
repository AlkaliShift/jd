package cn.shenghui.jd.dao.system.product.mapper;

import cn.shenghui.jd.dao.system.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:30
 */
@Mapper
public interface ProductMapper {

    /**
     * 根据输入的商品名称模糊搜索商品
     *
     * @param productName 商品名称
     * @return 商品列表
     */
    List<Product> fuzzySearch(@Param("productName") String productName);

    /**
     * 根据商品ID批量查询商品信息
     *
     * @param productIds 商品ID
     * @return 商品列表
     */
    List<Product> getProductsByIds(List<String> productIds);

    /**
     * 增加单件商品
     *
     * @param product 商品信息
     */
    void addProduct(Product product);

    /**
     * 更新单个商品
     *
     * @param product 商品信息
     */
    void updateProduct(Product product);

    /**
     * 更新商品上下架状态
     *
     * @param productId     商品ID
     * @param productStatus 商品上下架状态
     * @param orderTime     商品下架时间
     */
    void setProductStatus(@Param("productId") String productId, @Param("productStatus") char productStatus,
                          @Param("orderTime") String orderTime);
}
