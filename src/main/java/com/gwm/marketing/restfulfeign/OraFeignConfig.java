package com.gwm.marketing.restfulfeign;


import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

/**
 * @author fanht
 * @descrpiton 使用配置类声明http接口
 * @AutoConfigureBefore(FeignAutoConfiguration.class) 不使用默认的自动配置，手动实现okhttpclient配置到spring容器
 * @date 2022/7/25 16:00:30
 * @versio 1.0
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureAfter(FeignAutoConfiguration.class)
public class OraFeignConfig {

    private static final int DEFAULT_TIMEOUT = 10;


    /**
     * 注入自定义okHttpClient
     * @return
     */
    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient().newBuilder().
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                connectTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS).
                connectionPool(new ConnectionPool()).build();
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new OraFeignRequestIntercepter();
    }
    @Bean
    public Interceptor oraInterceptor(){
        return new OraClientAlermIntercepter();
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public Logger logger(){
        return new OraFeignLogger();
    }


    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties, OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        Integer maxTotalConnections = httpClientProperties.getMaxConnections();
        Long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    /**
     * 自定义请求日志拦截器
     * @param httpClientFactory
     * @param connectionPool
     * @param httpClientProperties
     * @return
     */
    @Bean
    public OkHttpClient httpClient(OkHttpClientFactory httpClientFactory, ConnectionPool connectionPool, FeignHttpClientProperties httpClientProperties) {
        Boolean followRedirects = httpClientProperties.isFollowRedirects();
        Integer connectTimeout = httpClientProperties.getConnectionTimeout();
        Boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
        return httpClientFactory.createBuilder(disableSslValidation)
                .connectTimeout((long)connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor( new OraClientAlermIntercepter())
                .build();
    }


}
