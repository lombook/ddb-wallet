package com.jinglitong.springshop.servcie;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.SN;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.utils.DateUtils;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.*;
import com.jinglitong.springshop.vo.response.OrderDKVo;
import com.jinglitong.springshop.vo.response.OrderDetailShippingVo;
import com.jinglitong.springshop.vo.response.OrderResponseVo;
import com.jinglitong.springshop.vo.response.SorderVo;
import com.jinglitong.springshop.vo.response.UserAssetVo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author fyy
 * @create 2019-01-21-12:22}
 */
@Service
@Slf4j
@Transactional
public class OrderService {


    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ReceiverMapper receiverMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderitemMapper orderitemMapper;

    @Autowired
    private OrderlogMapper orderlogMapper;

    @Autowired
    private PaymenttransactionMapper paymentTransactionMapper;

    @Autowired
    private CurrencyMapper currencyMapper;
    
    
    @Autowired
    private KukaOrderRewardService kukaOrderRewardService;
    
    @Autowired
    private SplitOrderService splitOrderService;

    @Autowired
    private OrderRealPayService orderRealPayService;
    
    @Autowired
    private IntegralWalletService integralWalletService;
    
    @Autowired
    private MqMessageRecordService mqMessageRecordService;
    
    @Autowired
    private OrderRealPayMapper orderRealPayMapper;
    
    @Autowired
    private IntegralPayService integralPayService;
    
    @Autowired
    private SkuPriceMapper skuPriceMapper;
    
    @Autowired
    private IntegralPayCancelService integralPayCancelService;
    
    @Autowired
    private SkuExtendInfoService skuExtendInfoService;

