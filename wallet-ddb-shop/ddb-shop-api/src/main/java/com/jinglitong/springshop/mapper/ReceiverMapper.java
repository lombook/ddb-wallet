package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Receiver;
import com.jinglitong.springshop.utils.MyMapper;

public interface ReceiverMapper extends MyMapper<Receiver> {
  Receiver selectByDefault(String custId);

    Receiver selectByZid(String zid);
}