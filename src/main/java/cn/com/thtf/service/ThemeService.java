package cn.com.thtf.service;

import cn.com.thtf.vo.ThemeListVO;
import cn.com.thtf.vo.ThemeSaveOrUpdateVO;

import java.util.List;

/**
 * ========================
 * 主题service接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/20
 * Time：8:46
 * Version: v1.0
 * ========================
 */
public interface ThemeService {
    void add(ThemeSaveOrUpdateVO themeSaveOrUpdateVO);

    void update(String themeId, ThemeSaveOrUpdateVO themeSaveOrUpdateVO);

    void delete(String themeId);

    List<ThemeListVO> listByParam(String themeName);

    void updateStatus(String themeId, String status);

    void setDefaultTheme(String themeId);
}
