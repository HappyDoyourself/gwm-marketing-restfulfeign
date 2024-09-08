package com.gwm.marketing.restfulfeign.alerm;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/26 15:40:26
 * @versio 1.0
 */
public class DingdingToken {

    /**
     * 钉钉告警地址
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DingdingToken{" +
                "url='" + url + '\'' +
                '}';
    }
}
