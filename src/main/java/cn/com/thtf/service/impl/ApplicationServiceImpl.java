package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.dao.ApplicationMapper;
import cn.com.thtf.dao.UserGroupApplicationMapper;
import cn.com.thtf.model.Application;
import cn.com.thtf.model.UserGroupApplication;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.vo.UserGroupApplicationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ========================
 * 系统应用service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：14:09
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);


    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserGroupApplicationMapper userGroupApplicationMapper;

    /**
     * 根据用户分组ID查询应用列表
     * @author: pyy
     * @date: 2019/6/10 14:10
     * @param userGroupId
     * @return
     */
    @Override
    public List<UserGroupApplicationVO> listByUserGroupId(String userGroupId) {
        //查询所有应用列表
        Example example = new Example(Application.class);
        example.createCriteria()
                .andEqualTo("status", Constants.ENABLE)
                .andEqualTo("deletedFlag", Constants.UN_DELETED);
        example.setOrderByClause(" PRIORITY ASC");

        List<Application> allApplicationList = applicationMapper.selectByExample(example);
        log.info("### 用户分组应用列表查询完毕,全部应用个数:{} ###", allApplicationList.size());

        //根据用户分组ID查询
        List<Application> userApplicationList = applicationMapper.selectByUserGroupId(userGroupId);
        log.info("### 用户分组应用列表查询完毕,当前用户分组应用个数:{} ###", userApplicationList.size());

        //过滤数据
        List<UserGroupApplicationVO> userGroupApplicationVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allApplicationList)) {
            userGroupApplicationVOList = allApplicationList.stream().map(application -> {
                UserGroupApplicationVO userGroupApplicationVO = new UserGroupApplicationVO();
                userGroupApplicationVO.setApplicationId(application.getId());
                userGroupApplicationVO.setApplicationName(application.getAppName());
                userGroupApplicationVO.setApplicationPriority(application.getPriority());
                userGroupApplicationVO.setUserGroupId(userGroupId);

                //初始化选中标记
                if (!CollectionUtils.isEmpty(userApplicationList) && userApplicationList.contains(application)) {
                    userGroupApplicationVO.setCheckbox(true);
                } else {
                    userGroupApplicationVO.setCheckbox(false);
                }

                return userGroupApplicationVO;
            }).collect(Collectors.toList());
        }


        return userGroupApplicationVOList;
    }

    /**
     * 修改用户分组的关联应用
     * @author: pyy
     * @date: 2019/6/11 9:24
     * @param userGroupId
     * @param appIds
     * @return: void
     */
    @Override
    public void updateUserGroupApplication(String userGroupId, List<String> appIds) {
        //清空该用户分组下的应用关联信息
        UserGroupApplication userGroupApplication = new UserGroupApplication();
        userGroupApplication.setUserGroupId(userGroupId);
        userGroupApplicationMapper.delete(userGroupApplication);
        log.info("### 清空该用户分组下的应用关联信息完毕 ###");

        //设置新的应用关联信息
        if (!CollectionUtils.isEmpty(appIds)) {
            for (String appId : appIds) {
                userGroupApplication.setApplicationId(appId);
                int count = userGroupApplicationMapper.insert(userGroupApplication);
                if (count != 1) {
                    log.error("### 添加用户分组和应用关联表数据异常 ###");
                    throw new CustomException(ResultCode.FAIL);
                }
            }
        }
        log.info("### 用户分组和应用关联关系更新完毕 ###");
    }
}
