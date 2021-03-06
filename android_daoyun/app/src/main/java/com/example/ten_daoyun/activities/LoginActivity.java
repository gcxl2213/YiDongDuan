package com.example.ten_daoyun.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.LoginBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.qqdemo.APPConstant;
import com.example.ten_daoyun.qqdemo.BaseUiListener;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.LogUtil;
import com.example.ten_daoyun.utils.ToastUtil;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ten_daoyun.utils.Util.md5;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.bt_go_password)
    Button btGoPassword;
    @BindView(R.id.bt_go_verify)
    Button btGoVerify;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.register_go)
    Button fab;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.send_verify_code)
    Button sendVerifyCode;
    @BindView(R.id.layout_password)
    LinearLayout layoutPassword;
    @BindView(R.id.layout_vercode)
    LinearLayout layoutVerCode;
    ProgressDialog progressDialog;
    //???????????????
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    @BindView(R.id.qq_go)
    Button qqGo;
    boolean typeStateStu = true;//???????????????qq??????
    @BindView(R.id.bt_switch_login_type)
    Button btSwitchType;

    int id;
    Boolean loginTypeVerifyCode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
        //????????????APPID?????????Context?????????
        mTencent = Tencent.createInstance(APPConstant.APP_ID, LoginActivity.this.getApplicationContext());
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("?????????....");
        progressDialog.setCancelable(false);
        loginTypeVerifyCode = false;
    }

    private void initData() {
        verifyStoragePermissions(this);
        String phone=SessionKeeper.getUserEmail(LoginActivity.this);
        if(phone!=""){
            etUsername.setText(phone);
            etPassword.setText(SessionKeeper.getUserPassword(LoginActivity.this));
            if (SessionKeeper.getAutoLogin(this)) {
                onBtGoPasswordClicked();
            }
        }
        loginTypeVerifyCode = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.bt_go_password)
    public void onBtGoPasswordClicked() {
        btGoPassword.setClickable(false);
        if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                String pwd = etPassword.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("phone", etUsername.getText().toString());
                params.put("password", pwd.length() > 20 ? pwd : md5(pwd));
                HttpUtil.login(params, new BaseObserver<LoginBean>() {
                    @Override
                    protected void onSuccess(LoginBean loginBean) {
                        if (loginBean.getCode().equals("20000")) {
                            saveData(loginBean.getData());
                            id = loginBean.getId();
                            loginSuccess();
                        } else {
                            btGoPassword.setClickable(true);
                            SessionKeeper.keepAutoLogin(getApplicationContext(),false);
                            ToastUtil.showMessage(LoginActivity.this, loginBean.getStatus(), ToastUtil.LENGTH_LONG);
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {
                        SessionKeeper.keepAutoLogin(getApplicationContext(),false);
                        btGoPassword.setClickable(true);
                        if (isNetWorkError)
                            ToastUtil.showMessage(LoginActivity.this, "??????????????????????????????", ToastUtil.LENGTH_LONG);
                        else {
                            ToastUtil.showMessage(LoginActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
                            LogUtil.e("LoginRequestError", "not network error ", e);
                        }
                    }
                });
            }
        }

    @OnClick(R.id.bt_go_verify)
    public void onBtGoVerifyClicked() {
        btGoVerify.setClickable(false);
        if (!etUsername.getText().toString().isEmpty()) {
            Map<String, String> params = new HashMap<>();
            params.put("phone", etUsername.getText().toString());
            params.put("veriCode", etVerifyCode.getText().toString());
            HttpUtil.verifyLogin(params, new BaseObserver<LoginBean>() {
                @Override
                protected void onSuccess(LoginBean loginBean) {
                    if (loginBean.getCode().equals("20000")) {
                        saveData(loginBean.getData());
                        id = loginBean.getId();
                        loginSuccess();
                    } else {
                        btGoVerify.setClickable(true);
                        SessionKeeper.keepAutoLogin(getApplicationContext(), false);
                        ToastUtil.showMessage(LoginActivity.this, loginBean.getStatus(), ToastUtil.LENGTH_LONG);
                    }
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {
                    progressDialog.dismiss();
                    SessionKeeper.keepAutoLogin(getApplicationContext(), false);
                    btGoVerify.setClickable(true);
                    if (isNetWorkError)
                        ToastUtil.showMessage(LoginActivity.this, "??????????????????????????????", ToastUtil.LENGTH_LONG);
                    else {
                        ToastUtil.showMessage(LoginActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
                        LogUtil.e("LoginRequestError", "not network error", e);
                    }
                }
            });
        }
    }
    @OnClick(R.id.send_verify_code)
    public void onSendVerifyCodeClicked() {
        if (!etUsername.getText().toString().isEmpty() && etUsername.getError() == null) {
            sendEmail(etUsername.getText().toString(), "login");
        }
    }

    private void sendEmail(String veriCode, String type) {
        HttpUtil.sendEmail(veriCode, type, new BaseObserver<DefaultResultBean<Object>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Object> objectDefaultResultBean) {
                progressDialog.dismiss();
                if (objectDefaultResultBean.getCode().equals("20000")) {
                    ToastUtil.showMessage(LoginActivity.this, "????????????", ToastUtil.LENGTH_LONG);
                } else {
                    ToastUtil.showMessage(LoginActivity.this, objectDefaultResultBean.getStatus(), ToastUtil.LENGTH_LONG);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                LogUtil.e("send email", "send email error", e);
                if (isNetWorkError)
                    ToastUtil.showMessage(LoginActivity.this, "??????????????????????????????", ToastUtil.LENGTH_SHORT);
                else
                    ToastUtil.showMessage(LoginActivity.this, "????????????", ToastUtil.LENGTH_SHORT);
            }
        });
    }
    @OnClick(R.id.qq_go)
    public void onQqGoClicked() {
        int type=typeStateStu ? 3: 2;
//        LoginBean loginBean=new LoginBean();
//        loginBean.setType(type);
//        loginBean.setToken("1");
//        // loginBean.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtc2ciOiI1LDE1ODc3NDA2MzgyNjkiLCJpYXQiOjE1ODc3NDEwMDEsImV4cCI6MTU4ODEwMTAwMX0.LIsXFro1xCDf0aB5AnLlZBjQaEUfIEEcj37qXLeo-aU");
//        saveData(loginBean);
//        loginSuccess();
        mIUiListener = new BaseUiListener(LoginActivity.this,mTencent,type);
        //all????????????????????????
        mTencent.login(LoginActivity.this, "all", mIUiListener);
    }
//    @OnClick(R.id.bt_switch_type)
//    public void onswitchTypeClicked() {
//        typeStateStu = !typeStateStu;
//        btSwitchType.setText(typeStateStu ? "??????QQ??????" : "??????QQ??????");
//    }
    private static final int REQUEST_EXTERNAL_STORAGE = 10001;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static boolean verifyStoragePermissions(Activity activity) {
        try {
            //???????????????????????????
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                LogUtil.d("????????????", "???????????????");
                // ???????????????????????????????????????????????????????????????
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                return false;
            } else {
                LogUtil.d("????????????", "????????????");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveData(LoginBean loginBean) {
        String pwd = etPassword.getText().toString();
        SessionKeeper.loginSave(LoginActivity.this, loginBean);
        SessionKeeper.keepUserPassword(LoginActivity.this, pwd.length() > 20 ? pwd : md5(pwd));
    }

    @OnClick(R.id.register_go)
    public void onFabClicked() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void loginSuccess() {
        btGoPassword.setClickable(true);
        btGoVerify.setClickable(true);
        SessionKeeper.keepAutoLogin(this, true);
        Explode explode = new Explode();
        explode.setDuration(500);

        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
        Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
        i2.putExtra("id", String.valueOf(id));
        startActivity(i2, oc2.toBundle());
        finish();
    }

    @OnClick(R.id.tv_forgot_password)
    public void onViewClicked() {
        startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // ?????????????????????????????????
            int i = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            // ?????????????????? ?????? GRANTED---??????  DINIED---??????
            if (i != PackageManager.PERMISSION_GRANTED) {
                LogUtil.d("????????????", "????????????");
            } else {
                LogUtil.d("????????????", "????????????");
            }
        }
    }
    @OnClick(R.id.bt_switch_login_type)
    public void onBtSwitchLoginTypeClicked() {
        loginTypeVerifyCode = !loginTypeVerifyCode;
        System.out.println(loginTypeVerifyCode);
        switchType();
    }
    private void switchType() {
        layoutPassword.setVisibility(loginTypeVerifyCode ? View.GONE : View.VISIBLE);
        layoutVerCode.setVisibility(loginTypeVerifyCode ? View.VISIBLE : View.GONE);
        btSwitchType.setText(loginTypeVerifyCode ? "????????????" : "???????????????");
        sendVerifyCode.setVisibility(loginTypeVerifyCode ? View.VISIBLE : View.GONE);
        tvForgotPassword.setVisibility(loginTypeVerifyCode ? View.GONE : View.VISIBLE);
        btGoVerify.setVisibility(loginTypeVerifyCode ? View.VISIBLE : View.GONE);
        btGoPassword.setVisibility(loginTypeVerifyCode ? View.GONE : View.VISIBLE);
    }
}
