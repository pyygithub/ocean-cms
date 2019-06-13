package cn.com.thtf.utils;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/12
 * Time：11:44
 * Version: v1.0
 * ========================
 */
public class UrlUtil {

    /**
     * 获取url地址ip和端口
     * @param url
     * @return
     */
    public static String getIpAndPort(String url) {
        //这样获取的方式，不请求就能获取到域名
        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            throw new CustomException(ResultCode.ILLEGAL_URL);
        }
        String domain = urlObj.getHost();
        int port = urlObj.getPort();
        return domain + ":" + port;
    }

    /**
     * 获取url地址ip
     * @param url
     * @return
     */
    public static String getIp(String url) {
        //这样获取的方式，不请求就能获取到域名
        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            throw new CustomException(ResultCode.ILLEGAL_URL);
        }
        String domain = urlObj.getHost();
        int port = urlObj.getPort();
        return domain;
    }

}
