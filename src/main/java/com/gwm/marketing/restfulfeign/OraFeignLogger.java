package com.gwm.marketing.restfulfeign;

import com.gwm.marketing.restfulfeign.alerm.OraRpcDingdingConfiguration;
import com.gwm.marketing.restfulfeign.dto.ThirdAlarmDto;
import com.gwm.marketing.restfulfeign.thirdapi.ThreadAlarmBufferTrigger;
import feign.Logger;
import feign.Request;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/26 14:09:39
 * @versio 1.0
 */
@Configuration
public class OraFeignLogger extends Logger {

    private static final int PASS_STATUS= 200;
    /**sso定义的400，此处不抛出*/
    private static final  int SSO_STATUS = 400;
    @Resource
    private OraRpcDingdingConfiguration oraRpcDingdingConfiguration;

    @Resource
    private ThreadAlarmBufferTrigger threadAlarmBufferTrigger;

    @Override
    protected Response logAndRebufferResponse(String configKey,
                                              Level logLevel,
                                              feign.Response response,
                                              long elapsedTime) throws IOException {
        /**todo 此处传的response 只能读一次，body底层用的是InputStream。里面有个isRepeatable()属性，属性为false.
         * True if asInputStream() and asReader() can be called more than once. 此处用了另外的实现，ByteArrayBody 这个属性默认是true。其实也可以自定义（比如OraResponse）,不过已经
         * 有了，就不在重复造轮子了
         *  https://github.com/OpenFeign/feign/issues/1187*/
        Response byteResponse = Response.builder().status(response.status()).reason(response.reason()).request(response.request()).headers(response.headers())
                .body(IOUtils.toString(response.body().asInputStream(),StandardCharsets.UTF_8),StandardCharsets.UTF_8).build();
        if(PASS_STATUS != byteResponse.status()  && SSO_STATUS != byteResponse.status()){
            Request request = byteResponse.request();
            String bodyMsg = request.body() != null ? new String(request.body()): "";
            if(byteResponse.body() != null){
                String returnRes = IOUtils.toString(byteResponse.body().asReader(StandardCharsets.UTF_8));
                returnRes = returnRes.length()>=1000?returnRes.substring(0,1000):returnRes;
                ThirdAlarmDto dto = ThirdAlarmDto.builder().applicationName(OraRpcDingdingConfiguration.applicationName).ip(OraIpUtil.initIp()).method(request.httpMethod().name())
                        .env(OraRpcDingdingConfiguration.env).requestUri(request.url()).traceId(MDC.get("traceId")).exMessage(returnRes).timeOut(elapsedTime).requestMsg(StringUtils.isEmpty(bodyMsg)?"请求参数为空":bodyMsg)
                        .build();
                threadAlarmBufferTrigger.exMessageEnque(dto);
            } else {
                ThirdAlarmDto dto = ThirdAlarmDto.builder().applicationName(OraRpcDingdingConfiguration.applicationName).ip(OraIpUtil.initIp()).method(request.httpMethod().name())
                        .env(OraRpcDingdingConfiguration.env).requestUri(request.url()).traceId(MDC.get("traceId")).timeOut(elapsedTime).requestMsg(StringUtils.isEmpty(bodyMsg)?"请求body参数为空":bodyMsg)
                        .build();
                threadAlarmBufferTrigger.exMessageEnque(dto);
            }
        } else if (elapsedTime > oraRpcDingdingConfiguration.getRpc().getRpcTimeOut()) {
            Request request = byteResponse.request();
            String bodyMsg = request.body() != null ? new String(request.body()): "";
            try {
                //todo 不去解析bodyData，因为在debug模式下会出现 stream is close的问题 虽然能解决（参考解决方案：https://github.com/OpenFeign/feign/issues/1208），但是debug模式经常使用 不建议。这样会导致的问题是 钉钉告警不能打印显示三方返回的信息
                ThirdAlarmDto dto = ThirdAlarmDto.builder().applicationName(OraRpcDingdingConfiguration.applicationName).ip(OraIpUtil.initIp()).method(request.httpMethod().name())
                        .env(OraRpcDingdingConfiguration.env).requestUri(request.url()).traceId(MDC.get("traceId")).timeOut(elapsedTime).requestMsg(StringUtils.isEmpty(bodyMsg)?"请求body参数为空":bodyMsg)
                        .build();
                threadAlarmBufferTrigger.timeOutEnque(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return byteResponse;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
    }



}
