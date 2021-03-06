package com.example.ten_daoyun.http;

import com.example.ten_daoyun.httpBean.CheckBean;
import com.example.ten_daoyun.httpBean.CheckListBean;
import com.example.ten_daoyun.httpBean.ChildrenListBean;
import com.example.ten_daoyun.httpBean.CourseInfoBean;
import com.example.ten_daoyun.httpBean.CoursesListBean;
import com.example.ten_daoyun.httpBean.DefaultResultBean;
import com.example.ten_daoyun.httpBean.DictInfoListBean;
import com.example.ten_daoyun.httpBean.LoginBean;
import com.example.ten_daoyun.httpBean.ParentListBean;
import com.example.ten_daoyun.httpBean.RegisterBean;
import com.example.ten_daoyun.httpBean.SearchListBean;
import com.example.ten_daoyun.httpBean.StudentsListBean;
import com.example.ten_daoyun.httpBean.SystemBean;
import com.example.ten_daoyun.httpBean.UploadAvatarBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {


    /**
     * 注册接口
     *
     * @param params 手机号和密码
     * @return 是否成功
     */
    @Multipart
    @POST("user/create/register")
    Observable<RegisterBean> httpRegisterInterface(@PartMap Map<String, String> params, @Part("isTeacher") Boolean isTeacher);

    /**
     * 密码登录接口
     *
     * @param params
     * @return
     */
    @GET("user/search/password")
    Observable<LoginBean> httpLoginInterface(@QueryMap Map<String, String> params);


    /**
     * 验证码登录接口
     *
     * @param params
     * @return
     */
    @GET("user/search/verifyCode")
    Observable<LoginBean> httpVerifyCodeLoginInterface(@QueryMap Map<String, String> params);

    /**
     * 忘记密码接口
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("user/update/password")
    Observable<DefaultResultBean<Object>> httpForgotPwdInterface(@PartMap Map<String, String> params);

    /**
     * 修改用户信息接口
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("user/update")
    Observable<DefaultResultBean<Object>> httpModifyUserInfoInterface(@PartMap Map<String, String> params);
    /**
     * 查看系统信息
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("system/infos")
    Observable<SystemBean> httpGetSystemInfo(@PartMap Map<String, String> params);
    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @GET("verifyCode/mobileCode")
    Observable<DefaultResultBean<Object>> httpSendEmailInterface(@Query("phone") String phone, @Query("type") String type);

    /**
     * 上传头像
     *
     * @param params
     * @param file
     * @return
     */
    @Multipart
    @POST("user/avatar")
    Observable<UploadAvatarBean> httpUploadAvatarInterface(@PartMap Map<String, String> params, @Part MultipartBody.Part file);

    /**
     * 学生添加课程到课表
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("sc/create")
    Observable<DefaultResultBean<Object>> httpAddCourseInterface(@PartMap Map<String, String> params);

    /**
     * 获取课程学生列表
     *
     * @param courseId
     * @return
     */
    @GET("user/search/course")
    Observable<StudentsListBean> httpGetStudentsListInterface(@Query("courseId") String courseId);


    /**
     * 搜索课程
     *
     * @return
     */
    @GET("course/search/numList")
    Observable<SearchListBean> httpSearchCourseInterface(@Query("courseNum") String courseNum);

    /**
     * 获取课程信息
     *
     * @param courseNum
     * @return
     */
    @GET("course/search/numObject")
    Observable<CourseInfoBean> httpGetCourseInfoInterface(@Query("courseNum") String courseNum);

    /**
     * 修改课程信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/info")
    Observable<DefaultResultBean<Object>> httpModifyCourseInfoInterface(@PartMap Map<String, String> params);

    /**
     * 从课程删除学生
     *
     * @param params
     * @return
     */
    @Multipart
    @DELETE("course/students")
    Observable<DefaultResultBean<Object>> httpDeleteStudentInterface(@PartMap Map<String, String> params);

    /**
     * 删除签到信息
     *
     * @param params
     * @return
     */
    @Multipart
    @DELETE("course/checklist")
    Observable<DefaultResultBean<Object>> httpDeleteCheckInterface(@PartMap Map<String, String> params);

    /**
     * 修改学生信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/students")
    Observable<DefaultResultBean<Object>> httpModifyStudentInterface(@PartMap Map<String, String> params);

    /**
     * 修改签到信息
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("course/checklist")
    Observable<DefaultResultBean<Object>> httpModifyCheckInterface(@PartMap Map<String, String> params);

    /**
     * 添加学生到课程
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("course/stu2course")
    Observable<DefaultResultBean<Object>> httpAddStu2CourseInterface(@PartMap Map<String, String> params);

    /**
     * 获取课程列表
     *
     * @return
     */
    @GET("course/search")
    Observable<CoursesListBean> httpGetCoursesListInterface();

    /**
     * 创建课程
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("course/create")
    Observable<DefaultResultBean<Object>> httpCreateCourseInterface(@PartMap Map<String, String> params);

    /**
     * 签到
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("stuSignin/create")
    Observable<DefaultResultBean<Boolean>> httpCheckInterface(@PartMap Map<String, String> params);

    /**
     * 开始签到
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("signinPublish/create")
    Observable<CheckBean> httpStartCheckInterface(@PartMap Map<String, String> params);

    /**
     * 停止签到
     *
     * @param params
     * @return
     */
    @Multipart
    @PUT("signinPublish/update")
    Observable<CheckBean> httpStopCheckInterface(@PartMap Map<String, String> params);

    /**
     * 查询是否能签到
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("check/cancheck")
    Observable<DefaultResultBean<Boolean>> httpCanCheckInterface(@PartMap Map<String, String> params);

//    /**
//     * 获取字典内容
//     *
//     * @param token
//     * @param typename
//     * @return
//     */
//    @GET("dict/infos4name")
//    Observable<DictInfoListBean> httpGetDictInfoInterface(@Query("token") String token, @Query("typename") String typename);
    /**
     * 获取父亲表列表
     *
     * @return
     */
    @GET("organizatio/search")
    Observable<ParentListBean> httpGetParentListInterface();

    /**
     * 获取子表列表
     *
     * @return
     */
    @GET("organizatio/search/child")
    Observable<ChildrenListBean> httpGetChildrenListInterface(@Query("parentId") String parentId);
}
