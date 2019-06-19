package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.mapper.AdminGroupApplicationMapper;
import cn.com.thtf.mapper.AdminGroupMapper;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.mapper.ApplicationMapper;
import cn.com.thtf.model.Admin;
import cn.com.thtf.model.AdminGroup;
import cn.com.thtf.model.AdminGroupApplication;
import cn.com.thtf.model.Application;
import cn.com.thtf.service.AdminGroupService;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.StringUtils;
import cn.com.thtf.utils.UserUtil;
import cn.com.thtf.vo.AdminGroupApplicationVO;
import cn.com.thtf.vo.AdminGroupListVO;
import cn.com.thtf.vo.AdminGroupSaveOrUpdateVO;
import cn.com.thtf.common.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ========================
 * 用户应用分组service接口实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:54
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AdminGroupServiceImpl implements AdminGroupService {

    private static final Logger log = LoggerFactory.getLogger(AdminGroupServiceImpl.class);

    @Autowired
    private AdminGroupMapper adminGroupMapper;

    @Autowired
    private AdminGroupApplicationMapper adminGroupApplicationMapper;

    @Autowired
    private ApplicationMapper applicationMapper;



    /**
     * 添加管理员应用分组
     * @param adminGroupSaveOrUpdateVO
     */
    @Override
    public void add(AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO) {
        //验证分组名称是否已存在
        boolean isExists = isExistsByGroupName(adminGroupSaveOrUpdateVO.getName());
        if (isExists) {
            throw new CustomException(ResultCode.BUSINESS_NAME_EXISTED);
        }

        //初始化AdminGroup实体类
        AdminGroup adminGroup = new AdminGroup();

        //复制VO属性值到adminGroup
        BeanUtils.copyProperties(adminGroupSaveOrUpdateVO, adminGroup);
        adminGroup.setId(SnowflakeId.getId() + "");
        adminGroup.setOwnerCode(UserUtil.getUserId());
        adminGroup.setCreateUserCode(UserUtil.getUserId());
        adminGroup.setCreateUserName(UserUtil.getUsername());
        adminGroup.setCreateTime(new Timestamp(new Date().getTime()));

        //查询当前用户分组序号最大值
        Example example = new Example(AdminGroup.class);
        example.createCriteria().andEqualTo("ownerCode", UserUtil.getUserId());
        //排序
        example.setOrderByClause(" ORDER_NO DESC");
        //执行查询
        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);

        //如果存在分组信息-当前分组序号 = 历史最大序号 + 1
        if (!CollectionUtils.isEmpty(adminGroupList)) {
            adminGroup.setOrderNo(adminGroupList.get(0).getOrderNo() + 1);
        }
        //如果当前用户分组为第一个则初始化：orderNo=0
        else {
            adminGroup.setOrderNo(0);
        }

        int count = adminGroupMapper.insert(adminGroup);
        if (count <= 0) {
            log.error("### 用户应用分组添加失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }


    /**
     * 修改管理员应用分组
     * @param adminGroupId
     * @param adminGroupSaveOrUpdateVO
     */
    @Override
    public void update(String adminGroupId, AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO) {
        //初始化AdminGroup实体类
        AdminGroup adminGroup = new AdminGroup();
        adminGroup.setId(adminGroupId);
        //根据id查询
        AdminGroup adminGroupOld = adminGroupMapper.selectOne(adminGroup);
        if (adminGroupOld == null) {
            log.error("### 该用户应用分组不存在，id={} ###", adminGroupId);
            throw new CustomException(ResultCode.FAIL);
        }

        //如果分组名称被修改-执行重复校验
        if (!adminGroupOld.getName().equals(adminGroupSaveOrUpdateVO.getName())) {
            //验证分组名称是否已存在
            boolean isExists = isExistsByGroupName(adminGroupSaveOrUpdateVO.getName());
            if (isExists) {
                throw new CustomException(ResultCode.BUSINESS_NAME_EXISTED);
            }
        }

        //页面传输的参数覆盖数据库属性值
        BeanUtils.copyProperties(adminGroupSaveOrUpdateVO, adminGroupOld);
        adminGroupOld.setLastUpdateUserCode(UserUtil.getUserId());
        adminGroupOld.setLastUpdateUserName(UserUtil.getUsername());
        adminGroupOld.setLastUpdateTime(new Timestamp(new Date().getTime()));

        //执行修改
        int count = adminGroupMapper.updateByPrimaryKey(adminGroupOld);
        if (count != 1) {
            log.error("### 用户应用分组修改失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 删除管理员应用分组
     * @param id
     */
    @Override
    public void delete(String id) {
        //判断该分组下是否存在应用
        Example example = new Example(AdminGroupApplication.class);
        example.createCriteria().andEqualTo("adminGroupId", id);
        List<AdminGroupApplication> adminGroupApplicationList = adminGroupApplicationMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(adminGroupApplicationList)) {
            log.info("### 应用分组已经被应用使用，不能删除, adminGroupId={} ###", id);
            throw new CustomException(ResultCode.BUSINESS_GROUP_NO_ALLOWED_DEL);
        }

        AdminGroup adminGroup = new AdminGroup();
        adminGroup.setId(id);
        int count = adminGroupMapper.deleteByPrimaryKey(adminGroup);
        if (count != 1) {
            log.error("### 用户应用分组删除失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 查询管理员应用分组列表
     * @return
     */
    @Override
    public List<AdminGroupListVO> list() {
        //根据用户ID查询应用分组列表信息
        Example example = new Example(AdminGroup.class);
        //排序
        example.setOrderByClause(" ORDER_NO ASC");
        //执行查询
        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);

        //过滤数据
        List<AdminGroupListVO> adminGroupListVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(adminGroupList)) {
            adminGroupListVOList = adminGroupList.stream().map(adminGroup -> {
                AdminGroupListVO adminGroupListVO = new AdminGroupListVO();
                BeanUtils.copyProperties(adminGroup, adminGroupListVO);
                return adminGroupListVO;
            }).collect(Collectors.toList());
        }
        return adminGroupListVOList;
    }

    /**
     * 根据管理员分组ID查询应用列表
     * @param adminGroupId
     * @return
     */
    @Override
    public List<AdminGroupApplicationVO> listByAdminGroupId(String adminGroupId) {
        //查询所有应用列表
        Example example = new Example(Application.class);
        example.createCriteria()
                .andEqualTo("status", Constants.ENABLE)
                .andEqualTo("deletedFlag", Constants.UN_DELETED);
        example.setOrderByClause(" PRIORITY ASC");

        List<Application> allApplicationList = applicationMapper.selectByExample(example);
        log.info("### 用户分组应用列表查询完毕,全部应用个数:{} ###", allApplicationList.size());

        //根据用户分组ID查询
        List<Application> adminApplicationList = applicationMapper.selectByAdminGroupId(adminGroupId);
        log.info("### 用户分组应用列表查询完毕,当前用户分组应用个数:{} ###", adminApplicationList.size());

        //过滤数据
        List<AdminGroupApplicationVO> adminGroupApplicationVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allApplicationList)) {
            adminGroupApplicationVOList = allApplicationList.stream().map(application -> {
                AdminGroupApplicationVO adminGroupApplicationVO = new AdminGroupApplicationVO();
                adminGroupApplicationVO.setApplicationId(application.getId());
                adminGroupApplicationVO.setApplicationName(application.getAppName());
                adminGroupApplicationVO.setApplicationPriority(application.getPriority());
                adminGroupApplicationVO.setAdminGroupId(adminGroupId);

                //初始化选中标记
                if (!CollectionUtils.isEmpty(adminApplicationList) && adminApplicationList.contains(application)) {
                    adminGroupApplicationVO.setCheckbox(true);
                } else {
                    adminGroupApplicationVO.setCheckbox(false);
                }

                return adminGroupApplicationVO;
            }).collect(Collectors.toList());
        }


        return adminGroupApplicationVOList;
    }

    /**
     * 修改用户分组的关联应用
     * @param adminGroupId
     * @param appIds
     */
    @Override
    public void updateAdminGroupApplication(String adminGroupId, List<String> appIds) {
        //清空该用户分组下的应用关联信息
        AdminGroupApplication adminGroupApplication = new AdminGroupApplication();
        adminGroupApplication.setAdminGroupId(adminGroupId);
        adminGroupApplicationMapper.delete(adminGroupApplication);
        log.info("### 清空该用户分组下的应用关联信息完毕 ###");

        //设置新的应用关联信息
        if (!CollectionUtils.isEmpty(appIds)) {
            for (String appId : appIds) {
                adminGroupApplication.setApplicationId(appId);
                int count = adminGroupApplicationMapper.insert(adminGroupApplication);
                if (count != 1) {
                    log.error("### 添加用户分组和应用关联表数据异常 ###");
                    throw new CustomException(ResultCode.FAIL);
                }
            }
        }
        log.info("### 用户分组和应用关联关系更新完毕 ###");
    }

    /**
     * 调整分组顺序
     * @param adminGroupId
     * @param type
     */
    @Override
    public void moveOrder(String adminGroupId, String type) {
        //查询当前要移动的分组对象
        AdminGroup currGroup = adminGroupMapper.selectByPrimaryKey(adminGroupId);
        if (currGroup == null) {
            log.error("### 用户分组查询失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        //判断操作排序类型
        if (Constants.UP.equals(type)) { //上移

            moveUp(adminGroupId, currGroup);
        } else if (Constants.DOWN.equals(type)) { // 下移

            moveDown(adminGroupId, currGroup);
        } else if (Constants.TOP.equals(type)) { // 置顶

            moveTop(adminGroupId, currGroup);
        } else if (Constants.BOTTOM.equals(type)) { // 置底

            moveBottom(adminGroupId, currGroup);
        }
    }

    /**
     * 置底
     * @param adminGroupId
     * @param currGroup
     */
    private void moveBottom(String adminGroupId, AdminGroup currGroup) {
        //查询最底部分组对象
        Example example = new Example(AdminGroup.class);
        example.setOrderByClause(" ORDER_NO DESC");
        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(adminGroupList)) {
            log.info("### 没查到最底部元素 ###");
            return;
        }
        AdminGroup bottomGroup = adminGroupList.get(0);
        //如果当前分组和最底分组相同
        if (bottomGroup.getId().equals(adminGroupId)) {
            log.info("### 已经是最底端元素, adminGroupId={}###", adminGroupId);
            throw new CustomException(ResultCode.BUSINESS_IS_BOTTOM);
        }

        //置底操作 ，被操作记录 编号是置顶帖子(编号+1)
        currGroup.setOrderNo(bottomGroup.getOrderNo() + 1);

        //更新数据库
        adminGroupMapper.updateByPrimaryKey(currGroup);

        log.info("### 分组置底成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 置顶
     * @param adminGroupId
     * @param currGroup
     */
    private void moveTop(String adminGroupId, AdminGroup currGroup) {
        //查询最顶部分组对象
        Example example = new Example(AdminGroup.class);
        example.setOrderByClause(" ORDER_NO ASC");
        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(adminGroupList)) {
            log.info("### 没查到最顶部元素 ###");
            return;
        }
        AdminGroup topGroup = adminGroupList.get(0);
        //如果当前分组和最顶分组相同
        if (topGroup.getId().equals(adminGroupId)) {
            log.info("### 已经是最顶端元素, adminGroupId={}###", adminGroupId);
            throw new CustomException(ResultCode.BUSINESS_IS_TOP);
        }

        //置顶操作 ，被操作记录 编号是置顶帖子(编号-1)
        currGroup.setOrderNo(topGroup.getOrderNo() - 1);

        //更新数据库
        adminGroupMapper.updateByPrimaryKey(currGroup);

        log.info("### 分组置顶成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 下移
     * @param adminGroupId
     * @param currGroup
     */
    private void moveDown(String adminGroupId, AdminGroup currGroup) {
        //查询当前要移动的下一个分组对象
        Example example = new Example(AdminGroup.class);
        example.createCriteria().andGreaterThan("orderNo", currGroup.getOrderNo());
        example.setOrderByClause(" ORDER_NO ASC");

        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(adminGroupList)) {
            log.info("### 已经是最顶端元素, adminGroupId={}###", adminGroupId);
            throw new CustomException(ResultCode.BUSINESS_IS_TOP);
        }

        AdminGroup nextGroup = adminGroupList.get(0);
        //交换orderNo的值
        Integer temp = currGroup.getOrderNo();   //当前分组位置
        currGroup.setOrderNo(nextGroup.getOrderNo());
        nextGroup.setOrderNo(temp);

        //更新到数据库
        int currCount = adminGroupMapper.updateByPrimaryKey(currGroup);
        int nextCount = adminGroupMapper.updateByPrimaryKey(nextGroup);

        if (currCount != 1 || nextCount != 1) {
            log.error("### 修改分组排序号错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        log.info("### 分组下移成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 上移
     * @param adminGroupId
     * @param currGroup
     */
    private void moveUp(String adminGroupId, AdminGroup currGroup) {
        //查询当前要移动的上一个分组对象
        Example example = new Example(AdminGroup.class);
        example.createCriteria().andLessThan("orderNo", currGroup.getOrderNo());
        example.setOrderByClause(" ORDER_NO DESC");

        List<AdminGroup> adminGroupList = adminGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(adminGroupList)) {
            log.info("### 已经是最顶端元素, adminGroupId={}###", adminGroupId);
            throw new CustomException(ResultCode.BUSINESS_IS_TOP);
        }

        AdminGroup prevGroup = adminGroupList.get(0);
        //交换orderNo的值
        Integer temp = currGroup.getOrderNo();   //当前分组位置
        currGroup.setOrderNo(prevGroup.getOrderNo());
        prevGroup.setOrderNo(temp);

        //更新到数据库
        int currCount = adminGroupMapper.updateByPrimaryKey(currGroup);
        int prevCount = adminGroupMapper.updateByPrimaryKey(prevGroup);

        if (currCount != 1 || prevCount != 1) {
            log.error("### 修改分组排序号错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        log.info("### 分组上移成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 校验分组名称是否已存在
     * @param adminGroupName
     * @return
     */
    private boolean isExistsByGroupName(String adminGroupName) {
        Example example = new Example(AdminGroup.class);
        example.createCriteria()
                .andEqualTo("name", adminGroupName);
        int count = adminGroupMapper.selectCountByExample(example);

        Map<String, Object> result = new HashMap<>();

        if (count > 0) {
            log.info("### 校验分组名称是否已存在：当前分组名称已存在, adminGroupName={} ###", adminGroupName);
            return true;//已存在
        } else {
            return false;//不存在
        }
    }
}



