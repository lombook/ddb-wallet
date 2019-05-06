package com.jinglitong.wallet.checkserver.common;

import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConstantDict {
    //阻塞队列控制 大小为1W
    public final static BlockingQueue<KJNotice> ORDER_CONTROL = new LinkedBlockingQueue<KJNotice>(10000);
    //分红数据用户树缓存Map
    public static Map<String, CustomerInfo4LevelDiff> CUSTOMER_TREE_DATA = new HashMap<String, CustomerInfo4LevelDiff>();
    /**
	 * 分红制度：
	 * 		数据结构：{000011={1=a6a87f3701aa52a6b7a10b588f6a2f74_0, 2=a9705b7635e55e758668419ab5351d93_0, ... , 6=69dcd24e0e4b5c8396ea94673d9775a5_5},
	 * 			 	001100={1=a6a87f3701aa52a6b7a10b588f6a2f74_0, 2=a9705b7635e55e758668419ab5351d93_0, ... ,6=69dcd24e0e4b5c8396ea94673d9775a5_0},
	 * 				...}
	 * 		数据介绍：  1. "000011"为外层map的key,代表64中分红情况中的一种; "{1=a6a87f3701aa52a6b7a10b588f6a2f74_0, 2=a9705b7635e55e758668419ab5351d93_0, ... , 6=69dcd24e0e4b5c8396ea94673d9775a5_5}"为外层map的value,代表每种情况中6种股东级别在这种情况中的分红比例信息;
	 * 		 	    2. "1"为内层map的key，值为股东级别; "a6a87f3701aa52a6b7a10b588f6a2f74_0"为内层map的value;其中："a6a87f3701aa52a6b7a10b588f6a2f74"为该级股东在分红规则明细表中的zid;"0"该股东级别的分红比例。
	 */
	public static Map<String, Map<String,String>> INIT_BONUS_SYSTEM = new HashMap<>();
	
	 public static Map<String, CustomerInfo4LevelDiff> VALIDATA_TREE_DATA = new HashMap<String, CustomerInfo4LevelDiff>();
}
