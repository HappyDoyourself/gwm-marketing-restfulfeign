package com.gwm.marketing.restfulfeign.alerm;

/**
 * @author fanht
 * @descrpiton
 * @date 2022/7/26 15:40:38
 * @versio 1.0
 */
public class DingdingRpcAlerm {
    /**
     * 远程调用开关 false 关闭 true 打开
     */
    private boolean rpcSwitch;
    /**
     * rpc调用时长
     */
    private Long rpcTimeOut;

    public boolean isRpcSwitch() {
        return rpcSwitch;
    }

    public void setRpcSwitch(boolean rpcSwitch) {
        this.rpcSwitch = rpcSwitch;
    }

    public Long getRpcTimeOut() {
        return rpcTimeOut;
    }

    public void setRpcTimeOut(Long rpcTimeOut) {
        this.rpcTimeOut = rpcTimeOut;
    }

    @Override
    public String toString() {
        return "DingdingRpcAlerm{" +
                "rpcSwitch=" + rpcSwitch +
                ", rpcTimeOut=" + rpcTimeOut +
                '}';
    }
}
