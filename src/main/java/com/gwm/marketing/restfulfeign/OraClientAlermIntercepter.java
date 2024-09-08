package com.gwm.marketing.restfulfeign;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/26 13:20:43
 * @versio 1.0
 */
@Configuration
public class OraClientAlermIntercepter implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
    }
}
