package cn.com.thtf.utils;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.hutool.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 获取当前登录的用户
 * @author pyy
 */
public class SecurityUtils {

    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException(ResultCode.PERMISSION_EXPIRE);
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     * @return 系统用户名称
     */
    public static String getUsername(){
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("username", String.class);
    }

    /**
     * 获取系统用户id
     * @return 系统用户id
     */
    public static Long getUserId(){
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Long.class);
    }
}
