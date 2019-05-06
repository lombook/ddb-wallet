package com.jinglitong.wallet.server.common.BatchTransfer;

/**
 *批量转账常量
 */
public class BatchTransferConstants {

    /**
     * 状态 0：待验证
     */
    public static final int BATCH_TRANSFER_WAIT_VALID = 0;

    /**
     * 状态 1：验证通过
     */
    public static final int BATCH_TRANSFER_VALID_OK = 1;
    /**
     * 状态  2：发送成功
     */
    public static final int BATCH_TRANSFER_SEND_OK = 2;
    /**
     * 状态  3：发送失败
     */
    public static final int BATCH_TRANSFER_SEND_BAD = 3;
    /**
     * 状态 4：部分成功
     */
    public static final int BATCH_TRANSFER_PART_OK = 4;
    /**
     * 状态   5:转账中
     */
    public static final int BATCH_TRANSFER_SENDING = 5;


    /**
     * 0 ：未验证
     */

    public static final int BATCH_TRANSFER_DETAIL_WAIT_VALID = 0;
    /**
     *  1：验证通过
     */
    public static final int BATCH_TRANSFER_DETAIL_VALID_OK = 1;
    /**
     * 2：验证失败
     */
    public static final int BATCH_TRANSFER_DETAIL_VALID_BAD = 2;
    /**
     * 3：发币失败
     */
    public static final int BATCH_TRANSFER_DETAIL_SEND_BAD = 3;
    /**
     * 4：发币成功
     */
    public static final int BATCH_TRANSFER_DETAIL_SEND_OK = 4;

    /**
     * 默认登录密码
     */
    public static final String DEFAULT_LOGIN_PASSWORD = "9vWf62w0wWr63cN9HJ5fqUuLIIrgSfgLXAFM";

    /**
     *  注册反馈文件名
     */
    public static final String DEFAULT_REG_BACK_FILE_NAME = "注册反馈.xlsx";

    /**
     *  注册反馈文件名
     */
    public static final String DEFAULT_USER_INFO_BACK_FILE_NAME = "用户信息反馈.xlsx";

    /**
     * 模板错误
     */
    public static final String TEMPLATE_ERROR = "模板导入失败，请检查模板是否正确。";

    /**
     * 井通调用接口操作成功
     */
    public static String JINGTONG_CALL_SUCCESS = "tesSUCCESS";

    /**
     * 井通swt  标识
     */
    public static String JINGTONG_CURRENCY_SWT = "SWT";

    /**
     * 井通swt  标识
     */
    public static String JINGTONG_CURRENCY_CNT = "CNY";

    /**
     * 井通官网api 地址
     */
    public static String JINGTONG_OFFICIAL_API_URL = "https://api.jingtum.com/";

    /**
     * 未支付
     */
    public static int pay_info_pay_status_not_pay = 0;

    /**
     *申请支付成功
      */
    public static int pay_info_pay_status_apply_pay_ok = 1;
    /**
     *申请支付失败
     */
    public static int pay_info_pay_status_apply_pay_bad = 2;
    /**
     *支付成功
     */
    public static int pay_info_pay_status_pay_ok = 3;
    /**
     *支付失败
     */
    public static int pay_info_pay_status_pay_bad = 4;

    /**
     * 通知支付次数
     */
    public static final int NOTIFY_PAY_SELLER_TIMES = 6;


    /**
     * 访问key
     */
    public static final  String APP_KEY ="appkey";

    /**
     * APP数值
     */
    public static final String APP_KEY_VALUE = "mNUjxFfgPccqKUMwWMRgBp0yrftK0XLjFRB0Sm2QBYtNPAPGrPowM2xtpn24";

    /**
     * 时间戳
     */
    public static final  String TIMESTAMP ="timestamp";

    /**
     * 签名
     */
    public static final  String SIGN ="sign";


}
