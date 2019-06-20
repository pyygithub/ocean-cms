package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.mapper.ImageMapper;
import cn.com.thtf.mapper.ThemeMapper;
import cn.com.thtf.mapper.UsersMapper;
import cn.com.thtf.model.Image;
import cn.com.thtf.model.Theme;
import cn.com.thtf.model.Users;
import cn.com.thtf.service.ThemeService;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.UserUtil;
import cn.com.thtf.vo.ThemeListVO;
import cn.com.thtf.vo.ThemeSaveOrUpdateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ========================
 * 主题service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/20
 * Time：8:46
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ThemeServiceImpl implements ThemeService {

    private static final Logger log = LoggerFactory.getLogger(ThemeServiceImpl.class);

    @Autowired
    private ThemeMapper themeMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 主题添加
     * @param themeSaveOrUpdateVO
     */
    @Override
    public void add(ThemeSaveOrUpdateVO themeSaveOrUpdateVO) {
        Theme theme = new Theme();
        theme.setId(SnowflakeId.getId() + "");
        BeanUtils.copyProperties(themeSaveOrUpdateVO, theme);

        //查询主题序号最大值
        Example example = new Example(Theme.class);
        //排序
        example.setOrderByClause(" ORDER_NO DESC");
        //执行查询
        List<Theme> themeList = themeMapper.selectByExample(example);

        //非一次添加-当前分组序号 = 历史最大序号 + 1
        if (!CollectionUtils.isEmpty(themeList)) {
            theme.setOrderNo(themeList.get(0).getOrderNo() + 1);
        }
        //第一次：序号 = 0
        else {
            theme.setOrderNo(0);
        }

        theme.setCreateUserCode(UserUtil.getUserId());
        theme.setCreateUserName(UserUtil.getUsername());
        theme.setCreateTime(new Timestamp(new Date().getTime()));
        theme.setStatus(Constants.ENABLE);
        theme.setDeletedFlag(Constants.UN_DELETED);
        int count = themeMapper.insert(theme);
        if (count != 1) {
            log.error("### 主题添加：添加到数据库错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 主题修改
     * @param themeId
     * @param themeSaveOrUpdateVO
     */
    @Override
    public void update(String themeId, ThemeSaveOrUpdateVO themeSaveOrUpdateVO) {
        //根据ID查询主题信息
        Theme oldTheme = themeMapper.selectByPrimaryKey(themeId);
        if (oldTheme == null) {
            log.error("### 主题修改：id = {}, 主题查无数据 ###", themeId);
            throw new CustomException(ResultCode.RESULT_DATA_NONE);
        }

        //查询该主题是否有用户使用
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("themeId", themeId);
        int useCount = usersMapper.selectCountByExample(example);
        if (useCount > 0) {
            //判断主题图片ID是否修改
            if (!StringUtils.equals(oldTheme.getThemeImageId(), themeSaveOrUpdateVO.getThemeImageId())) {
                log.error("### 主题修改：id = {}, 主题已经被用户使用不能更改图片信息 ###", themeId);
                throw new CustomException(ResultCode.BUSINESS_THEME_NO_ALLOWED_UPDATE);
            }
        }

        //如果没有被使用可以随意修改
        BeanUtils.copyProperties(themeSaveOrUpdateVO, oldTheme);
        oldTheme.setLastUpdateUserCode(UserUtil.getUserId());
        oldTheme.setLastUpdateUserName(UserUtil.getUsername());
        oldTheme.setLastUpdateTime(new Timestamp(new Date().getTime()));

        int updateCount = themeMapper.updateByPrimaryKeySelective(oldTheme);
        if (updateCount != 1) {
            log.error("### 主题修改: 修改主题记录失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 主题删除
     * @param themeId
     */
    @Override
    public void delete(String themeId) {
        //根据ID查询主题数据
        Theme theme = themeMapper.selectByPrimaryKey(themeId);
        if (theme == null) {
            log.error("### 主题删除：id = {}, 主题查无数据 ###", themeId);
            throw new CustomException(ResultCode.RESULT_DATA_NONE);
        }

        //查询该主题是否有用户使用
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("themeId", themeId);
        int useCount = usersMapper.selectCountByExample(example);
        if (useCount > 0) {
            log.error("### 主题删除：该主题已经被用户使用不允许删除###");
            throw new CustomException(ResultCode.BUSINESS_THEME_NO_ALLOWED_DEL);
        }
        //判断是否为默认主题-不允许删除
        if (Constants.THEME_DEFAULT.equals(theme.getIsDefault())) {
            log.error("### 主题删除：id = {}, 该主题为默认主题不允许删除 ###", themeId);
            throw new CustomException(ResultCode.BUSINESS_THEME_DEFAULT_NO_ALLOWED_DEL);
        }

        //删除主题-逻辑删除
        theme.setDeletedFlag(Constants.DELETED);
        theme.setLastUpdateTime(new Timestamp(new Date().getTime()));
        theme.setLastUpdateUserCode(UserUtil.getUserId());
        theme.setLastUpdateUserName(UserUtil.getUsername());
        int updateCount = themeMapper.updateByPrimaryKeySelective(theme);
        if (updateCount != 1) {
            log.error("### 主题删除: 逻辑删除主题记录失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 主题列表查询
     * @param themeName
     * @return
     */
    @Override
    public List<ThemeListVO> listByParam(String themeName) {
        //查询条件
        Example example = new Example(Theme.class);
        example.createCriteria()
                .andLike("name", "%" + themeName + "%")
                .andEqualTo("deletedFlag", Constants.UN_DELETED);

        //排序：默认主题-降序  排序号-升序
        example.setOrderByClause(" IS_DEFAULT DESC, ORDER_NO ASC");

        //查询数据库
        List<Theme> themeList = themeMapper.selectByExample(example);

        List<ThemeListVO> themeListVOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(themeList)) {
            themeList.forEach(theme -> {
                ThemeListVO themeVO = new ThemeListVO();
                BeanUtils.copyProperties(theme, themeVO);
                themeVO.setCreateTime(theme.getCreateTime().getTime());
                themeVO.setLastUpdateTime(theme.getLastUpdateTime().getTime());

                //查找关联主题图片对象
                Image image = imageMapper.selectByPrimaryKey(theme.getThemeImageId());
                themeVO.setImage(image);

                themeListVOs.add(themeVO);
            });

        }

        return themeListVOs;
    }

    /**
     * 主题状态修改
     * @param themeId
     * @param status
     */
    @Override
    public void updateStatus(String themeId, String status) {
        //参数验证
        if (!Constants.ENABLE.equals(status) && !Constants.DISABLE.equals(status)) {
            log.error("### 主题状态修改：状态码错误 status={} ###", status);
            throw new CustomException(ResultCode.PARAM_TYPE_BIND_ERROR);
        }

        //根据ID查询主题数据
        Theme oldTheme = themeMapper.selectByPrimaryKey(themeId);
        if (oldTheme == null) {
            log.error("### 主题状态修改：id = {}, 主题查无数据 ###", themeId);
            throw new CustomException(ResultCode.RESULT_DATA_NONE);
        }

        //如果要停用
        if (Constants.DISABLE.equals(status)) {
            //查询该主题是否有用户使用-不允许停用
            Example example = new Example(Users.class);
            example.createCriteria().andEqualTo("themeId", themeId);
            int useCount = usersMapper.selectCountByExample(example);
            if (useCount > 0) {
                log.error("### 主题状态修改：该主题已经被用户使用不允许停用###");
                throw new CustomException(ResultCode.BUSINESS_THEME_NO_ALLOWED_DISABLE);
            }
            //判断是否为默认主题-不允许停用
            if (Constants.THEME_DEFAULT.equals(oldTheme.getIsDefault())) {
                log.error("### 主题状态修改：id = {}, 该主题已经被用户使用不允许停用 ###", themeId);
                throw new CustomException(ResultCode.BUSINESS_THEME_NO_ALLOWED_DISABLE);
            }
        }

        //执行修改
        Theme theme = new Theme();
        theme.setId(themeId);
        theme.setStatus(status);
        theme.setLastUpdateTime(new Timestamp(new Date().getTime()));
        theme.setLastUpdateUserCode(UserUtil.getUserId());
        theme.setLastUpdateUserName(UserUtil.getUsername());

        int updateCount = themeMapper.updateByPrimaryKeySelective(theme);
        if (updateCount != 1) {
            log.error("### 主题状态修改: 主题记录状态修改失败 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }
}
