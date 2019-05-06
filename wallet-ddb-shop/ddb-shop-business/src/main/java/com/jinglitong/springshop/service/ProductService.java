package com.jinglitong.springshop.service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.entity.Sku;
import com.jinglitong.springshop.entity.SkuExtendInfo;
import com.jinglitong.springshop.entity.SkuPrice;
import com.jinglitong.springshop.entity.SkuPriceHistory;
import com.jinglitong.springshop.entity.admin.ShopAdmin;
import com.jinglitong.springshop.mapper.CartItemsMapper;
import com.jinglitong.springshop.mapper.CurrencyMapper;
import com.jinglitong.springshop.mapper.ProductMapper;
import com.jinglitong.springshop.mapper.SkuExtendInfoMapper;
import com.jinglitong.springshop.mapper.SkuMapper;
import com.jinglitong.springshop.mapper.SkuPriceHistoryMapper;
import com.jinglitong.springshop.mapper.SkuPriceMapper;
import com.jinglitong.springshop.md5.MD5Utils;
import com.jinglitong.springshop.utils.DateUtils;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.ProductVo;
import com.jinglitong.springshop.vo.request.CurrencyGroup;
import com.jinglitong.springshop.vo.request.ProductAddParam;
import com.jinglitong.springshop.vo.request.ProductToUpdateParam;
import com.jinglitong.springshop.vo.request.CurrencyGroup;
import com.jinglitong.springshop.vo.response.ProductDetailVo;
import com.jinglitong.springshop.vo.response.ProductListVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import com.jinglitong.springshop.vo.response.ProductSeeVo;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    
    
    @Resource
    private SkuMapper skuMapper;
    
    @Resource
    private CurrencyMapper currencyMapper;

    @Autowired
    private SkuExtendInfoMapper skuExtendInfoMapper;
    
    @Autowired
    private SkuPriceMapper skuPriceMapper;
    
    @Autowired
    private SkuPriceHistoryMapper skuPriceHistoryMapper;
    
    @Autowired
    private CartItemsMapper cartItemsMapper;
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
    public ShopRespose  productSave(ProductAddParam param,ShopAdmin business) {
    	ShopRespose respose = new ShopRespose<>();
    	Product p = buildProduct(param);
    	p.setSellNum(0);
    	CurrencyGroup[] currencyGroup = param.getCurrencyGroup();
    	Sku sku = buildSku(param, p.getZid(),currencyGroup,business);
    	List<SkuPrice> skuPrice = buildSkuPrice(sku.getZid(),currencyGroup);
    	if(skuPrice.size() > 1) {
    		BigDecimal normal  = null;
    		BigDecimal comb  = null;
    		for (SkuPrice sp : skuPrice) {
    			if(sp.getPriceType() == 0) {//0:纯法币 1:非纯法币
    				normal = sp.getPrice();
    			}else {
    				comb = sp.getPrice();
    			}
    		}
    		if(comb.compareTo(normal) == 1) {
    			respose = new ShopRespose<>(ErrorEnum.ERROR_4003);
        		return respose;
    		}
    		
    	}
    	List<SkuExtendInfo> skuExtendInfo = buildExtendInfo(skuPrice, currencyGroup, sku.getZid());
    	p.setSn(getRandom());
    	sku.setSn(p.getSn());
    	int pc = productMapper.insert(p);
    	int skuc = skuMapper.insert(sku);
		int skuInfoc = skuExtendInfoMapper.insertList(skuExtendInfo);
		int skuP = skuPriceMapper.insertList(skuPrice);
    	if(pc==1 && skuc==1 && skuInfoc > 0 && skuP > 0) {
    		respose.setCode(IConstants.SUCCESS);
    		respose.setMessage(IConstants.SUCCESS_MSG);
    		return respose;
    	}else {
    		throw new RuntimeException("保存异常") ;
    	}
    }

    public List<SkuExtendInfo> buildExtendInfo(List<SkuPrice> skuPrice,CurrencyGroup []currencyGroup,String skuId) {
    	List<SkuExtendInfo> exList= new LinkedList<>();
    	for (CurrencyGroup cg : currencyGroup) {
    		Date date = new Date();
    		SkuExtendInfo skuExtendInfo = new SkuExtendInfo();
    		skuExtendInfo.setZid(UuidUtil.getUUID());
    		skuExtendInfo.setSkuId(skuId);
    		skuExtendInfo.setCreateTime(date);
    		skuExtendInfo.setUpdateTime(date);
    		if(cg.getCode().contains("-")) {//组合
				String [] valueArr = cg.getValue().split("-");
				if(new BigDecimal(valueArr[0]).compareTo(BigDecimal.ZERO)== 0) {
					continue;
				}else {
					skuExtendInfo.setsValue(new BigDecimal(cg.getSvalue()));
					skuExtendInfo.setDjbValue(new BigDecimal(cg.getDjbValue()));
					for (SkuPrice sp : skuPrice) {
						if(StringUtils.isNotEmpty(sp.getIntegralCId())) {
							skuExtendInfo.setSkuPriceId(sp.getZid());
						}
					}
				}
    		}else {
    			skuExtendInfo.setsValue(new BigDecimal(cg.getSvalue()));
				skuExtendInfo.setDjbValue(new BigDecimal(cg.getDjbValue()));
				for (SkuPrice sp : skuPrice) {
					if(StringUtils.isEmpty(sp.getIntegralCId())) {
						skuExtendInfo.setSkuPriceId(sp.getZid());
					}
				}
    		}
    		exList.add(skuExtendInfo);
		}
    	
    	return exList;
    }
	/**
	 * 创建对应积分项
	 * @param param
	 * @param skuZid
	 * @return
	 */
