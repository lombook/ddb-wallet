package com.jinglitong.springshop.servcie;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.entity.ProductCategory;
import com.jinglitong.springshop.mapper.ProductCategoryMapper;
import com.jinglitong.springshop.vo.request.CateTreeVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fyy
 * @create 2019-01-09-15:37}
 * 商品分类
 */
@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public List<ProductCategory> selectAll(){
    	return productCategoryMapper.selectAll();
    }
    /**
     * 
     * 功能说明:根据parentId,查询分类
     * @return
     */
    public List<CateTreeVo> selectProductCategoryList() {
    	List<CateTreeVo> list = productCategoryMapper.selectTree(0);
    	List<CateTreeVo> all  = new ArrayList<>();
    	for (int i = 0; i <list.size(); i++) {
    		list.get(i).setLevel(0);
    		sort(all,list.get(i));
		}
    	 System.out.println(JSON.toJSONString(all));
        return all;
    }
    /**
     * 
     * 功能说明:递归查询子分类
     * @param all
     * @param vo
     */
    public void sort(List<CateTreeVo> all,CateTreeVo vo) {
    	vo.setLevel(vo.getLevel() +1);
    	all.add(vo);
		List<CateTreeVo> list = productCategoryMapper.selectTree(vo.getId());
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setLevel(vo.getLevel());
			sort(all,list.get(i));
			}
    	
    }

    public List<ProductCategory> sellectBy2Grade() {
		List<ProductCategory> productCategoryList =	productCategoryMapper.selectByGrade(2);
		return productCategoryList;
    }
}
