package com.gwm.marketing.restfulfeign.sso;

import com.gwm.marketing.common.vo.beantech.BeanTechResponse;
import com.gwm.marketing.common.vo.beantech.BeanTechUserInfo;
import com.gwm.marketing.dto.beantech.LoginAccountDto;
import com.gwm.marketing.vo.beantech.ThirdAccountVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.util.List;
import java.util.Map;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/8/2 11:14:33
 * @versio 1.0
 */
@FeignClient(name = "feignSsoLoginMoving",url = "${gwm.sso.ssoUrl}")
public interface OraFeignSsoMovingClient {

    /**
     * 查询用户绑定的第三方账号
     *
     * @param headerMap
     * @return retrofit2.Call<com.gwm.marketing.common.vo.beantech.BeanTechResponse>
     * @author shengchangdong
     * @date 17:39 2021/3/8
     */
    @GetMapping("/app-api/api/v1.0/userAuth/getBindAccount")
    BeanTechResponse<BeanTechUserInfo>  getBindAccount(@RequestHeader Map<String, String> headerMap);


    /**
     * 手机+验证码登录
     *
     * @param headerMap
     * @param loginAccountDto
     * @return retrofit2.Call<com.gwm.marketing.dto.beantech.BeanTechResponse < com.gwm.marketing.common.vo.beantech.BeanTechUserInfo>>
     * @author shengchangdong
     * @date 16:03 2021/1/21
     */
    @PostMapping("/app-api/api/v1.0/userAuth/loginWithSMS")
    BeanTechResponse<BeanTechUserInfo> loginAccountWithSms(@RequestHeader Map<String, String> headerMap, @RequestBody LoginAccountDto loginAccountDto);

}
