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

    @ApiModelProperty("分组ID（修改必须，添加不需要）")
    private String id;

    @ApiModelProperty("分组名称")
    @NotBlank(message = "分组名称不能为空")
    private String name;

    @ApiModelProperty("分组描述")
    private String description;

    @ApiModelProperty("操作人ID")
    @NotBlank(message = "操作人ID不能为空")
    private String userId;

    @ApiModelProperty("操作人名称")
    @NotBlank(message = "操作人名称不能为空")
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
