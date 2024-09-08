package com.gwm.marketing.restfulfeign.thirdapi;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.phantomthief.collection.BufferTrigger;
import com.gwm.marketing.restfulfeign.OraHttpClient;
import com.gwm.marketing.restfulfeign.alerm.OraRpcDingdingConfiguration;
import com.gwm.marketing.restfulfeign.dto.ThirdAlarmDto;
import org.apache.commons.collections4.collection.SynchronizedCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/8/22 9:14
 */
@Component
public class ThreadAlarmBufferTrigger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OraRpcDingdingConfiguration oraRpcDingdingConfiguration;

    /**
     * 触发三方告警
     */
    BufferTrigger<ThirdAlarmDto> bufferTriggerExMessage = BufferTrigger.<ThirdAlarmDto,List<ThirdAlarmDto>>simple()
            .maxBufferCount(1000)
            .interval(60, TimeUnit.SECONDS)
            .setContainer(() -> synchronizedList(new ArrayList<>()),List::add)
            .consumer(this::consumerThirdAlarmExmessage)
            .build();

    BufferTrigger<ThirdAlarmDto> bufferTriggerExTimeOut = BufferTrigger.<ThirdAlarmDto,List<ThirdAlarmDto>>simple()
            .maxBufferCount(1000)
            .interval(60, TimeUnit.SECONDS)
            .setContainer(() -> synchronizedList(new ArrayList<>()),List::add)
            .consumer(this::consumerThirdAlarmTimeOut)
            .build();


    public void exMessageEnque(ThirdAlarmDto dto){
        bufferTriggerExMessage.enqueue(dto);
    }

    public void timeOutEnque(ThirdAlarmDto dto){
        bufferTriggerExTimeOut.enqueue(dto);
    }

    private   void consumerThirdAlarmExmessage(List<ThirdAlarmDto> listDto){
        Map<ThirdAlarmDto,Long> listMap = listDto.stream()
                .collect(Collectors.groupingBy(t -> t,Collectors.mapping(m->new ThirdAlarmDto(m.getRequestUri()),Collectors.counting())));
        if(listMap.size() > 0){
            String message= "调用三方接口异常告警:1分钟内告警条数:" + (listMap.values().stream().mapToLong(Long::longValue).sum()) + "条";
            String result = listMap.entrySet().stream().map(t->thirdAlarmToStr(t.getKey())+ ":" + t.getValue() +"条").collect(Collectors.joining());
            sendDingdingAlerm(message + result, oraRpcDingdingConfiguration.getToken().getUrl());
        }
    }

    private   void consumerThirdAlarmTimeOut(List<ThirdAlarmDto> listDto){
        Map<ThirdAlarmDto,Long> listMap = listDto.stream().collect(Collectors.groupingBy(t -> t,Collectors.mapping(m->new ThirdAlarmDto(m.getRequestUri()),Collectors.counting())));
        if(listMap.size() > 0){
            String message= "调用三方接口超时告警:1分钟内告警条数:" + (listMap.values().stream().mapToLong(Long::longValue).sum()) + "条";
            String result = listMap.entrySet().stream().map(t->thirdAlarmToStr(t.getKey())+ ":" + t.getValue() +"条").collect(Collectors.joining());
            sendDingdingAlerm(message + result, oraRpcDingdingConfiguration.getToken().getUrl());
        }
    }

    private String thirdAlarmToStr(ThirdAlarmDto tag){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            String res = objectMapper.writeValueAsString(tag);
            //去除反斜杠
            return  res.replace("\\","");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private  void sendDingdingAlerm(String message, String dingdingTokenUrl) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        content.put("content", message);
        jsonObject.put("text", content);
        OraHttpClient oraHttpClient = new OraHttpClient();
        String response = null;
        try {
            String[] dingdingArr = dingdingTokenUrl.split(",");
            if (dingdingArr != null && dingdingArr.length > 0) {
                for (int i = 0; i < dingdingArr.length; i++) {
                    response = oraHttpClient.post(dingdingArr[i], jsonObject.toJSONString());
                }
            }
        } catch (IOException e) {
            logger.info("钉钉返回信息:" + response);
            logger.error("发送钉钉告警异常",e);
        }
    }
}
