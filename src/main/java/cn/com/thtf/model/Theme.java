package cn.com.thtf.model;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_THEME")
public class Theme {

  @Id
  private String id;
  private String name;
  private String memo;
  private Integer orderNo;
  private String themeImageId;
  private String status;
  private String createUserCode;
  private String createUserName;
  private java.sql.Timestamp createTime;
  private String lastUpdateUserCode;
  private String lastUpdateUserName;
  private java.sql.Timestamp lastUpdateTime;
  private String deletedFlag;
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

  public String getThemeImageId() {
    return themeImageId;
  }

  public void setThemeImageId(String themeImageId) {
    this.themeImageId = themeImageId;
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


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
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


  public java.sql.Timestamp getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(java.sql.Timestamp lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }


  public String getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(String deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public String getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }
}
