package com.ecommerce.inventory_service.dto;


import lombok.Data;

@Data
public class OrderRequestItemDTO {

    private Long productid;
    private Integer quantity;
}
