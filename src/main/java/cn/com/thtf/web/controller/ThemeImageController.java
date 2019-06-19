package cn.com.thtf.web.controller;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.config.FtpConfig;
import cn.com.thtf.model.ThemeImage;
import cn.com.thtf.service.ThemeImageService;
import cn.com.thtf.utils.FtpUtil;
import cn.com.thtf.utils.UploadUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * ========================
 * 主题图片Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：14:19
 * Version: v1.0
 * ========================
 */
@Api(value = "ThemeImageController", description = "主题图片相关接口")
@RestController
@RequestMapping("/v1")
public class ThemeImageController {
    private static final Logger log = LoggerFactory.getLogger(ThemeImageController.class);

    @Autowired
    private ThemeImageService themeImageService;

    @Autowired
    private FtpConfig ftpConfig;

    /**
     * 图片上传
     * @param file
     * @return
     */
    @ApiOperation(value = "主题图片上传", notes = "主题图片上传")
    @PostMapping(value = "/uploadImage", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public Result uploadFiles(@Valid @ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file){
        String picSavePath = null;
        String httpPath = null;
        try {
            String oldName = file.getOriginalFilename();// 获取图片原来的名字
            String picNewName = UploadUtils.generateRandonFileName(oldName);//通过工具类产生新图片名称，防止重名
            picSavePath = UploadUtils.generateRandomDir(picNewName);//通过工具类把图片目录分级
            httpPath = FtpUtil.pictureUploadByConfig(ftpConfig, picNewName, picSavePath, file.getInputStream());// 上传到图片服务器的操作
            if (httpPath == null) {
                log.error("### 上传到FTP服务器失败 ###");
                throw new CustomException(ResultCode.FAIL);
            }

            log.info("### 上传到FTP服务器成功 ###");
        } catch (Exception e) {
            log.error("### 图片上传失败，e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }

        // 添加到数据库
        ThemeImage themeImage = themeImageService.saveImage(picSavePath, httpPath);
        return Result.SUCCESS(themeImage);
    }
}
