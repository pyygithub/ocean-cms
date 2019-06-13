package cn.com.thtf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:41
 * Version: v1.0
 * ========================
 */
@ApiModel(value = "AdminGroupSaveOrUpdateVO",description = "管理员应用分组保存或修改VO类")
public class AdminGroupSaveOrUpdateVO {

    @ApiModelProperty("分组名称")
    @NotBlank(message = "分组名称不能为空")
    private String name;

    @ApiModelProperty("分组描述")
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
