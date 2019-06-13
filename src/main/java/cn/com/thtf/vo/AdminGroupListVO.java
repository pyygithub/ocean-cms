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
@ApiModel(value = "AdminGroupListVO",description = "管理员应用分组列表VO类")
public class AdminGroupListVO {

    @ApiModelProperty("分组ID")
    private String id;

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("分组描述")
    private String description;

    @ApiModelProperty("序号")
    private Integer orderNo;


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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
