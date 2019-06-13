package cn.com.thtf.web.controller;

import cn.com.thtf.aop.log.Log;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.vo.AdminGroupApplicationVO;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;
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

    /**
     * 系统应用添加
     * @param applicationSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "系统应用添加", notes = "系统应用添加接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationSaveOrUpdateVO", value = "应用分组ID", required = true, dataType = "ApplicationSaveOrUpdateVO", paramType = "body")
    })
    @PostMapping("/application")
    public Result add(@Valid @RequestBody ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        applicationService.add(applicationSaveOrUpdateVO);

        return Result.SUCCESS();
    }

}
