package com.gwm.marketing.restfulfeign.sso;

import com.gwm.marketing.common.vo.beantech.BeanTechResponse;
import com.gwm.marketing.common.vo.beantech.BeanTechUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SSO接口调用-通过feign的方式来远程调用 测试使用
 * @author fanht
 * @version V1.0.0
 * @description
 * @date 2022-07-26
 **/

@FeignClient(name = "feignSsoLoginTest",url = "${gwm.sso.ssoUrl}")
public interface OraFeignSsoLoginClient {


    /**
     * 通过feign调用仙豆RPC - POST请求
     * @param beanThiredApiName  第三方请求名称
     * @param headerMap header入参
     * @param obj 请求对象
     * @return 返回结果
     */
    @PostMapping(value = "{beanThiredApiName}")
    <T>  BeanTechResponse<T>  postBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - GET请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap 请求对象
     * @param obj
     * @return 返回结果
     */
    @GetMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  getBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false) Object obj);


    /**
     * 通过feign调用仙豆RPC - GET请求 不带body参数
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap 请求对象
     * @return 返回结果
     */
    @GetMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  getBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);


    /**
     * 通过feign调用仙豆RPC - delete请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @DeleteMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  deleteBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - delete请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @return 返回结果
     */
    @DeleteMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  deleteBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);



    /**
     * 通过feign调用仙豆RPC - put请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @PutMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  putBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - patch请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @PatchMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  patchBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);


    /**
     * 通过feign调用仙豆RPC - put请求 body无请求参数
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @return 返回结果
     */
    @PutMapping(value = "{beanThiredApiName}")
    BeanTechResponse<BeanTechUserInfo>  putBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);



}

