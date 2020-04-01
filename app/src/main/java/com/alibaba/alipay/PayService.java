package com.alibaba.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author：thisfeng
 * @time 2019/4/18 22:41
 * 支付服务流程：
 * 1、首先接收到第三方应用的绑定，请求支付。
 * <p>
 * 2、然后拉起我们的支付页面Activity ，拉起之后回绑服务，服务内定义并返回支付的动作类 给 PayActivity，
 * <p>
 * 3、通过用户输入的密码判断是否正确， 从服务连接中拿到 IBinder 也是就是这个支付的动作类， 进行服务的动作回调
 */
public class PayService extends Service {


    private static final String TAG = "PayService";

    private ThirdPartPayImpl thirdPartPay;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        String action = intent.getAction();
        Log.d(TAG, "action --->" + action);

        if (!TextUtils.isEmpty(action)) {
            if ("com.alibaba.alipay.THIRD_PART_PAY".equals(action)) {
                //通过 action 说明这是第三方要求我们支付宝进行支付
                thirdPartPay = new ThirdPartPayImpl();
                //提取指全局供外部交互时调用
                return thirdPartPay;
            }
        }

        //PayActivity 回绑服务 返回的对象 进行交互，无指定action 默认返回此对象
        return new PayAction();
    }

    /**
     * 定义支付宝的支付 服务逻辑动作
     */
    public class PayAction extends Binder {

        /**
         * 实际的支付是比较复杂的，比如说加密，向服务器发起请求，等待服务器的结果，多次握手等
         * <p>
         * 支付方法
         */
        public void pay(float payMoney) {
            Log.d(TAG, "pay money is --->" + payMoney);

            if (thirdPartPay != null) {
                //回调告诉远程第三方 支付成功
                thirdPartPay.onPaySuccess();
            }
        }

        /**
         * 用户点击界面上的取消/退出
         */
        public void onUserCancel() {
            if (thirdPartPay != null) {
                //回调告诉远程 支付失败
                thirdPartPay.onPayFaild(-1, "user cancel pay...");
            }
        }
    }

    /**
     * 第三方调用起 跨进程 进行支付
     */
    private class ThirdPartPayImpl extends ThirdPartPayAction.Stub {


        private ThirdPartPayResult callBack;

        @Override
        public void requestPay(String orderInfo, float payMoney, ThirdPartPayResult callBack) throws RemoteException {
            this.callBack = callBack;

            Log.d(TAG, "requestPay --->orderInfo:" + orderInfo + " payMoney:" + payMoney);

            //第三方应用发起请求，拉起 打开一个支付页面
            Intent intent = new Intent();
            intent.setClass(PayService.this, PayActivity.class);
            intent.putExtra(Const.KEY_BILL_INFO, orderInfo);
            intent.putExtra(Const.KEY_PAY_MONEY, payMoney);
            //新的 task 中打开
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        /**
         * 定义相同的方法，进行回调 ，给外部调用
         */
        public void onPaySuccess() {
            try {
                callBack.onPaySuccess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onPayFaild(int errorCode, String errorMsg) {
            try {
                callBack.onPayFaild(errorCode, errorMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}