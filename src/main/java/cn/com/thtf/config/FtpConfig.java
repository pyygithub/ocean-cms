package cn.com.thtf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * ========================
 * FtpConfig 配置类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：13:58
 * Version: v1.0
 * ========================
 */
@Component
public class FtpConfig {
    /**
     * 服务器ip地址
     */
    @Value("${ftp.address}")
    private String FTP_ADDRESS;

    /**
     * 端口号 21
     */
    @Value("${ftp.port}")
    private String FTP_PORT;

    /**
     * ftp用户名
     */
    @Value("${ftp.username}")
    private String FTP_USERNAME;

    /**
     * ftp用户密码
     */
    @Value("${ftp.password}")
    private String FTP_PASSWORD;

    /**
     * 基本路径，用户图片 /home/ftptest/tenement/house_images
     */
    @Value("${ftp.basepath}")
    private String FTP_BASEPATH;

    /**
     * 下载地址基础url，这个是配置的图片服务器的地址
     */
    @Value("${ftp.server-url}")
    private String IMAGE_BASE_URL;

    public String getFTP_ADDRESS() {
        return FTP_ADDRESS;
    }

    public void setFTP_ADDRESS(String fTP_ADDRESS) {
        FTP_ADDRESS = fTP_ADDRESS;
    }

    public String getFTP_PORT() {
        return FTP_PORT;
    }

    public void setFTP_PORT(String fTP_PORT) {
        FTP_PORT = fTP_PORT;
    }

    public String getFTP_USERNAME() {
        return FTP_USERNAME;
    }

    public void setFTP_USERNAME(String fTP_USERNAME) {
        FTP_USERNAME = fTP_USERNAME;
    }

    public String getFTP_PASSWORD() {
        return FTP_PASSWORD;
    }

    public void setFTP_PASSWORD(String fTP_PASSWORD) {
        FTP_PASSWORD = fTP_PASSWORD;
    }



    public String getIMAGE_BASE_URL() {
        return IMAGE_BASE_URL;
    }

    public void setIMAGE_BASE_URL(String iMAGE_BASE_URL) {
        IMAGE_BASE_URL = iMAGE_BASE_URL;
    }

    public String getFTP_BASEPATH() {
        return FTP_BASEPATH;
    }

    public void setFTP_BASEPATH(String fTP_BASEPATH) {
        FTP_BASEPATH = fTP_BASEPATH;
    }
}
