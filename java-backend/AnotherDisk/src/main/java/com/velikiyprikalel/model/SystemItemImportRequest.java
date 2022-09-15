package com.velikiyprikalel.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.Valid;


/**
 * SystemItemImportRequest
 */
public class SystemItemImportRequest {

  @JsonProperty("items")
  @Valid
  private List<SystemItemImport> items = null;

  @JsonProperty("updateDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updateDate;

  public SystemItemImportRequest items(List<SystemItemImport> items) {
    this.items = items;
    return this;
  }

  public SystemItemImportRequest addItemsItem(SystemItemImport itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Импортируемые элементы
   * @return items
  */
  @Valid 
  public List<SystemItemImport> getItems() {
    return items;
  }

  public void setItems(List<SystemItemImport> items) {
    this.items = items;
  }

  public SystemItemImportRequest updateDate(OffsetDateTime updateDate) {
    this.updateDate = updateDate;
    return this;
  }

  /**
   * Время обновления добавляемых элементов.
   * @return updateDate
  */
  @Valid 
  public OffsetDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(OffsetDateTime updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemItemImportRequest systemItemImportRequest = (SystemItemImportRequest) o;
    return Objects.equals(this.items, systemItemImportRequest.items) &&
        Objects.equals(this.updateDate, systemItemImportRequest.updateDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, updateDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemItemImportRequest {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    updateDate: ").append(toIndentedString(updateDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

