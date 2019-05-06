package com.jinglitong.wallet.ddbapi.model.view;

import com.jinglitong.wallet.api.model.view.PageVO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
public class DdbCustOrderTreeVO  extends PageVO {

    /**
     * 产品名
     */
    private String inteName;




}