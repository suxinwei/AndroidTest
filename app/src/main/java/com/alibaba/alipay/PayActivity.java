package com.alibaba.alipay;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suxinwei.authcode.R;

/**
 * @author：thisfeng
 * @time 2019/4/18 22:52
 * 支付页面
 */

public class PayActivity extends AppCompatActivity {

    private final String TAG = "PayActivity";

    private boolean isBind;

    private PayService.PayAction payAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        doBindService();
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (payAction != null) {
            payAction.onUserCancel();
        }
    }

    private void initView() {
        Intent intent = getIntent();
        String billInfo = intent.getStringExtra(Const.KEY_BILL_INFO);
        final float payMoney = intent.getFloatExtra(Const.KEY_PAY_MONEY, 0);

        TextView tvPayInfo = findViewById(R.id.tvPayInfo);
        TextView tvPayMoney = findViewById(R.id.tvPayMoney);
        final EditText edtPayPwd = findViewById(R.id.edtPayPwd);

        tvPayInfo.setText("支付账单：" + billInfo);
        tvPayMoney.setText("支付金额：¥" + payMoney);

        findViewById(R.id.btnCommit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = edtPayPwd.getText().toString().trim();

                if ("123456".equals(password) && payAction != null) {
                    //模拟如果密码输入成功就去调用支付，实际上应该请求后端进行加密验证
                    payAction.pay(payMoney);
                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PayActivity.this, "支付密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 支付页面回绑服务，等待获取服务中的支付结果
     * 因为我们的Activity 也要跟 服务 进行通讯，告诉服务通讯结果，所以也要绑定服务
     * <p>
     * 綁定服務
     */
    private void doBindService() {

        Intent intent = new Intent(this, PayService.class);
//        intent.setAction("com.alibaba.alipay.THIRD_PART_PAY");//回绑页面不需要指定这个了，这里不指定设置的话就会默认返回 了 PayAction 这个对象
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setPackage(this.getPackageName());
        isBind = bindService(intent, connection, BIND_AUTO_CREATE);

        Log.d(TAG, "Bind Pay Service..");
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            //从服务连接中拿到 支付动作
            payAction = (PayService.PayAction) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isBind && connection != null) {
            unbindService(connection);
            Log.d(TAG, "unBind Pay Service..");
            connection = null;
            isBind = false;
        }

    }
}