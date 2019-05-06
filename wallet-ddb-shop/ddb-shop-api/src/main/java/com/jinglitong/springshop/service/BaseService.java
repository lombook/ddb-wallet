package com.jinglitong.springshop.service;

import com.jinglitong.springshop.utils.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName BaseService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/18 14:12
 * @Version 1.0
 **/
public abstract class BaseService<T> {
    @Autowired
    private MyMapper<T> myMapper;

    public int add(T entity){
        return myMapper.insertSelective(entity);
    }

    public int addList(List<T> list){
        return myMapper.insertList(list);
    }

    public T selectByPrimaryKey(String id){
        return myMapper.selectByPrimaryKey(id);
    }

    public T selectOne(T entity){
        return myMapper.selectOne(entity);
    }

    public List<T> listAll(){ return myMapper.selectAll();}

    public List<T> list(T entity){
        return myMapper.select(entity);
    }

    public int updateByPrimaryKey(T entity){
        return myMapper.updateByPrimaryKeySelective(entity);
    }

    public int updateByExampleSelective(T entity, Example example){
        return myMapper.updateByExampleSelective(entity,example);
    }
}
