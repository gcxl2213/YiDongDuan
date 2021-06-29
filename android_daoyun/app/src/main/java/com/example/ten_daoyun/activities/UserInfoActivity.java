package com.example.ten_daoyun.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.droidbond.loadingbutton.LoadingButton;
import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.DictInfoListBean;
import com.example.ten_daoyun.httpBean.LoginBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.ToastUtil;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity {
    private static final int WHAT_SAVE_SUCCESS = 1;
    private static final int WHAT_SAVE_FAILED = 2;
    private static final int WHAT_QUIT = 3;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.job_num)
    EditText jobNum;
    @BindView(R.id.organization)
    EditText organization;
    @BindView(R.id.save)
    LoadingButton save;
    @BindView(R.id.school_input)
    TextInputLayout schoolInput;

    TextInputLayout professionInput;

    String uid = "";
    LoginBean user;

    List<List<DictInfoListBean>> data = new ArrayList<>();
    @BindView(R.id.userinfo_back)
    Button userinfodBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra("uid");
        initData();
        initView();
        DialogSettings.style = DialogSettings.STYLE_MATERIAL;
        DialogSettings.tip_theme = DialogSettings.THEME_LIGHT;
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT;
        DialogSettings.use_blur = false;
    }

    private void initData() {
//        data.add(new ArrayList<>());
//        data.add(new ArrayList<>());
//        data.add(new ArrayList<>());
//        HttpUtil.getDictInfo(SessionKeeper.getToken(this), getResources().getString(R.string.http_get_school_info), new BaseObserver<DictInfoListBean>() {
//            @Override
//            protected void onSuccess(DictInfoListBean dictInfoListBean) {
//                if (dictInfoListBean.getResult_code().equals("200")) {
//                    if (dictInfoListBean.getData() != null)
//                        for (DictInfoListBean info : dictInfoListBean.getData())
//                            data.get(info.getType_level() - 1).add(info);
//                } else
//                    ToastUtil.showMessage(UserInfoActivity.this, dictInfoListBean.getResult_desc(), ToastUtil.LENGTH_SHORT);
//            }
//
//            @Override
//            protected void onFailure(Throwable e, boolean isNetWorkError) {
//                if (isNetWorkError)
//                    ToastUtil.showMessage(UserInfoActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
//                else
//                    ToastUtil.showMessage(UserInfoActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
//            }
//        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        user = SessionKeeper.getUserInfo(this);
        name.setText(user.getName());
        phone.setText(user.getPhone());

        jobNum.setText(user.getStu_code());
        organization.setText(user.getSchool());

    }
    @OnClick(R.id.userinfo_back)
    public void onUserinfodBackClicked() {
        onBackPressed();
    }
    @OnClick(R.id.save)
    public void onSaveClicked() {
        save.setClickable(false);
        Map<String, String> params = new HashMap<>();
        params.put("id", uid);
        params.put("name", name.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("jobNum", jobNum.getText().toString());
        params.put("organization", organization.getText().toString());

        HttpUtil.modifyUserInfo(params, new BaseObserver<DefaultResultBean<Object>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Object> objectDefaultResultBean) {
                if (objectDefaultResultBean.getCode().equals("20000"))
                    mHandler.sendEmptyMessage(WHAT_SAVE_SUCCESS);
                else {
                    mHandler.sendEmptyMessage(WHAT_SAVE_FAILED);
                    ToastUtil.showMessage(UserInfoActivity.this, objectDefaultResultBean.getResult_desc());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                mHandler.sendEmptyMessage(WHAT_SAVE_FAILED);
                if (isNetWorkError)
                    ToastUtil.showMessage(UserInfoActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(UserInfoActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SAVE_SUCCESS:
                    save.setClickable(true);
                    ToastUtil.showMessage(UserInfoActivity.this, "保存成功");
                    saveUserInfo();
                    break;
                case WHAT_SAVE_FAILED:
                    save.setClickable(true);
                    break;
                case WHAT_QUIT:
                    finish();
                    break;
            }
        }
    };

    private void saveUserInfo() {
        SessionKeeper.keepUserNickName(this,name.getText().toString());
        LoginBean bean = SessionKeeper.getUserInfo(this);
        bean.setNick_name(name.getText().toString());
        bean.setPhone(phone.getText().toString());
        bean.setStu_code(jobNum.getText().toString());
        bean.setSchool(organization.getText().toString());
        SessionKeeper.keepUserInfo(this,bean);
        mHandler.sendEmptyMessageDelayed(WHAT_QUIT,1000);
    }
}
