package com.huanmedia.videochat.common.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.huanmedia.videochat.common.pay.OnPayListener;

import java.util.Map;

public class AliPayTools {

    private static final int SDK_PAY_FLAG = 1;
    private static OnPayListener sOnRequestListener;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        sOnRequestListener.onSuccess(resultInfo);
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        sOnRequestListener.onError("支付失败：还未安装支付宝");
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        sOnRequestListener.onError("支付失败：正在处理中");
                    } else if (TextUtils.equals(resultStatus, "5000")) {
                        sOnRequestListener.onError("支付失败：重复请求");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        sOnRequestListener.onError("支付失败：支付被取消");
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        sOnRequestListener.onError("支付失败：网络连接出错");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        sOnRequestListener.onError(resultStatus);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public static void aliPay(final Activity activity, String appid, boolean isRsa2, String alipay_rsa_private, AliPayModel aliPayModel, OnPayListener onRxHttp1) {
        sOnRequestListener = onRxHttp1;
        Map<String, String> params = AliPayOrderInfoUtil.buildOrderParamMap(appid, isRsa2, aliPayModel.getOut_trade_no(), aliPayModel.getName(), aliPayModel.getMoney(), aliPayModel.getDetail());
        String orderParam = AliPayOrderInfoUtil.buildOrderParam(params);

        String privateKey = alipay_rsa_private;

        String sign = AliPayOrderInfoUtil.getSign(params, privateKey, isRsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void aliPay(final Activity activity, OnPayListener onRxHttp1, String orderInfo) {
        sOnRequestListener = onRxHttp1;

        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void aliAuth(final Activity activity, OnPayListener onRxHttp1, String authInfo) {
        sOnRequestListener = onRxHttp1;

        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(authInfo, true);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //apiname=com.alipay.account.auth&app_id=xxxxx&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid=xxxxx&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id=20141225xxxx&sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D
    public static void aliAuth(final Activity activity, OnPayListener onAuthListener, String app_id, String pid, String target_id) {
        sOnRequestListener = onAuthListener;
        Map<String, String> params = AliPayOrderInfoUtil.buildAuthInfoMap(pid, app_id, target_id, true);
        String orderParam = AliPayOrderInfoUtil.buildAuthParam(params);

        String privateKey = "";
        String sign = AliPayOrderInfoUtil.getSign(params, privateKey, true);
        final String authInfo = orderParam + "&" + sign;

        Runnable authRunnable = () -> {
            AuthTask alipay = new AuthTask(activity);
            Map<String, String> result = alipay.authV2(authInfo, true);
            Log.i("msp", result.toString());
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(authRunnable);
        payThread.start();
    }
}