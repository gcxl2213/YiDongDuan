package com.example.ten_daoyun.http;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpUtil extends HttpBase {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static Retrofit mRetrofit;

    /**
     * 初始化Retrofit
     *
     * @return
     */
    @NonNull
    private static Retrofit init() {
        if (mRetrofit != null) return mRetrofit;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(HTTP_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit;
    }

    /**
     * 用户模块 <==================================================================================>
     */

    public static void registerUser(Map<String, String> params, Boolean isTeacher, BaseObserver<RegisterBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpRegisterInterface(params, isTeacher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void login(Map<String, String> params, BaseObserver<LoginBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpLoginInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void verifyLogin(Map<String, String> params, BaseObserver<LoginBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpVerifyCodeLoginInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void forgotPwd(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpForgotPwdInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyUserInfo(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyUserInfoInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void sendEmail(String veriCode, String type, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSendEmailInterface(veriCode, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 获取系统参数
     * @param params
     * @param callback
     */
    public static void getSystemInfo(Map<String, String> params, BaseObserver<SystemBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetSystemInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
    /**
     * 上传头像，这个后台还有问题，先不做了
     *
     * @param params
     * @param fileUrl  图片url
     * @param callback
     */
    public static void uploadAvatarInfo(Map<String, String> params, String fileUrl, BaseObserver<UploadAvatarBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);

        File file = new File(fileUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        service.httpUploadAvatarInterface(params, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 课程模块  <=============================================================================>
     */

    public static void addCourse(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpAddCourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getStudentsList(String courseId, BaseObserver<StudentsListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetStudentsListInterface(courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void searchCourse(String courseNum, BaseObserver<SearchListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSearchCourseInterface(courseNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getCourseInfo(String courseNum, BaseObserver<CourseInfoBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetCourseInfoInterface(courseNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyCourseInfo(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyCourseInfoInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void deleteStudent(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpDeleteStudentInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void deleteCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpDeleteCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyStudent(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyStudentInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void modifyCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpModifyCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void addStu2Course(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpAddStu2CourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getCoursesList(BaseObserver<CoursesListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetCoursesListInterface()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void createCourse(Map<String, String> params, BaseObserver<DefaultResultBean<Object>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCreateCourseInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    /**
     * 签到模块  <=============================================================================>
     */

    public static void check(Map<String, String> params, BaseObserver<DefaultResultBean<Boolean>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCheckInterface(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    public static void startCheck(Map<String, String> params, BaseObserver<CheckBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStartCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void stopCheck(Map<String, String> params, BaseObserver<CheckBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpStopCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void canCheck(Map<String, String> params, BaseObserver<DefaultResultBean<Boolean>> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpCanCheckInterface(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
    public static void getParentList(BaseObserver<ParentListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetParentListInterface()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void getChildrenList(String parentId, BaseObserver<ChildrenListBean> callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpGetChildrenListInterface(parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
//    /**
//     * 数据字典模块  <=============================================================================>
//     */
//    public static void getDictInfo(String token, String typename, BaseObserver<DictInfoListBean> callback) {
//        Retrofit retrofit = init();
//        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
//        service.httpGetDictInfoInterface(token, typename)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(callback);
//    }
}
