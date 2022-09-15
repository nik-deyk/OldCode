package com.velikiyprikalel.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;


/**
 * SystemItemHistoryResponse
 */
public class SystemItemHistoryResponse {

  @JsonProperty("items")
  @Valid
  private List<SystemItemHistoryUnit> items = null;

  public SystemItemHistoryResponse items(List<SystemItemHistoryUnit> items) {
    this.items = items;
    return this;
  }

  public SystemItemHistoryResponse addItemsItem(SystemItemHistoryUnit itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * История в произвольном порядке.
   * @return items
  */
  @Valid 
  public List<SystemItemHistoryUnit> getItems() {
    return items;
  }

  public void setItems(List<SystemItemHistoryUnit> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemItemHistoryResponse systemItemHistoryResponse = (SystemItemHistoryResponse) o;
    return Objects.equals(this.items, systemItemHistoryResponse.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemItemHistoryResponse {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

