package cn.com.thtf.web.controller;

import cn.com.thtf.aop.log.Log;
import cn.com.thtf.service.UserGroupService;
import cn.com.thtf.vo.UserGroupListVO;
import cn.com.thtf.vo.UserGroupSaveOrUpdateVO;
import cn.com.thtf.common.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * ========================
 * 用户应用分组Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:36
 * Version: v1.0
 * ========================
 */
@Api(value = "UserGroupController", description = "用户应用分组接口")
@RestController
@RequestMapping("/v1")
public class UserGroupController {

    private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);

    @Autowired
    private UserGroupService userGroupService;

    @Log(businessCode = "1", operationDesc = "用户应用分组添加")
    @ApiOperation(value = "用户应用分组添加", notes = "用户应用分组添加接口")
    @PostMapping("/userGroup")
    public Result add(@Valid UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO) {
        userGroupService.add(userGroupSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    @ApiOperation(value = "用户应用分组修改", notes = "用户应用分组修改接口")
    @PutMapping("/userGroup")
    public Result update(@Valid UserGroupSaveOrUpdateVO userGroupSaveOrUpdateVO) {
        userGroupService.update(userGroupSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    @Log(businessCode = "1", operationDesc = "用户应用分组删除")
    @ApiOperation(value = "用户应用分组删除", notes = "用户应用分组删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "应用分组ID", required = true, dataType = "String", paramType = "path")
    })
    @DeleteMapping("/userGroup/{id}")
    public Result del(@PathVariable("id") String id) {
        userGroupService.delete(id);

        return Result.SUCCESS();
    }

    @ApiOperation(value = "用户应用分组列表", notes = "根据用户ID查询应用分组列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/userGroups")
    public Result list(@RequestParam("userId") String userId) {
        List<UserGroupListVO> userGroupVOList = userGroupService.list(userId);

        return Result.SUCCESS(userGroupVOList);
    }

    @ApiOperation(value = "调整用户应用分组顺序", notes = "调整用户应用分组顺序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGroupId", value = "分组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "操作类型：1=上移 2=下移 3=置顶 4=置底", required = true, dataType = "String", paramType = "path")
    })
    @PutMapping("/moveOrder/{userGroupId}/{type}")
    public Result moveOrder(@PathVariable("userGroupId") String userGroupId, @PathVariable("type") String type) {
        userGroupService.moveOrder(userGroupId, type);

        return Result.SUCCESS();
    }
}
