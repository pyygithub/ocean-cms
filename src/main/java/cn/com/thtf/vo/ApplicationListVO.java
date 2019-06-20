package cn.com.thtf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：9:41
 * Version: v1.0
 * ========================
 */
@ApiModel(value = "ApplicationListVO",description = "系统应用列表VO类")
public class ApplicationListVO {

    @ApiModelProperty("应用ID")
    private String id;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("图标路径")
    private String imagePath;

    @ApiModelProperty("主页地址")
    private String link;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("应用类型ID")
    private String typeId;

    @ApiModelProperty("应用分类图标路径")
    private String imageTypePath;

    @ApiModelProperty("应用状态 1：启用 0：停用")
    private String status;

    @ApiModelProperty("创建人编码")
    private String createUserCode;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改人名称")
    private String lastUpdateUserCode;

    @ApiModelProperty("修改人名称")
    private String lastUpdateUserName;

    @ApiModelProperty("修改时间")
    private Long lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public String getCreateUserCode() {
        return createUserCode;
    }

    public void setCreateUserCode(String createUserCode) {
        this.createUserCode = createUserCode;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateUserCode() {
        return lastUpdateUserCode;
    }

    public void setLastUpdateUserCode(String lastUpdateUserCode) {
        this.lastUpdateUserCode = lastUpdateUserCode;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
