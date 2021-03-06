package com.example.ten_daoyun.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ten_daoyun.httpBean.CoursesListBean;
import com.example.ten_daoyun.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<CoursesListBean> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public CoursesListAdapter(List<CoursesListBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.course_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoursesListBean bean = data.get(position);
        if (mOnItemClickListener != null) {
            holder.courseListItem.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
        }
        holder.courseNameTV.setText("课程名称："+bean.getCourseName());
        holder.classNameTV.setText("班级名称："+bean.getClassName());
        holder.termTV.setText("学期："+bean.getTerm());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_name_TV)
        TextView courseNameTV;
        @BindView(R.id.class_name_TV)
        TextView classNameTV;
        @BindView(R.id.term_TV)
        TextView termTV;
        @BindView(R.id.course_list_item)
        RelativeLayout courseListItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
