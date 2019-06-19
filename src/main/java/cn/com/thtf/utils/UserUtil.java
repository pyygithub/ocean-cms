package cn.com.thtf.utils;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

import java.util.Map;

public class UserUtil {


    /**
     * @return
     * @description 获取用户的id
     */
    public static String getUserId() {
        try {
            Assertion ass = AssertionHolder.getAssertion();
            return (String) ass.getPrincipal().getAttributes().get("id");
        } catch (Exception e) {
            throw new CustomException(ResultCode.PERMISSION_EXPIRE);
        }
    }

    /**
     * @return
     * @description 获取用户username
     */
    public static String getUsername() {
        try {
            Assertion ass = AssertionHolder.getAssertion();
            return ass.getPrincipal().getName();
        } catch (Exception e) {
            throw new CustomException(ResultCode.PERMISSION_EXPIRE);
        }
    }

    /**
     * @return
     * @description 获取用户的信息
     */
    public static Map<String, Object> getUserMessage() {
        try {
            Assertion ass = AssertionHolder.getAssertion();
            return ass.getPrincipal().getAttributes();
        } catch (Exception e) {
            throw new CustomException(ResultCode.PERMISSION_EXPIRE);
        }
    }
}
