package cn.com.thtf.model;


import javax.persistence.Table;

@Table(name = "HYJZ_ADMIN_GROUP_APPLICATION")
public class AdminGroupApplication {

  private String adminGroupId;
  private String applicationId;


  public String getAdminGroupId() {
    return adminGroupId;
  }

  public void setAdminGroupId(String adminGroupId) {
    this.adminGroupId = adminGroupId;
  }


  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

}
