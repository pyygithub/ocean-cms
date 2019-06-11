package cn.com.thtf.service;

import cn.com.thtf.vo.UserGroupApplicationVO;

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

    List<UserGroupApplicationVO> listByUserGroupId(String userGroupId);

    void updateUserGroupApplication(String userGroupId, List<String> appIds);
}
