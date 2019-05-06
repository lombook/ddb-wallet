package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.SN;
import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.Orderitem;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.mapper.CurrencyMapper;
import com.jinglitong.springshop.mapper.OrderitemMapper;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.mapper.SkuMapper;
import com.jinglitong.springshop.utils.DateUtils;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.CalculateCartVo;

@Service
public class SplitOrderService {
	
	 @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderitemMapper orderitemMapper;
    
    @Autowired
    private CurrencyMapper currencyMapper;

    //国内商品按照skuPrice拆单
    public void skuSplitOrder(Orders orders, CalculateCartVo calculateCartVo, HashSet orderSn,Map<String,BigDecimal> sValueMap,Map<String,BigDecimal> djbValueMap) {
        Date date= new Date();
        for (CartItems cartItem: calculateCartVo.getCartItemsList()) {
        	//根据skuId获得storeId
        	String storeId = skuMapper.selectStoreIdbyZid(cartItem.getSkuPriceId());
        	BigDecimal sValue = sValueMap.get(cartItem.getSkuPriceId());
        	BigDecimal djbValue = djbValueMap.get(cartItem.getSkuPriceId());
                Map<String,Object> skuMap  = skuMapper.selectByPriceAndWeight(cartItem.getSkuPriceId());
                Orders ord = new Orders();
                ord.setOrderParent(orders.getZid());
                ord.setZid(UuidUtil.getUUID());
                BigDecimal amount = new BigDecimal(skuMap.get("price").toString()).multiply(new BigDecimal(cartItem.getQuantity()));
                ord.setAmount(amount);
                ord.setAddress(orders.getAddress());
                ord.setAreaname(orders.getAreaname());
                ord.setCompletedate(null);
                ord.setConsignee(orders.getConsignee());                
                ord.setFreight(new BigDecimal("0"));
                ord.setInvoicecontent("");
                ord.setInvoicetitle("");
                ord.setIsallocatedstock(0);
                ord.setMemo("");
                ord.setOffsetamount(new BigDecimal("0"));
                ord.setPhone(orders.getPhone());
                ord.setShippingmethodname("包邮");
                while (true){
                    String s = SN.OrderSn(4);
                    if(!orderSn.contains(s)){
                    	orderSn.add(s);
                        ord.setSn(s);
                        break;
                    }
                }
                ord.setStatus(0);
                ord.setWeight((Integer) skuMap.get("weight"));
                ord.setPrice(new BigDecimal(skuMap.get("price").toString()));
                ord.setStoreId(skuMap.get("store").toString());
                ord.setCurrencyId(skuMap.get("currencyId").toString());
                Currency c = new Currency();
                c.setZid(ord.getCurrencyId());
                c = currencyMapper.selectOne(c);
                /*if("AUD".equals(c.getCurrencycode())){
                	orders.setExpire(DateUtils.addHours(date, IConstants.ABROADORDER_EXPIR_TIME));
                	//orders.setExpire(DateUtils.addMinutes(date, 5));
                }else{*/
                	orders.setExpire(DateUtils.addYears(date, IConstants.HOMEORDER_EXPIR_TIME));
                /*}*/
                ord.setZipcode(orders.getZipcode());
                ord.setAreaId(orders.getAreaId());
                ord.setShippingmethodId("1");
                ord.setCustId(orders.getCustId());
                ord.setCreateTime(date);
                ord.setUpdateTime(date);
                ord.setStoreId(storeId);
                ord.setPriceCode((String) skuMap.get("priceCode"));;
                ord.setIntegralCId((String) skuMap.get("integralCId"));
                ord.setIntegralCode((String) skuMap.get("integralCode"));
                ord.setIntegralPrice(skuMap.get("integralPrice")== null ? new BigDecimal(0):new BigDecimal(skuMap.get("integralPrice").toString()));
                ord.setSValue(sValue.multiply(new BigDecimal(cartItem.getQuantity())));
                ord.setDjbValue(djbValue.multiply(new BigDecimal(cartItem.getQuantity())));
                ordersMapper.insert(ord);
                Orderitem orderitem = createOrderItem(skuMap, cartItem.getQuantity());
                orderitem.setOrdersId(ord.getZid());
                orderitem.setSValue(sValue.multiply(new BigDecimal(cartItem.getQuantity())));
                orderitem.setDjbValue(djbValue.multiply(new BigDecimal(cartItem.getQuantity())));
                orderitemMapper.insert(orderitem);
        }
    }
   
