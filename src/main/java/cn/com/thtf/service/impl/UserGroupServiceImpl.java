package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.dao.UserGroupApplicationMapper;
import cn.com.thtf.dao.UserGroupMapper;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.model.UserGroup;
import cn.com.thtf.model.UserGroupApplication;
import cn.com.thtf.service.UserGroupService;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.StringUtils;
import cn.com.thtf.vo.UserGroupListVO;
import cn.com.thtf.vo.UserGroupSaveOrUpdateVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class UserGroupServiceImpl implements UserGroupService {

    private static final Logger log = LoggerFactory.getLogger(UserGroupServiceImpl.class);

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserGroupApplicationMapper userGroupApplicationMapper;

    /**
     * 添加用户应用分组
     * @author: pyy
     * @date: 2019/6/10 9:56
     * @param userGroupSaveOrUpdateVO
     * @return: void
     */
    @Override
    public void add(UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO) {
        //初始化UserGroup实体类
        UserGroup userGroup = new UserGroup();

        //复制VO属性值到userGroup
        BeanUtils.copyProperties(userGroupSaveOrUpdateVO, userGroup);
        userGroup.setId(SnowflakeId.getId() + "");
        userGroup.setCreateUserCode(userGroupSaveOrUpdateVO.getUserId());
        userGroup.setCreateUserName(userGroupSaveOrUpdateVO.getUsername());
        userGroup.setCreateTime(new Timestamp(new Date().getTime()));

        //查询当前用户分组序号最大值
        Example example = new Example(UserGroup.class);
        example.createCriteria().andEqualTo("createUserCode", userGroupSaveOrUpdateVO.getUserId());
        //排序
        example.setOrderByClause(" ORDER_NO DESC");
        //执行查询
        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);

        //如果存在分组信息-当前分组序号 = 历史最大序号 + 1
        if (!CollectionUtils.isEmpty(userGroupList)) {
            userGroup.setOrderNo(userGroupList.get(0).getOrderNo() + 1);
        }
        //如果当前用户分组为第一个则初始化：orderNo=0
        else {
            userGroup.setOrderNo(0);
        }

        int count = userGroupMapper.insert(userGroup);
        if (count <= 0) {
            log.error("### 用户应用分组添加失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 修改用户应用分组
     * @author: pyy
     * @date: 2019/6/10 11:05
     * @param userGroupSaveOrUpdateVO
     * @return: void
     */
    @Override
    public void update(UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO) {
        //初始化UserGroup实体类
        UserGroup userGroup = new UserGroup();
        userGroup.setId(userGroupSaveOrUpdateVO.getId());
        //根据id查询
        UserGroup userGroupOld = userGroupMapper.selectOne(userGroup);
        if (userGroupOld == null) {
            log.error("### 该用户应用分组不存在，id={} ###", userGroupSaveOrUpdateVO.getId());
            throw new CustomException(ResultCode.FAIL);
        }

        //页面传输的参数覆盖数据库属性值
        BeanUtils.copyProperties(userGroupSaveOrUpdateVO, userGroupOld);
        userGroupOld.setLastUpdateUserCode(userGroupSaveOrUpdateVO.getUserId());
        userGroupOld.setLastUpdateUserName(userGroupSaveOrUpdateVO.getUsername());
        userGroupOld.setLastUpdateTime(new Timestamp(new Date().getTime()));

        //执行修改
        int count = userGroupMapper.updateByPrimaryKey(userGroupOld);
        if (count != 1) {
            log.error("### 用户应用分组修改失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isBlank(id)) {
            log.error("### 分组ID不能为空 ###");
            throw new CustomException(ResultCode.INVALID_PARAM);
        }

        //判断该分组下是否存在应用
        Example example = new Example(UserGroupApplication.class);
        example.createCriteria().andEqualTo("userGroupId", id);
        List<UserGroupApplication> userGroupApplicationList = userGroupApplicationMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(userGroupApplicationList)) {
            log.info("### 应用分组已经被应用使用，不能删除, userGroupId={} ###", id);
            throw new CustomException(ResultCode.GROUP_NO_ALLOWED_DEL);
        }

        UserGroup userGroup = new UserGroup();
        userGroup.setId(id);
        int count = userGroupMapper.deleteByPrimaryKey(userGroup);
        if (count != 1) {
            log.error("### 用户应用分组删除失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    @Override
    public List<UserGroupListVO> list(String userId) {
        if (StringUtils.isBlank(userId)) {
            log.error("### 用户ID不能为空 ###");
            throw new CustomException(ResultCode.INVALID_PARAM);
        }

        //根据用户ID查询应用分组列表信息
        Example example = new Example(UserGroup.class);
        //查询条件
        example.createCriteria().andEqualTo("createUserCode", userId);
        //排序
        example.setOrderByClause(" ORDER_NO ASC");
        //执行查询
        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);

        //过滤数据
        List<UserGroupListVO> userGroupListVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userGroupList)) {
            userGroupListVOList = userGroupList.stream().map(userGroup -> {
                UserGroupListVO userGroupListVO = new UserGroupListVO();
                BeanUtils.copyProperties(userGroup, userGroupListVO);
                return userGroupListVO;
            }).collect(Collectors.toList());
        }
        return userGroupListVOList;
    }

    /**
     * 调整分组顺序
     * @author: pyy
     * @date: 2019/6/10 16:03
     * @param userGroupId
     * @param type
     * @return: void
     */
    @Override
    public void moveOrder(String userGroupId, String type) {

        //参数校验
        if (StringUtils.isBlank(userGroupId)) {
            log.error("### 用户分组ID不能为空 ###");
            throw new CustomException(ResultCode.INVALID_PARAM);
        }
        if (StringUtils.isBlank(type)) {
            log.error("### 排序操作类型不能为空 ###");
            throw new CustomException(ResultCode.INVALID_PARAM);
        }

        //查询当前要移动的分组对象
        UserGroup currGroup = userGroupMapper.selectByPrimaryKey(userGroupId);
        if (currGroup == null) {
            log.error("### 用户分组查询失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        //判断操作排序类型
        if (Constants.UP.equals(type)) { //上移

            moveUp(userGroupId, currGroup);
        } else if (Constants.DOWN.equals(type)) { // 下移

            moveDown(userGroupId, currGroup);
        } else if (Constants.TOP.equals(type)) { // 置顶

            moveTop(userGroupId, currGroup);
        } else if (Constants.BOTTOM.equals(type)) { // 置底

            moveBottom(userGroupId, currGroup);
        }
    }

    /**
     * 置底
     * @param userGroupId
     * @param currGroup
     */
    private void moveBottom(String userGroupId, UserGroup currGroup) {
        //查询最底部分组对象
        Example example = new Example(UserGroup.class);
        example.setOrderByClause(" ORDER_NO DESC");
        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(userGroupList)) {
            log.info("### 没查到最底部元素 ###");
            return;
        }
        UserGroup bottomGroup = userGroupList.get(0);
        //如果当前分组和最底分组相同
        if (bottomGroup.getId().equals(userGroupId)) {
            log.info("### 已经是最底端元素, userGroupId={}###", userGroupId);
            throw new CustomException(ResultCode.IS_BOTTOM);
        }

        //置底操作 ，被操作记录 编号是置顶帖子(编号+1)
        currGroup.setOrderNo(bottomGroup.getOrderNo() + 1);

        //更新数据库
        userGroupMapper.updateByPrimaryKey(currGroup);

        log.info("### 分组置底成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 置顶
     * @param userGroupId
     * @param currGroup
     */
    private void moveTop(String userGroupId, UserGroup currGroup) {
        //查询最顶部分组对象
        Example example = new Example(UserGroup.class);
        example.setOrderByClause(" ORDER_NO ASC");
        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(userGroupList)) {
            log.info("### 没查到最顶部元素 ###");
            return;
        }
        UserGroup topGroup = userGroupList.get(0);
        //如果当前分组和最顶分组相同
        if (topGroup.getId().equals(userGroupId)) {
            log.info("### 已经是最顶端元素, userGroupId={}###", userGroupId);
            throw new CustomException(ResultCode.IS_TOP);
        }

        //置顶操作 ，被操作记录 编号是置顶帖子(编号-1)
        currGroup.setOrderNo(topGroup.getOrderNo() - 1);

        //更新数据库
        userGroupMapper.updateByPrimaryKey(currGroup);

        log.info("### 分组置顶成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 下移
     * @param userGroupId
     * @param currGroup
     */
    private void moveDown(String userGroupId, UserGroup currGroup) {
        //查询当前要移动的下一个分组对象
        Example example = new Example(UserGroup.class);
        example.createCriteria().andGreaterThan("orderNo", currGroup.getOrderNo());
        example.setOrderByClause(" ORDER_NO ASC");

        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userGroupList)) {
            log.info("### 已经是最顶端元素, userGroupId={}###", userGroupId);
            throw new CustomException(ResultCode.IS_TOP);
        }

        UserGroup nextGroup = userGroupList.get(0);
        //交换orderNo的值
        Integer temp = currGroup.getOrderNo();   //当前分组位置
        currGroup.setOrderNo(nextGroup.getOrderNo());
        nextGroup.setOrderNo(temp);

        //更新到数据库
        int currCount = userGroupMapper.updateByPrimaryKey(currGroup);
        int nextCount = userGroupMapper.updateByPrimaryKey(nextGroup);

        if (currCount != 1 || nextCount != 1) {
            log.error("### 修改分组排序号错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        log.info("### 分组下移成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }

    /**
     * 上移
     * @param userGroupId
     * @param currGroup
     */
    private void moveUp(String userGroupId, UserGroup currGroup) {
        //查询当前要移动的上一个分组对象
        Example example = new Example(UserGroup.class);
        example.createCriteria().andLessThan("orderNo", currGroup.getOrderNo());
        example.setOrderByClause(" ORDER_NO DESC");

        List<UserGroup> userGroupList = userGroupMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userGroupList)) {
            log.info("### 已经是最顶端元素, userGroupId={}###", userGroupId);
            throw new CustomException(ResultCode.IS_TOP);
        }

        UserGroup prevGroup = userGroupList.get(0);
        //交换orderNo的值
        Integer temp = currGroup.getOrderNo();   //当前分组位置
        currGroup.setOrderNo(prevGroup.getOrderNo());
        prevGroup.setOrderNo(temp);

        //更新到数据库
        int currCount = userGroupMapper.updateByPrimaryKey(currGroup);
        int prevCount = userGroupMapper.updateByPrimaryKey(prevGroup);

        if (currCount != 1 || prevCount != 1) {
            log.error("### 修改分组排序号错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        log.info("### 分组上移成功，当前分组排序号:orderNo={} ###", currGroup.getOrderNo());
    }
}






















