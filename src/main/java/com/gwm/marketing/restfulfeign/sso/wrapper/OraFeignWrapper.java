package com.gwm.marketing.restfulfeign.sso.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwm.marketing.common.config.BeanTechConfig;
import com.gwm.marketing.common.config.UserMemberLevelConfig;
import com.gwm.marketing.common.enums.SourceAppEnum;
import com.gwm.marketing.common.exception.BaseException;
import com.gwm.marketing.common.result.CommonResultStatus;
import com.gwm.marketing.common.util.BrandUtils;
import com.gwm.marketing.common.util.SignUtil;
import com.gwm.marketing.common.vo.beantech.BeanRestErrorCode;
import com.gwm.marketing.common.vo.beantech.BeanTechResponse;
import com.gwm.marketing.common.vo.beantech.BeanTechStringResponse;
import com.gwm.marketing.restfulfeign.dto.RequestParamDto;
import com.gwm.marketing.restfulfeign.sso.OraToBeanFeignClient;
import feign.Request;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpMethod.*;

/**
 * @author fanht
 * @descrpiton 欧拉迁移 每次header信息都可能会更改，如果每次都修改入参，之前的调用代码都会修改。入参修改为对象，如果入参有更改，只需要更改入参即可
 * @date 2022/10/10 15:18:03
 * @versio 1.0
 */
@Component
public class OraFeignWrapper<T>  {

    @Resource
    private BrandUtils brandUtils;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BeanTechConfig beanTechConfig;

    @Resource
    private OraToBeanFeignClient oraToBeanFeignClient;

