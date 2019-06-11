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
@ApiModel(value = "UserGroupApplicationVO",description = "用户分组包含应用列表VO类")
public class UserGroupApplicationVO {
    @ApiModelProperty("分组ID")
    private String userGroupId;

    @ApiModelProperty("应用ID")
    private String applicationId;

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("应用优先级")
    private String applicationPriority;

    @ApiModelProperty("选中标记")
    private Boolean checkbox;

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
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

    public String getApplicationPriority() {
        return applicationPriority;
    }

    public void setApplicationPriority(String applicationPriority) {
        this.applicationPriority = applicationPriority;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }
}
