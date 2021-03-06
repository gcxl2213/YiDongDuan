package com.example.ten_daoyun.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ten_daoyun.httpBean.StudentsListBean;
import com.example.ten_daoyun.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private List<StudentsListBean> data;
    private String experience;
    private Context context;

    public StudentListAdapter(String experience,List<StudentsListBean> data, Context context) {
        this.experience=experience;
        this.data = data;
        this.context = context;
    }
    public void setExperience(String experience){
        this.experience=experience;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.student_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.studentName.setText("姓名："+data.get(i).getName());
        viewHolder.studentCode.setText("学号: "+data.get(i).getJobNum());
        viewHolder.organization.setText("组织: "+data.get(i).getOrganization());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_name)
        TextView studentName;
        @BindView(R.id.student_code)
        TextView studentCode;
        @BindView(R.id.organization)
        TextView organization;
        @BindView(R.id.course_list_item)
        LinearLayout courseListItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
