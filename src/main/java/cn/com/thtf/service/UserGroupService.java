package cn.com.thtf.service;

import cn.com.thtf.vo.UserGroupListVO;
import cn.com.thtf.vo.UserGroupSaveOrUpdateVO;

import java.util.List;

/**
 * ========================
 * 用户应用分组service接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:53
 * Version: v1.0
 * ========================
 */
public interface UserGroupService {

    void add(UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO);

    void update(UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO);

    void delete(String id);

    List<UserGroupListVO> list(String userId);

    void moveOrder(String userGroupId, String type);
}
