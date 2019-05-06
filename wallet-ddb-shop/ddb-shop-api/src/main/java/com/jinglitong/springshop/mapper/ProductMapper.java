package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.ProductVo;
import com.jinglitong.springshop.vo.request.ProductAddParam;
import com.jinglitong.springshop.vo.request.ProductToUpdateParam;
import com.jinglitong.springshop.vo.response.ProductDetailVo;
import com.jinglitong.springshop.vo.response.ProductListVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import com.jinglitong.springshop.vo.response.ProductSeeVo;

import java.util.List;

public interface ProductMapper extends MyMapper<Product> {
    /**
     * 查询商品列表
     * @param product
     * @return
     */
    List<ProductResponseVo> selectProductsOrder(ProductVo product);
    
    /**
     * 
     * 功能说明:查询商品详情
     * @param id
     * @return
     */
    ProductDetailVo selectGoodsDetailsById(String id);
    
    /**
     * 
     * 功能说明:web商品列表查询
     * @param vo
     * @return
     */
    List<ProductListVo> getProductList(ProductListVo vo);
    
    /**
     * 
     * 功能说明:查看商品
     * @param vo
     * @return
     */
    ProductSeeVo seeProduct(ProductSeeVo vo);
    /**
     * 
     * 功能说明:
     * @param vo
     * @return
     */
    ProductToUpdateParam toUpdateProduct(ProductAddParam vo);
}