package com.jinglitong.springshop.servcie;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.entity.Sku;
import com.jinglitong.springshop.entity.SkuPrice;
import com.jinglitong.springshop.mapper.CurrencyMapper;
import com.jinglitong.springshop.mapper.ProductMapper;
import com.jinglitong.springshop.mapper.SkuMapper;
import com.jinglitong.springshop.mapper.SkuPriceMapper;
import com.jinglitong.springshop.utils.DateUtils;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.ProductVo;
import com.jinglitong.springshop.vo.request.ProductAddParam;
import com.jinglitong.springshop.vo.response.ProductDetailVo;
import com.jinglitong.springshop.vo.response.ProductListVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import com.jinglitong.springshop.vo.response.ProductSeeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName ProductService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 17:36
 * @Version 1.0
 **/
@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    
    
    @Resource
    private SkuMapper skuMapper;
    
    @Resource
    private CurrencyMapper currencyMapper;
    
    @Resource
    private SkuPriceMapper skuPriceMapper; 
    
    /**
     * desc 根据sn 分页查询商品
     * @param pageNum
     * @param pageSize
     * @param sn
     * @return
     */
    public PageInfo<Product> selectProductsBySn(String pageNum,String pageSize,String sn){
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        Product p = new Product();
        if(sn!=null&&!"".equals(sn)){
            p.setSn(sn);
        }
        List<Product> products = productMapper.select(p);
        PageInfo<Product> info = new PageInfo<>(products);
        return info;
    }

    public PageInfo<ProductResponseVo> selectProductsOrder(ProductVo product) {
        PageHelper.startPage(product.getPageNum(), product.getPageSize());
        List<ProductResponseVo> products = productMapper.selectProductsOrder(product);
        PageInfo<ProductResponseVo> info = new PageInfo<ProductResponseVo>(products);
        return info;
    }
    /**
     * 
     * 功能说明:app根据商品id查询商品详情
     * @param id
     * @return
     */
    public ProductDetailVo selectProductDetails(String id) {
    	return productMapper.selectGoodsDetailsById(id);
    }
    /**
     * 
     * 功能说明:web查询商品列表
     * @param vo
     * @return
     */
    public PageInfo<ProductListVo> getProductList(ProductListVo vo){
    	PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
    	List<ProductListVo> list = productMapper.getProductList(vo);
    	PageInfo<ProductListVo> info = new PageInfo<ProductListVo>(list);
    	//messageQueueService.send(RabbitConstant.ORDER_EXCHANGE, RabbitConstant.ORDER_QUEUE_NAME,"{\"id\":\"1\"}");
    	return info;
    }
    /**
     * 
     * 功能说明:添加商品
     * @param param
     */
    @Transactional
    public ShopRespose  productSave(ProductAddParam param) {
    	Product p = buildProduct(param);
    	Sku sku = buildSku(param, p.getZid());
    	//SkuPriceValue spv = buildSkuPriceValue(param, p.getZid(), sku.getZid());
    	Currency cu = new Currency();
    	cu.setZid(param.getCurrencyId());
    	List<Currency> currencyList =  currencyMapper.select(cu);
    	List<Map<String, String>> json = new ArrayList<>();
    	for (Currency currency : currencyList) {
			Map<String, String> m = new HashMap<>();
			m.put("code", currency.getCurrencycode());
			m.put("name", currency.getCurrencyname());
			m.put("unit", currency.getCurrencyunit());
			m.put("price", param.getPrice());
			json.add(m);
		}
    	sku.setCurrencyItems(JSON.toJSONString(json));
    	ShopRespose respose = new ShopRespose<>();
    	p.setSn(getRandom());
    	int pc = productMapper.insert(p);
    	int skuc = skuMapper.insert(sku);
    	//int spvc = skuPriceValueMapper.insert(spv);
    	if(pc==1 && skuc==1) {
    		respose.setCode(IConstants.SUCCESS);
    		respose.setMessage(IConstants.SUCCESS_MSG);
    		return respose;
    	}else {
    		throw new RuntimeException("保存异常") ;
    	}
    }
    public String getRandom() {
    	int random = (int)((Math.random()*9+1)*1000);
    	String date = DateUtils.formatDate(new Date(), "yyyyMMdd");
    	date = date + random;
    	Product sn = new Product();
    	sn.setSn(date);
        sn= productMapper.selectOne(sn);
    	if(sn == null ) {
    		return date;
    	}else {
    		return getRandom();
    	}
    }
    /**
     * 
     * 功能说明:构建product
     * @param param
     * @return
     */
    private Product buildProduct(ProductAddParam param) {
    	Product p = new Product();
    	p.setZid(UuidUtil.getUUID());
    	if(!StringUtils.isEmpty(param.getCaption())) {
    		p.setCaption(param.getCaption());
    	}
    	if(!StringUtils.isEmpty(param.getImage())) {
    		p.setImage(param.getImage());
    	}
    	p.setIntroduction(param.getIntroduction());
    	p.setIslist(param.getIsList());
    	if(!StringUtils.isEmpty(param.getKeyword())) {
    		p.setKeyword(param.getKeyword());
    	}
    	if(!StringUtils.isEmpty(param.getMemo())) {
    		p.setMemo(param.getMemo());
    	}
    	p.setName(param.getName().trim());
    	p.setStoreId("");
    	if(!StringUtils.isEmpty(param.getWeight())) {
    		p.setWeight(Integer.valueOf(param.getWeight()));
    		p.setWeightUnit(param.getWeightUnit());
    	}
    	if(!StringUtils.isEmpty(param.getBrandId())) {
    		p.setBrandId(param.getBrandId());
    	}
    	p.setProductCategoryId(param.getCategoryId());
    	p.setCreatedTime(new Date());
    	p.setUpdatedTime(new Date());
    	p.setBenchmarkprice(new BigDecimal(param.getPrice()));
    	return p;
    }
    /**
     * 
     * 功能说明:构建sku
     * @param param
     * @param productId
     * @return
     */
    private Sku buildSku(ProductAddParam param,String productId) {
    	Sku sku = new Sku();
    	sku.setZid(UuidUtil.getUUID());
    	sku.setIsdefault(true);
    	if(!StringUtils.isEmpty(param.getSn())) {
    		sku.setSn(param.getSn());
    	}
    	sku.setSellOut(0);
    	sku.setProductId(productId);
    	sku.setCreatedTime(new Date());
    	sku.setUpdatedTime(new Date());
    	sku.setPrice(new BigDecimal(param.getPrice()));
    	if(!StringUtils.isEmpty(param.getCurrencyId())) {
    		sku.setCurrencyId(param.getCurrencyId());
    	}
    	return sku;
    }
    /**
     * 
     * 功能说明:构建SkuPriceValue
     * @param param
     * @param productId
     * @param skuId
     * @return
     */
