package com.jinglitong.wallet.reportserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.reportserver.mapper.CustomerMapper;
import com.jinglitong.wallet.reportserver.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportUserService {

    @Autowired
    private CustomerMapper customerMapper;

    @Value("${ddb.tree.root}")
    private String ddbTreeRootId; 
    @Value("${ddb.appid}")
    private String ddbAppId; 




    public static  HashMap<String,String>  orderMap = new HashMap();
    static {
        orderMap.put("1","高科技种苗");
        orderMap.put("3","高科技种苗+栽培费(入股)");
        orderMap.put("4","2年元宝枫树(入股)");
        orderMap.put("5","3年元宝枫树(入股)");
        orderMap.put("6","4年元宝枫树(入股)");
        orderMap.put("7","5年元宝枫树(入股)");
        orderMap.put("8","枫王元宝枫籽油 单瓶(入股)");
        orderMap.put("9","枫王元宝枫籽油 3瓶/盒(入股)");
        orderMap.put("10","大脑卫士元宝枫籽油 单瓶(入股)");
        orderMap.put("11","大脑卫士元宝枫籽油 3瓶/盒(入股)");
        orderMap.put("12","2年元宝枫树*500棵(入股)");
        orderMap.put("13","2年元宝枫树*5棵(入股)");
        orderMap.put("14","2年元宝枫树*50棵(入股)");
    }

    public Map getUserInfo(Map map) {
        if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
        List<Map> userInfoList = customerMapper.selectByAccountInviteCodeAndNumber(map);

        PageInfo pageinfo = new PageInfo(userInfoList);
        HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",userInfoList);
       return dataList;
    }

    public Map getOrderGive(Map<String, String> map) {
        if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
        List<Map<String, Object>> orderGiveList = customerMapper.selectOrderAccount(map);
        List<String> testCusts = customerMapper.selectTestCusts();
        PageInfo pageinfo = new PageInfo(orderGiveList);
        for (Map<String,Object> ordMap:orderGiveList) {
        	if(ordMap != null && ordMap.get("shop_trade") != null){
        		if(testCusts.contains(customerMapper.selectCustIdbyOrder(ordMap.get("shop_trade").toString()))){
        			ordMap.put("isTester", "1");
        		}else{
        			ordMap.put("isTester", "0");
        		}
        	}
            Object shubei = ordMap.get("shubei");
            if(shubei != null){
                ordMap.put("shubei",new BigDecimal(shubei.toString()).divide(new BigDecimal("100")).toString());
            }
            Object miaobei = ordMap.get("miaobei");
            if(miaobei != null){
                ordMap.put("miaobei",new BigDecimal(miaobei.toString()).divide(new BigDecimal("100")).toString());
            }
            String rule_id = orderMap.get(ordMap.get("rule_id"));
            ordMap.put("rule_id",rule_id);
        }
        
        HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",orderGiveList);

        return dataList;
    }
    
    public Map<String ,Object> getteamActualDividends(Map<String ,String> map) {
        if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
        List<Map<String, Object>> teamActualDividends = customerMapper.selectteamActualDividends(map);

        List<String> testCusts = customerMapper.selectTestCusts();
        for(int i = 0; i < teamActualDividends.size(); i ++){
        	Map<String, Object> tmpmap = teamActualDividends.get(i);
        	if(tmpmap != null && tmpmap.get("shop_trade") != null){
        		if(testCusts.contains(customerMapper.selectCustIdbyOrder(tmpmap.get("shop_trade").toString()))){
        			tmpmap.put("isTester", "1");
        		}else{
        			tmpmap.put("isTester", "0");
        		}
        	}
        	
        }
        PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(teamActualDividends);
        
        HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",teamActualDividends);
       return dataList;
    }
    
    public Map<String ,Object> getRelationshipTree(Map<String ,String> map) {
        if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
        List<Map<String, Object>> teamActualDividends = customerMapper.selectRelationshipTree(map);

        PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(teamActualDividends);
        for (Map<String,Object> ordMap:teamActualDividends) {
            Object sum_amount = ordMap.get("sum_amount");
            if(sum_amount != null){
                ordMap.put("sum_amount",new BigDecimal(sum_amount.toString()).divide(new BigDecimal("100")).toString());
            }
            
            Object team_sum_amount = ordMap.get("team_sum_amount");
            if(team_sum_amount != null){
                ordMap.put("team_sum_amount",new BigDecimal(team_sum_amount.toString()).divide(new BigDecimal("100")).toString());
            }
        } 
        HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",teamActualDividends);
       return dataList;
    }
    
    public List<Map<String ,String>> getallChildren(Map<String ,String> map) {
    	if(map == null || map.get("id") == null){
    		map.put("id", ddbTreeRootId);
        	map.put("pId", null);
        	map.put("name", ddbTreeRootId);
    	}else{
    		map.put("name", map.get("id"));
    	}
    	
    	List<Map<String, String>> staticTree = breadthFirst(map);
        for(int i = 0; i < staticTree.size(); i ++){
        	if(staticTree.get(i) != null && staticTree.get(i).get("id") != null){
        		staticTree.get(i).put("name", staticTree.get(i).get("id"));
        	}
        }
       return staticTree;
    }
    
    public Map<String ,Object> getTreeChildren(Map<String ,String> map) {
    	Map<String ,Object> resmap = new HashMap<String ,Object>();
    	if(map == null || map.get("account") == null){
    		map.put("account", ddbTreeRootId);
    	}else{
    		map.put("label", map.get("account"));
    	}
    	map.put("id", ddbTreeRootId);
    	
    	
    	List<Map<String ,Object>> childrenlist = new ArrayList<Map<String ,Object>>();
    	List<Map<String, Object>> children = customerMapper.selectChildren(map.get("account"),ddbAppId);
    	for(int i = 0; i < children.size(); i ++){
    		Map<String ,Object> childmap = new HashMap<String ,Object>();
    		childmap.put("id", children.get(i).get("id")) ;
    		if(children.get(i).get("custName") != null && !children.get(i).get("custName").equals("")){
    			children.get(i).put("id", children.get(i).get("id") + "-" + children.get(i).get("custName"));
    		}
    		childmap.put("label", children.get(i).get("id"));    		
    		childrenlist.add(childmap);
    	}
    	
    	resmap.put("id", map.get("account"));
    	String custName = customerMapper.selectCustNamebyAccount(ddbAppId,map.get("account"));
    	if(custName != null && !custName.equals("")){
    		map.put("account", map.get("account") + "-" +custName);
    	}  	
    	resmap.put("label", map.get("account"));
    	resmap.put("children", childrenlist);
    	
    	return JsonUtil.toJsonSuccess("成功", resmap);
    }
    
    public Map<String ,Object> getListChildren(Map<String ,String> map) {
    	if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
    	if(map == null || map.get("account") == null){
    		map.put("account", ddbTreeRootId);
    	}
    	List<Map<String, Object>> selfchildren = customerMapper.selectselfChildren(map.get("account"),ddbAppId);
    	PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(selfchildren);
        for (Map<String,Object> ordMap:selfchildren) {
            Object sum_amount = ordMap.get("sum_amount");
            if(sum_amount != null){
                ordMap.put("sum_amount",new BigDecimal(sum_amount.toString()).divide(new BigDecimal("100")).toString());
            }
            
            Object team_sum_amount = ordMap.get("team_sum_amount");
            if(team_sum_amount != null){
                ordMap.put("team_sum_amount",new BigDecimal(team_sum_amount.toString()).divide(new BigDecimal("100")).toString());
            }
        } 
        if(selfchildren.size() > 0){
        	Map<String,Object> tempmap = selfchildren.get(0);
            String custName = customerMapper.selectCustNamebyAccount(ddbAppId,tempmap.get("account").toString());
        	if(custName != null && !custName.equals("")){
        		tempmap.put("cust_name", custName);
        	}
        }
        HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",selfchildren);
        return JsonUtil.toJsonSuccess("成功", dataList);
    }
    
    public Map<String ,Object> getOrders(Map<String ,String> map) {
    	if (map.get("page") != null && map.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("rows").toString()));
        }
    	map.put("appId", ddbAppId);
    	//订单号、订单金额、商品数量、用户账号、订单时间？
    	List<Map<String, Object>> orders = customerMapper.selectOrders(map);
    	PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(orders);
    	List<String> testCusts = customerMapper.selectTestCusts();
        for(int i = 0; i < orders.size(); i ++){
        	Map<String, Object> tmpmap = orders.get(i);
        	if(tmpmap != null && tmpmap.get("shop_trade") != null){
        		if(testCusts.contains(tmpmap.get("cust_id").toString())){
        			tmpmap.put("isTester", "1");
        		}else{
        			tmpmap.put("isTester", "0");
        		}
        	}
        	String rule_id = orderMap.get(tmpmap.get("rule_id"));
        	tmpmap.put("rule_id",rule_id);
        	
        }
    	
    	HashMap<String, Object> dataList = new HashMap<>();
        dataList.put("pageCount",pageinfo.getTotal());
        dataList.put("customers",orders);
        return JsonUtil.toJsonSuccess("成功", dataList);
    }
    
    
    
    
    
    private List<Map<String, String>> breadthFirst(Map<String, String> root) {
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Deque<Map<String, String>> nodeDeque = new ArrayDeque<Map<String, String>>();
        Map<String, String> node = null;
        nodeDeque.add(root);
        while (!nodeDeque.isEmpty()) {
            node = nodeDeque.peekFirst();
            nodeDeque.pop();
            //访问节点
            list.add(node);
            //获得节点的子节点，对于二叉树就是获得节点的左子结点和右子节点
            List<Map<String, Object>> children = customerMapper.selectChildren((String)(node.get("id")),ddbAppId);
            if (children != null && !children.isEmpty()) {
                for (Map child : children) {
                    nodeDeque.add(child);
                }
            }
        }
        return list;
    }

    public HashMap<String, String> getrules() {
        return orderMap;
    }
}
