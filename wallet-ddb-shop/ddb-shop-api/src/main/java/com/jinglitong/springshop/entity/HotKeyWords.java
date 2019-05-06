package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

@Data
@ToString
@Table(name = "hot_key_words")
public class HotKeyWords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关键词
     */
    @Column(name = "key_words")
    private String keyWords;

    /**
     * 排序优先级0最大
     */
    private Integer orders;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取关键词
     *
     * @return key_words - 关键词
     */
    public String getKeyWords() {
        return keyWords;
    }

    /**
     * 设置关键词
     *
     * @param keyWords 关键词
     */
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords == null ? null : keyWords.trim();
    }

    /**
     * 获取排序优先级0最大
     *
     * @return orders - 排序优先级0最大
     */
    public Integer getOrders() {
        return orders;
    }

    /**
     * 设置排序优先级0最大
     *
     * @param orders 排序优先级0最大
     */
    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}