package cn.com.thtf.web.controller;

import cn.com.thtf.aop.log.Log;
import cn.com.thtf.common.response.QueryResult;
import cn.com.thtf.common.response.Result;
import cn.com.thtf.service.AdminGroupService;
import cn.com.thtf.vo.AdminGroupApplicationVO;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * ========================
 * 管理员应用分组Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:36
 * Version: v1.0
 * ========================
 */
@Api(value = "AdminGroupController", description = "管理员应用分组接口")
@RestController
@RequestMapping("/v1")
public class AdminGroupController {

    private static final Logger log = LoggerFactory.getLogger(AdminGroupController.class);

    @Autowired
    private AdminGroupService adminGroupService;

//    /**
//     * 查询管理员应用分组列表
//     * @return
//     */
//    @ApiOperation(value = "查询管理员应用分组列表查询", notes = "根据管理员ID查询应用分组列表查询接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "name", value = "分组名称", required = false, dataType = "String", paramType = "query")
//    })
//    @GetMapping("/adminGroups")
//    public Result list(@RequestParam(value = "name", required = false) String name) {
//        List<AdminGroupListVO> adminGroupVOList = adminGroupService.listByParam(name);
//
//        return Result.SUCCESS(adminGroupVOList);
//    }

    /**
     * 分页查询管理员应用分组列表
     * @return
     */
    @ApiOperation(value = "分页查询管理员应用分组列表", notes = "分页查询管理员应用分组列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "分组名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页尺寸", required = true, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/adminGroups")
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam("page") Integer page,
                       @RequestParam("size") Integer size) {
        QueryResult<AdminGroupListVO> queryResult = adminGroupService.listPageByParam(name, page, size);

        return Result.SUCCESS(queryResult);
    }


    /**
     * 管理员应用分组添加
     * @param adminGroupSaveOrUpdateVO
     * @return
     */
    @Log(businessCode = "1", operationDesc = "管理员应用分组添加")
    @ApiOperation(value = "管理员应用分组添加", notes = "管理员应用分组添加接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupSaveOrUpdateVO", value = "应用分组新增数据", required = true, dataType = "AdminGroupSaveOrUpdateVO", paramType = "body")
    })
    @PostMapping("/adminGroup")
    public Result add( @Valid @RequestBody AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO) {
        adminGroupService.add(adminGroupSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 管理员应用分组修改
     * @param adminGroupSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "管理员应用分组修改", notes = "管理员应用分组修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupId", value = "应用分组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "adminGroupSaveOrUpdateVO", value = "应用分组修改数据", required = true, dataType = "AdminGroupSaveOrUpdateVO", paramType = "body")
    })
    @PutMapping("/adminGroup/{adminGroupId}")
    public Result update(@Valid @NotBlank(message = "分组ID不能为空") @PathVariable("adminGroupId") String adminGroupId,
                         @RequestBody AdminGroupSaveOrUpdateVO adminGroupSaveOrUpdateVO) {
        adminGroupService.update(adminGroupId, adminGroupSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 根据ID删除管理员应用分组
     * @param adminGroupId
     * @return
     */
    @Log(businessCode = "1", operationDesc = "管理员应用分组删除")
    @ApiOperation(value = "管理员应用分组删除", notes = "管理员应用分组删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupId", value = "应用分组ID", required = true, dataType = "String", paramType = "path")
    })
    @DeleteMapping("/adminGroup/{adminGroupId}")
    public Result del(@Valid @NotBlank(message = "分组ID不能为空") @PathVariable("adminGroupId") String adminGroupId) {
        adminGroupService.delete(adminGroupId);

        return Result.SUCCESS();
    }


    /**
     * 调整管理员应用分组顺序
     * @param adminGroupId
     * @param type
     * @return
     */
    @ApiOperation(value = "调整管理员应用分组顺序", notes = "调整管理员应用分组顺序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupId", value = "分组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "操作类型：1=上移 2=下移 3=置顶 4=置底", required = true, dataType = "String", paramType = "path")
    })
    @PutMapping("/moveOrder/{adminGroupId}/{type}")
    public Result moveOrder(@Valid @NotBlank(message = "分组ID不能为空") @PathVariable("adminGroupId") String adminGroupId,
                            @NotBlank(message = "操作类型不能为空") @PathVariable("type") String type) {
        adminGroupService.moveOrder(adminGroupId, type);

        return Result.SUCCESS();
    }

    /**
     * 查询指定分组下的应用列表
     * @param adminGroupId
     * @return
     */
    @ApiOperation(value = "查询指定分组下的应用列表",notes = "查询指定分组下的应用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupId", value = "管理员应用分组ID", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/adminGroup/{adminGroupId}/applications")
    public Result listByUserGroupId(@Valid @NotBlank(message = "分组ID不能为空") @PathVariable("adminGroupId") String adminGroupId) {
        List<AdminGroupApplicationVO> adminGroupApplicationVOList = adminGroupService.listByAdminGroupId(adminGroupId);

        return Result.SUCCESS(adminGroupApplicationVOList);
    }

    /**
     * 修改管理员应用分组的关联应用
     * @param adminGroupId
     * @param appIds
     * @return
     */
    @ApiOperation(value = "修改管理员应用分组的关联应用",notes = "修改管理员应用分组的关联应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminGroupId", value = "管理应用分组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appIds", value = "选中应用ID集合", required = true, dataType = "List", paramType = "body")
    })
    @PutMapping("/adminGroup/{adminGroupId}/applications")
    public Result updateUserGroupApplication(@Valid @NotBlank(message = "管理应用分组ID不能为空") @PathVariable("adminGroupId") String adminGroupId,
                                             @NotEmpty(message = "选中应用ID集合不能为空") @RequestBody List<String> appIds) {
        adminGroupService.updateAdminGroupApplication(adminGroupId, appIds);

        return Result.SUCCESS();
    }
}
