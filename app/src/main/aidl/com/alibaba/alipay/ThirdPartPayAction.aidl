// ThirdPartPayAction.aidl
package com.alibaba.alipay;

// Declare any non-default types here with import statements
//使用要导入完整包名！！
import com.alibaba.alipay.ThirdPartPayResult;

interface ThirdPartPayAction {
    /*
        发起支付请求 接口
        */
        void requestPay( String orderInfo, float payMoney,ThirdPartPayResult callBack);
}