   @Transactional
   public ShopRespose createOrder(CalculateCartVo calculateCartVo,LoginVo info) {
       Boolean isSplit = true;
       String orderZid = "";
       BigDecimal amount =  BigDecimal.ZERO;
       HashSet<String> orderSn = new HashSet<>();
       String currencyCode = "";
       //验证sku是否存在
        List<Map<String, Object>> skuList = cartMapper.calculateCart(calculateCartVo.getCartItemsList());
        Map<String,BigDecimal> skuPriceSValueMap = new HashMap<String,BigDecimal>();
        Map<String,BigDecimal> skuPriceDJBValueMap = new HashMap<String,BigDecimal>();
        BigDecimal mainOrderSValue = BigDecimal.ZERO;
        BigDecimal mainOrderDJBValue = BigDecimal.ZERO;
        if(skuList.size() == calculateCartVo.getCartItemsList().size()){
        	 currencyCode  = skuList.get(0).get("currencyCode").toString();
             for (Map<String,Object> sku: skuList) {
                 String  currencyResult = sku.get("currencyCode").toString();
                 if(!currencyCode.equals(currencyResult)){
                     return new ShopRespose(ErrorEnum.ERROR_3103);
                 }
             }
        	Orders orders = new Orders();
            createOrders(orders,calculateCartVo.getReceiver());
            orders.setCustId(info.getZid());
            // 计算总价格
            BigDecimal sum = BigDecimal.ZERO;//法币总价
            BigDecimal integralsum = BigDecimal.ZERO;//积分总价
            Integer weight = 0;
            //生成订单和订单项

            String integralCode = null;//积分code
            String integralCId = null;
            String priceCode = null;
            ArrayList<Orderitem> orderitemList = new ArrayList<>();
            for (Map<String,Object> map : skuList) {            	
            	if(map.get("integralCId") != null){
            		integralCId = (String)map.get("integralCId");
            	}
            	if(map.get("integralCode") != null){
            		integralCode = (String)map.get("integralCode");
            	}            	
            	priceCode = (String)map.get("priceCode");
                if(StringUtils.isEmpty(orders.getStoreId()) && StringUtils.isEmpty(orders.getCurrencyId()) ){
                    //orders.setStoreId((String) map.get("store"));
                    orders.setCurrencyId((String)map.get("currencyId"));
                }
                for (CartItems cartItem : calculateCartVo.getCartItemsList()) {
                    if(cartItem.getSkuPriceId().equals(map.get("skuPriceId"))){
                    	//计算SkuPriceId对应的svalue和djbvalue
                    	BigDecimal svalue = BigDecimal.ZERO;
                    	BigDecimal djbvalue = BigDecimal.ZERO;
                    	SkuExtendInfo skuExtendInfo = skuExtendInfoService.getBySkuPriceId((String)map.get("skuPriceId"));
                    	if(skuExtendInfo !=  null){
                    		if(skuExtendInfo.getsValue() != null){
                    			svalue = skuExtendInfo.getsValue();
                    		}
                    		if(skuExtendInfo.getDjbValue() != null){
                    			djbvalue = skuExtendInfo.getDjbValue();
                    		}
                    	}
                    	skuPriceSValueMap.put((String)map.get("skuPriceId"), svalue);
                    	skuPriceDJBValueMap.put((String)map.get("skuPriceId"), djbvalue);
                    	mainOrderSValue = mainOrderSValue.add(svalue.multiply(new BigDecimal(cartItem.getQuantity())));
                    	mainOrderDJBValue = mainOrderDJBValue.add(djbvalue.multiply(new BigDecimal(cartItem.getQuantity())));
                    	//计算金额和重量
                        BigDecimal price = (BigDecimal) map.get("price");
                        BigDecimal integral_price = (BigDecimal) map.get("integralPrice");
                        if(integral_price == null){
                        	integral_price = BigDecimal.ZERO;
                        }
                        Integer quantity = cartItem.getQuantity();
                        Integer oneWeight =(Integer) map.get("weight");
                        String weightUnit =(String) map.get("weight_unit");
                        //计算金额
                        BigDecimal multiply = price.multiply(new BigDecimal(quantity));
                        sum = sum.add(multiply);//法币总价累加                        
                        integralsum = integralsum.add(integral_price.multiply(new BigDecimal(quantity)));//积分总价累加
                        //计算重量
                        if("KG".equals(weightUnit.toLowerCase())){
                           oneWeight = oneWeight * 1000;
                        }
                        weight += oneWeight;
                        //生成订单项
                        Orderitem orderitem = splitOrderService.createOrderItem(map,cartItem.getQuantity());                      
                        orderitem.setOrdersId(orders.getZid());
                        orderitem.setSValue(svalue.multiply(new BigDecimal(cartItem.getQuantity())));
                        orderitem.setDjbValue(djbvalue.multiply(new BigDecimal(cartItem.getQuantity())));
                        orderitemList.add(orderitem);
                    }
                }
            }
            orders.setPrice(sum);
            orders.setSValue(mainOrderSValue);
            orders.setDjbValue(mainOrderDJBValue);      
            Date date = new Date();
            Currency c = new Currency();
            c.setZid(orders.getCurrencyId());
             c = currencyMapper.selectOne(c);
           /* if("AUD".equals(c.getCurrencycode())){
            	orders.setExpire(DateUtils.addHours(date, IConstants.ABROADORDER_EXPIR_TIME));
            	//orders.setExpire(DateUtils.addMinutes(date, 5));
            }else{*/
            	orders.setExpire(DateUtils.addYears(date, IConstants.HOMEORDER_EXPIR_TIME));
            /*} */
            orders.setPriceCode(priceCode);
            orders.setIntegralPrice(integralsum);
            orders.setIntegralCode(integralCode);
            orders.setIntegralCId(integralCId);
            

            //根据币种计算抵扣金额
            if(priceCode.equals("CNY")){

            	BigDecimal deduction = calculateCartVo.getDeductionWBAmount().add(calculateCartVo.getDeductionXBAmount());
            	if(deduction.compareTo(sum) > 0){
            		return new ShopRespose(ErrorEnum.ERROR_3103);
            	}
            	orders.setOffsetamount(deduction);
            }/*else if(priceCode.equals("AUD")){
            	orders.setOffsetamount(calculateCartVo.getDeductionAmount().divide(new BigDecimal(5)).setScale(2,BigDecimal.ROUND_DOWN));
            }*/
            
            orders.setAmount(sum.subtract(orders.getOffsetamount()));
            orders.setWeight(weight);
            while (true){
                String s = SN.OrderSn(4);
                if(orderSn.add(s)){
                    orders.setSn(s);
                    break;
                }
            }
            //添加订单支付表
            orders.setOrderParent("P");
            //保存订单
            int insert = ordersMapper.insert(orders);
            int res = createOrderRealPay(orders.getZid(),calculateCartVo.getDeductionWBAmount(),calculateCartVo.getDeductionXBAmount(),orders.getAmount(),orders.getPriceCode(),orders.getCurrencyId(),orders.getIntegralPrice(),orders.getIntegralCode(),orders.getIntegralCId());
            if(insert == 1 && res == 1){
                //保存订单项
                orderitemMapper.insertList(orderitemList);
            }
            if((orders.getIntegralPrice() != null && new BigDecimal(0).compareTo(orders.getIntegralPrice()) < 0 )|| (calculateCartVo.getDeductionWBAmount() != null && new BigDecimal(0).compareTo(calculateCartVo.getDeductionWBAmount()) < 0)|| (calculateCartVo.getDeductionXBAmount() != null && new BigDecimal(0).compareTo(calculateCartVo.getDeductionXBAmount()) < 0)){
            	integralPayService.integralPay(orders.getZid(), orders.getCustId());
            }           
            if(isSplit){
                //拆单，如果时国外商品，则每个商品拆成一个订单，如果时国内商品，则一个sku拆成一个订单
            	/*if(currencyCode.equals("AUD")){
            		splitOrderService.splitOrder(orders,calculateCartVo,orderSn,skuPriceSValueMap,skuPriceDJBValueMap);
            	}
            	else */if(currencyCode.equals("CNY")){
            		splitOrderService.skuSplitOrder(orders,calculateCartVo,orderSn,skuPriceSValueMap,skuPriceDJBValueMap);
            	}
                
            }
            CartVo cartVo = new CartVo();
            cartVo.setCustId(info.getZid());
            Cart cart = cartMapper.selectByCustIdOrClientId(cartVo);
            if(!"buyNow".equals(calculateCartVo.getNowBuy())){
                for (CartItems car:calculateCartVo.getCartItemsList()) {
                    //修改购物车相应sku
                    Integer cartItems =cartMapper.updateByCartIdAndSkuId(cart.getZid(),car.getSkuPriceId(),car.getQuantity());
                    if(cartItems == 0){
                        cartMapper.deleteBycartIdAndskuId(cart.getZid(),car.getSkuPriceId());
                    }
                }
                Integer num = orderitemMapper.selectCountByCartid(cart.getZid());
                if(num == 0){
                    cartMapper.deleteByPrimaryKey(cart.getId());
                }
            }
            //加入订单号
            orderZid=orders.getZid();
            amount = orders.getAmount();
        }else {
        	return new ShopRespose(ErrorEnum.ERROR_3102);
        }
        HashMap resultMap = new HashMap();
        resultMap.put("orderZid", orderZid);
        resultMap.put("amount", amount);
        return new ShopRespose(IConstants.SUCCESS, "成功", resultMap);
    }
   private int createOrderRealPay(String orderId,BigDecimal deductionWBAmount,BigDecimal deductionXBAmount,BigDecimal amount,String currencyCode,String currencyId,BigDecimal integralAmount,String integralCode,String integralId){
	   if(deductionWBAmount != null && deductionWBAmount.compareTo(new BigDecimal(0)) > 0){
       	//插入抵扣支付
       	OrderRealPay orderRealPay = new OrderRealPay();
       	orderRealPay.setZid(UuidUtil.getUUID());
           Currency currency = new Currency();
           currency.setCurrencycode("wubei_all");
           currency = currencyMapper.selectOne(currency);
           int res = orderRealPayService.add(orderId, currency.getZid(), deductionWBAmount, currency.getCurrencycode(), 1);
           if(res < 1)
        	   return 0;
       }
	   if(deductionXBAmount != null && deductionXBAmount.compareTo(new BigDecimal(0)) > 0){
	       	//插入抵扣支付
	       	OrderRealPay orderRealPay = new OrderRealPay();
	       	orderRealPay.setZid(UuidUtil.getUUID());
	           Currency currency = new Currency();
	           currency.setCurrencycode("xianbei_all");
	           currency = currencyMapper.selectOne(currency);
	           int res = orderRealPayService.add(orderId, currency.getZid(), deductionXBAmount, currency.getCurrencycode(), 1);
	           if(res < 1)
	        	   return 0;
	       }
       if(integralAmount != null && integralAmount.compareTo(new BigDecimal(0)) > 0){
    	 //插入积分支付
    	   int res = orderRealPayService.add(orderId, integralId, integralAmount, integralCode, 1);
           if(res < 1)
        	   return 0;       	
       }
       if(amount != null /*&& amount.compareTo(new BigDecimal(0)) > 0*/){
    	 //插入法币支付
    	   int res = orderRealPayService.add(orderId, currencyId, amount, currencyCode, 0);
           if(res < 1)
        	   return 0;
       }
       return 1;	   
   }
    /**
     * 创建订单
     * @param orders
     * @param receiver
     */
    private void createOrders(Orders orders, Receiver receiver) {
        Receiver receiv = receiverMapper.selectByZid(receiver.getZid());
        Date date= new Date();
        orders.setZid(UuidUtil.getUUID());
        orders.setAddress(receiv.getAddress());
        orders.setAreaname(receiv.getAreaname());
        orders.setCompletedate(null);
        orders.setConsignee(receiv.getConsignee());
        orders.setFreight(BigDecimal.ZERO);
        orders.setInvoicecontent("");
        orders.setInvoicetitle("");
        orders.setIsallocatedstock(0);
        orders.setMemo("");
        orders.setOffsetamount(new BigDecimal("0"));
        orders.setPhone(receiv.getPhone());
        orders.setPrice(new BigDecimal("0"));
        orders.setShippingmethodname("包邮");
        orders.setSn("");
        orders.setStatus(0);
        orders.setWeight(0);
        orders.setZipcode(receiv.getZipcode());
        orders.setAreaId(receiv.getAreaId());
        orders.setCreateTime(date);
        orders.setUpdateTime(date);
    }

