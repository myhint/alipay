package com.example.alipay.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.alipay.service.alipay.page.AliPagePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Blake on 2018/11/2
 * <p>
 * 支付宝-电脑网站支付API
 */
@RestController
@RequestMapping("/api/pay/alipay/page-pay")
public class AliPagePayController {

    @Autowired
    private AliPagePayService aliPagePayService;

    /**
     * 支付宝-电脑网站支付
     *
     * @param request
     * @param response
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    @GetMapping
    public AlipayTradePagePayResponse aliPagePay(HttpServletRequest request, HttpServletResponse response)
            throws AlipayApiException, IOException {

        return aliPagePayService.executePagePay(request, response);
    }

    /**
     * 支付宝电脑网站支付之异步通知
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/sync/notify")
    public String aliPagePaySyncNotify(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {

        return aliPagePayService.receiveSyncNotify(request, response);
    }

}
