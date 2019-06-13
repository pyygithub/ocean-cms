package cn.com.thtf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：14:05
 * Version: v1.0
 * ========================
 */
@ApiModel(value = "AdminGroupApplicationVO",description = "管理员应用分组包含应用列表VO类")
public class AdminGroupApplicationVO {
    @ApiModelProperty("分组ID")
    private String adminGroupId;

    @ApiModelProperty("应用ID")
    private String applicationId;

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("应用优先级")
    private Integer applicationPriority;

    @ApiModelProperty("选中标记")
    private Boolean checkbox;

    public String getAdminGroupId() {
        return adminGroupId;
    }

    public void setAdminGroupId(String adminGroupId) {
        this.adminGroupId = adminGroupId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getApplicationPriority() {
        return applicationPriority;
    }

    public void setApplicationPriority(Integer applicationPriority) {
        this.applicationPriority = applicationPriority;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }
}
