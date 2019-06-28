package cn.com.thtf.utils;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.config.FtpConfig;
import cn.com.thtf.vo.UploadFileResult;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ========================
 * FTP工具类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：13:56
 * Version: v1.0
 * ========================
 */
public class FtpUtil {

    private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 初始化配置
     * @param ftpConfig
     * @param fileNewName
     * @param fileSavePath
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String fileUploadByConfig(FtpConfig ftpConfig, String fileNewName, String fileSavePath,
                                               InputStream inputStream) throws IOException {
        log.info("### fileUploadByConfig ###");
        String fileHttpPath = null;

        boolean flag = uploadFile(ftpConfig.getFTP_ADDRESS(), ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USERNAME(),
                ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_BASEPATH(), fileSavePath, fileNewName, inputStream);

        if (!flag) {
            return fileHttpPath;
        }
        fileHttpPath = ftpConfig.getServerUrl() + fileSavePath + "/" + fileNewName;
        log.info("### fileHttpPath={} ###", fileHttpPath);
        return fileHttpPath;
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param host
     *            FTP服务器hostname
     * @param ftpPort
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param basePath
     *            FTP服务器基础目录
     * @param filePath
     *            FTP服务器文件存放路径。
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String host, String ftpPort, String username, String password, String basePath,
                                     String filePath, String filename, InputStream input) {
        int port = Integer.parseInt(ftpPort);
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir))
                        continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();// 这个设置允许被动连接--访问远程ftp时需要
            // 上传文件
            if (!ftp.storeFile(filename, input)) {
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     *  上传文件到远程ftp服务器
     * @param ftpConfig
     * @param file
     * @return
     */
    public static UploadFileResult uploadFileToRemoteServer(FtpConfig ftpConfig, MultipartFile file) {
        try {
            String fileNewName = null;  //文件新名称
            String fileSavePath = null; //文件保持相对路径
            String httpPath = null;     //文件http绝对路径
            String oldName = file.getOriginalFilename();// 获取文件原来的名字
            fileNewName = UploadUtils.generateRandonFileName(oldName);//通过工具类产生新文件名称，防止重名
            fileSavePath = UploadUtils.generateRandomDir(fileNewName);//通过工具类把文件目录分级
            httpPath = FtpUtil.fileUploadByConfig(ftpConfig, fileNewName, fileSavePath, file.getInputStream());// 上传到图片服务器的操作
            if (httpPath == null) {
                log.error("### 上传文件到FTP服务器失败 ###");
                throw new CustomException(ResultCode.FAIL);
            }
            log.info("### 上传文件到FTP服务器成功 ###");

            UploadFileResult uploadFileResult = new UploadFileResult();
            uploadFileResult.setFileNewName(fileNewName);
            uploadFileResult.setFileSavePath(fileSavePath);
            uploadFileResult.setHttpPath(httpPath);
            return uploadFileResult;
        } catch (Exception e) {
            log.error("### 文件上传失败，e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     *  上传图片到远程ftp服务器
     * @param ftpConfig
     * @param fileInputStream
     * @param originalFilename
     * @return
     */
    public static UploadFileResult uploadImageToRemoteServer(FtpConfig ftpConfig, InputStream fileInputStream, String originalFilename) {
        try {
            String fileNewName = null;  //文件新名称
            String fileSavePath = null; //文件保持相对路径
            String httpPath = null;     //文件http绝对路径
            String oldName = originalFilename;// 获取文件原来的名字
            fileNewName = UploadUtils.generateRandonFileName(oldName);//通过工具类产生新文件名称，防止重名
            fileSavePath = UploadUtils.generateRandomDir(fileNewName);//通过工具类把文件目录分级
            httpPath = FtpUtil.fileUploadByConfig(ftpConfig, fileNewName, fileSavePath, fileInputStream);// 上传到图片服务器的操作
            if (httpPath == null) {
                log.error("### 上传文件到FTP服务器失败 ###");
                throw new CustomException(ResultCode.FAIL);
            }
            log.info("### 上传文件到FTP服务器成功 ###");

            UploadFileResult uploadFileResult = new UploadFileResult();
            uploadFileResult.setFileNewName(fileNewName);
            uploadFileResult.setFileSavePath(fileSavePath);
            uploadFileResult.setHttpPath(httpPath);
            return uploadFileResult;
        } catch (Exception e) {
            log.error("### 文件上传失败，e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }
}
