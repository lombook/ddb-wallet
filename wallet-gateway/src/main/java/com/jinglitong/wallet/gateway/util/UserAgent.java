package com.jinglitong.wallet.gateway.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class UserAgent {
  static UASparser uasParser = null;

  static {
    try {
      uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static UserAgentInfo userAgent(HttpServletRequest request){
    try {
      return uasParser.parse(request.getHeader("User-Agent"));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  public static void main(String[] args) {
//    String str = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A456";
    String str = "Mozilla/5.0 (Linux; Android 6.0.1; MX4 Build/MOB30M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.106 Mobile Safari/537.36";
    try {
      UserAgentInfo userAgentInfo = UserAgent.uasParser.parse(str);
      log.info("操作系统家族：" + userAgentInfo.getOsFamily());
      log.info("操作系统详细名称：" + userAgentInfo.getOsName());
      log.info("浏览器名称和版本:" + userAgentInfo.getUaName());
      log.info("类型：" + userAgentInfo.getType());
      log.info("浏览器名称：" + userAgentInfo.getUaFamily());
      log.info("浏览器版本：" + userAgentInfo.getBrowserVersionInfo());
      log.info("设备类型：" + userAgentInfo.getDeviceType());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