    //国外商品按照个数拆单
    public void splitOrder(Orders orders, CalculateCartVo calculateCartVo,HashSet orderSn,Map<String,BigDecimal> sValueMap,Map<String,BigDecimal> djbValueMap) {
        Date date= new Date();
        for (CartItems  cartItem: calculateCartVo.getCartItemsList()) {
        	//根据购物车项查找到商品对应的商铺，设置到子订单中
        	String storeId = skuMapper.selectStoreIdbyZid(cartItem.getSkuPriceId());
        	BigDecimal sValue= sValueMap.get(cartItem.getSkuPriceId());
        	BigDecimal djbValue= djbValueMap.get(cartItem.getSkuPriceId());
            for (int i = 0;i<cartItem.getQuantity();i++){
                Map<String,Object> skuMap  = skuMapper.selectByPriceAndWeight(cartItem.getSkuPriceId());
                Orders ord = createSubOrder(orders,storeId,skuMap,orderSn);
                //子订单添加svalue和djbvalue
                ord.setSValue(sValue);
                ord.setDjbValue(djbValue);
                ordersMapper.insert(ord);
                Orderitem orderitem = createOrderItem(skuMap,1);               
                orderitem.setOrdersId(ord.getZid());
                orderitem.setSValue(sValue);
                orderitem.setDjbValue(djbValue);
                orderitemMapper.insert(orderitem);

            }
        }
    }
    /**
     * 生成订单项
     * @param map
     * @param cartItem
     * @param orderitem
     */
    public Orderitem createOrderItem(Map<String,Object> map, Integer quantity) {
    	Orderitem orderitem = new Orderitem();
        Date date= new Date();
        orderitem.setZid(UuidUtil.getUUID());
        orderitem.setIsdelivery(true);
        orderitem.setName(map.get("name").toString());
        orderitem.setPrice(new BigDecimal(map.get("price").toString()));
        orderitem.setShippedquantity(0);
        orderitem.setQuantity(quantity);
        orderitem.setSn(map.get("sn").toString());
        orderitem.setThumbnail(map.get("image").toString());
        orderitem.setType(0);
        orderitem.setWeight((Integer) map.get("weight"));
        orderitem.setSkuId(map.get("skuId").toString());
        orderitem.setCurrencyId(map.get("currencyId").toString());
        orderitem.setCreateTime(date);
        orderitem.setUpdateTime(date);
        orderitem.setSpecifications(map.get("specificationValues")!=null?map.get("specificationValues").toString():null);
        orderitem.setPriceCode((String)map.get("priceCode"));
        orderitem.setIntegralCId((String) map.get("integralCId"));
        orderitem.setIntegralCode((String) map.get("integralCode"));
        orderitem.setIntegralPrice(map.get("integralPrice")== null ? new BigDecimal(0):new BigDecimal(map.get("integralPrice").toString()));
        return orderitem;
    }
    private Orders createSubOrder(Orders forder,String storeId,Map<String,Object> skuMap,HashSet orderSn){
		Date date = new Date();
		Orders ord = new Orders();
		ord.setOrderParent(forder.getZid());
		ord.setZid(UuidUtil.getUUID());
		ord.setAmount(new BigDecimal(skuMap.get("price").toString()));
		ord.setAddress(forder.getAddress());
		ord.setAreaname(forder.getAreaname());
		ord.setCompletedate(null);
		ord.setConsignee(forder.getConsignee());
		ord.setFreight(new BigDecimal("0"));
		ord.setInvoicecontent("");
		ord.setInvoicetitle("");
		ord.setIsallocatedstock(0);
		ord.setMemo("");
		ord.setOffsetamount(new BigDecimal("0"));
		ord.setPhone(forder.getPhone());
		ord.setPrice(new BigDecimal("0"));
		ord.setShippingmethodname("包邮");
		while (true) {
			String s = SN.OrderSn(4);
			if (!orderSn.contains(s)) {
				orderSn.add(s);
				ord.setSn(s);
				break;
			}
		}
		ord.setStatus(0);
		ord.setWeight((Integer) skuMap.get("weight"));
		ord.setPrice(new BigDecimal(skuMap.get("price").toString()));
		ord.setStoreId(skuMap.get("store").toString());
		ord.setCurrencyId(skuMap.get("currencyId").toString());
		Currency c = new Currency();
		c.setZid(ord.getCurrencyId());
		c = currencyMapper.selectOne(c);
		/*if ("AUD".equals(c.getCurrencycode())) {
			ord.setExpire(DateUtils.addHours(date, IConstants.ABROADORDER_EXPIR_TIME));
			//ord.setExpire(DateUtils.addMinutes(date, 5));
		} else {*/
			ord.setExpire(DateUtils.addYears(date, IConstants.HOMEORDER_EXPIR_TIME));
		/*}*/
		ord.setZipcode(forder.getZipcode());
		ord.setAreaId(forder.getAreaId());
		ord.setShippingmethodId("1");
		ord.setCustId(forder.getCustId());
		ord.setCreateTime(date);
		ord.setUpdateTime(date);
		ord.setStoreId(storeId);
		ord.setPriceCode((String) skuMap.get("priceCode"));
		ord.setIntegralCId((String) skuMap.get("integralCId"));
		ord.setIntegralCode((String) skuMap.get("integralCode"));
		ord.setIntegralPrice(skuMap.get("integralPrice") == null ? new BigDecimal(0)
				: new BigDecimal(skuMap.get("integralPrice").toString()));
		return ord;
    }

