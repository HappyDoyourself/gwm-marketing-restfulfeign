package com.gwm.marketing.restfulfeign;

import com.alibaba.fastjson.JSONObject;
import com.gwm.marketing.common.config.BeanTechConfig;
import com.gwm.marketing.common.enums.SourceAppEnum;
import com.gwm.marketing.common.util.SignUtil;
import com.gwm.marketing.restfulfeign.thirdapi.UserBeanTechConstants;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

import static org.springframework.http.HttpMethod.POST;


/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/26 10:36:14
 * @versio 1.0
 */
@Configuration
public class OraFeignRequestIntercepter implements RequestInterceptor {


    private static final String HTTP_REQUEST_START_TIMESTAMP = "ORA_HTTP_REQUEST_START_TIMESTAMP";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(!requestTemplate.hasRequestVariable(HTTP_REQUEST_START_TIMESTAMP)){
            //添加请求时间
            requestTemplate.header(HTTP_REQUEST_START_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        }
    }


}
