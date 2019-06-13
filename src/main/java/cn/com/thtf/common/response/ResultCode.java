package cn.com.thtf.common.response;


/**
 * ========================
 * 通用响应状态
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/6
 * Time：10:10
 * Version: v1.0
 * ========================
 */
public enum ResultCode {

    SUCCESS(0,"操作成功！"),
    FAIL(-1,"操作失败！"),
    UNAUTHENTICATED(10001,"此操作需要登陆系统！"),
    UNAUTHORISE(10002,"权限不足，无权操作！"),
    UNVALID(10003,"登录状态过期！"),
    LIMIT(10004, "访问次数受限制"),
    GROUP_NO_ALLOWED_DEL(20001, "应用分组已经被应用使用，不能删除"),
    IS_TOP(20002, "已经到最顶部"),
    IS_BOTTOM(20003, "已经到最底部"),

    NO_SECRET(40001, "缺少secret参数"),
    APP_SECRET_ERROR(40002, "获取 access_token 时 sceret格式错误，或者  sceret中ip错误。请开发者认真比对 secret 的正确性"),
    INVALID_TOKEN(40003, "无效token"),
    LEGAL_REQ_PARAM(40004, "不合法参数"),
    NO_ACCESS_TOKEN(40005, "缺少token参数"),
    TOKEN_EXPIRED(40006, "token过期"),
    ILLEGAL_REQUEST(40007, "非法请求"),
    SECRET_ERROR_IP(40008, "sceret中IP信息错误"),
    SECRET_EXPIRED(40009, "sceret过期"),
    INVALID_PARAM(40035, "不合法的参数"),
    ILLEGAL_URL(40039, "不合法URL"),
    DIFFERENT_IP(40040, "主页地址和注销地址的IP与端口不一致"),

    SERVER_ERROR(99999,"抱歉，系统繁忙，请稍后重试！");

    //操作代码
    int code;
    //提示信息
    String message;
    ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
