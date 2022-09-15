package com.velikiyprikalel.model;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип элемента - папка или файл
 */
public enum SystemItemType {
  
  FILE("FILE"),
  
  FOLDER("FOLDER");

  private String value;

  SystemItemType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SystemItemType fromValue(String value) {
    for (SystemItemType b : SystemItemType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

