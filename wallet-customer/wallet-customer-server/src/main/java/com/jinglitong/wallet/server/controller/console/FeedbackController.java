package com.jinglitong.wallet.server.controller.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.FeedbackTypeEnum;
import com.jinglitong.wallet.server.service.FeedbackService;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinglitong.wallet.api.model.view.FeedbackVo;

@Controller
@RequestMapping(value="/console/")
public class FeedbackController extends BaseController{

	@Autowired
    FeedbackService feedbackService;
	
	/**
     * 查询反馈列表
     * create by zhu
     * @return
     */
	@ResponseBody
	@RequestMapping(value="getFeedbackList.json",method=RequestMethod.POST)
	public Map getFeedbackList(@RequestBody FeedbackVo vo ) {
		setGlobalAdminAppId(vo);
		Map map =feedbackService.getFeedbackList(vo);
		return map;
	}
	
	/**
     * 查询反馈类型
     * create by zhu
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "feedbackType.json", method = RequestMethod.POST)
    public Map getFeedbackType() {
    	List list = new ArrayList<>();
    	
    	Map map1=new HashMap<String,Object>();
    	map1.put("code", FeedbackTypeEnum.OPION_FEEDBACK.getCode());
    	map1.put("name", FeedbackTypeEnum.OPION_FEEDBACK.getName());
    	list.add(map1);
    	
    	Map map2=new HashMap<String,Object>();
    	map2.put("code", FeedbackTypeEnum.PRODUCT_SUGGEST.getCode());
    	map2.put("name", FeedbackTypeEnum.PRODUCT_SUGGEST.getName());
    	list.add(map2);
    	
    	Map map3=new HashMap<String,Object>();
    	map3.put("code", FeedbackTypeEnum.FEATURE_APPLY.getCode());
    	map3.put("name", FeedbackTypeEnum.FEATURE_APPLY.getName());
    	list.add(map3);
    	
    	Map map4=new HashMap<String,Object>();
    	map4.put("code", FeedbackTypeEnum.OTHER.getCode());
    	map4.put("name", FeedbackTypeEnum.OTHER.getName());
    	list.add(map4);
    	
        return JsonUtil.toJsonSuccess("查询成功", list);

    }

    /**
     * 反馈处理
     * createby   zhu
     */
    @ResponseBody
    @RequestMapping(value="updFeedback.json" ,method=RequestMethod.POST)
    public Map updFeedback(@RequestBody FeedbackVo vo) {
		/*setGlobalAdminAppId(vo);*/
    	int i =feedbackService.updFeedback(vo);
    	if(i==1){
    		return JsonUtil.toJsonSuccess("处理成功");
    	}else{
    		return JsonUtil.toJsonError(ErrorEnum.ERROR_31465.getCode(), "处理失败");
    	}
    	
    }
    
    
    
}
