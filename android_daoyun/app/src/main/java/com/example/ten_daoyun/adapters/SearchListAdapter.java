package com.example.ten_daoyun.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ten_daoyun.httpBean.SearchListBean;
import com.example.ten_daoyun.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SearchListBean> data;
    private Context context;
    private OnListListener mOnListListener;
    private String userType;

    public SearchListAdapter(List<SearchListBean> data, Context context, String userType, OnListListener mOnListListener) {
        this.data = data;
        this.context = context;
        this.mOnListListener = mOnListListener;
        this.userType = userType;
    }
    public void setData(List<SearchListBean> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.search_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SearchListBean bean = data.get(i);
        viewHolder.courseName.setText("课程名称："+bean.getCourseName());
        viewHolder.className.setText("班级名称："+bean.getClassName());
        viewHolder.term.setText("学期："+bean.getTerm());
        viewHolder.addCourse.setOnClickListener(v -> mOnListListener.onButtonClick(v, data.get(i).getId()));
        viewHolder.searchListItem.setOnClickListener(v -> mOnListListener.onItemClick(v, data.get(i).getId(), data.get(i).getCourseNum()));
        if (this.userType.equals("2"))
            viewHolder.addCourse.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnListListener {
        void onButtonClick(View view, String courseId);

        void onItemClick(View view, String courseId, String courseNum);
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_name)
        TextView courseName;
        @BindView(R.id.class_name)
        TextView className;
        @BindView(R.id.term)
        TextView term;
        @BindView(R.id.add_course)
        Button addCourse;
        @BindView(R.id.search_list_item)
        RelativeLayout searchListItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
