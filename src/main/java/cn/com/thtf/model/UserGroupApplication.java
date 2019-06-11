package cn.com.thtf.model;


import javax.persistence.Table;

@Table(name = "HYJZ_USER_GROUP_APPLICATION")
public class UserGroupApplication {

  private String userGroupId;
  private String applicationId;


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

}
