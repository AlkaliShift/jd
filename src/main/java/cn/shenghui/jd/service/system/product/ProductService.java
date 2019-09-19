package cn.shenghui.jd.service.system.product;

import cn.shenghui.jd.constants.UniversalConstants;
import cn.shenghui.jd.constants.system.product.ProductConstants;
import cn.shenghui.jd.dao.system.order.dto.OrderProduct;
import cn.shenghui.jd.dao.system.product.dto.ProductDetails;
import cn.shenghui.jd.dao.system.product.mapper.ProductMapper;
import cn.shenghui.jd.dao.system.product.model.Product;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 13:29
 */
@Service
public class ProductService {

    private ProductMapper productMapper;

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表
     *
     * @param content 搜索内容
     * @return 商品列表
     */
    public PageInfo<ProductDetails> getProductList(String content, int page, int limit) {
        PageHelper.startPage(page, limit);
        return new PageInfo<>(productMapper.getProductList(content));
    }

    /**
     * 根据商品ID批量搜索商品
     *
     * @param productIds 商品ID集
     * @return 特定多件商品
     */
    public List<Product> getProductsByIds(List<String> productIds) {
        return productMapper.getProductsByIds(productIds);
    }

    /**
     * 增加单个商品
     *
     * @param product 商品
     */
    public void addProduct(Product product) {
        String productId = String.valueOf(productMapper.countProduct() + 1);
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
     * @param productIds    商品ID集
     * @param productStatus 商品上下架状态
     */
    public void setProductStatus(List<String> productIds, String productStatus) {
        String time = DateFormatUtils.format(new Date(), UniversalConstants.PATTERN_TIME_FOR_ID);
        if ((ProductConstants.PRODUCT_UP).equals(productStatus)) {
            productMapper.productUp(productIds, productStatus, time);
        } else if (ProductConstants.PRODUCT_DOWN.equals(productStatus)) {
            productMapper.productDown(productIds, productStatus, time);
        }
    }

    /**
     * 根据商品ID删除商品
     *
     * @param productId 商品ID
     */
    public void removeProduct(String productId) {
        productMapper.removeProduct(productId);
    }

    /**
     * 模糊查询商品信息，若搜索内容为空，则返回所有商品信息列表（用户页）
     *
     * @param content 搜索内容
     * @return 商品列表
     */
    public PageInfo<ProductDetails> getProductListUser(String content, int page, int limit) {
        PageHelper.startPage(page, limit);
        return new PageInfo<>(productMapper.getProductListUser(content, ProductConstants.PRODUCT_UP));
    }

    /**
     * 根据商品ID批量搜索商品（用户页）
     *
     * @param productIds 商品ID集
     * @return 商品信息
     */
    public List<ProductDetails> getProductsByIdsUser(List<String> productIds) {
        return productMapper.getProductsByIdsUser(productIds);
    }

    /**
     * 获取详细商品信息
     *
     * @param userId     用户ID
     * @param productIds 商品ID集
     * @return 详细商品信息列表
     */
    public List<OrderProduct> getProductDetails(String userId, List<String> productIds) {
        return productMapper.getProductDetails(userId, productIds);
    }

    /**
     * 冻结商品库存
     *
     * @param products 商品详细信息
     */
    public void freezeNum(List<OrderProduct> products) {
        int availableNum;
        int frozenNum;
        for (OrderProduct product : products) {
            frozenNum = product.getFrozenNum() + product.getProductNum();
            availableNum = product.getAvailableNum() - product.getProductNum();
            product.setAvailableNum(availableNum);
            product.setFrozenNum(frozenNum);
            this.setFrozenNum(product);
        }
    }

    /**
     * 解冻商品库存
     *
     * @param products 商品ID和数量
     */
    public void unFreezeNum(List<OrderProduct> products) {
        int availableNum;
        int frozenNum;
        for (OrderProduct product : products) {
            frozenNum = product.getFrozenNum() - product.getProductNum();
            availableNum = product.getAvailableNum() + product.getProductNum();
            product.setAvailableNum(availableNum);
            product.setFrozenNum(frozenNum);
            this.setFrozenNum(product);
        }
    }

    /**
     * 冻结/解冻库存的事务管理
     *
     * @param orderProduct 商品详细信息
     */
    private void setFrozenNum(OrderProduct orderProduct) {
        productMapper.setFrozenNum(orderProduct);
    }

    /**
     * 查询该商品分类下是否有商品
     *
     * @param categoryId 商品种类ID
     * @return 存在：true/不存在：false
     */
    public boolean ifExistProduct(String categoryId) {
        return productMapper.ifExistProduct(categoryId) != 0;
    }

    /**
     * 上传图片：更新图片路径
     *
     * @param productId 商品ID
     * @param path      图片路径
     */
    public void updateImagePath(String productId, String path) {
        productMapper.updateImagePath(productId, path);
    }
}
