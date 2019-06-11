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
@ApiModel(value = "UserGroupListVO",description = "用户应用分组列表VO类")
public class UserGroupListVO {

    @ApiModelProperty("分组ID")
    private String id;

    @ApiModelProperty("分组名称")
    private String name;


    @ApiModelProperty("序号")
    private String orderNo;


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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
