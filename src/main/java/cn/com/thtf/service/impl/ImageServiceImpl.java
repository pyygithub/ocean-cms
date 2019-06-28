package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.config.FtpConfig;
import cn.com.thtf.mapper.ImageMapper;
import cn.com.thtf.model.Image;
import cn.com.thtf.service.ImageService;
import cn.com.thtf.utils.*;
import cn.com.thtf.vo.UploadFileResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ========================
 * 主题图片Service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：14:35
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private FtpConfig ftpConfig;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 保存主题图片
     * @param file
     * @return
     */
    @Override
    public Image uploadImage(MultipartFile file) {
        //上传图片到ftp服务器
        UploadFileResult uploadResult = FtpUtil.uploadFileToRemoteServer(ftpConfig, file);

        //保存主题图片到数据库
        Image image = saveImage(uploadResult);
        return image;
    }

    /**
     * 图片裁剪
     * @param file
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    @Override
    public Image cutImage(MultipartFile file, int x, int y, int width, int height) {

        try {
            // 读取图片文件
            InputStream is = file.getInputStream();
            // 获取原文件名称
            String originalFilename = file.getOriginalFilename();
            // 获取文件格式
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            // ImageReader解码指定格式
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext);
            ImageReader reader = it.next();
            // 获取图片流
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            // 输入源中的图像将只按顺序读取
            reader.setInput(iis, true);
            // 描述如何对流进行解码
            ImageReadParam param = reader.getDefaultReadParam();
            // 图片裁剪区域
            Rectangle rect = new Rectangle(x, y, width, height);
            // 提供一个 BufferedImage，将其用作解码像素数据的目标
            param.setSourceRegion(rect);
            // 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象
            BufferedImage bi = reader.read(0, param);

            // 将BufferedImage转换为InputStream
            InputStream inputStream = FileUtil.bufferedImageToInputStream(bi, ext);
            // 上传图片到ftp服务器
            UploadFileResult uploadResult = FtpUtil.uploadImageToRemoteServer(ftpConfig, inputStream, originalFilename);

            //保存主题图片到数据库
            Image image = saveImage(uploadResult);
            log.info("### 图片保存完毕 ###");
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.SYSTEM_UPLOAD_ERROR);
        }
    }

    /**
     * 保存图片到数据库
     * @param uploadResult
     * @return
     */
    private Image saveImage(UploadFileResult uploadResult) {
        Image image = new Image();
        image.setId(SnowflakeId.getId() + "");
        image.setPath(uploadResult.getFileSavePath() + "/" + uploadResult.getFileNewName());//相对路径
        image.setHttpPath(uploadResult.getHttpPath());//图片访问http绝对路径
        image.setUserId(UserUtil.getUserId());
        image.setCreateTime(new Timestamp(new Date().getTime()));
        image.setType(Constants.IMAGE_TYPE_THEME);
        int count = imageMapper.insert(image);
        if (count != 1) {
            log.error("### 保存主题图片：添加数据库错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        return image;
    }

}
