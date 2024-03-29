package com.mx.path.core.common.serialization;

import java.time.LocalDate;

import lombok.Data;

/**
 * Used by {@link LocalDateDeserializer}
 *
 * @deprecated Will be removed with {@link LocalDateDeserializer}
 */
@Deprecated
@Data
public class LocalDateJava8 {
  private int day;
  private int month;
  private int year;

  public LocalDateJava8(LocalDate localDate) {
    this.day = localDate.getDayOfMonth();
    this.month = localDate.getMonthValue();
    this.year = localDate.getYear();
  }

  public final LocalDate toLocalDate() {
    return LocalDate.of(
        this.getYear(),
        this.getMonth(),
        this.getDay());
  }
}
