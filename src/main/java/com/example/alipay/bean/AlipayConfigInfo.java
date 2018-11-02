package com.example.alipay.bean;

import java.util.Objects;

/**
 * Created by Blake on 2018/11/1
 */

public class AlipayConfigInfo {

    /**
     * 支付宝应用id
     */
    private String appid;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 商户持有私钥
     */
    private String privateKey;

    /**
     * 签名算法类型
     */
    private String signType;

    /**
     * 请求格式
     */
    private String format;

    /**
     * 字符集编码
     */
    private String charset;

    /**
     * 支付宝网关addr
     */
    private String gateway;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlipayConfigInfo that = (AlipayConfigInfo) o;
        return Objects.equals(appid, that.appid) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(signType, that.signType) &&
                Objects.equals(format, that.format) &&
                Objects.equals(charset, that.charset) &&
                Objects.equals(gateway, that.gateway);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appid, publicKey, privateKey, signType, format, charset, gateway);
    }
}
