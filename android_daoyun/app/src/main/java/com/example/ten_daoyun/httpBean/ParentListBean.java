package com.example.ten_daoyun.httpBean;

import java.util.List;

public class ParentListBean extends DefaultResultBean<List<ParentListBean>> {


    private int id;
    private int parentId;
    private String name;
    private List<ChildrenListBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildrenListBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenListBean> children) {
        this.children = children;
    }



}
