package com.gwm.marketing.restfulfeign.dto;

import lombok.*;

import java.util.Objects;
/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/8/22 17:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ThirdAlarmDto {

    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 环境dev/test/uat/prod
     */
    private String env;
    /**
     * ip地址
     */
    private String ip;
    /**
     * traceId
     */
    private String traceId;
    /**
     * 异常信息
     */
    private String exMessage;
    /**
     * 接口名称
     */
    private String requestUri;
    /**
     * 请求方法get/post/put/delete
     */
    private String method;

    private Long timeOut;

    private String requestMsg;
    public ThirdAlarmDto(String exMessage, String requestUri) {
        this.exMessage = exMessage;
        this.requestUri = requestUri;
    }

    public ThirdAlarmDto(String requestUri) {
        this.requestUri = requestUri;
    }

    /**
     * todo:goupBy分组时候是根据equals和hashCode分组的
     * @param o 对于异常告警需要异常信息和接口名称都是同一个
     *          对于超时告警，接口名称相同即可
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ThirdAlarmDto tag = (ThirdAlarmDto) o;
        return requestUri.equals(tag.requestUri);
    }
    /**
     * 根据请求地址分组统计
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(requestUri);
    }

}
