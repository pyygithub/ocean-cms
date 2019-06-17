package cn.com.thtf.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_TOKEN")
public class Token {

  @Id
  private String id;
  private String token;
  private String ip;
  private String applicationId;
  private String secret;
  private String publicKey;
  private String privateKey;
  private String authLevel;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }


  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }


  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }


  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }


  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }


  public String getAuthLevel() {
    return authLevel;
  }

  public void setAuthLevel(String authLevel) {
    this.authLevel = authLevel;
  }

}