/*	private SkuExtendInfo buildSkuWxtendInfo(ProductAddParam param, String skuZid) {
		Date date = new Date();
		SkuExtendInfo skuExtendInfo = new SkuExtendInfo();
		skuExtendInfo.setZid(UuidUtil.getUUID());
		skuExtendInfo.setSkuId(skuZid);
		skuExtendInfo.setsValue(param.getSvalue());
		skuExtendInfo.setDjbValue(param.getDjbValue());
		skuExtendInfo.setCreateTime(date);
		skuExtendInfo.setUpdateTime(date);
		return skuExtendInfo;
	}*/

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
    	if(!StringUtils.isEmpty(param.getWeight())) {
    		p.setWeight(Integer.valueOf(param.getWeight()));
    		p.setWeightUnit(param.getWeightUnit());
    	}
    	if(!StringUtils.isEmpty(param.getBrandId())) {
    		p.setBrandId(param.getBrandId());
    	}
    	if(!StringUtils.isEmpty(param.getStoreId())) {
    		p.setStoreId(param.getStoreId());
    	}
    	p.setProductCategoryId(param.getCategoryId());
    	p.setCreatedTime(new Date());
    	p.setUpdatedTime(new Date());
    	//p.setBenchmarkprice(new BigDecimal(param.getPrice()));
    	return p;
    }
    /**
     * 
     * 功能说明:构建sku
     * @param param
     * @param productId
     * @return
     */
    private Sku buildSku(ProductAddParam param,String productId,CurrencyGroup []currencyGroup,ShopAdmin business) {
    	log.info(JSON.toJSONString(currencyGroup));
    	Sku sku = new Sku();
    	int supportIntegral = 0;
    	List<Map<String, String>> jsonList = new LinkedList<>();
    	
    	for (CurrencyGroup cg : currencyGroup) {
    		Map<String, String> mp = new HashMap<>();
    		if(cg.getValue().contains("-")) {
    			String [] codeArr = cg.getCode().split("-");
				String [] valueArr = cg.getValue().split("-");
				if(new BigDecimal(valueArr[0]).compareTo(BigDecimal.ZERO) == 1) {
    				supportIntegral = 1;
    			}
				//法币
		    	Currency record = new Currency();
				record.setCurrencycode(codeArr[0]);
				record = currencyMapper.selectOne(record);
				mp.put("code", codeArr[0]);
    			mp.put("name", record.getCurrencyname());
    			mp.put("unit", record.getCurrencyunit());
    			mp.put("price", valueArr[0]);
    			//积分
				Currency cu = new Currency();
				cu.setCurrencycode(codeArr[1]);
				cu = currencyMapper.selectOne(cu);
    			mp.put("IntegralCode", codeArr[1]);
    			mp.put("IntegralName", cu.getCurrencyname());
    			mp.put("IntegralUnit", cu.getCurrencyunit());
    			mp.put("IntegralPrice", valueArr[1]);
    			jsonList.add(mp);
    		}else {
    			Currency one = new Currency();
    			one.setCurrencycode(cg.getCode());
    			one = currencyMapper.selectOne(one);
    			mp.put("code", cg.getCode());
    			mp.put("name", one.getCurrencyname());
    			mp.put("unit", one.getCurrencyunit());
    			mp.put("price", cg.getValue());
    			jsonList.add(mp);
    		}
			if(cg.getIsDefault() == 1) {
				if(cg.getCode().contains("-")) {//組合
					String [] codeArr = cg.getCode().split("-");
					String [] valueArr = cg.getValue().split("-");
					//积分
					Currency cu = new Currency();
					cu.setCurrencycode(codeArr[1]);
					cu = currencyMapper.selectOne(cu);
					sku.setIntegralPrice(valueArr[1]);
			    	sku.setIntegralCId(cu.getZid());
			    	sku.setIntegralCode(codeArr[1]);
			    	//法币
			    	Currency record = new Currency();
					record.setCurrencycode(codeArr[0]);
					record = currencyMapper.selectOne(record);
			    	sku.setPrice(new BigDecimal(valueArr[0]));
			    	sku.setCurrencyId(record.getZid());
			    	sku.setPriceCode(codeArr[0]);
			    	
				}else {//纯币种
					Currency record = new Currency();
					record.setCurrencycode(cg.getCode());
					record = currencyMapper.selectOne(record);
					sku.setPrice(new BigDecimal(cg.getValue()));
			    	sku.setCurrencyId(record.getZid());
			    	sku.setPriceCode(cg.getCode());
			    	sku.setIntegralPrice("0");
			    	sku.setIntegralCId("");
			    	sku.setIntegralCode("");
				}
			}
		}
    	sku.setCurrencyItems(JSON.toJSONString(jsonList));
    	sku.setSupportIntegral(supportIntegral);
    	sku.setZid(UuidUtil.getUUID());
    	sku.setIsdefault(true);
    	if(!StringUtils.isEmpty(param.getSn())) {
    		sku.setSn(param.getSn());
    	}
    	sku.setSellOut(0);
    	sku.setProductId(productId);
    	sku.setCreatedTime(new Date());
    	sku.setUpdatedTime(new Date());
    	sku.setUpdateOper(business.getUserName());
    	log.info("sku bean ok");
    	return sku;
    }
    @Transactional
    public List<SkuPrice> buildSkuPrice(String zid,CurrencyGroup[] currencyGroup) {
    	
    	List<SkuPrice> spList = new LinkedList<>();
    	for (CurrencyGroup cg : currencyGroup) {
    		SkuPrice skuPrice = new SkuPrice();
    		skuPrice.setZid(UuidUtil.getUUID());
    		skuPrice.setSkuId(zid);
    		if(cg.getCode().contains("-")) {//組合 [{"code":"AUD","value":"100"},{"code":"AUD-DJB","value":"90-20"}]
    			String [] codeArr = cg.getCode().split("-");
				String [] valueArr = cg.getValue().split("-");
				if(new BigDecimal(valueArr[0]).compareTo(BigDecimal.ZERO)== 0) {
					continue;
				}
				Currency record = new Currency();
				record.setCurrencycode(codeArr[0]);
				record = currencyMapper.selectOne(record);
    			skuPrice.setCurrencyId(record.getZid());
    			skuPrice.setPrice(new BigDecimal(valueArr[0]));
    			skuPrice.setPriceCode(codeArr[0]);
    			
    			Currency cu = new Currency();
				cu.setCurrencycode(codeArr[1]);
				cu = currencyMapper.selectOne(cu);
    			skuPrice.setIntegralCId(cu.getZid());
    			skuPrice.setIntegralPrice(new BigDecimal(valueArr[1]));
    			skuPrice.setIntegralCode(codeArr[1]);
    			skuPrice.setPriceType(1);
    		}else {//纯币种
    			Currency record = new Currency();
				record.setCurrencycode(cg.getCode());
				record = currencyMapper.selectOne(record);
    			skuPrice.setCurrencyId(record.getZid());
    			skuPrice.setPrice(new BigDecimal(cg.getValue()));
    			skuPrice.setPriceCode(cg.getCode());
    			skuPrice.setPriceType(0);
    		}
    		skuPrice.setIsDefault(cg.getIsDefault());
    		skuPrice.setCreatedTime(new Date());
    		skuPrice.setUpdatedTime(new Date());
    		spList.add(skuPrice);
		}
    	
    	return  spList;
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
     * 功能说明:去修改商品
     * @param vo
     * @return
     */
    public ProductToUpdateParam toUpdateProduct(ProductAddParam vo) {
    	
    	return productMapper.toUpdateProduct(vo);
    }
    
    
    /**
     * 
     * 功能说明:修改商品
     * @param vo
     * @return
     */
    @Transactional
    public ShopRespose updateProduct(ProductAddParam vo,ShopAdmin business) {
    	ShopRespose respose = new ShopRespose<>();
    	
    	Sku param = new Sku();
    	param.setZid(vo.getZid());
    	param = skuMapper.selectOne(param);
    	
    	Product p = buildProduct(vo);
    	p.setZid(null);
    	p.setCreatedTime(null);
    	Weekend<Product>weekend = Weekend.of(Product.class);
    	WeekendCriteria<Product,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(Product::getZid, param.getProductId());
    	int productC = productMapper.updateByExampleSelective(p, weekend);
    	
    	
    	Sku sku = buildSku(vo, param.getProductId(), vo.getCurrencyGroup(), business);
    	
    	sku.setId(param.getId());
    	sku.setUpdateOper(business.getUserName());
    	sku.setZid(param.getZid());
    	sku.setCreatedTime(null);
    	sku.setSn(null);
    	int skuC = skuMapper.updateByPrimaryKeySelective(sku);
    	for (CurrencyGroup cg : vo.getCurrencyGroup()) {
    		if(new BigDecimal(cg.getDjbValue()).compareTo(BigDecimal.ZERO) != 0 || new BigDecimal(cg.getDjbValue()).compareTo(BigDecimal.ZERO) != 0) {
    			skuExtendInfoMapper.updateBySkuId(cg.getZid(),new BigDecimal(cg.getSvalue()),new BigDecimal(cg.getDjbValue()));
    		}else {
    			SkuExtendInfo exRecord = new SkuExtendInfo();
    			if(StringUtils.isNotEmpty(cg.getZid())) {
    				exRecord.setSkuPriceId(cg.getZid());
    				int skuEx = skuExtendInfoMapper.delete(exRecord);
    				if(skuEx > 1) {
    					throw new RuntimeException("修改异常");
    				}
    			}
    		}
		}
    	CurrencyGroup[] currencyGroup = vo.getCurrencyGroup();
    	updateSkuPrice(currencyGroup,sku.getZid());
    	
    	if(productC == 1 && skuC == 1) {
    		respose.setCode(IConstants.SUCCESS);
    		respose.setMessage(IConstants.SUCCESS_MSG);
    		return respose;
    	}else {
    		throw new RuntimeException("修改异常");
    	}
    	
    }

    /**
     * 
     * 功能说明:修改情况分为四种：
     * 1.aud ->aud+integ
     * 2.aud ->aud
     * 3.aud + integ-> aud
     * 4.aud + integ-> aud + integ
     * @param currencyGroup
     * @param skuZid
     * @return
     */
	public int updateSkuPrice(CurrencyGroup[] currencyGroup,String skuZid) {
		log.info("接收到的前端json currencyGroup: {}",JSON.toJSONString(currencyGroup));
		for (CurrencyGroup cg : currencyGroup) {
			SkuPrice local = new SkuPrice();
			SkuPrice skuPrice = new SkuPrice();
			boolean nullFlag = false;
			if(StringUtils.isNotEmpty(cg.getZid())) {
				local.setZid(cg.getZid());
				local = skuPriceMapper.selectOne(local);
				skuPrice.setZid(cg.getZid());
			}else {
				nullFlag = true;
			}
			boolean deleFlag = false;
			if (cg.getCode().contains("-")) {// 組合 [{"code":"AUD","value":"100"},{"code":"AUD-DJB","value":"90-20","zid":"123"}]
				String[] codeArr = cg.getCode().split("-");
				String[] valueArr = cg.getValue().split("-");
				if(new BigDecimal(valueArr[0]).compareTo(BigDecimal.ZERO)== 0) {
					if(StringUtils.isNotEmpty(cg.getZid())) {
						deleFlag = true;
						SkuPrice dele = new SkuPrice();
						dele.setZid(cg.getZid());
						skuPriceMapper.delete(dele);
						CartItems deleCart = new CartItems();
						deleCart.setSkuPriceId(cg.getZid());
						cartItemsMapper.delete(deleCart);
						cartItemsMapper.deleteCart();
					}
				}else {
					Currency record = new Currency();
					record.setCurrencycode(codeArr[0]);
					record = currencyMapper.selectOne(record);
					skuPrice.setCurrencyId(record.getZid());
					skuPrice.setPrice(new BigDecimal(valueArr[0]));
					skuPrice.setPriceCode(codeArr[0]);

					Currency cu = new Currency();
					cu.setCurrencycode(codeArr[1]);
					cu = currencyMapper.selectOne(cu);
					skuPrice.setIntegralCId(cu.getZid());
					skuPrice.setIntegralPrice(new BigDecimal(valueArr[1]));
					skuPrice.setIntegralCode(codeArr[1]);
					skuPrice.setPriceType(1);
					
					skuPrice.setUpdatedTime(new Date());
					if(nullFlag) {
						skuPrice.setIsDefault(cg.getIsDefault());
						skuPrice.setZid(UuidUtil.getUUID());
						skuPrice.setSkuId(skuZid);
						skuPrice.setCreatedTime(new Date());
						skuPriceMapper.insert(skuPrice);
						SkuExtendInfo extendRecord = new SkuExtendInfo();
						extendRecord.setZid(UuidUtil.getUUID());
						extendRecord.setSkuId(skuZid);
						extendRecord.setSkuPriceId(skuPrice.getZid());
						extendRecord.setsValue(new BigDecimal(cg.getSvalue()));
						extendRecord.setDjbValue(new BigDecimal(cg.getDjbValue()));
						extendRecord.setCreateTime(new Date());
						extendRecord.setUpdateTime(new Date());
						skuExtendInfoMapper.insert(extendRecord);
						continue;
					}else {
						skuPrice.setIsDefault(cg.getIsDefault());
						skuPrice.setId(local.getId());
						SkuExtendInfo seinfo = new SkuExtendInfo();
						seinfo.setSkuPriceId(cg.getZid());
						seinfo = skuExtendInfoMapper.selectOne(seinfo);
						if(seinfo == null && (new BigDecimal(cg.getSvalue()).compareTo(BigDecimal.ZERO) == 1 || new BigDecimal(cg.getDjbValue()).compareTo(BigDecimal.ZERO) == 1) ) {
							SkuExtendInfo extendRecord = new SkuExtendInfo();
							extendRecord.setZid(UuidUtil.getUUID());
							extendRecord.setSkuId(skuZid);
							extendRecord.setSkuPriceId(cg.getZid());
							extendRecord.setsValue(new BigDecimal(cg.getSvalue()));
							extendRecord.setDjbValue(new BigDecimal(cg.getDjbValue()));
							extendRecord.setCreateTime(new Date());
							extendRecord.setUpdateTime(new Date());
							skuExtendInfoMapper.insert(extendRecord);
						}
					}
				}
			} else {// 纯币种
				Currency record = new Currency();
				record.setCurrencycode(cg.getCode());
				record = currencyMapper.selectOne(record);
				skuPrice.setCurrencyId(record.getZid());
				skuPrice.setPrice(new BigDecimal(cg.getValue()));
				skuPrice.setPriceCode(cg.getCode());
				skuPrice.setPriceType(0);
				skuPrice.setIsDefault(cg.getIsDefault());
				skuPrice.setUpdatedTime(new Date());
				skuPrice.setId(local.getId());
				
				SkuExtendInfo seinfo = new SkuExtendInfo();
				seinfo.setSkuPriceId(cg.getZid());
				seinfo = skuExtendInfoMapper.selectOne(seinfo);
				if(seinfo == null && (new BigDecimal(cg.getSvalue()).compareTo(BigDecimal.ZERO) == 1 || new BigDecimal(cg.getDjbValue()).compareTo(BigDecimal.ZERO) == 1) ) {
					SkuExtendInfo extendRecord = new SkuExtendInfo();
					extendRecord.setZid(UuidUtil.getUUID());
					extendRecord.setSkuId(skuZid);
					extendRecord.setSkuPriceId(cg.getZid());
					extendRecord.setsValue(new BigDecimal(cg.getSvalue()));
					extendRecord.setDjbValue(new BigDecimal(cg.getDjbValue()));
					extendRecord.setCreateTime(new Date());
					extendRecord.setUpdateTime(new Date());
					skuExtendInfoMapper.insert(extendRecord);
				}
			}
			boolean flag = compareSkuPrice(local, skuPrice);
			if(!flag || deleFlag) {
				SkuPriceHistory his = new SkuPriceHistory();
				his.setZid(UuidUtil.getUUID());
				his.setSkuId(skuZid);
				his.setCurrencyId(local.getCurrencyId());
				his.setPrice(local.getPrice());
				his.setPriceCode(local.getPriceCode());
				his.setIntegralCId(local.getIntegralCId());
				his.setIntegralPrice(local.getIntegralPrice());
				his.setIntegralCode(local.getIntegralCode());
				his.setPriceType(local.getPriceType());
				his.setIsDefault(local.getIsDefault());
				his.setEndTime(new Date());
				his.setBeginTime(local.getUpdatedTime());
				if(StringUtils.isNotEmpty(his.getCurrencyId())) {
					skuPriceHistoryMapper.insert(his);
				}
			}
			skuPriceMapper.updateByPrimaryKeySelective(skuPrice);
		}

		return 0;
	}
	
	public boolean compareSkuPrice (SkuPrice f,SkuPrice s) {
		if(f.getPrice() == null || s.getPrice() == null) {
			return false;
		}
		if(f.getPrice().compareTo(s.getPrice()) != 0) {
			return false;
		}
		if(!f.getPriceCode().equals(s.getPriceCode())) {
			return false;
		}
		if(s.getIntegralPrice() == null && f.getIntegralPrice() != null) {
			return false;
		}
		if(s.getIntegralPrice() != null && f.getIntegralPrice() == null) {
			return false;
		}
		
		if(s.getIntegralPrice() != null && f.getIntegralPrice() != null && f.getIntegralPrice().compareTo(s.getIntegralPrice()) != 0) {
			return false;
		}
		if( StringUtils.isNotEmpty(s.getIntegralCode()) && StringUtils.isNotEmpty(f.getIntegralCode()) &&!f.getIntegralCode().equals(s.getIntegralCode())) {
			return false;
		}
		return true;
	}
    public static void main(String[] args) {
    	System.out.println(new BigDecimal("0.00").compareTo(BigDecimal.ZERO));
        System.out.println((int)((Math.random()*9+1)*1000));  
	}
}
