package com.example.alipay.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.example.alipay.service.alipay.app.AliAppPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Blake on 2018/11/1
 * <p>
 * 支付宝支付-APP支付API
 */
@RestController
@RequestMapping("/api/pay/alipay/app-pay")
public class AliAppPayController {

    @Autowired
    private AliAppPayService aliAppPayService;

    /**
     * APP支付
     */
    @GetMapping
    public void aliAppPay(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {

        aliAppPayService.executeAppPay(request, response);
    }

    /**
     * APP支付之异步通知
     */
    @PostMapping("/notify")
    public String aliAppPaySyncNotify(HttpServletRequest request, HttpServletResponse response)
            throws AlipayApiException {

        aliAppPayService.receiveSyncNotify(request, response);

        return "APP支付之异步通知";
    }

}
