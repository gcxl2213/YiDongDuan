package com.example.ten_daoyun.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.ten_daoyun.httpBean.CheckBean;
import com.example.ten_daoyun.httpBean.ChildrenListBean;
import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.DictInfoListBean;
import com.example.ten_daoyun.httpBean.ParentListBean;
import com.example.ten_daoyun.httpBean.RegisterBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.LogUtil;
import com.example.ten_daoyun.utils.ToastUtil;
import com.example.ten_daoyun.utils.Util;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    //发送验证码
    private static final int WHAT_SEND_EMAIL = 1001;
    //验证码正确
    private static final int WHAT_NETWORK_ERROR = 1002;
    //可重新发送短信
    private static final int WHAT_CAN_RESEND = 1003;
    //设置倒数时间
    private static final int WHAT_SET_TIME = 1004;
    //操作正确
    private static final int WHAT_OPERATION_SUCCESS = 1005;
    //操作失败
    private static final int WHAT_OPERATION_FAIL = 1006;
    //注册成功 返回
    private static final int WHAT_BACK = 1008;
    @BindView(R.id.regist_back)
    Button registBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.send_verify_code)
    Button sendVerifyCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repeatpassword)
    EditText etRepeatpassword;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.et_user_code)
    EditText etUserCode;
//    @BindView(R.id.et_user_school)
//    EditText etUserSchool;
//    @BindView(R.id.et_user_department)
//    EditText etUserDepartment;
//    @BindView(R.id.et_user_profession)
//    EditText etUserProfession;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.bt_switch_type)
    Button btSwitchType;
//    @BindView(R.id.is_teacher)
//    RadioButton isTeacher;
    @BindView(R.id.ll_stu)
    LinearLayout llStu;
