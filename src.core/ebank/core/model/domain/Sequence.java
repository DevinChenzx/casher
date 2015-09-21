package ebank.core.model.domain;

import java.io.Serializable;

public class Sequence implements Serializable {

 
  private static final long serialVersionUID = 8088519061325195044L;
  private String name;
  private String nextId;

  /* Constructors */

  public Sequence() {
  }

  public Sequence(String name, String nextId) {
    this.name = name;
    this.nextId = nextId;
  }

  /* JavaBeans Properties */

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getNextId() { return nextId; }
  public void setNextId(String nextId) { this.nextId = nextId; }

}
