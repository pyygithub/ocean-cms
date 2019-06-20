package cn.com.thtf.web.controller;

import cn.com.thtf.common.response.Result;
import cn.com.thtf.service.ThemeService;
import cn.com.thtf.vo.ThemeListVO;
import cn.com.thtf.vo.ThemeSaveOrUpdateVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ========================
 * 主题Controller
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：14:19
 * Version: v1.0
 * ========================
 */
@Api(value = "ThemeController", description = "主题相关接口")
@RestController
@RequestMapping("/v1")
public class ThemeController {
    private static final Logger log = LoggerFactory.getLogger(ThemeController.class);

    @Autowired
    private ThemeService themeService;

    /**
     * 主题列表查询
     * @return
     */
    @ApiOperation(value = "主题列表查询", notes = "主题列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeName", value = "主题名称", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/themes")
    public Result list(@RequestParam(value = "themeName", required = false) String themeName) {
        List<ThemeListVO> themeListVOList = themeService.listByParam(themeName);

        return Result.SUCCESS(themeListVOList);
    }

    /**
     * 主题添加
     * @param themeSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "主题添加", notes = "主题添加接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeSaveOrUpdateVO", value = "主题信息", required = true, dataType = "ThemeSaveOrUpdateVO", paramType = "body")
    })
    @PostMapping("/theme")
    public Result add(@Valid @RequestBody ThemeSaveOrUpdateVO themeSaveOrUpdateVO) {
        themeService.add(themeSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 主题修改
     * @param themeSaveOrUpdateVO
     * @return
     */
    @ApiOperation(value = "主题修改", notes = "主题修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "themeSaveOrUpdateVO", value = "主题信息", required = true, dataType = "ThemeSaveOrUpdateVO", paramType = "body")
    })
    @PutMapping("/theme/{themeId}")
    public Result update(@Valid @NotBlank(message = "主题ID不能为空") @PathVariable("themeId") String themeId,
                         @RequestBody ThemeSaveOrUpdateVO themeSaveOrUpdateVO) {
        themeService.update(themeId, themeSaveOrUpdateVO);

        return Result.SUCCESS();
    }

    /**
     * 主题删除
     * @param themeId
     * @return
     */
    @ApiOperation(value = "主题删除", notes = "主题删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题ID", required = true, dataType = "String", paramType = "path")
    })
    @DeleteMapping("/theme/{themeId}")
    public Result update(@Valid @NotBlank(message = "主题ID不能为空") @PathVariable("themeId") String themeId) {
        themeService.delete(themeId);

        return Result.SUCCESS();
    }

    /**
     * 主题状态修改
     * @param themeId
     * @param status
     * @return
     */
    @ApiOperation(value = "主题状态修改", notes = "主题状态修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态 1：启用 0：停用", required = true, dataType = "String", paramType = "path")
    })
    @PatchMapping("/theme/{themeId}/{status}")
    public Result update(@Valid @NotBlank(message = "主题Id不能为空") @PathVariable("themeId") String themeId,
                         @NotBlank(message = "状态不能为空") @PathVariable("status") String status) {
        themeService.updateStatus(themeId, status);

        return Result.SUCCESS();
    }

    /**
     * 默认主题设置
     * @param themeId
     * @return
     */
    @ApiOperation(value = "默认主题设置", notes = "默认主题设置接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题ID", required = true, dataType = "String", paramType = "path")
    })
    @PatchMapping("/theme/{themeId}/$setDefault")
    public Result setDefaultTheme(@Valid @NotBlank(message = "主题Id不能为空") @PathVariable("themeId") String themeId) {
        themeService.setDefaultTheme(themeId);

        return Result.SUCCESS();
    }
}
