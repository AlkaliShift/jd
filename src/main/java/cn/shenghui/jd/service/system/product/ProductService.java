package cn.shenghui.jd.service.system.product;

import cn.shenghui.jd.dao.system.product.mapper.ProductMapper;
import cn.shenghui.jd.dao.system.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
public class ProductService {

    private ProductMapper productMapper;
    private static int ID = 0;

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 模糊搜索商品
     *
     * @return 商品列表
     * TODO
     */
    public List<Product> getProductList() {
        return productMapper.getProductList();
    }

    /**
     * 根据ID搜索商品
     *
     * @param productId 商品ID
     * @return 特定单件商品
     */
    public Product getProductById(String productId) {
        return productMapper.getProductById(productId);
    }

    /**
     * 增加单个商品
     *
     * @param product 商品
     */
    public void addProduct(Product product) {
        String categoryId = product.getCategoryId();
        ID = ID + 1;
        String productId = categoryId + "-" + ID;
        product.setProductId(productId);
        productMapper.addProduct(product);
    }

    /**
     * 修改单个商品信息
     *
     * @param product 商品
     */
    public void updateProductById(Product product) {
        productMapper.updateProductById;
    }

    /**
     * 上下架商品，上架为1，下架为0
     *
     * @param productStatus 商品上下架状态
     */
    public void updateProductStatus(char productStatus) {
        productMapper.updateProductStatus(productStatus);
    }
}
