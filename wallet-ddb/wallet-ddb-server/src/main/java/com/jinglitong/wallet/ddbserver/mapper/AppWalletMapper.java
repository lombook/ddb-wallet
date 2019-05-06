package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.api.model.view.AppWalletVo;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface AppWalletMapper extends MyMapper<AppWallet> {
  /*  @Select("select zid,wallet_name from app_wallet")
    @Results({
            @Result(property = "zid",  column = "zid"),
            @Result(property = "walletName", column = "wallet_name")
    })*/
    List<HashMap<String,Object>> selectWalletName(Admin admin);

    HashMap<String,Object> getAppWalletBoolean(HashMap<String, Object> map);
    @Select("select sms_reg_code,sms_validation_code,mail_title, must_invite,show_financing,show_auto_invest from app_wallet where zid = #{appId}")
    @Results({
            @Result(property = "smsRegCode",  column = "sms_reg_code"),
            @Result(property = "smsValidationCode",  column = "sms_validation_code"),
            @Result(property = "mailTitle",  column = "mail_title"),
            @Result(property = "mustInvite",  column = "must_invite",javaType = Integer.class),
            @Result(property = "group_value", column = "show_financing",javaType = Integer.class),
            @Result(property = "showAutoInvest", column = "show_auto_invest",javaType = Integer.class)
    })
   List<Map<String,Object>> getPropertiesMap(PropertieVO vo);

    List<Map> selectAppWalletList(AppWalletVo appWalletVo);

    List<AppWallet> selectByZid(String appId);

    String getProtocolMap(String appId);
    @Update("update app_wallet set state = #{state} where id = #{id}")
    int updateStateById(AppWallet appWallet);
}