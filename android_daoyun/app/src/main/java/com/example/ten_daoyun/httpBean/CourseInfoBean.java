package com.example.ten_daoyun.httpBean;

public class CourseInfoBean extends DefaultResultBean<CourseInfoBean> {

    /**
     * course_id : 24
     * course_name : XXXX课程
     * course_code : 124151
     * place : 12441421
     * location : 121412
     * time : 1214124
     * stu_count : 5
     * teacher : XXX
     * creater_uid : 14
     * check_count : 1
     */

    private String id;
    private String className;
    private String classroom;
    private String courseName;
    private String courseNum;
    private String school;
    private String academy;
    private String creatTime;
    private int teacherId;
    private int creater;
    private int term;
    private int currentSignin;
    private boolean canJoin;
    private boolean end;

    public int getCurrentSignin() {
        return currentSignin;
    }

    public void setCurrentSignin(int currentSignin) {
        this.currentSignin = currentSignin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isCanJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
