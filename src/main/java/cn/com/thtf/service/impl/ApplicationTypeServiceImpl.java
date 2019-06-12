package cn.com.thtf.service.impl;

import cn.com.thtf.mapper.ApplicationTypeMapper;
import cn.com.thtf.model.ApplicationType;
import cn.com.thtf.service.ApplicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ========================
 * 系统应用分类service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/12
 * Time：9:47
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationTypeServiceImpl implements ApplicationTypeService {

    @Autowired
    private ApplicationTypeMapper applicationTypeMapper;

    /**
     * 查询系统应用分类列表
     * @return
     */
    @Override
    public List<ApplicationType> list() {
        return applicationTypeMapper.selectAll();
    }
}
