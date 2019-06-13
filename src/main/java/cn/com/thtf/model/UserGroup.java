package cn.com.thtf.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_USER_GROUP")
public class UserGroup {

  @Id
  private String id;
  private String name;
  private String description;
  private Integer orderNo;
  private String ownerCode;
  private String createUserCode;
  private String createUserName;
  private java.sql.Timestamp createTime;
  private String lastUpdateUserCode;
  private String lastUpdateUserName;
  private java.sql.Timestamp lastUpdateTime;


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

  public String getOwnerCode() {
    return ownerCode;
  }

  public void setOwnerCode(String ownerCode) {
    this.ownerCode = ownerCode;
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

}
