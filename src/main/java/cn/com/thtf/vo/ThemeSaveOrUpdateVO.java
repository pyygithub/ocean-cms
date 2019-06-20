package cn.com.thtf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:41
 * Version: v1.0
 * ========================
 */
@ApiModel(value = "ThemeSaveOrUpdateVO",description = "主题添加和修改VO类")
public class ThemeSaveOrUpdateVO {

    @ApiModelProperty("主题名称")
    @NotBlank(message = "主题名称不能为空")
    private String name;

    @ApiModelProperty("主题备注")
    private String memo;

    @ApiModelProperty("主题图片ID")
    @NotBlank(message = "主题图片ID不能为空")
    private String themeImageId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getThemeImageId() {
        return themeImageId;
    }

    public void setThemeImageId(String themeImageId) {
        this.themeImageId = themeImageId;
    }
}
