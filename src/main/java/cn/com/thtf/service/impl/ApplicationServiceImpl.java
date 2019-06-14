package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.mapper.*;
import cn.com.thtf.model.*;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.utils.RSAUtil;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.UrlUtil;
import cn.com.thtf.utils.UserUtil;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;
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

    @Value("${cas-server.client-add-url}")
    private String clientAddUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private AdminGroupApplicationMapper adminGroupApplicationMapper;

    @Autowired
    private UserGroupApplicationMapper userGroupApplicationMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    /**
     * 系统应用添加
     * @param applicationSaveOrUpdateVO
     */
    @Override
    public void add(ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        //分别取出主页地址和注销地址中的IP或域名 + 端口
        String link = applicationSaveOrUpdateVO.getLink();
        String logoutUrl = applicationSaveOrUpdateVO.getLogoutUrl();
        //判断主页地址和注销地址IP和端口是否相同
        if (!UrlUtil.getIpAndPort(link).equals(UrlUtil.getIpAndPort(logoutUrl))) {
            log.error("### 系统应用添加：主页地址和注销地址IP不一致. link={}, logouUrl={} ###", link, logoutUrl);
            throw new CustomException(ResultCode.DIFFERENT_IP);
        }

        //保存应用到CAS Server
        String ipAndPort = UrlUtil.getIpAndPort(link);
        JSONObject jsonObject = restTemplate.postForObject(clientAddUrl, null, JSONObject.class, ipAndPort, logoutUrl);
        if (jsonObject == null || jsonObject.getInteger("code") != 200) {
            log.error("### 系统应用添加：注册CAS client 错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用添加：注册应用到CAS server成功 ###");

        //保存应用
        Application application = new Application();
        String applicationId = SnowflakeId.getId() + "";
        String userId = UserUtil.getUserId();
        String username = UserUtil.getUsername();

        application.setId(applicationId);
        BeanUtils.copyProperties(applicationSaveOrUpdateVO, application);
        application.setStatus(Constants.DISABLE);
        application.setCreateUserCode(userId);
        application.setCreateUserName(username);
        application.setCreateTime(new Timestamp(new Date().getTime()));
        application.setDeletedFlag(Constants.UN_DELETED);
        int appCount = applicationMapper.insert(application);
        if (appCount != 1) {
            log.error("### 系统应用添加：保存应用错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用添加：保存应用成功 ###");

        //保存应用和管理员分组关系
        String adminGroupId = applicationSaveOrUpdateVO.getAdminGroupId();
        AdminGroupApplication adminGroupApplication = new AdminGroupApplication();
        adminGroupApplication.setAdminGroupId(adminGroupId);
        adminGroupApplication.setApplicationId(applicationId);
        int groupAppCount = adminGroupApplicationMapper.insert(adminGroupApplication);
        if (groupAppCount != 1) {
            log.error("### 系统应用添加：保存应用和管理员应用分组关系错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用添加：保存应用和管理员应用分组关系成功 ###");

        //保存应用到第三方系统：存储应用IP和秘钥信息
        Token token = new Token();
        token.setId(SnowflakeId.getId() + "");
        token.setIp(UrlUtil.getIp(link));
        token.setApplicationId(applicationId);
        token.setPublicKey(RSAUtil.getPublicKey());
        token.setPrivateKey(RSAUtil.getPrivateKey());
        token.setAuthLevel(applicationSaveOrUpdateVO.getAuthLevel() == null ? Constants.AUTH_LEVEL_BASE : applicationSaveOrUpdateVO.getAuthLevel());
        int tokenCount = tokenMapper.insert(token);
        if (tokenCount != 1) {
            log.error("### 系统应用添加：保存应用到第三方系统错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用添加：保存应用到第三方系统成功 ###");

        //查询所有用户分组列表
        List<UserGroup> userGroupList = userGroupMapper.selectAll();
        if (CollectionUtils.isEmpty(userGroupList)) {
            log.error("### 系统应用添加：查询所有用户分组列表错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用添加：查询所有用户分组列表成功 ###");

        //按用户编码(ownerCode)分组
        Map<String, List<UserGroup>> collect = userGroupList.stream().collect(Collectors.groupingBy(UserGroup::getOwnerCode));
        if (collect != null) {
            collect.entrySet().forEach(entry -> {
                String ownerCode = entry.getKey();
                List<UserGroup> userGroups = entry.getValue();
                //获取当前应用对应的分组名称
                String adminGroupName = applicationSaveOrUpdateVO.getAdminGroupName();

                //查询同名的分组对象
                Optional<UserGroup> optional = userGroups.stream().filter(group -> group.getName().equals(adminGroupName)).findFirst();

                //同步到用户应用分组两种情况:
                // a.如果新添加应用的分组名称在用户中存在，那么直接把新应用放在分组中即可
                if (optional.isPresent()) {
                    //保存应用和用户应用分组关系
                    saveUserGroupApplication(applicationId, optional.get().getId());
                    log.info("### 系统应用添加：新添加应用的分组名称在用户中存在，直接把新应用放在分组中成功 ###");
                }
                //  b.如果新添加应用的分组名称在用户中不存在，需要先添加分组，然后再把新应用放到分组中
                else {
                    UserGroup userGroup = new UserGroup();
                    userGroup.setId(SnowflakeId.getId() + "");
                    userGroup.setName(adminGroupName);
                    userGroup.setDescription("由管理员【" + username + "】创建,系统同步生成");
                    userGroup.setOwnerCode(ownerCode);
                    userGroup.setCreateUserCode(userId);
                    userGroup.setCreateUserName(username);
                    userGroup.setCreateTime(new Timestamp(new Date().getTime()));

                    //查询当前用户分组序号最大值
                    Example example = new Example(UserGroup.class);
                    example.createCriteria().andEqualTo("ownerCode", ownerCode);
                    example.setOrderByClause(" ORDER_NO DESC");
                    List<UserGroup> userGroupOrderList = userGroupMapper.selectByExample(example);

                    //如果存在分组信息-当前分组序号 = 历史最大序号 + 1
                    if (!CollectionUtils.isEmpty(userGroupOrderList)) {
                        userGroup.setOrderNo(userGroupOrderList.get(0).getOrderNo() + 1);
                    }
                    //如果当前用户分组为第一个则初始化：orderNo=0
                    else {
                        userGroup.setOrderNo(0);
                    }
                    //保存新用户分组到数据库
                    int userGroupCount = userGroupMapper.insert(userGroup);
                    if (userGroupCount != 1) {
                        log.error("### 系统应用添加：保存新用户分组到数据库错误 ###");
                        throw new CustomException(ResultCode.FAIL);
                    }

                    //保存应用和用户应用分组关系
                    saveUserGroupApplication(applicationId, userGroup.getId());
                    log.info("### 系统应用添加：新添加应用的分组名称在用户中不存在，先添加分组，然后再把新应用放到分组中成功 ###");
                }

            });
        }
    }

    /**
     * 保存应用和用户应用分组关系
     * @param applicationId
     * @param userGroupId
     */
    private void saveUserGroupApplication(String applicationId, String userGroupId) {
        UserGroupApplication userGroupApplication = new UserGroupApplication();
        userGroupApplication.setUserGroupId(userGroupId);
        userGroupApplication.setApplicationId(applicationId);
        int userGroupAppCount = userGroupApplicationMapper.insert(userGroupApplication);
        if (userGroupAppCount != 1) {
            log.error("### 系统应用添加：保存应用和用户应用分组关系错误, userGroupId={}, applicationId={} ###", userGroupId, applicationId);
            throw new CustomException(ResultCode.FAIL);
        }
    }

}
