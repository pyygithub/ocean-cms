package cn.com.thtf.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_APPLICATION")
public class Application {

  @Id
  private String id;
  private String appName;
  private String imagePath;
  private String link;
  private String priority;
  private String typeId;
  private String imageTypePath;
  private String status;
  private String createUserCode;
  private String createUserName;
  private java.sql.Timestamp createTime;
  private String lastUpdateUserCode;
  private String lastUpdateUserName;
  private java.sql.Timestamp lastUpdateTime;
  private String deletedFlag;


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


  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
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

}
