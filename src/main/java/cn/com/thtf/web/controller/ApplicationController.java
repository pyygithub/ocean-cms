package cn.com.thtf.web.controller;

import cn.com.thtf.aop.log.Log;
import cn.com.thtf.common.response.QueryResult;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.vo.AdminGroupApplicationVO;
import cn.com.thtf.vo.ApplicationListVO;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;
import cn.com.thtf.vo.ThemeListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;


//    /**
//     * 系统应用列表查询
//     * @return
//     */
//    @ApiOperation(value = "系统应用列表查询", notes = "系统应用列表查询接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "appName", value = "系统应用名称", required = false, dataType = "String", paramType = "query")
//    })
//    @GetMapping("/applications")
//    public Result list(@RequestParam(value = "appName", required = false) String appName) {
//        List<ApplicationListVO> applicationListVOList = applicationService.listByParam(appName);
//
//        return Result.SUCCESS(applicationListVOList);
//    }

    /**
     * 系统应用列表分页查询
     * @return
     */
    @ApiOperation(value = "系统应用列表分页查询", notes = "系统应用列表分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "系统应用名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页尺寸", required = true, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/applications")
    public Result list(@RequestParam(value = "appName", required = false) String appName,
                       @RequestParam("page") Integer page,
                       @RequestParam("size") Integer size) {
        QueryResult<ApplicationListVO> queryResult = applicationService.listPageByParam(appName, page, size);

        return Result.SUCCESS(queryResult);
    }

    /**
     * 系统应用添加
     * @param applicationSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "系统应用添加", notes = "系统应用添加接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationSaveOrUpdateVO", value = "应用分组信息", required = true, dataType = "ApplicationSaveOrUpdateVO", paramType = "body")
    })
    @PostMapping("/application")
    public Result add(@Valid @RequestBody ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        applicationService.add(applicationSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 系统应用修改
     * @param appId
     * @param applicationSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "系统应用修改", notes = "系统应用修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "applicationSaveOrUpdateVO", value = "应用分组信息", required = true, dataType = "ApplicationSaveOrUpdateVO", paramType = "body")
    })
    @PutMapping("/application/{appId}")
    public Result update(@Valid @NotBlank(message = "应用Id不能为空") @PathVariable("appId") String appId,
                         @RequestBody ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        applicationService.update(appId, applicationSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 系统应用状态修改
     * @param appId
     * @param status
     * @return
     */
    @ApiOperation(value = "系统应用状态修改", notes = "系统应用状态修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态1：启用 0：停用", required = true, dataType = "String", paramType = "path")
    })
    @PatchMapping("/application/{appId}/{status}")
    public Result update(@Valid @NotBlank(message = "应用Id不能为空") @PathVariable("appId") String appId,
                         @NotBlank(message = "状态不能为空") @PathVariable("status") String status) {
        applicationService.updateStatus(appId, status);

        return Result.SUCCESS();
    }


}
