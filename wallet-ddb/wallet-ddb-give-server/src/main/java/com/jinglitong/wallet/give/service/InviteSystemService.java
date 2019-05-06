package com.jinglitong.wallet.give.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class InviteSystemService {
   /* @Resource
    private InviteSystemMapper inviteSystemMapper;

    @Resource
    private DdbShoreholderMapper ddbShoreholderMapper;

    @Value("${ddb_app_id}")
    private String appId;

    public List<String> getInviteUser(String custerId){
        List<String> result = new ArrayList();
        boolean goon = true;
        while(goon){
            custerId = getNextCust(custerId,result);
            if(StringUtils.isEmpty(custerId) || result.size() >= 3){
                goon = false;
            }
        }
        return result;
    }

    private String getNextCust(String custId,List<String> result){
        InviteSystem inviteSystem = new InviteSystem();
        inviteSystem.setCustId(custId);
        inviteSystem.setAppId(appId);
        inviteSystem = inviteSystemMapper.selectOne(inviteSystem);
        if(null != inviteSystem) {
            checkLevelList(inviteSystem.getLevelOneCustId(),result);
            checkLevelList(inviteSystem.getLevelTwoCustId(),result);
            checkLevelList(inviteSystem.getLevelThreeCustId(),result);
            return inviteSystem.getLevelThreeCustId();
        }else {
        	return "";
        }
    }

    private void checkLevelList(String custId,List<String> result){
        if(null != custId && !StringUtils.isEmpty(custId)){
            DdbShoreholder ddbShoreholder = new DdbShoreholder();
            ddbShoreholder.setCustomerId(custId);
            ddbShoreholder = ddbShoreholderMapper.selectOne(ddbShoreholder);
            if(ddbShoreholder != null && !ddbShoreholder.getCustomerType().equals(4)){
                result.add(ddbShoreholder.getCustomerId()+","+ddbShoreholder.getCustomerType());
                log.info("股东邀请人："+ddbShoreholder.getCustomerId()+","+ddbShoreholder.getCustomerType());
            }
        }
    }*/
}
