package com.jinglitong.springshop.plugin;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinglitong.springshop.entity.PayConfig;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.servcie.PayConfigService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Plugin - 支付
 * 
 */
public abstract class PaymentPlugin implements Comparable<PaymentPlugin> {



    /**
     * Logger
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

	/**
	 * "支付方式名称"属性名称
	 */
	public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";

    /**
     * "支付币种"属性名称
     */
    public static final String PAYMENT_CURRENTCY = "currency";

	/**
	 * 默认超时时间
	 */
	public static final Integer DEFAULT_TIMEOUT = 24 * 60 * 60;

	/**
	 * ali同步回调
	 */
	public static final String ALIPAY_RESULT_VIEW_NAME = "/com/jinglitong/jmall/web/plugin/alipayMWebPayment/result";



    @Autowired
	private PayConfigService pluginConfigService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public abstract String getSiteUrl();
	
	@Autowired
	private com.jinglitong.springshop.mapper.PayConfigMapper payConfigMapper;
	
	private static Map<String, PayConfig> payConfigMap = new HashMap<>();

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		PayConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsenabled() : false;
	}
	public PayConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}
	@PostConstruct //指定该方法在对象被创建后马上调用 相当于配置文件中的init-method属性
	public void init(){		
		//到数据库中查找到所有的PayConfig 并将对象存放到payConfigMap中 zid为key		
		List<PayConfig> list = payConfigMapper.selectAll();
		for(int i = 0; i < list.size(); i ++){
			PayConfig payConfig = list.get(i);
			payConfigMap.put(payConfig.getZid(), payConfig);
		}
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		//PayConfig pluginConfig = getPluginConfig();
		PayConfig pluginConfig = payConfigMap.get(getId());
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}
	
	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName(){
		PayConfig pluginConfig = payConfigMap.get(getId());
		return pluginConfig != null ? pluginConfig.getName() : null;
	}

	/**
	 * 获取支付方式名称
	 * 
	 * @return 支付方式名称
	 */
	public String getPaymentName() {
		PayConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(PAYMENT_NAME_ATTRIBUTE_NAME) : null;
	}


	/**
	 * DABToken支付
	 */
	/**
	 * 连接Map键值对
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(key + "=" + (value != null ? value : ""));
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 连接Map值
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(value != null ? value : "");
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}
	
	
	/**
	 * 支付前处理
	 * 
	 * @param paymentPlugin
	 *            支付插件
	 * @param paymentDescription
	 *            支付描述
	 * @param extra
	 *            附加内容
	 * @throws Exception
	 */
	public abstract void prePayHandle(PaymentPlugin paymentPlugin, Paymenttransaction paymentTransaction,String paymentDescription, String extra) throws Exception;
	
	/**
	 * 支付处理
	 * 
	 * @param paymentPlugin
	 *            支付插件
	 * @param paymentDescription
	 *            支付描述
	 * @param extra
	 *            附加内容
	 * @throws Exception
	 */
	public abstract String payHandle(PaymentPlugin paymentPlugin, Paymenttransaction paymentTransaction,String paymentDescription, String extra);

	/**
	 * 支付后处理
	 * 
	 * @param paymentPlugin
	 *            支付插件
	 * @param paymentDescription
	 *            支付描述
	 * @param extra
	 *            附加内容
	 * @param isPaySuccess
	 *            是否支付成功
	 * @throws Exception
	 */
	public abstract void postPayHandle(PaymentPlugin paymentPlugin, String paymentDescription, String extra, boolean isPaySuccess) throws Exception ;
	
	/**
	 * 验签
	 * @param map 需要验证的参数map
	 * @param paymentPlugin 支付插件
	 * @return
	 */
	public abstract boolean veritySign(Map<String, Object> map,PaymentPlugin paymentPlugin);
	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		PaymentPlugin other = (PaymentPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

    public static class PayResult {
        private int code;
        private String message;

        public PayResult() {
            this.code = 0;
            this.message = "SUCCESS";
        }

        public PayResult(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}