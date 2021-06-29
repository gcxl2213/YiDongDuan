package com.example.ten_daoyun.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.GPSUtils;
import com.example.ten_daoyun.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCourseActivity extends AppCompatActivity {
    private static final int WHAT_CREATE_SUCCESS = 1001;
    private static final int WHAT_CREATE_FAILED = 1002;
    private static final int WHAT_FINISH_ACTIVITY = 1003;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.course_name)
    EditText courseName;
    @BindView(R.id.classroom)
    EditText classroom;
    @BindView(R.id.class_name)
    EditText className;
    @BindView(R.id.term)
    EditText term;
    @BindView(R.id.course_teacher)
    EditText courseTeacher;
    @BindView(R.id.school)
    EditText school;
    @BindView(R.id.academy)
    EditText academy;
    @BindView(R.id.loading_button)
    Button loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        courseTeacher.setText(SessionKeeper.getUserInfo(this).getName());
    }

    @OnClick(R.id.loading_button)
    public void onViewClicked() {
        loadingButton.setClickable(false);
        loadingButton.setText("提交中...");
        Map<String, String> params = new HashMap<>();
        params.put("teacherId ", SessionKeeper.getUserId(this));
        params.put("courseName ", courseName.getText().toString());
        params.put("className", className.getText().toString());
        params.put("classroom ", classroom.getText().toString());
//        params.put("location", GPSUtils.getInstance(this).getLocationString());
        params.put("term", term.getText().toString());
        params.put("school", school.getText().toString());
        params.put("academy", academy.getText().toString());
        HttpUtil.createCourse(params, new BaseObserver<DefaultResultBean<Object>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Object> objectDefaultResultBean) {
                if (objectDefaultResultBean.getCode().equals("20000")) {
                    mHandler.sendEmptyMessage(WHAT_CREATE_SUCCESS);
                } else {
                    ToastUtil.showMessage(getApplicationContext(), objectDefaultResultBean.getResult_desc());
                    mHandler.sendEmptyMessage(WHAT_CREATE_FAILED);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError)
                    ToastUtil.showMessage(getApplicationContext(), "网络出错，请检查网络", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getApplicationContext(), e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CREATE_SUCCESS:
                    ToastUtil.showMessage(getApplicationContext(), "创建成功");
                    mHandler.sendEmptyMessageDelayed(WHAT_FINISH_ACTIVITY, 1000);
                    break;
                case WHAT_CREATE_FAILED:
                    loadingButton.setClickable(true);
                    loadingButton.setText("提交");
                    break;
                case WHAT_FINISH_ACTIVITY:
                    finish();
                    break;
            }
        }
    };
}