    @Resource
    private UserMemberLevelConfig userMemberLevelConfig;

    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * POST请求,body参数不为空
     * @param dto
     * @param <T>
     * @return
     */
    public <T> BeanTechResponse<T> oraPostRequestBean(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto,POST.name());
            if(CollectionUtils.isEmpty(map)){
               throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.debug("sapp调用仙豆接口POST请求,header:{},body:{}",JSONObject.toJSONString(map),JSONObject.toJSONString(dto.getObj()));
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.postBeanThiredFeign(dto.getBeanTechUrl(),map,dto.getObj());
            logger.debug("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode()) ) {
                logger.debug("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error("调用仙豆返回失败,返回信息:" +  beanTechResponse.getDescription() + "入参:" +  objectMapper.writeValueAsString(dto.getObj()) );
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error(  "调用仙豆返回失败:"+JSONObject.toJSONString(dto.getObj()), e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }



    /**
     * GET请求,body参数不为空
     * @param dto
     * @param <T>
     * @return
     */
    public <T> BeanTechResponse<T> oraGetRequestBean(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto,GET.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.debug("ora调用仙豆接口GET请求,header:{},body:{}",JSONObject.toJSONString(map),JSONObject.toJSONString(dto.getObj()));
            BeanTechStringResponse<T> beanTechResponse = null;
            if(dto.getObj() instanceof  Map){
                beanTechResponse = oraToBeanFeignClient.getBeanThiredFeign(dto.getBeanTechUrl(),map, (Map<String, String>) dto.getObj());
            }else {
                Map<String,String> mapRequest = null;
                try {
                    Class<?> clazz = dto.getObj().getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    mapRequest = new HashMap<>();
                    for(Field field: fields){
                        field.setAccessible(true);
                        Object obj =  field.get(dto.getObj());
                        if(Objects.nonNull(obj)){
                            mapRequest.put(field.getName(),obj.toString());
                        }
                    }
                    logger.info("mapRequest:" + JSONObject.toJSONString(mapRequest));
                } catch (Exception e) {
                    logger.error("通过反射转换异常",e);
                }
                beanTechResponse = oraToBeanFeignClient.getBeanThiredFeign(dto.getBeanTechUrl(),map, mapRequest);
            }
            logger.debug("仙豆验证码登录返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.debug("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(dto.getObj())+ "调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }


    /**
     * get方法,body无参数
     * @param dto  请求的参数对象
     * @param <T>  返回结果
     * @return
     */
    public <T> BeanTechResponse<T> oraGetRequestBeanNoBodyParam(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto,GET.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口GET请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口GET请求body入参:" + JSONObject.toJSONString(dto.getObj()));
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.getBeanThiredFeignNoBodyParam(dto.getBeanTechUrl(),map);
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(dto.getObj())+ "调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }






    /**
     * DELETE方法,body有参数
     * @param dto  请求的参数对象
     * @param <T>  返回结果
     * @return
     */
    public <T> BeanTechResponse<T> oraDeleteRequestBean(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto, DELETE.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口DELETE请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口DELETE请求body入参:" + JSONObject.toJSONString(dto.getObj()));
            /**todo 此处需要校验 如果为空，不能用此方法*/
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.deleteBeanThiredFeign(dto.getBeanTechUrl(),map,dto.getObj() == null ?
                    null : objectMapper.convertValue(dto.getObj(),dto.getCla().getClass()));
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(dto.getObj())+ "调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }


    /**
     * DELETE方法,body无参数
     * @param dto  请求的参数对象
     * @param <T>  返回结果
     * @return
     */
    public <T> BeanTechResponse<T> oraDeleteRequestBeanNoBodyParam(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto, DELETE.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口DELETE请求head入参:" + JSONObject.toJSONString(map));
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.deleteBeanThiredFeignNoBodyParam(dto.getBeanTechUrl(),map);
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error("调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }



    /**
     * PUT方法,body有参数
     * @param dto  请求的参数对象
     * @param <T>  返回结果
     * @return
     */
    public <T> BeanTechResponse<T> oraPutRequestBean(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto, PUT.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口PUT请求head入参:" + JSONObject.toJSONString(map));
            logger.info("ora调用仙豆接口PUT请求body入参:" + JSONObject.toJSONString(dto.getObj()));
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.putBeanThiredFeign(dto.getBeanTechUrl(),map,dto.getObj() == null ?
                    null : objectMapper.convertValue(dto.getObj(),dto.getCla().getClass()));
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error( JSONObject.toJSONString(dto.getObj())+ "调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }


    /**
     * PUT方法,body无参数
     * @param dto  请求的参数对象
     * @param <T>  返回结果
     * @return
     */
    public <T> BeanTechResponse<T> oraPutRequestBeanNoBodyParam(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto, PUT.name());

            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口PUT请求head入参:" + JSONObject.toJSONString(map));
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.putBeanThiredFeignNoBodyParam(dto.getBeanTechUrl(),map);
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error( "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error("调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }


    /**
     * 公共方法 相比之前的,如果有新增参数 可以在此处处理逻辑
     * @param dto
     * @param method
     * @param <T>
     * @return
     */
    public  <T> Map<String, String> requestBeanMap(RequestParamDto dto,String method){
        try {
            String appKey = getBeanTechAppKey(dto.getSourceApp());
            String secretKey = this.getBeanTechSecretKey(dto.getSourceApp());
            //如果是GET请求，入参需要传map
            Map<String, String> requestMap = new HashMap<>();
            if(dto.getObj() instanceof  Map){
                requestMap  = GET.name().equals(method)? (Map<String, String>) dto.getObj() :null;
            }
            Map<String, String> map = SignUtil.getBeanTechHeader(method, dto.getBeanTechUrl(),
                    appKey, secretKey, dto.getObj(),requestMap , dto.getSourceApp(), dto.getSourceAppVer());
            if(dto.isHasToken()){
                map.put("accessToken", dto.getToken());
            }
             if(!StringUtils.isEmpty(dto.getBrandCode())){
                map.put("brandCode",dto.getBrandCode());
                 map.put("brandId",dto.getBrandCode());
            }

            if(!StringUtils.isEmpty(dto.getBeanId())){
                map.put("beanId",dto.getBeanId());
            }
            if(!CollUtil.isEmpty(requestMap) && StrUtil.isNotBlank(requestMap.get("brandId"))){
                map.put("brandId",requestMap.get("brandId"));
            }
            if(!StringUtils.isEmpty(dto.getCompanyCode())){
                map.put("companyCode",dto.getCompanyCode());
            }
            String traceId = MDC.get("traceId");
            if(StringUtils.isNotEmpty(traceId)){
                map.put("traceId",traceId);
                map.put("gwmRequestTimestamp", String.valueOf(System.currentTimeMillis()));
            }

             //todo 后续如果header有变动的 可以在此处处理
            return map;
        } catch (Exception e) {
            logger.error("==========调用仙豆接口拼接参数异常============",e);
            return new HashMap<>(4);
        }
    }

    /**
     * 自定义超时时间请求
     *
     * @param dto:
     * @return: com.gwm.marketing.common.vo.beantech.BeanTechResponse<T>
     * @author: brant
     * @version: v1
     * @date: 2023/7/18 14:19
     */
    public <T> BeanTechResponse<T> postRequestBean(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto,POST.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.debug("ora调用仙豆接口POST请求,header:{},body:{}",JSONObject.toJSONString(map),JSONObject.toJSONString(dto.getObj()));
            Integer connectTimeout = userMemberLevelConfig.getConnectTimeout();
            Integer readTimeout = userMemberLevelConfig.getReadTimeout();
            Request.Options options = new Request.Options(connectTimeout, readTimeout, true);
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.postBeanThiredFeignForTime(dto.getBeanTechUrl(),map,dto.getObj(),options);
            logger.debug("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode()) ) {
                logger.debug("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error("调用仙豆返回失败,返回信息:" +  beanTechResponse.getDescription() + "入参:" +  objectMapper.writeValueAsString(dto.getObj()) );
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error(  "调用仙豆返回失败:"+JSONObject.toJSONString(dto.getObj()), e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
        }
    }

    /**
     * 自定义超时时间请求
     *
     * @param dto:
     * @return: com.gwm.marketing.common.vo.beantech.BeanTechResponse<T>
     * @author: brant
     * @version: v1
     * @date: 2023/7/18 14:19
     */
    public <T> BeanTechResponse<T> getRequestBeanNoBody(RequestParamDto dto) {
        try {
            Map<String, String> map = this.requestBeanMap(dto,GET.name());
            if(CollectionUtils.isEmpty(map)){
                throw new BaseException(CommonResultStatus.BEAN_REQUEST_NULL_EXCEPTION);
            }
            logger.info("ora调用仙豆接口GET请求head入参:{}",JSONObject.toJSONString(map));
            Integer connectTimeout = userMemberLevelConfig.getConnectTimeout();
            Integer readTimeout = userMemberLevelConfig.getReadTimeout();
            Request.Options options = new Request.Options(connectTimeout, readTimeout, true);
            BeanTechStringResponse<T> beanTechResponse = oraToBeanFeignClient.getBeanThiredFeignForTime(dto.getBeanTechUrl(),map,options);
            logger.info("三方返回报文:" + JSONObject.toJSONString(beanTechResponse));
            if (beanTechResponse == null) {
                logger.error("调用仙豆返回信息为空,入参" + objectMapper.writeValueAsString(dto.getObj()));
                throw new BaseException(CommonResultStatus.BEAN_NULL);
            }
            if (BeanRestErrorCode.ORA_SUCCESS.equals(beanTechResponse.getCode())) {
                logger.info("调用仙豆返回信息,入参" + objectMapper.writeValueAsString(dto.getObj()));
                return new BeanTechResponse<>(BeanRestErrorCode.SUCCESS,beanTechResponse.getData());
            } else {
                logger.error(objectMapper.writeValueAsString(dto.getObj()) + "调用仙豆返回失败:", beanTechResponse.getDescription());
                throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
            }
        } catch (Exception e) {
            logger.error("getRequestBeanNoBody调用仙豆返回失败:", e);
            throw new BaseException(CommonResultStatus.BEAN_THIRD_ERROR);
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
            appKey = beanTechConfig.getOra().getPointAppKey();
        } else if (SourceAppEnum.PICKUP.getValue().equals(sourceApp)) {
            appKey = beanTechConfig.getPickup().getPointAppKey();
        }
        return appKey;
    }



    /**
     * 获取仙豆的SecretKey
     *
     * @param sourceApp
     * @return java.lang.String
     * @author
     * @date 16:01 2021/3/8
     */
    public String getBeanTechSecretKey(String sourceApp) {
        String secretKey = null;
        if (brandUtils.hasBrandConfig(sourceApp)) {
            secretKey = beanTechConfig.getOra().getPointSecretKey();
        } else if (SourceAppEnum.PICKUP.getValue().equals(sourceApp)) {
            secretKey = beanTechConfig.getPickup().getPointSecretKey();
        }
        return secretKey;
    }





}
