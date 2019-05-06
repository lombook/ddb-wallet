package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.api.model.view.AppWalletVo;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.ddbserver.mapper.AppWalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class AppWalletService {

    @Resource
    AppWalletMapper appWalletMapper;


    /**
     * 新增app钱包信息
     *
     * @param appWallet
     * @return
     */
    public int insertAppWallet(AppWallet appWallet){
        return appWalletMapper.insert(appWallet);
    }


    /**
     * 修改app钱包信息
     *
     * @param appWallet
     * @return
     */
    public int updateAppWallet(AppWallet appWallet){
        return appWalletMapper.updateByPrimaryKeySelective(appWallet);
    }

    /**
     * 删除app钱包信息
     *
     * @param appWallet
     * @return
     */
    public int deleteAppWallet(AppWallet appWallet){
        return appWalletMapper.deleteByPrimaryKey(appWallet.getId());
    }

    /**
     * 查询app钱包信息
     *
     * @param appWallet
     * @return
     */
    public AppWallet getOneAppWallet(AppWallet appWallet){
        return appWalletMapper.selectOne(appWallet);
    }


    /**
     * 分页查询app钱包信息
     *
     * @param appWalletVo
     * @return
     */
    public PageInfo<AppWallet> queryAppWalletByPage(AppWalletVo appWalletVo){
        if (appWalletVo.getPage() != null && appWalletVo.getRows() != null) {
            PageHelper.startPage(appWalletVo.getPage(), appWalletVo.getRows());
        }
        OrderByHelper.orderBy("created_time desc");
        List<Map> members = appWalletMapper.selectAppWalletList(appWalletVo);
        PageInfo pageinfo = new PageInfo(members);
        return pageinfo;
    }


    public List<HashMap<String,Object>> getWalletName(Admin admin) {
        List<HashMap<String,Object>> hashMap = appWalletMapper.selectWalletName(admin);
        return hashMap;
    }

    public int updateStateAppWallet(AppWallet appWallet) {
        return  appWalletMapper.updateStateById(appWallet);
    }


    public List<Map<String, Object>> getPropertiesMap(PropertieVO vo) {
        if (!StringUtils.isEmpty(vo.getAppId())) {
          List<Map<String,Object>> map = appWalletMapper.getPropertiesMap(vo);
            return map;
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getPropMap(PropertieVO vo) {
        if (!StringUtils.isEmpty(vo.getgName()) || !StringUtils.isEmpty(vo.getgKey())) {
            List<AppWallet> res = appWalletMapper.selectByZid(vo.getAppId());
            List resList = new ArrayList();
            if (res.size() > 0) {
                Map<String, String> resMap =new HashMap();;
                resMap.put("group_key", "licai");
                if (res.get(0).getShowFinancing()) {
                    resMap.put("group_value", 1 + "");
                } else {
                    resMap.put("group_value", 0 + "");
                }
                resMap.put("key_desc", "");
                resList.add(resMap);
                return resList;
            }
            return null;
        } else {
            return null;
        }
    }

    public List<Map<String, String>> getProtocolMap(PropertieVO vo) {
       String url = appWalletMapper.getProtocolMap(vo.getAppId());
       List  list =new ArrayList<>();
       if(!StringUtils.isEmpty(url)){
           Map<String, String> resultMap = new HashMap<String, String>() {
               {
                   put("group_key", "protocol");
                   put("group_value", url);
               }
           };
           list.add(resultMap);
       }
       return list;
    }

    public List<Map<String, String>> getDownLoadMap(PropertieVO vo) {
        List<AppWallet> res = appWalletMapper.selectByZid(vo.getAppId());
        if(!StringUtils.isEmpty(res.get(0).getDownloadPage())){
            List<Map<String, String>>  list =new ArrayList<>();
            Map<String, String> resultMap = new HashMap<String, String>() {
                {
                    put("group_key", "downloadpage");
                    put("group_value", res.get(0).getDownloadPage());
                }
            };
            list.add(resultMap);
            return list;
        }
        return null;
    }


    public AppWallet checkWalletZid(String zid) {
        List<AppWallet> appWallets = appWalletMapper.selectByZid(zid);
        if(appWallets == null || appWallets.size() == 0){
            return null;
        }else {
            return appWallets.get(0);
        }

    }
}
