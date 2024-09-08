package com.gwm.marketing.restfulfeign;

import okhttp3.*;

import java.io.IOException;


/**
 * httpclient rpc
 *
 * @param
 * @author fanht
 */
public class OraHttpClient {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    final OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}