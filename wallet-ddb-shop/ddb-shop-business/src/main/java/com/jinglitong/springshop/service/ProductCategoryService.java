package com.jinglitong.springshop.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.Brand;
import com.jinglitong.springshop.entity.ProductCategory;
import com.jinglitong.springshop.mapper.ProductCategoryMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.BrandVo;
import com.jinglitong.springshop.vo.request.CateTreeVo;
import com.jinglitong.springshop.vo.request.ProductCategoryVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
    
    public PageInfo<ProductCategoryVo> getCategoryList(ProductCategoryVo vo) {
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<ProductCategoryVo> list = productCategoryMapper.getCategoryList(vo);
		PageInfo<ProductCategoryVo> info = new PageInfo<ProductCategoryVo>(list);
		return info;
    }
    
    public void addCategory(ProductCategory vo) {
    	if(vo.getParentId() ==  0) {
    		vo.setParentId(0);
    		vo.setGrade(1);
    	}else {
    		ProductCategory  parentRec = new ProductCategory();
    		parentRec.setId(vo.getParentId());
    		parentRec = productCategoryMapper.selectOne(parentRec);
    		if(parentRec != null) {
    			vo.setGrade(parentRec.getGrade() + 1);
    		}else {
    			throw new RuntimeException("getParentId异常");
    		}
    	}
    	vo.setZid(UuidUtil.getUUID());
    	vo.setCreatedTime(new Date());
    	vo.setUpdatedTime(new Date());
    	int count = productCategoryMapper.insert(vo);
    	if(count != 1) {
    		throw new RuntimeException("入库异常");
    	}
    }
    
	public ProductCategory getCategoryById(ProductCategory vo) {

		Integer id = vo.getId();
		if(id == null) {
			throw new RuntimeException("参数异常");
		}
		ProductCategory b = productCategoryMapper.selectByPrimaryKey(id);
		
		return b;
	}
	
	public void delCategory(ProductCategory vo) {
		Integer id = vo.getId();
		if(id == null) {
			throw new RuntimeException("参数异常");
		}
		ProductCategory record = new ProductCategory();
		record.setParentId(id);
		List<ProductCategory> list = productCategoryMapper.select(record);
		if(list.size() == 0) {
			int count = productCategoryMapper.deleteByPrimaryKey(id);
			if(count != 1) {
				throw new RuntimeException("删除异常");
			}
		}else {
			throw new RuntimeException("删除异常");
		}
		
	}
	
	public void updateCategory(ProductCategory vo) {
		vo.setUpdatedTime(new Date());
		int count = productCategoryMapper.updateByPrimaryKeySelective(vo);
		if (count != 1) {
			throw new RuntimeException("修改异常");
		}
	}
}
