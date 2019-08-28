package cn.shenghui.jd.service.system.product;

import cn.shenghui.jd.dao.system.product.mapper.ProductMapper;
import cn.shenghui.jd.dao.system.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class ProductService {

    private ProductMapper productMapper;
    private static int ID = 0;

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 根据输入的商品名称模糊搜索商品
     *
     * @return 商品列表
     */
    public List<Product> fuzzySearch(String productName) {
        return productMapper.fuzzySearch(productName);
    }

    /**
     * 根据商品ID批量搜索商品
     *
     * @param productIds 商品ID
     * @return 特定多件商品
     */
    public List<Product> getProductsById(List<String> productIds) {
        return productMapper.getProductsById(productIds);
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
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    /**
     * 上下架商品，上架为1，下架为0
     *
     * @param productId     商品ID
     * @param productStatus 商品上下架状态
     */
    public void setProductStatus(String productId, char productStatus) {
        productMapper.setProductStatus(productId, productStatus);
    }
}