    /*public void oneQuitySplitOrder(Orders orders, CalculateCartVo calculateCartVo, HashSet orderSn) {
        Date date= new Date();
        for (CartItems cartItem: calculateCartVo.getCartItemsList()) {
            for (int i = 0;i<cartItem.getQuantity();i++){
                Map<String,Object> skuMap  = skuMapper.selectByPriceAndWeight(cartItem.getSkuId());
                Orders ord = new Orders();
                ord.setOrderParent(orders.getZid());
                ord.setZid(UuidUtil.getUUID());
                ord.setAmount(new BigDecimal(skuMap.get("price").toString()));
                ord.setAddress(orders.getAddress());
                ord.setAreaname(orders.getAreaname());
                ord.setCompletedate(null);
                ord.setConsignee(orders.getConsignee());
                
                ord.setFreight(new BigDecimal("0"));
                ord.setInvoicecontent("");
                ord.setInvoicetitle("");
                ord.setIsallocatedstock(0);
                ord.setMemo("");
                ord.setOffsetamount(new BigDecimal("0"));
                ord.setPhone(orders.getPhone());
                ord.setPrice(new BigDecimal("0"));
                ord.setShippingmethodname("包邮");
                while (true){
                    String s = SN.OrderSn(4);
                    if(!orderSn.contains(s)){
                        orderSn.add(s);
                        ord.setSn(s);
                        break;
                    }
                }
                ord.setStatus(0);
                ord.setWeight((Integer) skuMap.get("weight"));
                ord.setPrice(new BigDecimal(skuMap.get("price").toString()));
                ord.setStoreId(skuMap.get("store").toString());
                ord.setCurrencyId(skuMap.get("currencyId").toString());
                Currency c = new Currency();
                c.setZid(ord.getCurrencyId());
                c = currencyMapper.selectOne(c);
                if("AUD".equals(c.getCurrencycode())){
                	orders.setExpire(DateUtils.addDays(date, IConstants.ABROADORDER_EXPIR_TIME));
                }else{
                	orders.setExpire(DateUtils.addYears(date, IConstants.HOMEORDER_EXPIR_TIME));
                }
                ord.setZipcode(orders.getZipcode());
                ord.setAreaId(orders.getAreaId());
                ord.setShippingmethodId("1");
                ord.setCustId(orders.getCustId());
                ord.setCreateTime(date);
                ord.setUpdateTime(date);
                ordersMapper.insert(ord);
                Orderitem orderitem = new Orderitem();
                orderitem.setZid(UuidUtil.getUUID());
                orderitem.setIsdelivery(true);
                orderitem.setName(skuMap.get("name").toString());
                orderitem.setPrice(new BigDecimal(skuMap.get("price").toString()));
                orderitem.setShippedquantity(0);
                orderitem.setQuantity(1);
                orderitem.setSn(skuMap.get("sn").toString());
                orderitem.setThumbnail(skuMap.get("image").toString());
                orderitem.setType(0);
                orderitem.setWeight(Integer.valueOf(skuMap.get("weight").toString()));
                orderitem.setSkuId(skuMap.get("skuId").toString());
                orderitem.setCurrencyId(skuMap.get("currencyId").toString());
                orderitem.setOrdersId(ord.getZid());
                orderitem.setCreateTime(date);
                orderitem.setUpdateTime(date);
                orderitem.setSpecifications(skuMap.get("specificationValues")!=null?skuMap.get("specificationValues").toString():null);
                orderitemMapper.insert(orderitem);
            }
        }
    }*/

}