/*    private SkuPriceValue buildSkuPriceValue(ProductAddParam param,String productId,String skuId) {
    	SkuPriceValue spv = new SkuPriceValue();
    	spv.setZid(UuidUtil.getUUID());
    	spv.setProductId(productId);
    	spv.setSkuId(skuId);
    	spv.setPrice(new BigDecimal(param.getPrice()));
    	spv.setCurrencyId(param.getCurrencyId());
    	spv.setCreatedTime(new Date());
    	spv.setUpdatedTime(new Date());
    	return spv;
    }*/
    /**
     * 
     * 功能说明:商品下架
     * @param id
     * @return
     */
    public boolean lowerShelf(String id) {
    	Product p = new Product();
    	p.setIslist(false);
    	Weekend<Product>weekend = Weekend.of(Product.class);
    	WeekendCriteria<Product,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Product::getZid, id);
    	int count = productMapper.updateByExampleSelective(p, weekend);
    	if(count == 1) {
    		return true;
    	}
    	return false;
    }
    /**
     * 
     * 功能说明:查看商品
     * @param vo
     * @return
     */
    public ProductSeeVo seeProduct(ProductSeeVo vo) {
    	return productMapper.seeProduct(vo);
    }
    
    
    /**
     * 
     * 功能说明:修改商品
     * @param vo
     * @return
     */
    @Transactional
    public ShopRespose updateProduct(ProductAddParam vo) {
    	ShopRespose respose = new ShopRespose<>();
    	
    	Sku param = new Sku();
    	param.setZid(vo.getZid());
    	param = skuMapper.selectOne(param);
    	
    	if(!StringUtils.isEmpty(vo.getSn())) {
    		Product sn = new Product();
        	sn.setSn(vo.getSn());
        	List<Product> snList= productMapper.select(sn);
        	if(snList.size() > 0) {
        		if(snList.size() > 1) {
        			respose.setCode(ErrorEnum.ERROR_4001.getCode());
            		respose.setMessage(ErrorEnum.ERROR_4001.getMsg());
            		return respose;
        		}else {
        			if(!snList.get(0).getZid().equals(param.getProductId())) {
        				respose.setCode(ErrorEnum.ERROR_4001.getCode());
                		respose.setMessage(ErrorEnum.ERROR_4001.getMsg());
                		return respose;
        			}
        		}
        		
        	}
    	}
    	Product p = buildProduct(vo);
    	p.setZid(null);
    	p.setCreatedTime(null);
    	Weekend<Product>weekend = Weekend.of(Product.class);
    	WeekendCriteria<Product,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Product::getZid, param.getProductId());
    	int productC = productMapper.updateByExampleSelective(p, weekend);
    	
    	Sku sku = new Sku();
    	sku.setUpdatedTime(new Date());
    	sku.setId(param.getId());
    	if(!StringUtils.isEmpty(vo.getSn())) {
    		sku.setSn(vo.getSn());
    	}
    	if(!StringUtils.isEmpty(vo.getCurrencyId())) {
    		sku.setCurrencyId(vo.getCurrencyId());
    	}
    	if(!StringUtils.isEmpty(vo.getPrice())) {
    		sku.setPrice(new BigDecimal(vo.getPrice()));
    	}
    	sku.setZid(param.getZid());
    	int skuC = skuMapper.updateByPrimaryKeySelective(sku);
    	
    	/*SkuPriceValue spv = new SkuPriceValue();
    	spv.setPrice(new BigDecimal(vo.getPrice()));
    	spv.setCurrencyId(vo.getCurrencyId());
    	Weekend<SkuPriceValue>weekendP = Weekend.of(SkuPriceValue.class);
    	WeekendCriteria<SkuPriceValue,Object> CriteriaP =weekendP.weekendCriteria();
    	CriteriaP.andEqualTo(SkuPriceValue::getProductId, param.getProductId());
    	CriteriaP.andEqualTo(SkuPriceValue::getSkuId, param.getZid());
    	int spvC = skuPriceValueMapper.updateByExampleSelective(spv, weekendP);*/
    	 
    	if(productC == 1 && skuC==1) {
    		respose.setCode(IConstants.SUCCESS);
    		respose.setMessage(IConstants.SUCCESS_MSG);
    		return respose;
    	}else {
    		throw new RuntimeException("修改异常");
    	}
    	
    }
    
    public List<SkuPrice> getproductPrice(String skuId) {
    	SkuPrice sp = new SkuPrice();
    	sp.setSkuId(skuId);
    	List<SkuPrice> list = skuPriceMapper.select(sp);
    	return list;
    }
    public static void main(String[] args) {
        System.out.println((int)((Math.random()*9+1)*1000));  
	}
}
