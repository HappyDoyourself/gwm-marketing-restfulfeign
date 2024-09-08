package com.gwm.marketing.restfulfeign.sso;

import com.gwm.marketing.common.vo.beantech.BeanTechStringResponse;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author fanht
 * @descrpiton 欧拉迁移-feign调用 因为每个feign都一样，除了url地址不同。但是又不能把url写成固定的
 * @date 2022/10/11 18:15:58
 * @versio 1.0
 */
@FeignClient(name = "oraToBean",url = "${gwm.beantech.pointServerUrl}")
public interface OraToBeanFeignClient  {
    /**
     * 通过feign调用仙豆RPC - POST请求
     * @param beanThiredApiName  第三方请求名称
     * @param headerMap header入参
     * @param obj 请求对象
     * @return 返回结果
     */
    @PostMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T> postBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - POST请求 自定义超时时间
     * @param beanThiredApiName  第三方请求名称
     * @param headerMap header入参
     * @param obj 请求对象
     * @return 返回结果
     */
    @PostMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T> postBeanThiredFeignForTime(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj, Request.Options options);

    /**
     * 通过feign调用仙豆RPC - GET请求 todo:注意 如果RequestBody 含有入参，调用三方时候会转化为post请求 解决办法: https://www.itmuch.com/spring-cloud-sum/feign-multiple-params/
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap 请求对象
     * @param requestParam
     * @return 返回结果
     */
   //@GetMapping(value = "{beanThiredApiName}")
    @RequestMapping(path = "{beanThiredApiName}",method = RequestMethod.GET)
    <T> BeanTechStringResponse<T> getBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestParam(required = false) Map<String, String> requestParam);

    /**
     * 通过feign调用仙豆RPC - GET请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap 请求对象
     * @return 返回结果
     */
    @RequestMapping(path = "{beanThiredApiName}",method = RequestMethod.GET)
    <T> BeanTechStringResponse<T> getBeanThiredFeignForTime(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap,Request.Options options);

    /**
     * 通过feign调用仙豆RPC - GET请求 不带body参数
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap 请求对象
     * @return 返回结果
     */
    @GetMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  getBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);


    /**
     * 通过feign调用仙豆RPC - delete请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @DeleteMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  deleteBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - delete请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @return 返回结果
     */
    @DeleteMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  deleteBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);



    /**
     * 通过feign调用仙豆RPC - put请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @PutMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  putBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);

    /**
     * 通过feign调用仙豆RPC - patch请求
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @param obj
     * @return 返回结果
     */
    @PatchMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  patchBeanThiredFeign(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap, @RequestBody(required = false)  Object obj);


    /**
     * 通过feign调用仙豆RPC - put请求 body无请求参数
     * @param beanThiredApiName 第三方请求名称
     * @param headerMap
     * @return 返回结果
     */
    @PutMapping(value = "{beanThiredApiName}")
    <T> BeanTechStringResponse<T>  putBeanThiredFeignNoBodyParam(@PathVariable("beanThiredApiName")String beanThiredApiName, @RequestHeader Map<String, String> headerMap);


}
