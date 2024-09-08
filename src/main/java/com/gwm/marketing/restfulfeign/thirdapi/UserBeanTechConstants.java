package com.gwm.marketing.restfulfeign.thirdapi;

/**
 * 仙豆常量
 * @author shengchangdong
 * @since 2021-2-2 14:46:33
 */
public class UserBeanTechConstants {

    /**
     * 仙豆实名认证查询
     */
    public static  final String BEANTECH_GET_SIM_VERIFIED_STATE_URL =  "/bt-master-device/i/vehicle-device/getSimVerifiedState";
    /**
     * 仙豆获取验证码
     */
    public static  final String BEANTECH_GETSMSCODE_URL =  "/app-api/api/v1.0/userAuth/getSMSCode";

    /**
     * 仙豆校验验证码
     */
    public static  final String BEANTECH_CHECKSMSCODE_URL =  "/app-api/api/v1.0/userAuth/checkSMSCode";


    /**
     * 仙豆账号密码登录
     */
    public static  final String BEANTECH_LOGINACCOUNT_URL =  "/app-api/api/v1.0/userAuth/loginAccount";

    /**
     * 仙豆验证码登录
     */
    public static  final String BEANTECH_LOGINWITHSMS_URL =  "/app-api/api/v1.0/userAuth/loginWithSMS";


    /**
     * 仙豆第三方登录
     */
    public static  final String BEANTECH_LOGINWITHTRIPARTITE_URL =  "/app-api/api/v1.0/userAuth/loginWithTripartite";

    /**
     * 仙豆融合登录
     */
    public static  final String BEANTECH_LOGINSSOACCOUNT_URL =  "/app-api/api/v1.0/userAuth/loginSSOAccount";

    /**
     * 仙豆查看绑定的账号
     */
    public static  final String BEANTECH_GETBINDACCOUNT_URL =  "/app-api/api/v1.0/userAuth/getBindAccount";

    /**
     * 仙豆删除绑定的账号
     */
    public static  final String BEANTECH_DELETEBINDACCOUNT_URL =  "/app-api/api/v1.0/userAuth/deleteBindAccount";


    /**
     * 仙豆绑定信鸽
     */
    public static  final String BEANTECH_BINDXINGE_URL =  "/app-api/api/v1.0/weatherService/bind";


    /**
     * 仙豆刷新token
     */
    public static  final String BEANTECH_REFRESHTOKEN_URL =  "/app-api/api/v1.0/userAuth/refreshToken";


    /**
     * 仙豆添加积分
     */
    public static  final String BEANTECH_ADDPOINT_URL =  "/third-system-service/v1.0/user/addPoint";


    /**
     * 仙豆扣减积分
     */
    public static  final String BEANTECH_REDUCEPOINT_URL =  "/third-system-service/v1.0/user/reducePoint";


    /**
     * 仙豆查询积分总数
     */
    public static  final String BEANTECH_QUERYSUMPOINT_URL =  "/third-system-service/v1.0/user/querySumPoint";


    /**
     * 仙豆查询积分明细
     */
    public static  final String BEANTECH_QUERYDETAILPOINT_URL =  "/third-system-service/v1.0/user/queryDetailPoint";

    /**
     * 仙豆积分回退
     */
    public static  final String BEANTECH_REVERTADDPOINT_URL =  "/third-system-service/v1.0/user/revertAddPoint";

    /**
     * AMP三方登录判断已绑定
     */
    public static  final String AMP_LOGIN_SSO =  "SSO";

    /**
     * AMP三方登录判断已绑定
     */
    public static  final String AMP_LOGIN_ACCESSTOKEN =  "accessToken";


    /**
     * web端调用仙豆积分的APP版本号 默认值
     */
    public static  final String BEANTECH_APP_CVER =  "1.0.0";

    /**
     * 仙豆第三方未绑定状态码
     */
    public static final Integer BEANTECH_THIRD_CODE = 307999;



}
