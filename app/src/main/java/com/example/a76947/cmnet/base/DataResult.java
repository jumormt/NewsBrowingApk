package com.example.a76947.cmnet.base;

/**
 * Created by 76947 on 2017/12/14.
 */

import com.google.gson.annotations.Expose;
public class DataResult<T> {

    public static final int RESULT_Cache = -1;

    public static final int RESULT_OK = 200;// 发送成功
    public static final int NAMEHAS = 400;
    public static final int TOKENWRONG = 104;//TOKEN过期
    public static final int NODATA = 300;//没有数据
    public static final int RESULT_FAILED = 201;// 失败
    public static final int RESULT_SENDFAILED = 302;
    public static final int RESULT_PARAMWRONG = 303;
    public static final int RESULT_CODEWRONG = 304;
    public static final int RESULT_REGFAILD = 305;
    public static final int RESULT_ALLREADYREG = 306;// 不要重复注册
    public static final int RESULT_NOTREGISTER = 307;// 尚未注册
    public static final int RESULT_WRONGPARAM = 308;// 请正确填写参数
    public static final int RESULT_WRONGCODE = 309;// 验证码错误
    public static final int RESULT_LOGFAILD = 310;// 登录失败

    public static final int RESULT_DONOTEXIST = 311; // 用户不存在
    public static final int RESULT_PARAMISWRONG = 312; // 请正确填写参数
    public static final int RESULT_NEWPHONEEXIST = 321; // 新手机已占用
    public static final int RESULT_OLDCODEWRONG = 313; // 旧手机验证码错误
    public static final int RESULT_NEWCODEWRONG = 314; // 新手机验证码错误
    public static final int RESULT_BANDFAILD = 316;// 绑定失败

    public static final int RESULT_NOTHINGCHANGE = 317;// 未做任何修改
    public static final int RESULT_NAMETOLONG = 318;// 姓名过长
    public static final int RESULT_SEXWRONG = 	319 ;//性别不正确

    @Expose
    private String message = ""; // 错误时返回错误原因,成功返回"OK"
    @Expose
    private int status = RESULT_SENDFAILED; // 返回的状态码,200成功，默认302失败
    @Expose
    private String command = ""; // 接口命令
    @Expose
    private T data; // 数据返回
    @Expose
    private int[] page; // 页码

    public int[] getPage() {
        if (page == null) {
            page = new int[3];
        }
        return page;
    }



    public String getMessage() {
        return message;
    }



    public void setMessage(String message) {
        this.message = message;
    }



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}




