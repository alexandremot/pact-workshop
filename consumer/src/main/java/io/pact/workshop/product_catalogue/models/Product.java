package io.pact.workshop.product_catalogue.models;

import lombok.*;

@Data
public class Product {
  private  Long id;
  private String name;
  private String type;
  private String version;

  public Product(long id, String name, String type, String version) {
  }
}
