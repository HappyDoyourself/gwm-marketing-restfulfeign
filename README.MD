该项目主要是在通过feign来开启三方RPC调用时候，如果三方有超时或者异常，可以通过钉钉开启告警进行实时监控和打印，同时通过traceId来打通三方的调用链,当出现
超时或者耗时比较严重时候，能及时预警。
使用方式：参考：OraFeignSsoLoginClient
        调用方参考：uc服务的接口 ：不含有token的接口 /v1/user/loginByFeign/loginForCode、
                    #loginService.loginForCodeByFeign
           含有token的接口 v1/user/loginByFeign/getBindAccount
               