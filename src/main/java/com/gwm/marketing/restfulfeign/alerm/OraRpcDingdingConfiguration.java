package com.gwm.marketing.restfulfeign.alerm;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author fanht
 * @Description
 * @Date 2021/11/12 下午6:34
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "dingding")
public class OraRpcDingdingConfiguration {


    private DingdingToken token;


    private  DingdingRpcAlerm rpc;


    public DingdingToken getToken() {
        return token;
    }

    public void setToken(DingdingToken token) {
        this.token = token;
    }

    public DingdingRpcAlerm getRpc() {
        return rpc;
    }

    public void setRpc(DingdingRpcAlerm rpc) {
        this.rpc = rpc;
    }

    /**
     * 应用名称
     */
    public static   String applicationName;

    /**
     * 当前环境
     */
    public static String env;



    @Value("${spring.cloud.nacos.discovery.metadata.env}")
    public void setEnv(String env) {
        OraRpcDingdingConfiguration.env = env;
    }

    @Value("${spring.application.name}")
    public  void setApplicationName(String applicationName) {
        OraRpcDingdingConfiguration.applicationName = applicationName;
    }




}
