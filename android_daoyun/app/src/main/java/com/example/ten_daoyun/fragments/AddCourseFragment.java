package com.example.ten_daoyun.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.SearchListBean;
import com.example.ten_daoyun.R;
import com.example.ten_daoyun.activities.CourseInfoActivity;
import com.example.ten_daoyun.activities.CreateCourseActivity;
import com.example.ten_daoyun.adapters.SearchListAdapter;
import com.example.ten_daoyun.http.BaseObserver;
import com.example.ten_daoyun.http.HttpUtil;
import com.example.ten_daoyun.session.SessionKeeper;
import com.example.ten_daoyun.utils.LogUtil;
import com.example.ten_daoyun.utils.ToastUtil;
import com.example.ten_daoyun.zxing.android.CaptureActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseFragment extends Fragment implements SearchListAdapter.OnListListener, SearchView.OnQueryTextListener {
    private static final int WHAT_GET_DATA_SUCCESS = 1;
    private static final int WHAT_GET_DATA_FAILED = 2;
    private static final int WHAT_ADD_COURSE_FAILED = 3;
    private static final int WHAT_ADD_COURSE_SUCCESS = 4;
    @BindView(R.id.search_view)
    SearchView searchView;
    Unbinder unbinder;

    String searchString = "";
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;

    SearchListAdapter mAdapter;

    String id;
    int page = 1;
    int page_size = 10;

    boolean noMoreData = false;
    boolean loadingAppendData = false;

    List<SearchListBean> data = new ArrayList<>();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.focus)
    LinearLayout focus;
    //???????????????
    @BindView(R.id.scan)
    Button scan;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    @OnClick(R.id.scan)
    public void onScanClicked() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            goScan();
        }
    }
    /**
     * ???????????????????????????
     */
    private void goScan(){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    ToastUtil.showMessage(getActivity(), "?????????????????????????????????????????????????????????", ToastUtil.LENGTH_LONG);
                }
                break;
            default:
        }
    }
    String content="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                //?????????????????????
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                //?????????BitMap??????
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                ToastUtil.showMessage(getActivity(),content, ToastUtil.LENGTH_LONG);
//                searchView.setQuery(content,false);
                searchCourse(content);
            }
        }
    }
    public AddCourseFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        id = SessionKeeper.getUserId(getActivity());
        if (SessionKeeper.getUserType(getActivity()).equals("3"))
            fab.setVisibility(View.GONE);
        data.clear();
        searchView.setQuery("",true);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        refreshView.setOnRefreshListener(this::refreshData);
        refreshView.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        mAdapter = new SearchListAdapter(data, getActivity(), SessionKeeper.getUserType(getActivity()), this);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(mAdapter);
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1) && !noMoreData && !loadingAppendData)
                    appendData();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void searchCourse(String s) {
        noMoreData = false;
        page = 1;
        data.clear();
        sendMsg2Server(s);
    }

    private void sendMsg2Server(String courseNum) {
        HttpUtil.searchCourse(courseNum, new BaseObserver<SearchListBean>() {
            @Override
            protected void onSuccess(SearchListBean searchListBean) {
                if (searchListBean.getCode().equals("20000")) {
//                    ToastUtil.showMessage(getActivity(),searchListBean.getData().get(0).getCourseName(), ToastUtil.LENGTH_LONG);
                    data.addAll(searchListBean.getData());
                    mHandler.sendEmptyMessage(WHAT_GET_DATA_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(WHAT_GET_DATA_FAILED);
                    ToastUtil.showMessage(getActivity(), searchListBean.getStatus());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                mHandler.sendEmptyMessage(WHAT_GET_DATA_FAILED);
                if (isNetWorkError)
                    ToastUtil.showMessage(getActivity(), "??????????????????????????????", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getActivity(), e.getMessage(), ToastUtil.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refreshData() {
        data.clear();
        sendMsg2Server(searchString);
    }

    private void appendData() {
        loadingAppendData = true;
        page++;
        HttpUtil.searchCourse(searchString, new BaseObserver<SearchListBean>() {
            @Override
            protected void onSuccess(SearchListBean searchListBean) {
                loadingAppendData = false;
                if (searchListBean.getCode().equals("20000")) {
                    if (searchListBean.getData().size() == 0) {
                        noMoreData = true;
                        ToastUtil.showMessage(getActivity(), "?????????????????????");
                    }
                    data.addAll(searchListBean.getData());
                    mHandler.sendEmptyMessage(WHAT_GET_DATA_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(WHAT_GET_DATA_FAILED);
                    ToastUtil.showMessage(getActivity(), searchListBean.getStatus());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                loadingAppendData = false;
                mHandler.sendEmptyMessage(WHAT_GET_DATA_FAILED);
                if (isNetWorkError)
                    ToastUtil.showMessage(getActivity(), "??????????????????????????????", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getActivity(), e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_DATA_SUCCESS:
                    if (refreshView.isRefreshing())
                        refreshView.setRefreshing(false);
//                    ToastUtil.showMessage(getActivity(),data.get(0).getCourse_name(), ToastUtil.LENGTH_LONG);
                    mAdapter.notifyDataSetChanged();
                    break;
                case WHAT_GET_DATA_FAILED:
                    if (refreshView.isRefreshing())
                        refreshView.setRefreshing(false);
                    break;
                case WHAT_ADD_COURSE_SUCCESS:
                    ToastUtil.showMessage(getActivity(), "????????????");
                    refreshData();
                    break;
                case WHAT_ADD_COURSE_FAILED:
                    break;
            }
        }
    };

    @Override
    public void onButtonClick(View view, String courseId) {
        Map<String, String> params = new HashMap<>();
        params.put("courseId ", courseId);
        params.put("studentId ", id);
        HttpUtil.addCourse(params, new BaseObserver<DefaultResultBean<Object>>() {
            @Override
            protected void onSuccess(DefaultResultBean<Object> objectDefaultResultBean) {
                if (objectDefaultResultBean.getCode().equals("20000")) {
                    mHandler.sendEmptyMessage(WHAT_ADD_COURSE_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(WHAT_ADD_COURSE_FAILED);
                    ToastUtil.showMessage(getActivity(), objectDefaultResultBean.getStatus());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                mHandler.sendEmptyMessage(WHAT_ADD_COURSE_FAILED);
                if (isNetWorkError)
                    ToastUtil.showMessage(getActivity(), "??????????????????????????????", ToastUtil.LENGTH_LONG);
                else
                    ToastUtil.showMessage(getActivity(), e.getMessage(), ToastUtil.LENGTH_LONG);

            }
        });
    }

    @Override
    public void onItemClick(View view, String courseId, String courseNum) {
        Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
        intent.putExtra("fromSearch", true);
        intent.putExtra("courseId", courseId);
        intent.putExtra("courseNum", courseNum);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        data.clear();
        searchString = s;
        if (!s.isEmpty())
            searchCourse(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        LogUtil.d("onQueryTextChange", s);
        return false;
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), CreateCourseActivity.class));
    }

    @Override
    public void onResume() {
        try {
            View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                searchView.clearFocus();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            searchView.clearFocus();
        }
        focus.setFocusable(true);
        focus.setFocusableInTouchMode(true);
        focus.requestFocus();
        super.onResume();
    }
}
