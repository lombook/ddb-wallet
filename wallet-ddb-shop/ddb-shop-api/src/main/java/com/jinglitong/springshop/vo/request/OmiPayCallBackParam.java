package com.jinglitong.springshop.vo.request;

import lombok.Data;

/**
 * OmiPay支付回调函数接收的参数信息
 * @author prh10
 *
 */

@Data
public class OmiPayCallBackParam {
	
	/**
	 * 状态码 支付成功返回SUCCESS
	 */
	private String return_code;
	
	/**
	 * 随机字符串
	 */
	private String nonce_str;
	
	/**
	 * 时间戳
	 */
	private Long timestamp;
	
	/**
	 * 签名字符串
	 */
	private String sign;
	
	/**
	 * Omipay订单编号
	 */
	private String order_no;
	
	/**
	 * 外部订单号
	 */
	private String out_order_no;
	
	/**
	 * 订单支付货币编码
	 */
	private String currency;
	
	/**
	 * 金额 。单位为下单货币的最小货币值。例如currency=AUD，则100表示A$1.00
	 */
	private int total_amount;
	
	/**
	 * 订单创建时间 格式为yyyyMMddHHmmss
	 */
	private String order_time;
	
	/**
	 * 订单支付时间。订单支付时间，格式为yyyyMMddHHmmss；若订单未实际支付，返回空字符串
	 */
	private String pay_time;
	
	/**
	 * 汇率。 下单货币兑支付货币汇率，实际汇率的10^8倍
	 */
	private int exchange_rate;
	
	/**
	 * 人民币金额 ，单位为分。例如100表示￥1.00
	 */
	private int cny_amount;
	
}
