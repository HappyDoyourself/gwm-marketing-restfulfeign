package com.gwm.marketing.restfulfeign.sso.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.marketing.common.config.BeanTechConfig;
import com.gwm.marketing.common.enums.SourceAppEnum;
import com.gwm.marketing.common.util.BrandUtils;
import com.gwm.marketing.common.util.SignUtil;
import com.gwm.marketing.common.vo.beantech.BeanRestErrorCode;
import com.gwm.marketing.common.vo.beantech.BeanTechResponse;
import com.gwm.marketing.common.vo.beantech.BeanTechUserInfo;
import com.gwm.marketing.restfulfeign.sso.OraFeignSsoLoginClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/29 15:18:03
 * @versio 1.0
 */
@Component
public class OraFeignSsoWrapper<T>  {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BrandUtils brandUtils;

    @Resource
    private BeanTechConfig beanTechConfig;

    @Resource
    private OraFeignSsoLoginClient oraFeignSsoLoginClient;

    private static ObjectMapper objectMapper = new ObjectMapper();



    /**
     *
     * @param obj 请求的参数队形
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * @param cla 对象class
     * method GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param hasToken 是否需要传token true必填 原来的接口里面是否含有 accessToken，有的话必填
     * @param token  token值
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraPostRequestBean(Object obj, String sourceApp, String sourceAppVer,String beanTechUrl,Class<T> cla,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(obj,sourceApp,sourceAppVer,beanTechUrl,POST.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口POST请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口POST请求body入参:" + JSONObject.toJSONString(obj));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.postBeanThiredFeign(beanTechUrl,map,cla);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录异常;返回null");
                 return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info(objectMapper.writeValueAsString(obj) + "仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                 return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(obj)+ "仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }



    /**
     *
     * @param obj 请求的参数队形
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * @param cla 对象class
     * method GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraGetRequestBean(Object obj, String sourceApp, String sourceAppVer,String beanTechUrl,Class<T> cla,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(obj,sourceApp,sourceAppVer,beanTechUrl, GET.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口GET请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口GET请求body入参:" + JSONObject.toJSONString(obj));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.getBeanThiredFeign(beanTechUrl,map,cla);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info(objectMapper.writeValueAsString(obj) + "仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(obj)+ "仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }


    /**
     *
     * @param obj 请求的参数队形
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * method GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraGetRequestBeanNoBodyParam(Object obj, String sourceApp, String sourceAppVer,String beanTechUrl,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(obj,sourceApp,sourceAppVer,beanTechUrl, GET.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口GET请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口GET请求body入参:" + JSONObject.toJSONString(obj));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.getBeanThiredFeignNoBodyParam(beanTechUrl,map);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info(objectMapper.writeValueAsString(obj) + "仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(obj)+ "仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }






    /**
     *
     * @param obj 请求的参数队形
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * @param cla 对象class
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraDeleteRequestBean(Object obj, String sourceApp, String sourceAppVer,String beanTechUrl,Class<T> cla,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(obj,sourceApp,sourceAppVer,beanTechUrl, DELETE.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口DELETE请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口DELETE请求body入参:" + JSONObject.toJSONString(obj));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.deleteBeanThiredFeign(beanTechUrl,map,cla == null ?
                    null : objectMapper.convertValue(obj,cla));
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info(objectMapper.writeValueAsString(obj) + "仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(obj)+ "仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }


    /**
     *
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraDeleteRequestBeanNoBodyParam(String sourceApp, String sourceAppVer,String beanTechUrl,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(null,sourceApp,sourceAppVer,beanTechUrl, DELETE.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口DELETE请求head入参:" + JSONObject.toJSONString(map));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.deleteBeanThiredFeignNoBodyParam(beanTechUrl,map);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info("仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error("仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error("仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }



    /**
     *
     * @param obj 请求的参数队形
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * @param cla 对象class
     * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraPutRequestBean(Object obj, String sourceApp, String sourceAppVer,String beanTechUrl,Class<T> cla,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(obj,sourceApp,sourceAppVer,beanTechUrl, PUT.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口PUT请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口PUT请求body入参:" + JSONObject.toJSONString(obj));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.putBeanThiredFeign(beanTechUrl,map,cla == null ?
                    null : objectMapper.convertValue(obj,cla));
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info(objectMapper.writeValueAsString(obj) + "仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(obj) + "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(obj)+ "仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }


    /**
     * put请求  body无参数
     * @param sourceApp APP来源
     * @param sourceAppVer APP版本
     * @param beanTechUrl 请求的URL地址
     * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE  org.springframework.http.HttpMethod
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * @param <T> 返回结果
     * @return
     */
    public <T> BeanTechResponse<BeanTechUserInfo> oraPutRequestBeanNoBodyParam(String sourceApp, String sourceAppVer,String beanTechUrl,boolean hasToken,String token) {
        try {
            Map<String, String> map = this.requestBeanMap(null,sourceApp,sourceAppVer,beanTechUrl, PUT.name(),hasToken,token);
            if(CollectionUtils.isEmpty(map)){
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口PUT请求head入参:" + JSONObject.toJSONString(map));
            BeanTechResponse<BeanTechUserInfo> beanTechResponse = oraFeignSsoLoginClient.putBeanThiredFeignNoBodyParam(beanTechUrl,map);
            logger.info("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("仙豆验证码登录异常;返回null");
                return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_NULL);
            }
            if (BeanRestErrorCode.SUCCESS == beanTechResponse.getCode()) {
                logger.info("仙豆验证码登录成功!");
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error( "仙豆验证码登录失败:", beanTechResponse.getDescription());
                return new BeanTechResponse<>(beanTechResponse.getCode(),beanTechResponse.getDescription());
            }
        } catch (Exception e) {
            logger.error("仙豆验证码登录失败:", e);
            return  new BeanTechResponse<>(BeanRestErrorCode.BEAN_THIRD_ERROR);
        }
    }
    
    

    /**
     *
     * @param requestBody 请求入参
     * @param sourceApp APP
     * @param sourceAppVer 版本号
     * @param beanTechUrl 仙豆url
     * @param method 请求方法
     * @param hasToken 是否需要传token true必填
     * @param token  token值
     * @param <T> 返回值
     * @return
     */
    public  <T> Map<String, String> requestBeanMap(T requestBody, String sourceApp, String sourceAppVer,String beanTechUrl,String method,boolean hasToken,String token){
        try {
            String appKey = getBeanTechAppKey(sourceApp);
            String secretKey = this.getBeanTechSecretKey(sourceApp);
            Map<String, String> map = SignUtil.getBeanTechHeader(method, beanTechUrl,
                    appKey, secretKey, requestBody, null, sourceApp, sourceAppVer);
            if(hasToken){
                map.put("accessToken", token);
            }
            return map;
        } catch (Exception e) {
            logger.error("==========调用仙豆接口拼接参数异常============",e);
            return new HashMap<>(4);
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
