package cn.com.thtf.model;

import javax.persistence.Table;

@Table(name = "HYJZ_ADMIN")
public class Admin {

  private String id;
  private String userId;
  private String userCode;
  private String username;
  private String realName;
  private String shortName;
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


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }


  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
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
