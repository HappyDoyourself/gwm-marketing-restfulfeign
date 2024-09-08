package com.gwm.marketing.restfulfeign;

import feign.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import static feign.Util.UTF_8;
import static feign.Util.checkNotNull;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/10/17 15:52:34
 * @versio 1.0
 */
public   final class  OraResponse extends InputStream implements Response.Body {
    private final InputStream inputStream;

    private final Integer length;


    public OraResponse(InputStream inputStream, Integer length) {
        this.inputStream = inputStream;
        this.length = length;
    }

    private static Response.Body orNull(InputStream inputStream, Integer length) {
        if (inputStream == null) {
            return null;
        }
        return new OraResponse(inputStream, length);
    }

    @Override
    public Integer length() {
        return length;
    }

    /***
     * 重写 使得能够多次读  默认是false
     * @return
     */
    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public InputStream asInputStream() {
        return inputStream;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Reader asReader() {
        return new InputStreamReader(inputStream, UTF_8);
    }

    @Override
    public Reader asReader(Charset charset) throws IOException {
        checkNotNull(charset, "charset should not be null");
        return new InputStreamReader(inputStream, charset);
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
