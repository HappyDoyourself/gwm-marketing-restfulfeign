package com.gwm.marketing.restfulfeign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanht
 * @descrpiton 入参封装，防止每次入参都有新增
 * @date 2022/10/10 16:43:16
 * @versio 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamDto<T> {

    /**请求的参数*/
    private Object obj;
    /**APP来源*/
    private String sourceApp;
    /**APP版本*/
    private String sourceAppVer;
    /**请求的URL地址*/
    private String beanTechUrl;
    /**对象class*/
    private Class<T> cla;
    /**是否需要传token true必填 原来的接口里面是否含有 accessToken，有的话必填*/
    private boolean hasToken;
    /**token值*/
    private String token;
    /**欧拉迁移 brandCode*/
    private String brandCode;
    /**欧拉迁移 APP、H5、HU(终端信息，app，H5或者车机)*/
    private String terminal;

    private String beanId;

    private String companyCode;

}
