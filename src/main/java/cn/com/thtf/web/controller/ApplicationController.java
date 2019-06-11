package cn.com.thtf.web.controller;

import cn.com.thtf.common.response.Result;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.vo.UserGroupApplicationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ========================
 * 系统应用Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：13:54
 * Version: v1.0
 * ========================
 */
@Api(value = "ApplicationController", description = "系统应用相关接口")
@RestController
@RequestMapping("/v1")
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);

    @Autowired
    private ApplicationService applicationService;

    @ApiOperation(value = "查询用户分组的应用列表",notes = "查询用户分组的应用列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGroupId", value = "用户分组ID", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/userGroup/{userGroupId}/applications")
    public Result listByUserGroupId(@PathVariable("userGroupId") String userGroupId) {
        List<UserGroupApplicationVO> userGroupApplicationVOList = applicationService.listByUserGroupId(userGroupId);

        return Result.SUCCESS(userGroupApplicationVOList);
    }

    @ApiOperation(value = "修改用户分组的关联应用",notes = "修改用户分组的关联应用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGroupId", value = "用户分组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appIds", value = "选中应用ID集合", required = true, dataType = "List", paramType = "body")
    })
    @PutMapping("/userGroup/{userGroupId}/applications")
    public Result updateUserGroupApplication(@PathVariable("userGroupId") String userGroupId, @RequestBody List<String> appIds) {
        applicationService.updateUserGroupApplication(userGroupId, appIds);

        return Result.SUCCESS();
    }

}
