package com.example.alipay.service.alipay.page;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.alipay.bean.AlipayConfigInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Blake on 2018/11/2
 * <p>
 * 1. 统一收单下单并支付页面接口alipay.trade.page.pay：
 * 在支付宝端，partnerId与out_trade_no唯一对应一笔单据，商户端保证不同次支付out_trade_no不可重复，若重复，支付宝会关联到原单据，
 * 基本信息一致的情况下会以原单据为准进行支付。
 * 2. 统一收单交易退款接口alipay.trade.refund：
 * <p>
 * 2.1 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，支付宝将在收到退款请求并且
 * 验证成功之后，按照退款规则将支付款按原路退回到买家账号上。
 * 2.2 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款。
 * 2.3 支付宝退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
 * 2.4 一笔退款失败后重新提交，要采用原来的退款单号。
 * 2.5 总退款金额不能超过用户实际支付金额。
 * <p>
 * ====> 接口调试强烈建议使用 沙箱环境！接口在沙箱环境调通后，再转到线上环境进行测试与验收。
 * <p>
 * 3. 电脑网站支付沙箱接入注意点：
 * <p>
 * 电脑网站支付支持沙箱接入；在沙箱调通接口后，必须在线上进行测试与验收，所有返回码及业务逻辑以线上为准；
 * 电脑网站支付只支持余额支付，不支持银行卡、余额宝等其他支付方式；
 * 支付时，请使用沙箱买家账号支付；
 * 如果扫二维码付款时，请使用沙箱支付宝客户端扫码付款。
 */
@Service
public class AliPagePayServiceImpl implements AliPagePayService {

    private static final Logger logger = LoggerFactory.getLogger(AliPagePayServiceImpl.class);

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayConfigInfo aliPayConfig;

    /**
     * 支付宝支付之PC网站支付
     * <p>
     * 1.组装好相关params
     * 2.调起支付宝网站支付API
     * 3.调用response.getBody()，将其返回值直接通过httpServletResponse输出至前端HTML页面
     * ===>  httpServletResponse.getWriter().write(response.getBody());
     * 4.之后的工作将交由支付宝服务控制，展示付款二维码及本次交易的其他信息
     * 5.用户使用支付宝APP扫码完成支付业务。
     *
     * @param servletRequest
     * @param response
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    @Override
    public AlipayTradePagePayResponse executePagePay(HttpServletRequest servletRequest, HttpServletResponse response)
            throws AlipayApiException, IOException {

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        request.setBizContent("{" +
                "\"out_trade_no\":\"20160320010101001\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "\"total_amount\":4888," +
                "\"subject\":\"Iphone6 16G\"," +
                "\"body\":\"Iphone6 16G\"}");

        /**
         * 跳转地址就是支付完成之后，支付宝自动执行页面重定向,就是跳转到我们设置的页面
         // request.setReturnUrl("http://www.songshuiyang.site/return_url");
         * 通知地址就是支付宝会根据API中商户传入的notify_url，通过POST请求的形式将支付结果作为参数通知到商户系统。
         // request.setNotifyUrl("http://www.songshuiyang.site/notify_url");
         */

        // 正式调起 支付宝的电脑网站支付API请求
        AlipayTradePagePayResponse pagePayResponse = alipayClient.pageExecute(request);

        // 获取支付宝响应的Form表单
        String form = "";
        if (Objects.nonNull(pagePayResponse)) {
            form = pagePayResponse.getBody();
        }

        // 直接将完整的html表单输出至页面
        response.setContentType("text/html;charset=" + aliPayConfig.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();

        if (pagePayResponse.isSuccess()) { // StringUtils.isEmpty(subCode);
            logger.info("=========== 支付宝网站支付请求成功 =========== ");
        } else {
            logger.info("=========== 支付宝网站支付请求失败 =========== ");
        }

        return pagePayResponse;
    }

    /**
     * 支付宝电脑网站支付-异步通知
     *
     * @param request
     * @param response
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    @Override
    public String receiveSyncNotify(HttpServletRequest request, HttpServletResponse response)
            throws AlipayApiException, IOException {

        //将异步通知中收到的所有参数都存放到map中
        Map<String, String> paramsMap = new HashMap<>();
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, aliPayConfig.getPublicKey(),
                aliPayConfig.getCharset(), aliPayConfig.getSignType());

        if (signVerified) {
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验
            /**
             * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
             * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
             * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方
             * 4、验证app_id是否为该商户本身。
             */
            // 二次校验成功，继续商户自身业务处理，处理完成之后返回success
            PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.flush();
            writer.close();

            return "success";

        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            PrintWriter writer = response.getWriter();
            writer.write("failure");
            writer.flush();
            writer.close();
            return "failure";
        }
    }

}
