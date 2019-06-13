package cn.com.thtf.model;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "HYJZ_APPLICATION_TYPE")
public class ApplicationType {

  @Id
  private String id;
  private String name;


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

}
