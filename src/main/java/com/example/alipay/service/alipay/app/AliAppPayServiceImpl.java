package com.example.alipay.service.alipay.app;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.example.alipay.bean.AlipayConfigInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Blake on 2018/11/1
 */
@Service
public class AliAppPayServiceImpl implements AliAppPayService {

    private static final Logger logger = LoggerFactory.getLogger(AliAppPayServiceImpl.class);

    @Autowired
    private AlipayConfigInfo aliPayConfig;

    @Autowired
    private AlipayClient alipayClient;

    /**
     * 支付宝支付之APP支付
     *
     * @param request
     * @param response
     * @throws AlipayApiException
     * @throws IOException
     */
    @Override
    public void executeAppPay(HttpServletRequest request, HttpServletResponse response)
            throws AlipayApiException, IOException {

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest appPayRequest = new AlipayTradeAppPayRequest();

        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo("2018110210555601");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        appPayRequest.setBizModel(model);

        // TODO 待验证是否可访问
        appPayRequest.setNotifyUrl("http://alipay.example.com/api/pay/alipay/app-pay/notify");

        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse appPayResponse = alipayClient.sdkExecute(appPayRequest);

        response.setContentType("text/html;charset=" + aliPayConfig.getCharset());
        response.getWriter().write(appPayResponse.getBody());
        response.getWriter().flush();
        response.getWriter().close();

        logger.info("=========== 支付宝APP支付请求成功 =========== ");
    }

    /**
     * 支付宝APP支付之异步通知
     *
     * @param request
     * @param response
     * @return
     * @throws AlipayApiException
     */
    @Override
    public String receiveSyncNotify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)

        boolean flag = AlipaySignature.rsaCheckV1(params, aliPayConfig.getPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType());

        //4.对验签进行处理
        if (flag) {//验签通过
            //只处理支付成功的订单: 修改交易表状态,支付成功
            //更新交易表中状态
            return "success";
        } else {  //验签不通过
            System.err.println("验签失败");
            return "fail";
        }
    }

}
