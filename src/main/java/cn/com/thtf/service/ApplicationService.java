package cn.com.thtf.service;

import cn.com.thtf.vo.AdminGroupApplicationVO;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;

import java.util.List;

/**
 * ========================
 * 系统应用service接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：13:57
 * Version: v1.0
 * ========================
 */
public interface ApplicationService {

    void add(ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO);

    void update(String appId, ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO);
}
