package com.example.alipay.service.alipay.app;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Blake on 2018/11/1
 */
public interface AliAppPayService {

    /**
     * 支付宝支付-APP支付
     *
     * @param request
     * @param response
     * @throws AlipayApiException
     * @throws IOException
     */
    void executeAppPay(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException;

    /**
     * 支付宝APP支付之异步通知
     *
     * @param request
     * @param response
     * @return
     * @throws AlipayApiException
     */
    String receiveSyncNotify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException;
}
