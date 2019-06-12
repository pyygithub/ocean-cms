package cn.com.thtf.web.controller;

import cn.com.thtf.aop.log.Log;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.model.ApplicationType;
import cn.com.thtf.service.ApplicationTypeService;
import cn.com.thtf.vo.AdminGroupListVO;
import cn.com.thtf.vo.AdminGroupSaveOrUpdateVO;
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
 * 系统应用分类Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/12
 * Time：8:51
 * Version: v1.0
 * ========================
 */
@Api(value = "ApplicationTypeController", description = "系统应用分类相关接口")
@RestController
@RequestMapping("/v1")
public class ApplicationTypeController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationTypeController.class);

    @Autowired
    private ApplicationTypeService applicationTypeService;

    /**
     * 查询系统应用分类列表
     * @return
     */
    @ApiOperation(value = "查询系统应用分类列表", notes = "查询系统应用分类列表")
    @GetMapping("/applicationTypes")
    public Result list() {
        List<ApplicationType> applicationTypeList = applicationTypeService.list();

        return Result.SUCCESS(applicationTypeList);
    }
}
