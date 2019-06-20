package cn.com.thtf.model;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_USERS")
public class Users {

  @Id
  private String id;
  private String code;
  private String sortCode;
  private String name;
  private String shortName;
  private String englishName;
  private String username;
  private String password;
  private String emnumber;
  private String dutyId;
  private String mobilePhone;
  private String officePhone;
  private String officeLocation;
  private String extension;
  private String peopleNatureId;
  private String email;
  private String faxNumber;
  private String parentId;
  private String ifLeader;
  private String titleId;
  private String stateId;
  private String ifPhoneLogin;
  private String ifIpLimit;
  private String ifHiddenContact;
  private String signImage;
  private java.sql.Timestamp startDate;
  private java.sql.Timestamp endDate;
  private String themeId;
  private String fileSize;
  private String modifyUserId;
  private java.sql.Timestamp updateTime;
  private java.sql.Timestamp createTime;
  private String sex;
  private String introduction;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  public String getSortCode() {
    return sortCode;
  }

  public void setSortCode(String sortCode) {
    this.sortCode = sortCode;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }


  public String getEnglishName() {
    return englishName;
  }

  public void setEnglishName(String englishName) {
    this.englishName = englishName;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getEmnumber() {
    return emnumber;
  }

  public void setEmnumber(String emnumber) {
    this.emnumber = emnumber;
  }


  public String getDutyId() {
    return dutyId;
  }

  public void setDutyId(String dutyId) {
    this.dutyId = dutyId;
  }


  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }


  public String getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
  }


  public String getOfficeLocation() {
    return officeLocation;
  }

  public void setOfficeLocation(String officeLocation) {
    this.officeLocation = officeLocation;
  }


  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }


  public String getPeopleNatureId() {
    return peopleNatureId;
  }

  public void setPeopleNatureId(String peopleNatureId) {
    this.peopleNatureId = peopleNatureId;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getFaxNumber() {
    return faxNumber;
  }

  public void setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
  }


  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  public String getIfLeader() {
    return ifLeader;
  }

  public void setIfLeader(String ifLeader) {
    this.ifLeader = ifLeader;
  }


  public String getTitleId() {
    return titleId;
  }

  public void setTitleId(String titleId) {
    this.titleId = titleId;
  }


  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }


  public String getIfPhoneLogin() {
    return ifPhoneLogin;
  }

  public void setIfPhoneLogin(String ifPhoneLogin) {
    this.ifPhoneLogin = ifPhoneLogin;
  }


  public String getIfIpLimit() {
    return ifIpLimit;
  }

  public void setIfIpLimit(String ifIpLimit) {
    this.ifIpLimit = ifIpLimit;
  }


  public String getIfHiddenContact() {
    return ifHiddenContact;
  }

  public void setIfHiddenContact(String ifHiddenContact) {
    this.ifHiddenContact = ifHiddenContact;
  }


  public String getSignImage() {
    return signImage;
  }

  public void setSignImage(String signImage) {
    this.signImage = signImage;
  }


  public java.sql.Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(java.sql.Timestamp startDate) {
    this.startDate = startDate;
  }


  public java.sql.Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(java.sql.Timestamp endDate) {
    this.endDate = endDate;
  }


  public String getThemeId() {
    return themeId;
  }

  public void setThemeId(String themeId) {
    this.themeId = themeId;
  }


  public String getFileSize() {
    return fileSize;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }


  public String getModifyUserId() {
    return modifyUserId;
  }

  public void setModifyUserId(String modifyUserId) {
    this.modifyUserId = modifyUserId;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

}
