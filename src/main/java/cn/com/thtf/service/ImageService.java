package cn.com.thtf.service;

import cn.com.thtf.model.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * ========================
 * 主题图片Service接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：14:31
 * Version: v1.0
 * ========================
 */
public interface ImageService {

    Image uploadImage(MultipartFile file);

    Image cutImage(MultipartFile file, int x, int y, int width, int height);
}
