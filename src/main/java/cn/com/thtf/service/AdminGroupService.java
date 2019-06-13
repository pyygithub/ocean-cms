package cn.com.thtf.service;

import cn.com.thtf.vo.AdminGroupApplicationVO;
import cn.com.thtf.vo.AdminGroupListVO;
import cn.com.thtf.vo.AdminGroupSaveOrUpdateVO;

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
public interface AdminGroupService {

    void add(AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO);

    void update(String adminGroupId, AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO);

    void delete(String id);

    List<AdminGroupListVO> list(String userId);

    void moveOrder(String userGroupId, String type);

    List<AdminGroupApplicationVO> listByAdminGroupId(String adminGroupId);

    void updateAdminGroupApplication(String adminGroupId, List<String> appIds);
}