package com.jinglitong.springshop.vo.request;

import com.jinglitong.springshop.entity.CartItems;
import com.jinglitong.springshop.entity.Receiver;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fyy
 * @create 2019-01-21-10:20}
 */
@Data
public class CalculateCartVo {

    List<CartItems> cartItemsList ;
    Receiver receiver;
    String nowBuy;
    BigDecimal deductionWBAmount = BigDecimal.ZERO;
    BigDecimal deductionXBAmount = BigDecimal.ZERO;
}
