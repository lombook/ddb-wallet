package com.jinglitong.wallet.ddbserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.CustomerRelation;
import com.jinglitong.wallet.ddbapi.model.view.FriendsChangeNoticeVo;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.CustomerRelationMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CustomerRelationService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerRelationMapper customerRelationMapper;

	/**
	 * 游戏向钱包后台同步信息：好友增加减少通知
	 * 
	 * @param vo
	 */
	public Integer insertcustomerRelation(FriendsChangeNoticeVo vo) {
		// TODO Auto-generated method stub
		// String flowId = vo.getFlowId();
		String action = vo.getAction();// 动作(‘A’添加/’D’删除)
		String friendId = vo.getFriendId();
		// String nonceStr = vo.getNonceStr();
		// String sign = vo.getSign();
		String userId = vo.getUserId();
		int num = 0;
		CustomerRelation selectByUserIdAndFriendId = null;
		// 由于 friendId 变成了 112,22,22，格式 所以 需要遍历
		String[] friendIdArray = friendId.split(",");
		for (int i = 0; i < friendIdArray.length; i++) {
			friendId = friendIdArray[i];

			// 逻辑不变
			// 首先 根据 userid friendid 查询 ， 不管是删除操作 还是 添加操作 都要进行查询
			selectByUserIdAndFriendId = customerRelationMapper.selectByUserIdAndFriendId(userId,
					friendId);
			// 然后 根据 friendid userid 查询
			if (selectByUserIdAndFriendId == null) {

				selectByUserIdAndFriendId = customerRelationMapper.selectByUserIdAndFriendId(friendId, userId);
			}

			if ("D".equalsIgnoreCase(action)) {// 删除
				if (selectByUserIdAndFriendId != null) {
					num = customerRelationMapper.delete(selectByUserIdAndFriendId);
				} else {
					num = 1;// 数据库中不存在 提示已删除成功
				}

			} else { // A 添加
				if (selectByUserIdAndFriendId == null) { // 没有重复数据 插入
					CustomerRelation customerRelation = new CustomerRelation();
					// 查询 userid 的 account
					Customer user = customerMapper.selectByCustId(userId);
					String userAccount = user.getAccount();
					// 查询 friendid 的 account
					user = customerMapper.selectByCustId(friendId);
					String friendAccount = user.getAccount();
					customerRelation.setAccount(userAccount);
					customerRelation.setAppId(user.getAppId());
					customerRelation.setCreateTime(DateUtils.getDateTime());
					customerRelation.setCustId(userId);
					customerRelation.setFriendAccount(friendAccount);
					customerRelation.setFriendId(friendId);
					num = customerRelationMapper.insert(customerRelation);
				} else {
					num = 1;// 数据库中已存在 提示添加成功
				}
			}

		}

		return num;
	}
}
