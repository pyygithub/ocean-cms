package cn.com.thtf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/12
 * Time：10:29
 * Version: v1.0
 * ========================
 */
@ApiModel(value = "ApplicationSaveOrUpdateVO",description = "系统应用保存或修改VO类")
public class ApplicationSaveOrUpdateVO {

    @NotBlank(message = "应用名称不能为空")
    @ApiModelProperty("应用名称")
    private String appName;

    @NotBlank(message = "管理员应用分组ID不能为空")
    @ApiModelProperty("管理员应用分组ID")
    private String adminGroupId;

    @NotBlank(message = "管理员应用分组名称不能为空")
    @ApiModelProperty("管理员应用分组名称")
    private String adminGroupName;

    @NotBlank(message = "应用首页图标路径不能为空")
    @ApiModelProperty("应用首页图标路径")
    private String imagePath;

    @NotBlank(message = "应用主页地址不能为空")
    @ApiModelProperty("应用主页地址")
    private String link;

    @NotBlank(message = "应用注销(退出）地址不能为空")
    @ApiModelProperty("应用注销(退出）地址")
    private String logoutUrl;

    @NotNull(message = "优先级序号不能为空")
    @ApiModelProperty("优先级序号")
    private Integer priority;

    @NotBlank(message = "应用类型ID不能为空")
    @ApiModelProperty("应用类型ID")
    private String typeId;

    @NotBlank(message = "应用分类页面图标路径不能为空")
    @ApiModelProperty("应用分类页面图标路径")
    private String imageTypePath;

    @NotBlank(message = "状态不能为空")
    @ApiModelProperty("状态: 1 启用 0 停用(默认0）")
    private String status;

    @ApiModelProperty("校验级别: 1 普通校验 2 严格校验（默认1）")
    private String authLevel;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAdminGroupId() {
        return adminGroupId;
    }

    public void setAdminGroupId(String adminGroupId) {
        this.adminGroupId = adminGroupId;
    }

    public String getAdminGroupName() {
        return adminGroupName;
    }

    public void setAdminGroupName(String adminGroupName) {
        this.adminGroupName = adminGroupName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getImageTypePath() {
        return imageTypePath;
    }

    public void setImageTypePath(String imageTypePath) {
        this.imageTypePath = imageTypePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(String authLevel) {
        this.authLevel = authLevel;
    }
}
