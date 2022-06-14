package com.mx.models;

public final class Frequency extends MdxBase<Frequency> {

  private String description;
  private String id;
  private String name;

  public Frequency() {

  }

  public Frequency(String initialId, String initialName, String initialDescription) {
    id = initialId;
    description = initialDescription;
    name = initialName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

}
