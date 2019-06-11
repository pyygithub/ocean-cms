package cn.com.thtf.common.exception;


import cn.com.thtf.common.response.ResultCode;

/**
 * 自定义异常类型
 * @author pyy
 **/
public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        super(resultCode.message());
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }

}
