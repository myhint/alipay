package com.example.alipay.service.alipay.page;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePagePayResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Blake on 2018/11/2
 */
public interface AliPagePayService {

    /**
     * 支付宝支付-电脑网站支付
     *
     * @param request
     * @param response
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    AlipayTradePagePayResponse executePagePay(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException;

    /**
     * 支付宝电脑网站支付-异步通知
     *
     * @param request
     * @param response
     * @return
     */
    String receiveSyncNotify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException;
}