//    @BindView(R.id.ll_teacher)
//    LinearLayout llTeacher;
//    @BindView(R.id.act_user_school)
//    AppCompatAutoCompleteTextView actUserSchool;
//    @BindView(R.id.act_user_department)
//    AppCompatAutoCompleteTextView actUserDepartment;

    @BindView(R.id.spinner_school)
    Spinner spinner_school;
    @BindView(R.id.spinner_academy)
    Spinner spinner_academy;

    Thread sendCodeThread;
    int count = 60;
    boolean typeStateTeacher = false;//是否注册学生版
    int schoolId = 0;
    int departmentId = 0;
    int professionId = 0;
    ArrayAdapter<String> adapterSchool;
    ArrayAdapter<String> adapterDepartment;
    ArrayAdapter<String> adapterProfession;
    DataForm schoolForm = new DataForm();
    DataForm departmentForm = new DataForm();
    DataForm professionForm = new DataForm();
    List<List<DictInfoListBean>> data = new ArrayList<>();
    List<String> schoolData = new ArrayList<String>();
    List<String> academyData = new ArrayList<String>();
    List<Integer> parentData = new ArrayList<Integer>();
    int parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        DialogSettings.style = DialogSettings.STYLE_MATERIAL;
        DialogSettings.tip_theme = DialogSettings.THEME_LIGHT;
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT;
        DialogSettings.use_blur = false;

        initData();
        initView();

        String[] school ={"请选择学校","福州大学"};
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, school);
        spinner_school.setAdapter(schoolAdapter);
        spinner_school.setSelection(0, true);

        String[] academy ={"请选择学院","数学与计算机科学学院"};
        ArrayAdapter<String> academyAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, academy);
        spinner_academy.setAdapter(academyAdapter);
        spinner_academy.setSelection(0, true);
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
//                    ToastUtil.showMessage(RegisterActivity.this, dictInfoListBean.getResult_desc(), ToastUtil.LENGTH_SHORT);
//            }
//
//            @Override
//            protected void onFailure(Throwable e, boolean isNetWorkError) {
//                if (isNetWorkError)
//                    ToastUtil.showMessage(RegisterActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
//                else
//                    ToastUtil.showMessage(RegisterActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
//            }
//        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        switchType();
//        actUserSchool.setOnTouchListener(this::onActUserSchoolTouched);
//        actUserDepartment.setOnTouchListener(this::onActUserDepartmentTouched);
//        actUserProfession.setOnTouchListener(this::onActUserProfessionTouched);
        setEditTextListener();

    }

    private String[] listConvertToArray(List<String> strList) {
        String[] strArray = new String[strList.size()];
        for (int i = 0; i < strList.size(); i++)
        {
            strArray[i] = strList.get(i);
        }
        return strArray;
    }

    public void getParentList(){
        HttpUtil.getParentList(new BaseObserver<ParentListBean>() {
            @Override
            protected void onSuccess(ParentListBean parentListBean) {
                if(parentListBean.getCode().equals("20000")){
                    if(parentListBean.getData() != null){
                        for(ParentListBean info : parentListBean.getData()){
                            schoolData.add(info.getName());
                            parentData.add(info.getId());
                            getChildList(String.valueOf(info.getId()));
                        }

                    }
                }else {
                    ToastUtil.showMessage(RegisterActivity.this, parentListBean.getStatus(), ToastUtil.LENGTH_SHORT);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(RegisterActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(RegisterActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }
    public void getChildList(String parentId){
        HttpUtil.getChildrenList(parentId, new BaseObserver<ChildrenListBean>() {
            @Override
            protected void onSuccess(ChildrenListBean childrenListBean) {
                if(childrenListBean.getCode().equals("20000")){
                    if(childrenListBean.getData() != null){
                        for(ChildrenListBean info : childrenListBean.getData()){
                            academyData.add(info.getName());
                        }
                    }
                }else {
                    ToastUtil.showMessage(RegisterActivity.this, childrenListBean.getStatus(), ToastUtil.LENGTH_SHORT);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(RegisterActivity.this, "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(RegisterActivity.this, e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }


    @OnClick(R.id.regist_back)
    public void onRegistBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.send_verify_code)
    public void onSendVerifyCodeClicked() {
        if (!etPhone.getText().toString().isEmpty() && etPhone.getError() == null) {
            mHandler.sendEmptyMessage(WHAT_SEND_EMAIL);
            sendEmail(etPhone.getText().toString(), "register");
        }
    }

    private void sendEmail(String veriCode, String type) {
        HttpUtil.sendEmail(veriCode, type, new BaseObserver<DefaultResultBean<Object>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Object> objectDefaultResultBean) {
                if (objectDefaultResultBean.getCode().equals("20000")) {
                    ToastUtil.showMessage(RegisterActivity.this, "发送成功", ToastUtil.LENGTH_LONG);
                } else {
                    mHandler.sendEmptyMessage(WHAT_CAN_RESEND);
                    ToastUtil.showMessage(RegisterActivity.this, objectDefaultResultBean.getStatus(), ToastUtil.LENGTH_LONG);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                mHandler.sendEmptyMessage(WHAT_CAN_RESEND);
                LogUtil.e("send email", "send email error", e);
                if (isNetWorkError)
                    ToastUtil.showMessage(RegisterActivity.this, "请求失败，请检查网络", ToastUtil.LENGTH_SHORT);
                else
                    ToastUtil.showMessage(RegisterActivity.this, "发送失败", ToastUtil.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.bt_go)
    public void onBtGoClicked() {
        if (checkInput()) {
            WaitDialog.show(RegisterActivity.this, "注册中...");
            Map<String, String> p = new HashMap<>();
            p.put("phone", etPhone.getText().toString());
            p.put("veriCode", etVerifyCode.getText().toString());
            p.put("password", Util.md5(etPassword.getText().toString()));
            p.put("name", userName.getText().toString());
            p.put("jobNum", etUserCode.getText().toString());
            p.put("organization", spinner_school.getSelectedItem().toString()+spinner_academy.getSelectedItem().toString());
//            p.put("school", typeStateStu ? actUserSchool.getText().toString() : etUserSchool.getText().toString());
//            p.put("department", typeStateStu ? actUserDepartment.getText().toString() : etUserDepartment.getText().toString());
//            p.put("profession", typeStateStu ? actUserProfession.getText().toString() : etUserProfession.getText().toString());
            HttpUtil.registerUser(p, typeStateTeacher, new BaseObserver<RegisterBean>() {
                @Override
                protected void onSuccess(RegisterBean registerBean) {
                    WaitDialog.dismiss();
                    if (registerBean.getCode().equals("20000")) {
                        TipDialog.show(RegisterActivity.this, "注册成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                        mHandler.sendEmptyMessageDelayed(WHAT_BACK, 1500);
                    }
                    else
                        TipDialog.show(RegisterActivity.this, registerBean.getStatus(), TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {
                    WaitDialog.dismiss();
                    if (isNetWorkError)
                        TipDialog.show(RegisterActivity.this, "请检查网络连接", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                    else
                        TipDialog.show(RegisterActivity.this, e.getMessage(), TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                }
            });
        }
    }

    private boolean checkInput() {
        if (etPhone.getError() != null) return false;
        if (etPassword.getError() != null) return false;
        if (etRepeatpassword.getError() != null) return false;
        if(TextUtils.isEmpty(etPhone.getText().toString())||TextUtils.isEmpty(etVerifyCode.getText().toString())||
                TextUtils.isEmpty(etPassword.getText().toString())||TextUtils.isEmpty(etRepeatpassword.getText().toString())||
                TextUtils.isEmpty(userName.getText().toString())){
            TipDialog.show(RegisterActivity.this, "必填字段不能为空", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
            return false;
        }
        return etPhone.getError() == null;
    }


    @OnClick(R.id.bt_switch_type)
    public void onViewClicked() {
        typeStateTeacher = !typeStateTeacher;
        switchType();
    }


    private void switchType() {
        btSwitchType.setText(typeStateTeacher ? "切换注册学生" : "切换注册教师");
//        llStu.setVisibility(typeStateTeacher ? View.VISIBLE : View.GONE);
        //llTeacher.setVisibility(typeStateStu ? View.GONE : View.VISIBLE);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //点击发送验证码后 按钮文字改为提示多久可再次发送
                case WHAT_SEND_EMAIL:
                    sendVerifyCode.setClickable(false);
                    sendCodeThread = new Thread(canSendCode);
                    sendCodeThread.start();
                    break;
                //设置为可再发送
                case WHAT_CAN_RESEND:
                    count = 60;
                    sendVerifyCode.setText("发送验证码");
                    sendVerifyCode.setClickable(true);
                    break;
                //设置倒数时间
                case WHAT_SET_TIME:
                    String s = count + "秒";
                    sendVerifyCode.setText(s);
                    count--;
                    break;
                case WHAT_OPERATION_SUCCESS:
                    ToastUtil.showMessage(RegisterActivity.this, "注册成功", ToastUtil.LENGTH_LONG);
                    mHandler.sendEmptyMessageDelayed(WHAT_BACK, 500);
                    break;
                case WHAT_OPERATION_FAIL:
                    ToastUtil.showMessage(RegisterActivity.this, (String) msg.obj, ToastUtil.LENGTH_LONG);
                    break;
                case WHAT_BACK:
                    RegisterActivity.this.onBackPressed();
                    break;
            }
        }
    };

    /**
     * 发送验证码后倒数的线程
     */
    Runnable canSendCode = () -> {
        while (true) {
            if (count != 0) {
                mHandler.sendEmptyMessage(WHAT_SET_TIME);
            } else {
                mHandler.sendEmptyMessage(WHAT_CAN_RESEND);
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private void setEditTextListener() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (!Pattern.matches(Util.Password_Reg, s.toString())) {
                        etPassword.setError(Util.Password_Rule);
                    } else {
                        etPassword.setError(null);
                    }
                }
            }
        });
        etRepeatpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (!s.toString().equals(etPassword.getText().toString())) {
                        etRepeatpassword.setError("两次输入的密码不同");
                    } else {
                        etRepeatpassword.setError(null);
                    }
                }
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (!Pattern.matches(Util.Phone_Reg, s.toString())) {
                        etPhone.setError("请输入正确的手机号");
                    } else {
                        etPhone.setError(null);
                    }
                }
            }
        });
    }
}

class DataForm {
    List<String> info;
    List<Integer> ids;

    DataForm() {
        info = new ArrayList<>();
        ids = new ArrayList<>();
    }

    public DataForm(List<String> info, List<Integer> ids) {
        this.info = info;
        this.ids = ids;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public void clear() {
        info.clear();
        ids.clear();
    }

    public void add(String info, int belong) {
        this.info.add(info);
        this.ids.add(belong);
    }

}
