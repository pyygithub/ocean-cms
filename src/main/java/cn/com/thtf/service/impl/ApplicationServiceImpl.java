package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.QueryResult;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.mapper.*;
import cn.com.thtf.model.*;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.utils.RSAUtil;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.UrlUtil;
import cn.com.thtf.utils.UserUtil;
import cn.com.thtf.vo.ApplicationListVO;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;
import cn.com.thtf.vo.ThemeListVO;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
     *
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
            throw new CustomException(ResultCode.PARAM_NOT_COMPLETE);
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
     * 系统应用修改
     *
     * @param appId
     * @param applicationSaveOrUpdateVO
     */
    @Override
    public void update(String appId, ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        //分别取出主页地址和注销地址中的IP或域名 + 端口
        String link = applicationSaveOrUpdateVO.getLink();
        String logoutUrl = applicationSaveOrUpdateVO.getLogoutUrl();
        //判断主页地址和注销地址IP和端口是否相同
        if (!UrlUtil.getIpAndPort(link).equals(UrlUtil.getIpAndPort(logoutUrl))) {
            log.error("### 系统应用修改：主页地址和注销地址IP不一致. link={}, logouUrl={} ###", link, logoutUrl);
            throw new CustomException(ResultCode.PARAM_NOT_COMPLETE);
        }

        //获取当前登陆用户信息
        String userId = UserUtil.getUserId();
        String username = UserUtil.getUsername();

        //修改应用
        Application application = applicationMapper.selectByPrimaryKey(appId);
        BeanUtils.copyProperties(applicationSaveOrUpdateVO, application);
        application.setLastUpdateUserCode(userId);
        application.setLastUpdateUserName(username);
        application.setLastUpdateTime(new Timestamp(new Date().getTime()));

        int appCount = applicationMapper.updateByPrimaryKey(application);
        if (appCount != 1) {
            log.error("### 系统应用修改：修改应用错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用修改：修改应用成功 ###");

        //修改应用和管理员分组关系
        String adminGroupId = applicationSaveOrUpdateVO.getAdminGroupId();
        AdminGroupApplication adminGroupApplication = new AdminGroupApplication();
        adminGroupApplication.setAdminGroupId(adminGroupId);
        adminGroupApplication.setApplicationId(appId);
        Example example = new Example(AdminGroupApplication.class);
        example.createCriteria().andEqualTo("applicationId", appId);
        //根据applicationId修改关联关系
        int groupAppCount = adminGroupApplicationMapper.updateByExample(adminGroupApplication, example);
        if (groupAppCount != 1) {
            log.error("### 系统应用修改：修改应用和管理员应用分组关系错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用修改：修改应用和管理员应用分组关系成功 ###");

        //更新应用的第三方系统信息：存储应用IP和秘钥信息
        Token token = new Token();
        token.setIp(UrlUtil.getIp(link));
        token.setPublicKey(RSAUtil.getPublicKey());
        token.setPrivateKey(RSAUtil.getPrivateKey());
        token.setAuthLevel(applicationSaveOrUpdateVO.getAuthLevel() == null ? Constants.AUTH_LEVEL_BASE : applicationSaveOrUpdateVO.getAuthLevel());
        //根据applicationId更新token信息
        Example tokenExample = new Example(Token.class);
        tokenExample.createCriteria().andEqualTo("applicationId", appId);
        int tokenCount = tokenMapper.updateByExampleSelective(token, tokenExample);
        if (tokenCount != 1) {
            log.error("### 系统应用修改：更新应用到第三方系统错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        log.info("### 系统应用修改：更新应用到第三方系统成功 ###");
    }

    /**
     * 系统应用状态修改
     *
     * @param appId
     * @param status
     */
    @Override
    public void updateStatus(String appId, String status) {
        if (!Constants.ENABLE.equals(status) || !Constants.DISABLE.equals(status)) {
            log.error("### 系统应用状态修改: 状态码不合法  ###");
            throw new CustomException(ResultCode.PARAM_IS_INVALID);
        }

        //根据ID查询应用信息
        Application application = new Application();
        application.setId(appId);
        application.setLastUpdateUserName(UserUtil.getUserId());
        application.setLastUpdateUserName(UserUtil.getUsername());
        application.setLastUpdateTime(new Timestamp(new Date().getTime()));
        application.setStatus(status);

        //更新数据库
        int count = applicationMapper.updateByPrimaryKeySelective(application);
        if (count != 1) {
            log.error("### 系统应用状态修改：修改应用状态错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 系统应用列表查询
     * @param appName
     * @return
     */
    @Override
    public List<ApplicationListVO> listByParam(String appName) {
        //查询条件
        Example example = new Example(Application.class);
        example.createCriteria()
                .andLike("appName", "%" + appName + "%")
                .andEqualTo("deletedFlag", Constants.UN_DELETED);
        //排序
        example.setOrderByClause(" PRIORITY ASC");

        //执行查询
        List<Application> applicationList = applicationMapper.selectByExample(example);

        List<ApplicationListVO> applicationVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(applicationList)) {
            applicationList.forEach(application -> {
                ApplicationListVO applicationVO = new ApplicationListVO();
                BeanUtils.copyProperties(application, applicationVO);
                applicationVO.setCreateTime(application.getCreateTime().getTime());
                applicationVO.setLastUpdateTime(application.getLastUpdateTime().getTime());

                applicationVOList.add(applicationVO);
            });
        }

        return applicationVOList;
    }

    /**
     * 应用列表分页条件查询
     * @param appName
     * @param page
     * @param size
     * @return
     */
    @Override
    public QueryResult<ApplicationListVO> listPageByParam(String appName, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }

        //查询条件
        Example example = new Example(Application.class);
        example.createCriteria()
                .andLike("appName", "%" + appName + "%")
                .andEqualTo("deletedFlag", Constants.UN_DELETED);
        //排序
        example.setOrderByClause(" PRIORITY ASC");

        //分页查询
        PageHelper.startPage(page, size);

        //执行查询
        List<Application> applicationList = applicationMapper.selectByExample(example);

        //获取分页后数据
        PageInfo<Application> pageInfo = new PageInfo<>(applicationList);

        //过滤数据
        List<ApplicationListVO> applicationVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(applicationList)) {
            applicationList.forEach(application -> {
                ApplicationListVO applicationVO = new ApplicationListVO();
                BeanUtils.copyProperties(application, applicationVO);
                applicationVO.setCreateTime(application.getCreateTime().getTime());
                applicationVO.setLastUpdateTime(application.getLastUpdateTime().getTime());

                applicationVOList.add(applicationVO);
            });
        }

        //封装需要返回的实体数据
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(pageInfo.getTotal());
        queryResult.setList(applicationVOList);

        return queryResult;
    }

    /**
     * 保存应用和用户应用分组关系
     *
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
