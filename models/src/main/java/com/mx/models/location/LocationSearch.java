package com.mx.models.location;

/**
 * Used to pass all search attributes to location search function.
 * Binds to incoming location search query string parameters
 */
@SuppressWarnings({ "checkstyle:MemberName", "checkstyle:ParameterName", "checkstyle:MethodName" })
public class LocationSearch {

  // Fields

  // NOTE: Non standard naming to assist with parameter binding
  private Boolean has_atm;
  private Boolean has_service_fee;
  private Double latitude;
  private String location_type;
  private Double longitude;
  private Double search_radius;

  public final Boolean getHas_atm() {
    return has_atm;
  }

  public final void setHas_atm(Boolean has_atm) {
    this.has_atm = has_atm;
  }

  public final Boolean getHas_service_fee() {
    return has_service_fee;
  }

  public final void setHas_service_fee(Boolean has_service_fee) {
    this.has_service_fee = has_service_fee;
  }

  public final Double getLatitude() {
    return latitude;
  }

  public final void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public final String getLocation_type() {
    return location_type;
  }

  public final void setLocation_type(String location_type) {
    this.location_type = location_type;
  }

  public final Double getLongitude() {
    return longitude;
  }

  public final void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public final Double getSearch_radius() {
    return search_radius;
  }

  public final void setSearch_radius(Double search_radius) {
    this.search_radius = search_radius;
  }
}