    public Map<String,Object> getOrderList(String zid, OrderVO orderVO) {
        if (orderVO.getPageNum() != null && orderVO.getPageSize() != null) {
            PageHelper.startPage(orderVO.getPageNum(), orderVO.getPageSize());
        }
        orderVO.setZid(zid);
        List<OrderVO> orderList = ordersMapper.getOrderList(orderVO);
        for (OrderVO vo:orderList) {
            List<OrderIteamVO> OrderIteamVOList = orderitemMapper.selectByOrdersId(vo.getPzid());
            for (OrderIteamVO orderIteamVO : OrderIteamVOList) {
				if(StringUtils.isEmpty(orderIteamVO.getPriceId())) {
					SkuPrice skurec = new SkuPrice();
		        	skurec.setSkuId(orderIteamVO.getSkuId());
		        	skurec.setPriceCode(vo.getPriceCode());
		        	if(StringUtils.isEmpty(vo.getIntegralCode())) {
		        		skurec.setPriceType(0);
		        	}else {
		        		skurec.setIntegralCode(vo.getIntegralCode());
		        	}
		        	skurec = skuPriceMapper.selectOne(skurec);
		        	if(skurec != null) {
		        		orderIteamVO.setPriceId(skurec.getZid());
		        	}
				}
			}
            vo.setOrderIteamVOList(OrderIteamVOList);
            Integer count = ordersMapper.selectCountBystatusAndCustId("5",zid,vo.getPzid(),null);
            vo.setDelivered(count);
             count = ordersMapper.selectCountBystatusAndCustId(null,zid,vo.getPzid(),null);
            vo.setOrderNum(count);
            Orders orders =ordersMapper.selectByZid(vo.getPzid());
            vo.setAmount(orders.getAmount());
            vo.setPriceCode(orders.getPriceCode());
            vo.setIntegralCode(orders.getIntegralCode());
            vo.setIntegralPrice(orders.getIntegralPrice());
            vo.setPrice(orders.getPrice());
        }
        PageInfo pageinfo = new PageInfo(orderList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("orderList", orderList);
        return map;
    }

    public Map<String,Object> getOrderDetail(String zid, OrderVO orderVO) {
        HashMap<String, Object> resultMap = new HashMap<>();
        OrderResponseVo orderList = orderitemMapper.selectbyOrderDetail(orderVO);
        //这个时候物流相关的数据还没查，接下来查询每个子订单物流相关的数据
        List<SorderVo> subOrderList = orderList.getSorderVoList();
        for(SorderVo vo :subOrderList){
        	SkuPrice skurec = new SkuPrice();
        	skurec.setSkuId(vo.getSkuId());
        	skurec.setPriceCode(vo.getPriceCode());
        	if(StringUtils.isEmpty(vo.getIntegralCode())) {
        		skurec.setPriceType(0);
        	}else {
        		skurec.setIntegralCode(vo.getIntegralCode());
        	}
        	skurec = skuPriceMapper.selectOne(skurec);
        	if(skurec != null) {
        		vo.setPriceId(skurec.getZid());
        	}
        	//根据子订单号查询到物流信息
        	List<OrderDetailShippingVo> volist= ordersMapper.getOrderShipping(vo.getSsn());
        	vo.getOrdershippings();
        	for(OrderDetailShippingVo svo:volist){
        		vo.getOrdershippings().add(svo);
        	}
      
        }
        OrderRealPay real = new OrderRealPay();
        real.setOrderId(orderList.getPzid());
        List<OrderRealPay> realList =  orderRealPayMapper.select(real);
        List<OrderDKVo>orderDKVo = new LinkedList<>();
        for (OrderRealPay orderRealPay : realList) {
			if("xianbei_all".equals(orderRealPay.getCurrencyCode()) || "wubei_all".equals(orderRealPay.getCurrencyCode())) {
				OrderDKVo dkVo = new OrderDKVo();
				dkVo.setDkCode(orderRealPay.getCurrencyCode());
				dkVo.setDkPrice(orderRealPay.getAmount());
				orderDKVo.add(dkVo);
			}else if("baofen_all".equals(orderRealPay.getCurrencyCode())) {
				orderList.setIntegralCode(orderRealPay.getCurrencyCode());
		        orderList.setIntegralPrice(orderRealPay.getAmount());
			}else if("CNY".equals(orderRealPay.getCurrencyCode())) {
				orderList.setRealCode(orderRealPay.getCurrencyCode());
		        orderList.setRealPrice(orderRealPay.getAmount());
			}
		}
        orderList.setOrderDKVo(orderDKVo);
        
        if(StringUtils.isEmpty(orderList.getRealCode())) {
        	Currency cureck = new Currency();
        	cureck.setCurrencysign(orderList.getCurrency());
        	cureck = currencyMapper.selectOne(cureck);
        	if(cureck != null) {
        		orderList.setRealCode(cureck.getCurrencycode());
        	}
        		
        }
        resultMap.put("order",orderList);
        Integer deliver= ordersMapper.selectCountBystatusAndCustId("5", zid, orderVO.getPzid(),null);
        Integer allNumber = ordersMapper.selectCountBystatusAndCustId(null, zid, orderVO.getPzid(),null);
        orderList.setOrderAllnum(allNumber);
        orderList.setDeliver(deliver);
        return resultMap;
    }

    public Map<String,Object> updateOrderStatus(String zid, OrderVO orderVO) {
        HashMap<String, Object> resultMap = new HashMap<>();
        Integer num = 0;
        if("concel".equals(orderVO.getType())){
            Integer unPayNumber= ordersMapper.selectCountBystatusAndCustId("0", zid, orderVO.getPzid(),null);
            Integer allNumber = ordersMapper.selectCountBystatusAndCustId(null, zid, orderVO.getPzid(),null);
            if(unPayNumber.equals(allNumber)){
                num = ordersMapper.updateOrderStatusByCustIdAndOrderZid(zid,orderVO.getPzid(),2,null);
                Orders o = new Orders();
                o.setZid(orderVO.getPzid());
                o = ordersMapper.selectOne(o);
                integralPayCancelService.integralPayCancel(o.getZid(), o.getCustId());
            }
        }else if("complete".equals(orderVO.getType())) {
            Integer yifahuoNumber= ordersMapper.selectCountBystatusAndCustId("5", zid, orderVO.getPzid(),null);
            Integer allNumber = ordersMapper.selectCountBystatusAndCustId(null, zid, orderVO.getPzid(),null);
            if(yifahuoNumber.equals(allNumber)) {
                num = ordersMapper.updateOrderStatusByCustIdAndOrderZid(zid, orderVO.getPzid(), 3,new Date());
                List<Orders> orders = ordersMapper.selectPSByZid(orderVO.getPzid());
                boolean isAddNewAddress=false;
                for (Orders orde :orders) {
                    Orderlog orderLog = createOrderLog("订单完成",3,orde.getZid());
                    orderlogMapper.insert(orderLog);
                    if(!isAddNewAddress){
                        Currency currencyParam = new Currency();
                        currencyParam.setZid(orde.getCurrencyId());
                        Currency currency = currencyMapper.selectOne(currencyParam);
                        //只有在澳元支付的时候增加收货地址
                        /*if(currency.getCurrencycode().equals("AUD")){
                            addNewAddress(orde);
                            isAddNewAddress=true;
                        }*/
                    }
                }
                if(num > 0){
                	log.info("完成订单后mq开始" + orderVO.getPzid());
                    kukaOrderRewardService.sendMqToKj(orderVO.getPzid());
                    //kukaOrderRewardService.sendMqToKJForTeamReward(orderVO.getPzid());
                }
            }
        }
        resultMap.put("updateNum",num);
        return resultMap;
    }

    private Orderlog createOrderLog(String detail,Integer type,String orderId) {
        Orderlog orderlog = new Orderlog();
        orderlog.setDetail(detail);
        orderlog.setType(type);
        orderlog.setOrderId(orderId);
        orderlog.setCreatedTime(new Date());
        return orderlog;
    }

    /**
     * 手动确认收货，检查是否要增加收货地址
     * @param o
     */
    private void addNewAddress(Orders o){
        try {
            Receiver receiver = new Receiver();
            receiver.setAreaId(o.getAreaId());
            receiver.setAddress(o.getAddress());
            receiver.setPhone(o.getPhone());
            receiver.setConsignee(o.getConsignee());
            List<Receiver> list = receiverMapper.select(receiver);
            if(list==null||list.size()==0){
                receiver.setAreaname(o.getAreaname());
                receiver.setCustId(o.getCustId());
                receiver.setIsdefault((byte) 0);
                receiver.setZipcode(o.getZipcode());
                receiver.setZid(UuidUtil.getUUID());
                receiverMapper.insertSelective(receiver);
            }
        } catch (Exception e) {
            log.error("保存收货地址出错！",e);
        }
    }
    /**
     * 订单支付成功后做的处理
     * @param
     */
    public void dealAfterPaySuccess(Paymenttransaction paymentTransaction,int cny_amount){
        //将支付事务设置为支付成功
        paymentTransaction.setPaystatus(1);
        BigDecimal realAmount = new BigDecimal(cny_amount).divide(new BigDecimal(100));
        paymentTransaction.setRealAmount(realAmount);
        paymentTransaction.setIssuccess(true);
        Currency currency = new Currency();
        currency.setCurrencycode("CNY");
        currency = currencyMapper.selectOne(currency);
        paymentTransaction.setRealCurrencyId(currency.getZid());
        paymentTransaction.setUpdatedTime(new Date());
        paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

        //获得对应主订单,并对总订单进行处理
        Date date = new Date();
        Orders order = getByPaymentTransaction(paymentTransaction);
        order.setStatus(1);//更新主订单状态为发货中 主订单1为发货中
        order.setExpire(null);
        order.setUpdateTime(date);
        //ordersMapper.updateByPrimaryKey(order);
        List<Orderlog> orderloglist = new ArrayList<Orderlog>();
        Orderlog log = new Orderlog();
        log.setCreatedTime(new Date());
        log.setOrderId(order.getZid());
        log.setType(1);
        log.setDetail("主订单支付成功");
        orderloglist.add(log);

        //获得对应子订单,并对子订单进行处理
        List<Orders> orders = getChildren(order);

        for(int i = 0; i < orders.size(); i ++){
            //更新子订单状态为代发货 子订单4为待发货
            Orders o = orders.get(i);
            o.setStatus(4);
            o.setExpire(null);
            o.setUpdateTime(date);

            List<Map<String,Object>> list = ordersMapper.selelctSellNumByOrder(o.getZid());
            ordersMapper.updateProductSellNum(list);
            //ordersMapper.updateByPrimaryKey(o);
            Orderlog orderlog = new Orderlog();
            orderlog.setCreatedTime(new Date());
            orderlog.setOrderId(o.getZid());
            orderlog.setType(4);
            orderlog.setDetail("子订单支付成功");
            orderloglist.add(orderlog);
        }
        orders.add(order);
        ordersMapper.batchUpdateOrderStatusAfterPayCallBack(orders);
        orderlogMapper.insertList(orderloglist);
    }

    private Orders getByPaymentTransaction(Paymenttransaction paymentTransaction){
        Orders order = new Orders();
        order.setZid(paymentTransaction.getOrderId());
        return ordersMapper.selectOne(order);
    }

    public List<Orders> getChildren(Orders order){
        Weekend<Orders> weekend = Weekend.of(Orders.class);
        WeekendCriteria<Orders,Object> Criteria =weekend.weekendCriteria();
        Criteria.andEqualTo(Orders::getOrderParent, order.getZid());
        weekend.setOrderByClause("update_time desc");
        return ordersMapper.selectByExample(weekend);
    }


    /**
     * 订单自动过期
     */
    public void orderExpire(){
    	List<Orders> orders = ordersMapper.selectExpireParentOrder();
    	Integer count = ordersMapper.updateExpireStatus();
    	
    	for(Orders o : orders){
    		integralPayCancelService.integralPayCancel(o.getZid(), o.getCustId());
    	}
    	log.info("取消订单数量：{}",count);
    }

    /**
     * 订单自动完成
     */
    public void orderComplate(){
        Date date = DateUtils.addDays(new Date(), IConstants.ORDER_AUTOCOMPLETE_TIME * -1);
    	//Date date = DateUtils.addMinutes(new Date(), 3 * -1);
        //分批查询发货中所有父订单
        List<Orders> ordersList = ordersMapper.selectByUpdateTime(date,null);
        //遍历父订单
        for (Orders order : ordersList) {
            //查看子订单是否都是已发货
            //验证订单的修改时间是否是25天的修改
            Integer yifahuoNumber= ordersMapper.selectCountBystatusAndCustId("5", order.getCustId(), order.getZid(),date);
            log.info("状态为5订单数量"+yifahuoNumber);
            Integer allNumber = ordersMapper.selectCountBystatusAndCustId(null, order.getCustId(), order.getZid(),null);
            log.info("订单总的数量"+allNumber);
            if(yifahuoNumber.equals(allNumber)) {
                //是
                //查询updateTime的最大订单
                int num = ordersMapper.updateOrderStatusByCustIdAndOrderZid(order.getCustId(), order.getZid(), 3,new Date());
                List<Orders> orders = ordersMapper.selectPSByZid(order.getZid());
                for (Orders orde :orders) {
                    Orderlog orderLog = createOrderLog("订单完成",3,orde.getZid());
                    orderlogMapper.insert(orderLog);
                }
                if(num > 0){
                	 log.info("自动收货完成订单后mq开始" + order.getZid());
                     kukaOrderRewardService.sendMqToKj(order.getZid());
                     //kukaOrderRewardService.sendMqToKJForTeamReward(order.getZid());
                }
            }
        }

    }

    public Map<String,Object> getOrders(OrderRequestVo orderRequestVo) {
        if (orderRequestVo.getPageNum() != null && orderRequestVo.getPageSize() != null) {
            PageHelper.startPage(orderRequestVo.getPageNum(), orderRequestVo.getPageSize());
        }

        List<Map<String,Object>> orderList = ordersMapper.selectOrdersList(orderRequestVo);
        PageInfo pageinfo = new PageInfo(orderList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("orderList", orderList);
        return map;
    }

    public String getCurrencyCode(String orderId){    	
    	return ordersMapper.selectCurrencyCodeByOrder(orderId);
    }
}
