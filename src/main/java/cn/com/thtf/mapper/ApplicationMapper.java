package cn.com.thtf.mapper;

import cn.com.thtf.model.Application;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * ========================
 * 系统应用Mapper接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：14:13
 * Version: v1.0
 * ========================
 */
public interface ApplicationMapper extends Mapper<Application> {

    List<Application> selectByAdminGroupId(String adminGroupId);
}
