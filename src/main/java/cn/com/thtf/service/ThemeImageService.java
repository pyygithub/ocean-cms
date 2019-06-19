package cn.com.thtf.service;

import cn.com.thtf.model.ThemeImage;

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
public interface ThemeImageService {

    ThemeImage saveImage(String picSavePath, String httpPath);
}
