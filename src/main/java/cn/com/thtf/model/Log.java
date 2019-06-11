package cn.com.thtf.model;


import org.springframework.data.annotation.Id;

import javax.persistence.Table;

@Table(name = "HYJZ_LOG")
public class Log {

  @Id
  private String id;
  private String businessCode;
  private String operationDesc;
  private String operationContent;
  private String requestIp;
  private Long time;
  private String username;
  private java.sql.Timestamp createTime;
  private String deletedFlag;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getBusinessCode() {
    return businessCode;
  }

  public void setBusinessCode(String businessCode) {
    this.businessCode = businessCode;
  }


  public String getOperationDesc() {
    return operationDesc;
  }

  public void setOperationDesc(String operationDesc) {
    this.operationDesc = operationDesc;
  }

  public String getOperationContent() {
    return operationContent;
  }

  public void setOperationContent(String operationContent) {
    this.operationContent = operationContent;
  }


  public String getRequestIp() {
    return requestIp;
  }

  public void setRequestIp(String requestIp) {
    this.requestIp = requestIp;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(String deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public Log(Long time) {
    this.time = time;
  }
}
