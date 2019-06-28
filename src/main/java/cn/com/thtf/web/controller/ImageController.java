package cn.com.thtf.web.controller;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.config.FtpConfig;
import cn.com.thtf.model.Image;
import cn.com.thtf.service.ImageService;
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
@Api(value = "ImageController", description = "图片相关接口")
@RestController
@RequestMapping("/v1")
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;



    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "主题图片上传", notes = "主题图片上传")
    @PostMapping(value = "/uploadImage", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public Result uploadFiles(@Valid @ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file) {
        Image image = imageService.uploadImage(file);
        return Result.SUCCESS(image);
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     *
     * @param file 源图像文件
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    @ApiOperation(value = "系统应用列表分页查询", notes = "系统应用列表分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x", value = "目标切片起点坐标X", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "y", value = "目标切片起点坐标Y", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "width", value = "目标切片宽度", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "height", value = "目标切片高度", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/cutImage", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public Result cutImage(@Valid @ApiParam(value = "源图像文件", required = true) @RequestParam("file") MultipartFile file,
                                       @RequestParam int x,
                                       @RequestParam int y,
                                       @RequestParam int width,
                                       @RequestParam int height) {

        Image image = imageService.cutImage(file, x, y, width, height);
        return Result.SUCCESS(image);
    }
}
