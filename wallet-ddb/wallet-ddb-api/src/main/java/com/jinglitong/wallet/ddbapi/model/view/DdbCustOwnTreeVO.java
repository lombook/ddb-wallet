package com.jinglitong.wallet.ddbapi.model.view;

import com.jinglitong.wallet.api.model.view.PageVO;
import lombok.Data;

@Data
public class DdbCustOwnTreeVO extends PageVO {

    private String account;

    private String  treeNum;

}