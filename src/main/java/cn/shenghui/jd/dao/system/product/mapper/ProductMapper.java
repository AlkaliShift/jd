package cn.shenghui.jd.dao.system.product.mapper;

import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.product.dto.ProductDetails;
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
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表
     *
     * @param content 搜索内容
     * @return 商品列表
     */
    List<ProductDetails> getProductList(@Param("content") String content);

    /**
     * 根据商品ID批量查询商品信息
     *
     * @param productIds 商品ID集
     * @return 商品列表
     */
    List<Product> getProductsByIds(@Param("productIds") List<String> productIds);

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
     * 商品上架
     *
     * @param productIds    商品ID集
     * @param productStatus 商品上架状态
     * @param startTime     商品上架时间
     */
    void productUp(@Param("productIds") List<String> productIds, @Param("productStatus") String productStatus,
                   @Param("startTime") String startTime);

    /**
     * 商品下架
     *
     * @param productIds    商品ID集
     * @param productStatus 商品下架状态
     * @param endTime       商品下架时间
     */
    void productDown(@Param("productIds") List<String> productIds, @Param("productStatus") String productStatus,
                     @Param("endTime") String endTime);

    /**
     * 根据商品ID删除商品
     *
     * @param productId 商品ID
     */
    void removeProduct(@Param("productId") String productId);

    /**
     * 获得商品表行数
     *
     * @return 行数
     */
    int countProduct();

    /**
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表（用户页）
     *
     * @param content       搜索内容
     * @param productStatus 商品上架状态
     * @return 商品列表
     */
    List<ProductDetails> getProductListUser(@Param("content") String content, @Param("productStatus") String productStatus);

    /**
     * 根据商品ID商品信息（用户页）
     *
     * @param productIds 商品ID集
     * @return 商品列表
     */
    List<ProductDetails> getProductsByIdsUser(@Param("productIds") List<String> productIds);

    /**
     * 获取详细商品信息列表
     *
     * @param userId     用户ID
     * @param productIds 商品ID集
     * @return 商品信息列表
     */
    List<OrderProduct> getProductDetails(@Param("userId") String userId, @Param("productIds") List<String> productIds);

    /**
     * 冻结商品库存
     *
     * @param product 商品详细信息
     */
    void setFrozenNum(@Param("product") OrderProduct product);

    /**
     * 返回指定商品种类下的商品数量
     *
     * @param categoryId 商品种类ID
     * @return 指定商品种类下的商品数量
     */
    int ifExistProduct(@Param("categoryId") String categoryId);

    /**
     * 上传图片：更新图片路径
     *
     * @param productId 商品ID
     * @param path      图片路径
     */
    void updateImagePath(@Param("productId") String productId, @Param("path") String path);
}
