package cn.com.thtf.vo;

import cn.com.thtf.model.Image;
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
@ApiModel(value = "ThemeListVO",description = "主题列表VO类")
public class ThemeListVO {

    @ApiModelProperty("主题ID")
    private String id;

    @ApiModelProperty("主题名称")
    private String name;

    @ApiModelProperty("主题名称")
    private String memo;

    @ApiModelProperty("主题名称")
    private Integer orderNo;

    @ApiModelProperty("主题图片")
    private Image image;

    @ApiModelProperty("主题状态")
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

    @ApiModelProperty("是否默认主题：1 默认 0 非默认")
    private String isDefault;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
