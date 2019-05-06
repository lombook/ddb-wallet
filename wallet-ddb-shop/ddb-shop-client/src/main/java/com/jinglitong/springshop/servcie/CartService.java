package com.jinglitong.springshop.servcie;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.entity.Cart;
import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.entity.Sku;
import com.jinglitong.springshop.entity.SkuPrice;
import com.jinglitong.springshop.mapper.CartItemsMapper;
import com.jinglitong.springshop.mapper.CartMapper;
import com.jinglitong.springshop.mapper.SkuMapper;
import com.jinglitong.springshop.mapper.SkuPriceMapper;
import com.jinglitong.springshop.service.RedisService;
import com.jinglitong.springshop.utils.DateUtils;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.CalculateCartVo;
import com.jinglitong.springshop.vo.request.CartVo;
import com.jinglitong.springshop.vo.response.CartItemsVo;
import com.jinglitong.springshop.vo.response.CartResponseVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fyy
 * @create 2019-01-10-13:46}
 */
@Service
@Transactional
public class CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CartItemsMapper cartItemsMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SkuPriceMapper skuPriceMapper;

    /**
     * 查询购物车列表
     * @param cart
     * @return
     */
    public PageInfo<CartResponseVo> getCustomerCart(CartVo cart) {
        PageHelper.startPage(cart.getPageNum(), cart.getPageSize());
        List<CartResponseVo> cartItemsVoList = cartMapper.selectCustomerCart(cart);
        PageInfo<CartResponseVo> info = new PageInfo<CartResponseVo>(cartItemsVoList);
        return info;
    }

    /**
     * 添加商品
     * @param cart
     * @return
     */
    public Integer addCartItem(CartVo cart) {
        Integer flag = 1;//1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制
        Date date = new Date();

        if(cart.getCartItemsList() != null){
            List<CartItems> cartItemsList = cart.getCartItemsList();
            for (CartItems cartItem :cartItemsList) {
                cart.setSkuId(cartItem.getSkuId());
                cart.setSkuPriceId(cartItem.getSkuPriceId());
                flag =addCart(cart,flag,date,cartItem.getQuantity());
                if(flag != 1){
                    break;
                }
            }
        }else {
            flag = addCart(cart,flag,date,1);
        }
        return flag;
    }

    /**
     * 添加购物项
     * @param cart
     * @param flag
     * @param date
     */
    private Integer addCart(CartVo cart, Integer flag, Date date,int quality) {
    	//验证是否有该sku
    	SkuPrice spRecord = new SkuPrice();
    	spRecord.setZid(cart.getSkuPriceId());
    	List<SkuPrice> spRecordList = skuPriceMapper.select(spRecord);
    	if(spRecordList.size() != 1) {
    		flag= 6;
    		return flag;
    	}
        //验证是否有该sku
        Sku sku = skuMapper.selectByZid(cart.getSkuId());
        //验证购物车是否存在
        if(sku != null){
            Cart myCart = cartMapper.selectByCustIdOrClientId(cart);
            //购物车存在
            if(myCart != null){
                //验证商品是否存在
                CartItems cartItem = cartItemsMapper.selectByCartIdAndSkuId(myCart.getZid(),cart.getSkuId(),cart.getSkuPriceId());
                //商品存在
                if(cartItem != null){
                    //增加数量
                    Integer num =cartItemsMapper.ASOneNumByCartIdAndSkuId(
                            myCart.getZid(),cart.getSkuId(),cart.getSkuPriceId(),IConstants.CART_BIG_NUM,IConstants.CART_LITTLE_NUM,quality);
                    if(num <= 0){
                        flag = 2;
                    }
                }else {
                    //商品不存在
                    //验证购物车数量
                    if(cartItemsMapper.selectCountByCartId(myCart.getZid()) < IConstants.CART_CartBIG_NUM){
                        //添加商品
                        CartItems cartItems = new CartItems(UuidUtil.getUUID(),myCart.getZid(),quality,cart.getSkuId(),sku.getProductId(),cart.getSkuPriceId(),date ,date);
                        cartItemsMapper.insert(cartItems);
                        flag = 1;
                    }else {
                        flag = 4;
                    }
                }
            }else {
                //购物车不存在
                String cartId = UuidUtil.getUUID();
                Cart cart1 = new Cart(cartId, cart.getClientId(), cart.getCustId(), DateUtils.addDays(date, 1), "cart", date, date);
                cartMapper.insert(cart1);
                //创建购物车添加购物项
                CartItems cartItems = new CartItems(UuidUtil.getUUID(),cartId,quality,cart.getSkuId(),sku.getProductId(),cart.getSkuPriceId(),date ,date);
                cartItemsMapper.insert(cartItems);
            }
        }else {
            flag = 3;
        }
        return flag;
    }

    /**
     * 删除购物车商品
     * @param cart
     * @return
     */
    public Integer deleteCartItem(CartVo cart) {
        Integer flag = 1; //1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        //验证是否有购物车
        Cart myCart = cartMapper.selectByCustIdOrClientId(cart);
        //购物车存在
        if(myCart != null){
            //验证该购物车是否有该sku
            for (String skuPriceId : cart.getSkuPriceIdList()) {
                CartItems cartItem = cartItemsMapper.selectByCartIdAndSkuId(myCart.getZid(),null,skuPriceId);
                //存在该sku
                if(cartItem != null){
                    //删除该购物项
                    cartItemsMapper.deleteByPrimaryKey(cartItem.getId());
                    //验证该用户是否有其他购物项

                }else {
                    flag = 6;
                    return flag;
                }
            }
            Integer count =cartItemsMapper.selectCountByCartId(myCart.getZid());
            //无
            if(count == 0){
                //删除购物车
                cartMapper.deleteByPrimaryKey(myCart.getId());
            }
        }else {
            flag = 5;
        }
        return flag;
    }

    /**
     * 增减购物车商品数量
     * @param cart
     * @return
     */
    public Integer updateCartItemNum(CartVo cart) {
        Integer flag = 1;//1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        //验证是否有购物车
        Cart myCart = cartMapper.selectByCustIdOrClientId(cart);
        //购物车存在
        if(myCart != null){
            //验证该购物车是否有该sku,拿到数量
            CartItems cartItem = cartItemsMapper.selectByCartIdAndSkuId(myCart.getZid(),cart.getSkuId(),cart.getSkuPriceId());
            //存在
            if(cartItem != null){
                //判断是增还是减
                Integer num = cart.getNum();
                if("sub".equals(cart.getType())){
                    //减将数量*-1
                     num = cart.getNum() * -1;
                }
                //购物车数量和减去数量相加是否>=最小购物车常量 <=最大常量
                Integer resultNum = num + cartItem.getQuantity();
                if(resultNum >= IConstants.CART_LITTLE_NUM && resultNum <= IConstants.CART_BIG_NUM ){
                    //是修改数量
                    Integer update =cartItemsMapper.ASOneNumByCartIdAndSkuId(
                            myCart.getZid(),cart.getSkuId(),cart.getSkuPriceId(),IConstants.CART_BIG_NUM,IConstants.CART_LITTLE_NUM,num);
                    if(update ==0){
                        flag = 2;
                    }
                }else {
                    flag = 2;
                }
            }else {
                flag = 6;
            }

        }else {
            flag = 5;
        }
        return flag;
    }

    /**
     * 登录和未登录状态购物车同步
     * @param userName
     * @param clientId
     */
    public Integer synsClientIdToCustomerCart(String userName, String clientId) {
        Date date = new Date();
        Integer flag = 1;
        //查看该用户是否有未登录时产生的购物车
        CartVo cartVo = new CartVo();
        cartVo.setClientId(clientId);
        Cart sessioncart = cartMapper.selectByCustIdOrClientId(cartVo);
        //有
        if(sessioncart != null){
            //查看用户是否有登录时的购物车数据
            Customer customer = (Customer) redisService.getObject(userName);
            cartVo.setClientId("");
            cartVo.setCustId(customer.getCustId());
            Cart owncart = cartMapper.selectByCustIdOrClientId(cartVo);
            if(owncart != null){
                //有
                List<CartItems> cartItemsList = cartItemsMapper.selectByCartId(sessioncart.getZid());
                //比对修改
                for (CartItems cartItem : cartItemsList) {
                    CartItems liItem = cartItemsMapper.selectByCartIdAndSkuId(owncart.getZid(), cartItem.getSkuId(),cartItem.getSkuPriceId());
                    //存在
                    if(liItem != null){
                        //修改該數量
                        cartItemsMapper.ASOneNumByCartIdAndSkuId(owncart.getZid(),cartItem.getSkuId(),cartItem.getSkuPriceId(),IConstants.CART_BIG_NUM,IConstants.CART_LITTLE_NUM,cartItem.getQuantity());
                    }else {
                        //不存在插入
                        CartItems save = new CartItems(UuidUtil.getUUID(),owncart.getZid(),cartItem.getQuantity(),
                                cartItem.getSkuId(),cartItem.getProductId(),date,date);
                        cartItemsMapper.insert(save);
                    }
                }
                //清空未登录时的购物车
                cartMapper.deleteByCartId(sessioncart.getZid());
            }else {
                //无
                //将未登录购物车修改为已登录购物车
                //插入custId，清空clientId即可
                sessioncart.setClientId("");
                sessioncart.setCustId(customer.getCustId());
                cartMapper.updateByPrimaryKey(sessioncart);
            }
        }else {
            flag = 1;
        }
        return flag;
    }

    public List<Map<String,Object>> calculateCart(CalculateCartVo calculateCartVo,HashMap resultMap) {
        List<Map<String,Object>> skuList = cartMapper.calculateCart(calculateCartVo.getCartItemsList());
        resultMap.put("skuList",skuList);
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal integralsum = BigDecimal.ZERO;//积分总价
        
        String integralCode = null;//积分code
        String priceCode = null;
        for (Map<String,Object> map : skuList) {
        	if(map.get("integralCode") != null){
        		integralCode = (String)map.get("integralCode");
        	}
        	priceCode = (String)map.get("priceCode");
            for (CartItems cartItem : calculateCartVo.getCartItemsList()) {
                if(cartItem.getSkuPriceId().equals(map.get("skuPriceId"))){
                    BigDecimal price = (BigDecimal) map.get("price");
                    BigDecimal integral_price = (BigDecimal) map.get("integralPrice");
                    if(integral_price == null){
                    	integral_price = BigDecimal.ZERO;
                    }
                    Integer quantity = cartItem.getQuantity();
                    BigDecimal multiply = price.multiply(new BigDecimal(quantity+""));
                    sum = sum.add(multiply);
                    integralsum = integralsum.add(integral_price.multiply(new BigDecimal(quantity)));
                    map.put("quantity",cartItem.getQuantity());
                }
            }
        }
        resultMap.put("sum",sum);
        resultMap.put("priceCode",priceCode);
        resultMap.put("integralsum",integralsum);
        resultMap.put("integralCode",integralCode);
        return skuList;
    }
}
