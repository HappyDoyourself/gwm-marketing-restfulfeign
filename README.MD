该项目主要是在通过feign来开启三方RPC调用时候，如果三方有超时或者异常，可以通过钉钉开启告警进行实时监控和打印，同时通过traceId来打通三方的调用链,当出现
超时或者耗时比较严重时候，能及时预警。
使用方式：参考：OraFeignSsoLoginClient
        调用方参考：uc服务的接口 ：不含有token的接口 /v1/user/loginByFeign/loginForCode、
                    #loginService.loginForCodeByFeign
           含有token的接口 v1/user/loginByFeign/getBindAccount
告警如下：
调用三方接口超时告警:1分钟内告警条数:1条{"applicationName":"gwm-marketing-uc-server","env":"test","ip":"192.168.153.25","traceId":"b8a627bbd4e344328258875529dc4368",
"requestUri":"http://gw-third-system-test.beantechyun.cn/user-member-service/version2/medal/query/identity/batch","method":"POST","timeOut":1064,"requestMsg":"{"brandId":"CCG001",
"beanIds":["3168553706810310656"]}"}:1条