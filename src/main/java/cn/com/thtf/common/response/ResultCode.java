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

    /* 成功状态码 */
    SUCCESS(0,"操作成功！"),

    /* 错误状态码 */
    FAIL(-1,"操作失败！"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),

    /* 业务错误：30001-39999 */
    BUSINESS_GROUP_NO_ALLOWED_DEL(30001, "应用分组已经被应用使用，不能删除"),
    BUSINESS_IS_TOP(30002, "已经到最顶部"),
    BUSINESS_IS_BOTTOM(30003, "已经到最底部"),
    BUSINESS_NAME_EXISTED(30051, "名称已存在"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 数据错误：50001-599999 */
    RESULT_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),


    /* 权限错误：70001-79999 */
    PERMISSION_UNAUTHENTICATED(70001,"此操作需要登陆系统！"),
    PERMISSION_UNAUTHORISE(70002,"权限不足，无权操作！"),
    PERMISSION_EXPIRE(70003,"登录状态过期！"),
    PERMISSION_LIMIT(70004, "访问次数受限制");

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
