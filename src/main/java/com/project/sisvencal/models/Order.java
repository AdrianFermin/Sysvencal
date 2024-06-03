package com.project.sisvencal.models;

import lombok.Data;

@Data
public class Order {
    
    private Double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
