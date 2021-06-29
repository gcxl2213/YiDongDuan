package com.example.ten_daoyun.httpBean;

import java.util.List;

public class StudentsListBean extends DefaultResultBean<List<StudentsListBean>>{

    /**
     * id : 1
     * uid : 1
     * stu_code : 190327046
     * name : 李杰铃
     * gender : 男
     * school : 福州大学
     * department : 数计学院
     * profession : 计算机技术
     * phone :1875XX
     * lack_count :
     * check_count：
     */

    private int id;
    private int roleId;
    private String jobNum;
    private String name;
    private String organization;
    private boolean isTeacher;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
}
