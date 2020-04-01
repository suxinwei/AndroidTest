package com.alibaba.alipay;

interface ThirdPartPayResult {
    /*
    支付成功的回调
    */
    void onPaySuccess();
         /*
    支付失败的回调
    */
    void onPayFaild(in int errorCode , in String msg);

}