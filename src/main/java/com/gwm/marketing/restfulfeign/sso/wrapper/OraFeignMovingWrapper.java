package com.gwm.marketing.restfulfeign.sso.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.common.config.BeanTechConfig;
import com.gwm.marketing.common.enums.SourceAppEnum;
import com.gwm.marketing.common.result.Result;
import com.gwm.marketing.common.util.BrandUtils;
import com.gwm.marketing.common.util.SignUtil;
import com.gwm.marketing.common.vo.beantech.BeanTechResponse;
import com.gwm.marketing.common.vo.beantech.BeanTechUserInfo;
import com.gwm.marketing.dto.beantech.LoginAccountDto;
import com.gwm.marketing.restfulfeign.sso.OraFeignSsoMovingClient;
import com.gwm.marketing.restfulfeign.thirdapi.UserBeanTechConstants;
import com.gwm.marketing.vo.beantech.ThirdAccountVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/8/2 11:13:46
 * @versio 1.0
 */
@Component
public class OraFeignMovingWrapper {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BrandUtils brandUtils;

    @Resource
    private BeanTechConfig beanTechConfig;

    @Resource
    private OraFeignSsoMovingClient oraFeignSsoMovingClient;


    /**
     * 仙豆查看绑定的账号
     * @param sourceApp
     * @param sourceAppVer
     * @param accessToken
     * @return
     */
    public Result<List<ThirdAccountVo>> getBindAccount(String sourceApp, String sourceAppVer, String accessToken) {
        try {
            String appKey = this.getBeanTechAppKey(sourceApp);
            String secretKey = this.getBeanTechSecretKey(sourceApp);
            Map<String, String> map = SignUtil.getBeanTechHeader(GET.name(), UserBeanTechConstants.BEANTECH_GETBINDACCOUNT_URL,
                    appKey, secretKey, null, null, sourceApp, sourceAppVer);
            map.put("accessToken", accessToken);
            logger.info("ora feign入参:" + JSONObject.toJSONString(map));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoMovingClient.getBindAccount(map);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error( "仙豆验证码登录异常;返回null");
                return Result.fail();
            }
            if (0 == beanTechResponse.getCode()) {
                logger.debug("仙豆验证码登录成功!");
                return Result.ok(beanTechResponse.getData());
            } else {
                logger.error("仙豆验证码登录失败:", beanTechResponse.getDescription());
                return Result.error(beanTechResponse.getCode(), beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error("仙豆验证码登录失败:", e);
            return Result.fail();
        }
    }



    public Result<BeanTechUserInfo>  loginAccountWithSms(LoginAccountDto loginAccountDto, String sourceApp, String sourceAppVer) {
        try {
            String appKey = this.getBeanTechAppKey(sourceApp);
            String secretKey = this.getBeanTechSecretKey(sourceApp);
            Map<String, String> map = SignUtil.getBeanTechHeader(POST.name(), UserBeanTechConstants.BEANTECH_LOGINWITHSMS_URL,
                    appKey, secretKey, loginAccountDto, null, sourceApp, sourceAppVer);
            logger.info(loginAccountDto.getAccount()+"ora feign入参:" + JSONObject.toJSONString(map));
            logger.info(loginAccountDto.getAccount()+"ora feign入参:" + JSONObject.toJSONString(loginAccountDto));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoMovingClient.loginAccountWithSms(map,loginAccountDto);
            logger.info(loginAccountDto.getAccount()+"仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(loginAccountDto.getAccount() + "仙豆验证码登录异常;返回null");
                return Result.fail();
            }
            if (0 == beanTechResponse.getCode()) {
                logger.debug(loginAccountDto.getAccount() + "仙豆验证码登录成功!");
                return Result.ok(beanTechResponse.getData());
            } else {
                logger.error(loginAccountDto.getAccount() + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return Result.error(beanTechResponse.getCode(), beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error(loginAccountDto.getAccount() + "仙豆验证码登录失败:", e);
            return Result.fail();
        }
    }


    /**
     * 获取仙豆的AppKey
     *
     * @param sourceApp
     * @return java.lang.String
     * @author shengchangdong
     * @date 16:00 2021/3/8
     */
    public String getBeanTechAppKey(String sourceApp) {
        String appKey = null;
        if (brandUtils.hasBrandConfig(sourceApp)) {
            appKey = beanTechConfig.getOra().getAppKey();
        } else if (SourceAppEnum.PICKUP.getValue().equals(sourceApp)) {
            appKey = beanTechConfig.getPickup().getAppKey();
        }
        return appKey;
    }

    /**
     * 获取仙豆的SecretKey
     *
     * @param sourceApp
     * @return java.lang.String
     * @author shengchangdong
     * @date 16:01 2021/3/8
     */
    public String getBeanTechSecretKey(String sourceApp) {
        String secretKey = null;
        if (brandUtils.hasBrandConfig(sourceApp)) {
            secretKey = beanTechConfig.getOra().getSecretKey();
        } else if (SourceAppEnum.PICKUP.getValue().equals(sourceApp)) {
            secretKey = beanTechConfig.getPickup().getSecretKey();
        }
        return secretKey;
    }


}
