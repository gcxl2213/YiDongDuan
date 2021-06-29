package com.example.ten_daoyun.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ten_daoyun.httpBean.CheckBean;
import com.example.ten_daoyun.httpBean.CourseInfoBean;
import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.StudentsListBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.adapters.StudentListAdapter;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.httpBean.SystemBean;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.GPSUtils;
import com.example.ten_daoyun.utils.LogUtil;
import com.example.ten_daoyun.utils.ToastUtil;
import com.example.ten_daoyun.zxing.encode.EncodingHandler;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ten_daoyun.utils.Util.md5;

public class CourseInfoActivity extends AppCompatActivity {
    private static final int WHAT_REQUEST_FAILED = 1;
    private static final int WHAT_CHECK_SUCCESS = 2;
    private static final int WHAT_CHECK_FAILED = 3;
    private static final int WHAT_NETWORK_ERROR = 4;
    private static final int WHAT_GET_STUDENTS_SUCCESS = 5;
    private static final int WHAT_START_CHECK_SUCCESS = 6;
    private static final int WHAT_START_CHECK_FAILED = 7;
    private static final int WHAT_GET_CAN_CHECK_SUCCESS = 8;
    private static final int WHAT_GET_CAN_CHECK_FAILED = 9;
    private static final int REQUEST_TAKE_MEDIA = 102;
    private static final int WHAT_STOP_CHECK_SUCCESS=10;
    private static final int WHAT_STOP_CHECK_FAILED=11;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.course_name)
    TextView courseName;
    @BindView(R.id.term)
    TextView term;
    @BindView(R.id.course_num)
    TextView courseCode;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.sign_count)
    TextView signCount;
    @BindView(R.id.text_use_web)
    TextView textUseWeb;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.stop_check)
    Button stop_check;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.place)
    TextView place;
    @BindView(R.id.image_bitmap)
    ImageView image_bitmap;

    String courseId;
    String courseNum;
    String userType;

    List<StudentsListBean> students = new ArrayList<>();
    String experience="2";//默认先给2，然后再去读取服务器的系统参数
    StudentListAdapter mAdapter = new StudentListAdapter(experience, students, this);
    long duration_time = 365*24*60 * 1000 * 60;

    ProgressDialog progressDialog;

    int signState = -1;
    boolean canCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        userType = SessionKeeper.getUserType(this);
        courseId = getIntent().getStringExtra("courseId");
        courseNum = getIntent().getStringExtra("courseNum");
        Bitmap bitmap = EncodingHandler.createQRCode(courseNum, 700, 700, null);
        image_bitmap.setImageBitmap(bitmap);
        initView();
        initData();
    }

    private void initData() {
//        Map<String, String> params = new HashMap<>();
//        params.put("token", SessionKeeper.getToken(this));
//        params.put("infoid", "1");
//        HttpUtil.getSystemInfo(params,new BaseObserver<SystemBean>(){
//            @Override
//            protected void onSuccess(SystemBean systemBean) {
//                if(systemBean.getCode().equals("20000")){
//                    experience=systemBean.getData().get(0).getExperience();
//                    mAdapter.setExperience(experience);
//                    HttpUtil.getStudentsList(courseNum, new BaseObserver<StudentsListBean>() {
//                        @Override
//                        protected void onSuccess(StudentsListBean studentsListBean) {
//                            if (studentsListBean.getCode().equals("20000")) {
//                                students.clear();
//                                students.addAll(studentsListBean.getData());
//                                mHandler.sendEmptyMessage(WHAT_GET_STUDENTS_SUCCESS);
//                            } else {
//                                mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
//                            }
//                        }
//                        @Override
//                        protected void onFailure(Throwable e, boolean isNetWorkError) {
//                            if (isNetWorkError) {
//                                mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
//                            } else {
//                                LogUtil.e("get student list info", e.getMessage());
//                                mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
//                            }
//                        }
//                    });
//                } else {
//                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
//                }
//            }
//            @Override
//            protected void onFailure(Throwable e, boolean isNetWorkError) {
//                if (isNetWorkError) {
//                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
//                } else {
//                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
//                }
//            }
//        });
        HttpUtil.getStudentsList(courseId, new BaseObserver<StudentsListBean>() {
            @Override
            protected void onSuccess(StudentsListBean studentsListBean) {
                if (studentsListBean.getCode().equals("20000")) {
                    students.clear();
                    students.addAll(studentsListBean.getData());
                    mHandler.sendEmptyMessage(WHAT_GET_STUDENTS_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
                }
            }
            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("get student list info", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
                }
            }
        });
        HttpUtil.getCourseInfo(courseNum, new BaseObserver<CourseInfoBean>() {
            @Override
            protected void onSuccess(CourseInfoBean courseInfoBean) {
                if (courseInfoBean.getCode().equals("20000")) {
                    courseName.setText("课程名称: " + courseInfoBean.getData().getCourseName());
                    courseCode.setText("课程号: " + courseInfoBean.getData().getCourseNum());
                    className.setText("班级名称: " + courseInfoBean.getData().getClassName());
                    place.setText("教室地点："+courseInfoBean.getData().getClassroom());
                    term.setText("学期: " + courseInfoBean.getData().getTerm());;
                    signState = courseInfoBean.getData().getCurrentSignin();
                    if (userType.equals("3")) {
                        check.setText("签到");
                        check.setClickable(true);
                        check.setVisibility(View.VISIBLE);
                        stop_check.setVisibility(View.GONE);
                    } else if (userType.equals("2")) {
                        if (signState == -1) {
                            check.setText("发起签到");
                            check.setClickable(true);
                            check.setVisibility(View.VISIBLE);
                            stop_check.setVisibility(View.GONE);
                            System.out.println("1:"+signState);
                        } else {
//                            check.setText("签到中");
//                            check.setClickable(false);
                            check.setVisibility(View.GONE);
                            stop_check.setVisibility(View.VISIBLE);
                            System.out.println("2"+signState);
                        }
                    }
                } else {
                    ToastUtil.showMessage(getActivityContext(), courseInfoBean.getStatus());
                    stop_check.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                check.setClickable(false);
                check.setVisibility(View.GONE);
                stop_check.setVisibility(View.GONE);
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("get course info", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_REQUEST_FAILED);
                }
            }
        });

    }

    private void setButtonState() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SessionKeeper.getToken(getActivityContext()));
        params.put("course_id", courseId);
        HttpUtil.canCheck(params, new BaseObserver<DefaultResultBean<Boolean>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Boolean> booleanDefaultResultBean) {
                if (booleanDefaultResultBean.getCode().equals("20000")) {
                    canCheck = booleanDefaultResultBean.getData();
                    Message m = new Message();
                    m.what = WHAT_GET_CAN_CHECK_SUCCESS;
                    m.arg1 = booleanDefaultResultBean.getData() ? 1 : 0;
                    mHandler.sendMessage(m);
                } else {
                    mHandler.sendEmptyMessage(WHAT_GET_CAN_CHECK_FAILED);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                canCheck = false;
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("get can check info", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_GET_CAN_CHECK_FAILED);
                }
            }
        });
    }

    private void initView() {
//        setButtonState();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        check.setText(userType.equals("3") ? "签到" : "发起签到");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在签到.....");
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.check)
    public void onCheckClicked() {
        if (userType.equals("3")) {
            sign();
        } else {
            startCheck();
        }
    }

    /**
     * 停止签到
     */
    @OnClick(R.id.stop_check)
    public void onStopCheckClicked() {
        if (userType.equals("2")) {
            Calendar calendar = Calendar.getInstance();
            Map<String, String> params = new HashMap<>();
            params.put("courseId", courseId);
            HttpUtil.stopCheck(params, new BaseObserver<CheckBean>(){
                @Override
                protected void onSuccess(CheckBean checkBean) {
                    if (checkBean.getCode().equals("20000")) {
                        mHandler.sendEmptyMessage(WHAT_STOP_CHECK_SUCCESS);
                    }
                    else
                        ToastUtil.showMessage(getActivityContext(), checkBean.getStatus());
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {
                    if (isNetWorkError) {
                        mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                    } else {
                        LogUtil.e("start check", e.getMessage());
                        mHandler.sendEmptyMessage(WHAT_STOP_CHECK_FAILED);
                    }
                }
            });
        }
    }
    /**
     * 发起签到
     */
    private void startCheck() {
        if (userType.equals("2")) {
            Calendar calendar = Calendar.getInstance();
            Map<String, String> params = new HashMap<>();
            params.put("courseId", courseId);
            params.put("latitude", "100");
            params.put("longitude", "100");
            HttpUtil.startCheck(params, new BaseObserver<CheckBean>() {
                @Override
                protected void onSuccess(CheckBean checkBean) {
                    if (checkBean.getCode().equals("20000")) {
                        mHandler.sendEmptyMessage(WHAT_START_CHECK_SUCCESS);
                    } else
                        ToastUtil.showMessage(getActivityContext(), checkBean.getStatus());
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) {
                    if (isNetWorkError) {
                        mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                    } else {
                        LogUtil.e("start check", e.getMessage());
                        mHandler.sendEmptyMessage(WHAT_START_CHECK_FAILED);
                    }
                }
            });
        }
    }
    /**
     * 签到
     */
    private void sign() {
        if(userType.equals("3"))
            sendSignRequest();
    }

    private void sendSignRequest() {
        progressDialog.show();
        String now = String.valueOf(Calendar.getInstance().getTimeInMillis());
        Map<String, String> params = new HashMap<>();
        params.put("studentId", SessionKeeper.getUserId(getActivityContext()));
        params.put("courseId", courseId);
        params.put("latitude", "100");
        params.put("longitude", "100");
        HttpUtil.check(params, new BaseObserver<DefaultResultBean<Boolean>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Boolean> booleanDefaultResultBean) {
                if (booleanDefaultResultBean.getCode().equals("20000")) {
                    if (booleanDefaultResultBean.getData()) {
                        ToastUtil.showMessage(getActivityContext(), "签到成功");
                        mHandler.sendEmptyMessage(WHAT_CHECK_SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(WHAT_CHECK_FAILED);
                        ToastUtil.showMessage(getActivityContext(), booleanDefaultResultBean.getStatus());
                    }
                } else {
                    mHandler.sendEmptyMessage(WHAT_CHECK_FAILED);
                    ToastUtil.showMessage(getActivityContext(), booleanDefaultResultBean.getStatus());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                if (isNetWorkError) {
                    mHandler.sendEmptyMessage(WHAT_NETWORK_ERROR);
                } else {
                    LogUtil.e("check", e.getMessage());
                    mHandler.sendEmptyMessage(WHAT_CHECK_FAILED);
                    ToastUtil.showMessage(getActivityContext(), "签到失败");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    Context getActivityContext() {
        return this;
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "请求失败，请重试");
                    break;
                case WHAT_CHECK_SUCCESS:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    check.setText("已签到");
                    check.setClickable(false);
                    break;
                case WHAT_CHECK_FAILED:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    break;
                case WHAT_NETWORK_ERROR:
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    ToastUtil.showNetworkErrorMsg(getActivityContext());
                    break;
                case WHAT_GET_STUDENTS_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    break;
                case WHAT_START_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "开始签到成功");
                    check.setVisibility(View.GONE);
                    stop_check.setVisibility(View.VISIBLE);
//                    setButtonState();
                    break;
                case WHAT_START_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "开始签到失败,请重试");
                    break;
                case WHAT_STOP_CHECK_SUCCESS:
                    ToastUtil.showMessage(getActivityContext(), "停止签到成功");
                    check.setVisibility(View.VISIBLE);
                    stop_check.setVisibility(View.GONE);
//                    setButtonState();
                    break;
                case WHAT_STOP_CHECK_FAILED:
                    ToastUtil.showMessage(getActivityContext(), "停止签到失败,请重试");
                    break;
//                case WHAT_GET_CAN_CHECK_SUCCESS:
//                    int canCheck = msg.arg1;
//                    if (userType.equals("3")) {
//                        if (canCheck == 0) {
//                            check.setText("无签到");
//                            check.setClickable(false);
//                        }
//                    } else {
//                        if (canCheck == 1) {
//                            check.setText("签到中");
//                            check.setClickable(false);
//                            stop_check.setVisibility(View.VISIBLE);
//                        } else {
//                            check.setText("发起签到");
//                            check.setClickable(true);
//                            stop_check.setVisibility(View.GONE);
//                        }
//                    }
//                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_MEDIA:
                if (resultCode == RESULT_OK) {
                    sendSignRequest();
                } else {
                    ToastUtil.showMessage(CourseInfoActivity.this, "拍照失败");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
